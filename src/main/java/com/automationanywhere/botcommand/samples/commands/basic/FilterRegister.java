package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.commands.utils.Assets;
import com.automationanywhere.botcommand.samples.commands.utils.Debugger;
import com.automationanywhere.botcommand.samples.commands.utils.FindInListSchema;
import com.automationanywhere.botcommand.samples.commands.utils.Uteis;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.CodeType;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.*;

@BotCommand
@CommandPkg(
        label = "FilterRegister",
        name = "FilterRegister",
        icon = "pkg.svg",
        description = "[[FilterRegister.description]]",
        node_label = "[[FilterRegister.node_label]]",
        return_description = "[[FilterRegister.return_description]]",
        return_type = DataType.TABLE,
        return_required = true
)


public class FilterRegister {

    @Execute
    public TableValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[FilterRegister.table.label]]",description = "[[FilterRegister.table.description]]")
            @NotEmpty
            Table Tabela,
            @Idx(index = "2", type = TEXT)
            @Pkg(label = "[[FilterRegister.cols.label]]",description = "[[FilterRegister.cols.description]]")
            String i_colunas,
            @Idx(index = "5", type = AttributeType.CODE)
            @Pkg(label = "[[FilterRegister.code.label]]",description = "[[FilterRegister.code.description]]")
            @CodeType(value = "text/javascript")
            @NotEmpty
            String code
    ) {

        //========================================================== LITURA DO JS
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        String library = new Assets().codeLibrary();

        try{
            //this.alert(Thread.currentThread().getContextClassLoader().getResource("./config/library.js").toString());
            //InputStream inputStream =  Thread.currentThread().getContextClassLoader().getResource("./config/library.js").openStream();
            //String library = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            engine.eval(library);
            engine.eval(code);

            //engine.eval(Files.newBufferedReader(Paths.get("C:/Temp/js.js"), StandardCharsets.UTF_8));
        }
        catch (Exception e){
            throw new BotCommandException("Error when trying to load Js code!" + e.getMessage());
        }
        //============================================================ CHECKING COLUMNS
        List<Schema> SCHEMAS = Tabela.getSchema();
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);
        List<String> SCHEMA_NAMES = new ArrayList();


        SCHEMA_NAMES = Arrays.asList(i_colunas.split("\\|"));
        SCHEMA_NAMES = i_colunas == ""?fnd.shemaNames():SCHEMA_NAMES;
        for (String sc : SCHEMA_NAMES) {
            if (!fnd.exists(sc) && !sc.startsWith("@")) {
                throw new BotCommandException("Column '" + sc + "' not found!");
            }
        }
        List<Integer> SCHEMA_IDX = fnd.indexSchema(SCHEMA_NAMES);
        System.out.println(SCHEMA_NAMES);

        //============================================================ RUN FILTER
        Debugger debugger = new Debugger();
        Invocable inv = (Invocable) engine;
        List<Row> newTbl = new ArrayList();
        int counter = 0;
        int index = 0;


        for(Row rw: Tabela.getRows()){
            counter ++;
            Object params[] = {};
            List<Value> RowListValues = rw.getValues();
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
                Boolean filter = Boolean.parseBoolean(inv.invokeFunction("filter", params).toString());
                debugger.debug(inv,filter.toString());

                if(filter){
                    Row newRow = new Row(RowListValues);
                    newTbl.add(newRow);
                }
            }
            catch (Exception e){
                throw new BotCommandException("Error calling method 'filter': row " + counter + ":" + e.getMessage());
            }
        }

        Table tbl = new Table();
        TableValue OUTPUT = new TableValue();
        tbl.setRows(newTbl);
        tbl.setSchema(fnd.schemas);
        OUTPUT.set(tbl);

        return OUTPUT;

    }
}
