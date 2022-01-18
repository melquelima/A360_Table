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
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.commands.utils.FindInListSchema;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.GreaterThanEqualTo;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;

import java.util.ArrayList;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.*;
//import MaskFormatter;

//import java.Math;
//import Math;

//@BotCommand
@CommandPkg(
        label = "ListToHeaders",
        name = "ListToHeaders",
        icon = "pkg.svg",
        description = "[[ListToHeaders.description]]",
        node_label = "[[ListToHeaders.node_label]]",
        return_description = "[[ListToHeaders.return_description]]",
        return_type = DataType.TABLE,
        return_required = true
)


public class ListToHeaders {

    @Execute
    public TableValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[ListToHeaders.table.label]]",description = "[[ListToHeaders.table.description]]")
            @NotEmpty
                    Table Tabela,
            @Idx(index = "2", type = LIST )
            @Pkg(label = "[[ListToHeaders.rowIdx.label]]",description = "[[ListToHeaders.rowIdx.description]]")
            @NotEmpty
            @GreaterThanEqualTo("0")
                    ListValue<String> lista
    ) {
        //============================================================ CHECKING COLUMNS
//        List<Schema> SCHEMAS = new ArrayList<>(Tabela.getSchema());
//        List<String> SCHEMA_NAMES = new ArrayList<>();
//
//        if(rowIdx >= Tabela.getRows().size()){
//            throw new BotCommandException("Row '" + rowIdx + "' not found!");
//        }
//
//
//        for (Value col : Tabela.getRows().get(rowIdx.intValue()).getValues()) {
//            SCHEMA_NAMES.add(col.toString());
//        }
//        FindInListSchema fnd = new FindInListSchema(SCHEMA_NAMES);
//
//        if(deleteRow){
//            List<Row> rws = Tabela.getRows();
//            rws.remove(rowIdx.intValue());
//            Tabela.setRows(rws);
//        }
//
//      Tabela.setSchema(fnd.schemas);
        TableValue OUTPUT = new TableValue();
        OUTPUT.set(Tabela);
        return OUTPUT;
    }

}
