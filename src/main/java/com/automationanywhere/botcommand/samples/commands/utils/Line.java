package com.automationanywhere.botcommand.samples.commands.utils;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.model.table.Row;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private List<Value> values = new ArrayList();
    private Boolean isMysql = false;

    public Line(Row values, Boolean isMysql) {
        this.values = values.getValues();
        this.isMysql = isMysql;
    }

    public String LineQuery() {
        String line="";
        int cnt = 1;
        int lng = this.values.size();

        for(Value ln: this.values){
            line += this.toMySQLField(ln.toString());
            line += cnt != lng?",":"";
            cnt++;
        }
        return "(" + line + ")";
    }

    public String LineQuery(List<Integer> indexes) {
        String line="";
        int cnt = 1;
        int lng = indexes.size();

        for(int idx: indexes){
            if(this.isMysql) {
                line += this.toMySQLField(this.values.get(idx).toString());
            }else{
                line += this.toSQLField(this.values.get(idx).toString());
            }
            line += cnt != lng?",":"";
            cnt++;
        }

        return "(" + line + ")";
    }
    public String LineQuery(List<Integer> indexes,List<Integer> USDBRL,List<Integer> USD) {
        String line="";
        int cnt = 1;
        int lng = indexes.size();

        for(int idx: indexes){
            String value = this.values.get(idx).toString();

            if(USDBRL.indexOf(idx) >= 0){
                line += this.brlUsd(value);
            }
            else if(USD.indexOf(idx) >= 0){
                line += this.usd(value);
            }
            else if(this.isMysql){
                line += this.toMySQLField(value);
            }else{
                line += this.toSQLField(value);
            }

            line += cnt != lng?",":"";
            cnt++;
        }

        return "(" + line + ")";
    }

    private String usd(String value){
        String valor_tratado = value;
        valor_tratado = value.replace(",","");
        return valor_tratado.trim().isEmpty()?"0":valor_tratado;
    }

    private String brlUsd(String value){
        String valor_tratado = value;
        valor_tratado = value.replace(".","");
        valor_tratado = valor_tratado.replace(",",".");
        return valor_tratado.trim().isEmpty()?"0":valor_tratado;
    }

    private String toMySQLField(String value){
        String valor_tratado = value;
        valor_tratado = valor_tratado.replace("'","''");
        valor_tratado = valor_tratado.replace("\n","*-*");
        valor_tratado = valor_tratado.replace("\\","\\\\");
        return "'" + valor_tratado + "'";
    }
    private String toSQLField(String value){
        String valor_tratado = value;
        valor_tratado = valor_tratado.replace("'","''");
        valor_tratado = valor_tratado.replace("\n","*-*");
        return "'" + valor_tratado + "'";
    }

}
