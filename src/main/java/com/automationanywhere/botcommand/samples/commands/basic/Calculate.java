package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.NumberValue;
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
import com.automationanywhere.commandsdk.annotations.rules.GreaterThanEqualTo;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;

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
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "Calculate",
        label = "Calculate",
        node_label = "Calculate {{i_colunasCalc}} on {{i_tabela}} by condition",
        description = "",
        icon = "pkg.svg",
        return_type = DataType.NUMBER,
        return_required = true,
        return_description = "DataTable with the new column"
)


public class Calculate {

    @Execute
    public NumberValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "Tabela")
            @NotEmpty
            Table i_tabela,

            @Idx(index = "2", type = TEXT)
            @Pkg(label = "Colunas de Entrada",description = "Colunas a serem inseridas na condição Ex:'COl1|COL2'")
            @NotEmpty
            String i_colunasCalc,

            @Idx(index = "3", type = AttributeType.CODE)
            @Pkg(label = "javaScript Code",description = "sua função deve se chamar 'calc' obrigatoriamente e retornar o valor a ser somado\n return: Float ")
            @CodeType(value = "text/javascript")
            @NotEmpty
            String i_code

    ) {

        //========================================================== LITURA DO JS
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        String library = new Assets().codeLibrary();

        try{
            engine.eval(library);
            engine.eval(i_code);
            //engine.eval(Files.newBufferedReader(Paths.get("C:/Temp/js.js"), StandardCharsets.UTF_8));
        }
        catch (Exception e){
            throw new BotCommandException("Error when trying to load Js code!");
        }
        //============================================================ CHECKING COLUMNS
        List<Schema> SCHEMAS = i_tabela.getSchema();
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);
        List<String> SCHEMA_NAMES = new ArrayList();


        SCHEMA_NAMES = Arrays.asList(i_colunasCalc.split("\\|"));
        for (String sc : SCHEMA_NAMES) {
            if (!fnd.exists(sc) && !sc.startsWith("@")) {
                throw new BotCommandException("Column '" + sc + "' not found!");
            }
        }
        List<Integer> SCHEMA_IDX = fnd.indexSchema(SCHEMA_NAMES);

        //============================================================ RUN CALC
        Debugger debugger = new Debugger();
        Invocable inv = (Invocable) engine;
        int counter = 0;
        int index = 0;
        Double sum = 0.0;


        for(Row rw: i_tabela.getRows()){
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
                Double value = Uteis.ParseDouble(inv.invokeFunction("calc", params).toString());
                sum += value;
                debugger.debug(inv,value.toString());
            }
            catch (Exception e){
                throw new BotCommandException("Error calling method 'calc': row " + counter + ":" + e.getMessage());
            }
        }

        return new NumberValue(sum);

    }
}
