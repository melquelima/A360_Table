package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.commands.utils.*;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.CodeType;
import com.automationanywhere.commandsdk.annotations.rules.GreaterThanEqualTo;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import org.apache.commons.lang3.SerializationUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.*;

@BotCommand
@CommandPkg(label = "TableColumnForm",
        description = "Inclusao de uma nova coluna calculada", icon = "pkg.svg", name = "TableColumnForm",
        return_description = "", return_type = DataType.TABLE, return_required = true)


public class TableColumnForm {

    @Execute
    public TableValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "Tabela")
            @NotEmpty
            Table Tabela,
            @Idx(index = "2", type = TEXT)
            @Pkg(label = "Colunas de Entrada",description = "Colunas a serem inseridas no tratamento Ex:'COl1|COL2'")
            @NotEmpty
            String i_colunas,

            @Idx(index = "3", type = SELECT, options = {
                    @Idx.Option(index = "3.1", pkg = @Pkg(label = "NewColumn", value = "new")),
                    @Idx.Option(index = "3.2", pkg = @Pkg(label = "UpdateColumn", value = "update"))})
            @Pkg(label = "Posição da nova coluna na tabela", description = "", default_value = "new", default_value_type = DataType.STRING)
            @NotEmpty
            String typeNewCol,

            @Idx(index = "4", type = SELECT, options = {
                    @Idx.Option(index = "4.1", pkg = @Pkg(label = "ByName", value = "name")),
                    @Idx.Option(index = "4.2", pkg = @Pkg(label = "ByIndex", value = "index"))})
            @Pkg(label = "Selecione a coluna", description = "", default_value = "name", default_value_type = DataType.STRING)
            @NotEmpty
            String colType,

            @Idx(index = "4.1.1", type = TEXT)
            @Pkg(label = "Insira o nome da coluna",description = "Será adiconada no fim da tabela")
            @NotEmpty
            String i_columnName,

            @Idx(index = "4.2.1", type = NUMBER)
            @Pkg(label = "Insira o index da coluna")
            @NotEmpty
            @GreaterThanEqualTo(value = "0")
            Double i_idx,

            @Idx(index = "4.2.2", type = TEXT)
            @Pkg(label = "Insira o nome da coluna")
            @NotEmpty
            String i_columnNameByIndex,

            @Idx(index = "5", type = AttributeType.CODE)
            @Pkg(label = "javaScript Code",description = "sua função deve se chamar 'calc' obrigatoriamente ")
            @CodeType(value = "text/javascript")
            @NotEmpty
            String code
    ) {

        //========================================================== LITURA DO JS
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String library = new Assets().codeLibrary();

        try{
            engine.eval(library);
            engine.eval(code);
            //engine.eval(Files.newBufferedReader(Paths.get("C:/Temp/js.js"), StandardCharsets.UTF_8));
        }
        catch (Exception e){
            throw new BotCommandException("Error when trying to load Js code!");
        }
        //============================================================ CHECKING COLUMNS

        List<Schema> SCHEMAS = new ArrayList<>(Tabela.getSchema());
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);
        List<String> SCHEMA_NAMES = new ArrayList();


        SCHEMA_NAMES = Arrays.asList(i_colunas.split("\\|"));
        for (String sc : SCHEMA_NAMES) {
            if (!fnd.exists(sc) && !sc.startsWith("@")) {
                throw new BotCommandException("Column '" + sc + "' not found!");
            }
        }
        List<Integer> SCHEMA_IDX = fnd.indexSchema(SCHEMA_NAMES);

        //============================================================ CHECKING NEW COLUMN

        if(typeNewCol.equals("new")){
            if(colType.equals("name")){
                if(fnd.indexSchema(i_columnName)>=0){
                    throw new BotCommandException("Column '" + i_columnName + "' already exists in DataTable, please choose another column name!");
                }
            }else{
                if(i_idx > (fnd.shemaNames().size())){
                    throw new BotCommandException("Column '" + i_idx + "' not allowed in DataTable, please choose a index beetween 0 and last item in DataTable!");
                }
            }
        }else{
            if(colType.equals("name")){
                if(fnd.indexSchema(i_columnName)<0){
                    throw new BotCommandException("Column '" + i_columnName + "' not found in DataTable, please choose another existing column name!");
                }
            }else{
                if(i_idx > (fnd.shemaNames().size()-1) ){
                    throw new BotCommandException("Column '" + i_idx + "' not found in DataTable, please choose another existing column index!");
                }
            }
        }

        //============================================================ RUN CALC
        Debugger debugger = new Debugger();
        Invocable inv = (Invocable) engine;
        List<Row> newTbl = new ArrayList();
        int counter = 0;
        int index = 0;


        for(Row rw: Tabela.getRows()){
            counter ++;
            Object params[] = {};
            List<Value> RowListValues =  new ArrayList<>(rw.getValues());
            index = 0;

            //ADCIONA OS PARAMETROS DA LINHA
            for(Integer i: SCHEMA_IDX){
                if(i == -1){
                    String val = SCHEMA_NAMES.get(index).replace("@","");
                    params = Uteis.append(params, val);
                }else {
                    String val = RowListValues.get(i).toString();
                    params = Uteis.append(params, val);
                }
                index++;
            }

            //EXECUTA A FORMULA
            try{
                String newValue = inv.invokeFunction("calc", params).toString();
                debugger.debug(inv,newValue);

                if(colType.equals("name")){
                    if(typeNewCol.equals("new")){
                        RowListValues.add(new StringValue(newValue));
                    }else{
                        Integer idxcol = fnd.indexSchema(i_columnName);
                        RowListValues.set(idxcol,new StringValue(newValue));
                    }
                }else{
                    if(typeNewCol.equals("new")){
                        RowListValues.add(i_idx.intValue(),new StringValue(newValue));
                    }else{
                        RowListValues.set(i_idx.intValue(),new StringValue(newValue));
                    }
                }
                Row newRow = new Row(RowListValues);
                newTbl.add(newRow);

                //System.out.println(newValue);
            }
            catch (Exception e){
                throw new BotCommandException("Error calling method 'calc': row " + counter + ":" + e.getMessage());
            }
        }


        Table tbl = new Table();
        TableValue OUTPUT = new TableValue();
        tbl.setRows(newTbl);

        //============================================================ ARRUMANDO AS COLUNAS
        if(typeNewCol.equals("new")){
            if(colType.equals("name")){
                fnd.addSchema(i_columnName);
                tbl.setSchema(fnd.schemas);
            }else{
                fnd.addSchema(i_idx.intValue(),i_columnNameByIndex);
                tbl.setSchema(fnd.schemas);
            }
        }else{
            tbl.setSchema(fnd.schemas);
        }


        OUTPUT.set(tbl);
        return OUTPUT;

    }

}
