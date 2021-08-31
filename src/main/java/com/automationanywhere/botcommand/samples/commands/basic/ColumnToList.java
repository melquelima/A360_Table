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
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;
//import MaskFormatter;

//import java.Math;
//import Math;

@BotCommand
@CommandPkg(
        label = "ColumnToList",
        name = "ColumnToList",
        icon = "pkg.svg",
        description = "[[ColumnToList.description]]",
        node_label = "[[ColumnToList.node_label]]",
        return_description = "[[ColumnToList.return_description]]",
        return_type = DataType.LIST,
        return_required = true
)


public class ColumnToList {

    @Execute
    public ListValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[ColumnToList.table.label]]",description = "[[ColumnToList.table.description]]")
            @NotEmpty
            Table Tabela,
            @Idx(index = "2", type = TEXT)
            @Pkg(label = "[[ColumnToList.cols.label]]",description = "[[ColumnToList.cols.description]]")
            @NotEmpty
            String coluna
    ) {
        //============================================================ CHECKING COLUMNS
        List<Schema> SCHEMAS = Tabela.getSchema();
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);

        if (!fnd.exists(coluna)) {
            throw new BotCommandException("Column '" + coluna + "' not found!");
        }
        Integer SCHEMA_IDX = fnd.indexSchema(coluna);


        List<Value> OutPut = new ArrayList<Value>();

        for(Row rw: Tabela.getRows()){
            OutPut.add(new StringValue(rw.getValues().get(SCHEMA_IDX)));
        }
        ListValue rtrn =  new ListValue();
        rtrn.set(OutPut);
        return rtrn;
    }
}
