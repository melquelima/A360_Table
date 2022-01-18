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
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
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

import static com.automationanywhere.commandsdk.model.AttributeType.TABLE;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
//import MaskFormatter;

//import java.Math;
//import Math;

@BotCommand
@CommandPkg(
        label = "TrimHeaders",
        name = "TrimHeaders",
        icon = "pkg.svg",
        description = "[[TrimHeaders.description]]",
        node_label = "[[TrimHeaders.node_label]]",
        return_description = "[[TrimHeaders.return_description]]",
        return_type = DataType.TABLE,
        return_required = true
)


public class TrimHeaders {

    @Execute
    public TableValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[TrimHeaders.table.label]]",description = "[[TrimHeaders.table.description]]")
            @NotEmpty
                    Table Tabela
    ) {
        //============================================================ CHECKING COLUMNS
        List<Schema> SCHEMAS = new ArrayList<>(Tabela.getSchema());
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);
        List<String> SCHEMA_NAMES = fnd.shemaNames();


        for(int idx=0;idx<SCHEMA_NAMES.size();idx++){
            SCHEMA_NAMES.set(idx,SCHEMA_NAMES.get(idx).trim());
        }

        FindInListSchema fnd2 = new FindInListSchema(SCHEMA_NAMES);

        //FindInListSchema SCHEMA = new FindInListSchema(SCHEMA_NAMES);
        Tabela.setSchema(fnd2.schemas);
        TableValue OUTPUT = new TableValue();
        OUTPUT.set(Tabela);
        return OUTPUT;
    }

}
