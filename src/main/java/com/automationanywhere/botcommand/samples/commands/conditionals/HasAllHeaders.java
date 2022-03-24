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
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.samples.commands.utils.FindInListSchema;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;

import java.util.List;

import static com.automationanywhere.commandsdk.annotations.BotCommand.CommandType.Condition;
import static com.automationanywhere.commandsdk.model.AttributeType.*;


@BotCommand(commandType = Condition)
@CommandPkg(
        label = "HasAllHeaders",
        name = "HasAllHeaders",
        description = "[[HasAllHeaders.description]]",
        node_label = "[[HasAllHeaders.node_label]]"
)
public class HasAllHeaders {

    @ConditionTest
    public Boolean validate(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[HasAllHeaders.table.label]]",description = "[[HasAllHeaders.table.description]]")
            @NotEmpty
                    Table Tabela,
            @Idx(index = "2", type = LIST)
            @Pkg(label = "[[HasAllHeaders.list.label]]",description = "[[HasAllHeaders.list.description]]")
            @NotEmpty
                List<Value> list,
            @Idx(index = "3", type = CHECKBOX)
            @Pkg(label = "[[HasAllHeaders.btrim.label]]",description = "[[HasAllHeaders.btrim.description]]",default_value = "false",default_value_type = DataType.BOOLEAN)
            @NotEmpty
                    Boolean btrim
    ) {

        List<Schema> SCHEMAS = Tabela.getSchema();
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);
        List<String> SCHEMA_NAMES = fnd.shemaNames();


        if(btrim) {
            for (int idx = 0; idx < SCHEMA_NAMES.size(); idx++) {
                SCHEMA_NAMES.set(idx, SCHEMA_NAMES.get(idx).trim());
            }
            for (int idx = 0; idx < list.size(); idx++) {
                list.set(idx,new StringValue(list.get(idx).toString().trim()));
            }
        }
        for (int idx = 0; idx < list.size(); idx++) {

            if(!SCHEMA_NAMES.contains(list.get(idx).toString())){
                return false;
            }
        }

        return true;
    }

}
