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

            @Idx(index = "2", type = SELECT, options = {
                    @Idx.Option(index = "2.1", pkg = @Pkg(label = "ByName", value = "name")),
                    @Idx.Option(index = "2.2", pkg = @Pkg(label = "ByIndex", value = "index"))})
            @Pkg(label = "[[ColumnToList.getby.label]]",description = "[[ColumnToList.getby.description]]", default_value = "name", default_value_type = DataType.STRING)
            @NotEmpty
                    String getby,
            @Idx(index = "2.1.1", type = TEXT)
            @Pkg(label = "[[ColumnToList.byname.label]]",description = "[[ColumnToList.byname.description]]")
            @NotEmpty
                String byname,
            @Idx(index = "2.2.1", type = NUMBER)
            @Pkg(label = "[[ColumnToList.byindex.label]]",description = "[[ColumnToList.byindex.description]]")
            @NotEmpty
                    Double bynindex
    ) {
        //============================================================ CHECKING COLUMNS
        List<Schema> SCHEMAS = Tabela.getSchema();
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);


        Integer SCHEMA_IDX;
        if(getby.equals("name")) {
            if (!fnd.exists(byname)) {
                throw new BotCommandException("Column '" + byname + "' not found!");
            }
            SCHEMA_IDX=fnd.indexSchema(byname);
        }else{
            if (Tabela.getRows().size() < bynindex) {
                throw new BotCommandException("Column '" + bynindex + "' not found!");
            }
            SCHEMA_IDX = bynindex.intValue();
        }


        List<Value> OutPut = new ArrayList<Value>();

        for(Row rw: Tabela.getRows()){
            OutPut.add(new StringValue(rw.getValues().get(SCHEMA_IDX)));
        }
        ListValue rtrn =  new ListValue();
        rtrn.set(OutPut);
        return rtrn;
    }
}
