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
import com.automationanywhere.botcommand.data.model.record.Record;
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

@BotCommand
@CommandPkg(
        label = "RowToList",
        name = "RowToList",
        icon = "pkg.svg",
        description = "[[RowToList.description]]",
        node_label = "[[RowToList.node_label]]",
        return_description = "[[RowToList.return_description]]",
        return_type = DataType.LIST,
        return_sub_type = DataType.STRING,
        return_required = true
)


public class RowToList {

    @Execute
    public ListValue action(
            @Idx(index = "1", type = SELECT, options = {
                    @Idx.Option(index = "1.1", pkg = @Pkg(label = "Record", value = "record")),
                    @Idx.Option(index = "1.2", pkg = @Pkg(label = "Table", value = "table"))})
            @Pkg(label = "[[RowToList.type.label]]", description = "[[RowToList.type.description]]", default_value = "record", default_value_type = DataType.STRING)
            @NotEmpty
                    String type,
            @Idx(index = "1.1.1", type = RECORD)
            @Pkg(label = "[[RowToList.record.label]]",description = "[[RowToList.record.description]]")
            @NotEmpty
                    Record record,
            @Idx(index = "1.2.1", type = TABLE)
            @Pkg(label = "[[RowToList.table.label]]",description = "[[RowToList.table.description]]")
            @NotEmpty
                    Table Tabela,
            @Idx(index = "1.2.2", type = NUMBER)
            @Pkg(label = "[[RowToList.row.label]]",description = "[[RowToList.row.description]]")
            @NotEmpty
            @GreaterThanEqualTo("0")
                    Double row
    ) {
        List<Value> OutPut = new ArrayList<Value>();

        if(type.equals("record")){
            for(Value val: record.getValues()){
                OutPut.add(val);
            }
        }else{
            Row linha = Tabela.getRows().get(row.intValue());
            for(Value val: linha.getValues()){
                OutPut.add(val);
            }
        }

        ListValue rtrn =  new ListValue();
        rtrn.set(OutPut);
        return rtrn;
    }
}
