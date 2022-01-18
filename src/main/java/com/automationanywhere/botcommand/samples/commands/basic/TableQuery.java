/*
 * Copyright (c) 2020 Automation Anywhere.
 * All rights reserved.
 *
 * This software is the proprietary information of Automation Anywhere.
 * You shall use it only in accordance with the terms of the license agreement
 * you entered into with Automation Anywhere.
 */

/**
 *
 */
package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.commands.utils.FindInListSchema;
import com.automationanywhere.botcommand.samples.commands.utils.Line;
import com.automationanywhere.botcommand.samples.commands.utils.Uteis;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.LIST;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

@BotCommand
@CommandPkg(
        label = "TableQuery",
        name = "TableQuery",
        icon = "pkg.svg",

        description = "[[TableQuery.description]]",
        node_label = "[[TableQuery.node_label]]",
        return_description = "[[TableQuery.return_description]]",

        return_type = LIST,
        return_sub_type = STRING,
        return_required = true
)


public class TableQuery {

    @Execute
    public ListValue<String> action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[TableQuery.table.label]]",description = "[[TableQuery.table.description]]")
            @NotEmpty
            Table Tabela,
            @Idx(index = "2", type = NUMBER)
            @Pkg(label = "[[TableQuery.reg.label]]",description = "[[TableQuery.reg.description]]")
            @NotEmpty
            Double registros,
            @Idx(index = "3", type = TEXT)
            @Pkg(label = "[[TableQuery.col.label]]",description = "[[TableQuery.col.description]]")
            String i_colunas,
            @Idx(index = "4", type = CHECKBOX)
            @Pkg(label = "[[TableQuery.mysql.label]]",description = "[[TableQuery.mysql.description]]",default_value = "false",default_value_type = DataType.BOOLEAN)
            @NotEmpty
            Boolean isMysql,
            @Idx(index = "5", type = TEXT)
            @Pkg(label = "[[TableQuery.usd.label]]",description = "[[TableQuery.usd.description]]")
            String i_colunasUSD,
            @Idx(index = "6", type = TEXT)
            @Pkg(label = "[[TableQuery.usdbrl.label]]",description = "[[TableQuery.usdbrl.description]]")
            String i_colunasBRLUSD
    ) {
        ListValue<String> returnvalue = new ListValue<String>();

        //Table tb = Tabela.get();
        List<Row> ROWS = Tabela.getRows();
        List<Schema> SCHEMAS = Tabela.getSchema();
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);

        //======================================================VALIDA SE NAO TEM COLUNAS COM MESMO NOME
        for(String sc:fnd.shemaNames()){
            if(Uteis.countOccurrences(fnd.shemaNames(),sc)>1){
                throw new BotCommandException("Column '" + sc + "' has more than 1 occurence!");
            }
        }

        //======================================================VALIDA QUANTIDADE DE REGISTROS

        if(registros <= 0)
            throw new BotCommandException("Number of register migth be higher than 0");

        //====================================================== VALIDA SE OS CAMPOS EXISTEM
        List<String> SCHEMA_NAMES = new ArrayList();

        if(i_colunas != "") {
            SCHEMA_NAMES = Arrays.asList(i_colunas.split("\\|"));
            for (String sc : SCHEMA_NAMES) {
                if (!fnd.exists(sc)) {
                    throw new BotCommandException("Column '" + sc + "' not found!");
                }
            }
        }else{
            SCHEMA_NAMES = fnd.shemaNames();//["COL1","COL2"]
        }
        //====================================================== VALIDA SE OS CAMPOS EXISTEM (BRL/USD)
        List<String> SCHEMA_NAMES_BRLUSD = new ArrayList();
        if(i_colunasBRLUSD != "") {
            SCHEMA_NAMES_BRLUSD = Arrays.asList(i_colunasBRLUSD.split("\\|"));
            for (String sc : SCHEMA_NAMES_BRLUSD) {
                if (!fnd.exists(sc)) {
                    throw new BotCommandException("Column '" + sc + "' not found!");
                }
            }
        }
        //====================================================== VALIDA SE OS CAMPOS EXISTEM (USD)
        List<String> SCHEMA_NAMES_USD = new ArrayList();
        if(i_colunasUSD != "") {
            SCHEMA_NAMES_USD = Arrays.asList(i_colunasUSD.split("\\|"));
            for (String sc : SCHEMA_NAMES_USD) {
                if (!fnd.exists(sc)) {
                    throw new BotCommandException("USD:Column '" + sc + "' not found!");
                }
            }
        }


        //====================================================== CRIA OBJETOS DE TRATAMENTO
        List<Line> TABLE_QUERY = new ArrayList<>();
        List<Integer> SCHEMA_IDX = fnd.indexSchema(SCHEMA_NAMES); //["COL1","COL2"] -> [0,1]
        List<Integer> SCHEMA_BRLUSD_IDX = fnd.indexSchema(SCHEMA_NAMES_BRLUSD); //["COL1","COL2"] -> [0,1]
        List<Integer> SCHEMA_USD_IDX = fnd.indexSchema(SCHEMA_NAMES_USD);

        for(Row rw : ROWS) {
            Line line_query = new Line(rw, isMysql);
            TABLE_QUERY.add(line_query);
        }

        //====================================================== CRIA A LISTA DE QUERYS
        List<Value> rgstr = new ArrayList<Value>();

        String queryLine = "";
        int count = 0;
        while(TABLE_QUERY.size() > 0){
            count +=1;
            queryLine += TABLE_QUERY.remove(0).LineQuery(SCHEMA_IDX,SCHEMA_BRLUSD_IDX,SCHEMA_USD_IDX);

            if(count == registros.intValue() || TABLE_QUERY.size() == 0){
                rgstr.add(new StringValue(queryLine));
                queryLine = "";
                count =0;
            }else{
                queryLine += ",";
            }
        }

        returnvalue.set(rgstr);
        return returnvalue;
    }


}
