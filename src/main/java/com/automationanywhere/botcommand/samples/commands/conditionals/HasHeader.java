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
package com.automationanywhere.botcommand.samples.commands.conditionals;

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
import java.util.List;

import static com.automationanywhere.commandsdk.annotations.BotCommand.CommandType.Condition;
import static com.automationanywhere.commandsdk.model.AttributeType.*;


@BotCommand(commandType = Condition)
@CommandPkg(
        label = "HasHeader",
        name = "HasHeader",
        description = "[[HasHeader.description]]",
        node_label = "[[HasHeader.node_label]]"
)
public class HasHeader {

    @ConditionTest
    public Boolean validate(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[HasHeader.table.label]]",description = "[[HasHeader.table.description]]")
            @NotEmpty
                    Table Tabela,
            @Idx(index = "2", type = TEXT)
            @Pkg(label = "[[HasHeader.colname.label]]",description = "[[HasHeader.colname.description]]")
            @NotEmpty
                    String colname,
            @Idx(index = "3", type = CHECKBOX)
            @Pkg(label = "[[HasHeader.btrim.label]]",description = "[[HasHeader.btrim.description]]",default_value = "false",default_value_type = DataType.BOOLEAN)
            @NotEmpty
                    Boolean btrim,
            @Idx(index = "4", type = CHECKBOX)
            @Pkg(label = "[[HasHeader.not.label]]",description = "[[HasHeader.not.description]]",default_value = "false",default_value_type = DataType.BOOLEAN)
            @NotEmpty
                    Boolean not
    ) {

        List<Schema> SCHEMAS = Tabela.getSchema();
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);
        List<String> SCHEMA_NAMES = fnd.shemaNames();

        if(btrim) {
            for (int idx = 0; idx < SCHEMA_NAMES.size(); idx++) {
                SCHEMA_NAMES.set(idx, SCHEMA_NAMES.get(idx).trim());
            }
        }
        if(not) {
            return !SCHEMA_NAMES.contains(colname);
        }else{
            return SCHEMA_NAMES.contains(colname);
        }
    }

}
