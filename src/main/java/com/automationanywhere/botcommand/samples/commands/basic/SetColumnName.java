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
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.TABLE;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.AttributeType.*;
//import MaskFormatter;

//import java.Math;
//import Math;

@BotCommand
@CommandPkg(
        label = "SetColumnName",
        name = "SetColumnName",
        icon = "pkg.svg",
        description = "[[SetColumnName.description]]",
        node_label = "[[SetColumnName.node_label]]",
        return_description = "[[SetColumnName.return_description]]",
        return_type = DataType.TABLE,
        return_required = true
)


public class SetColumnName {

    @Execute
    public TableValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[SetColumnName.table.label]]",description = "[[SetColumnName.table.description]]")
            @NotEmpty
                    Table Tabela,

            @Idx(index = "2", type = SELECT, options = {
                    @Idx.Option(index = "2.1", pkg = @Pkg(label = "ByName", value = "name")),
                    @Idx.Option(index = "2.2", pkg = @Pkg(label = "ByIndex", value = "index"))})
            @Pkg(label = "[[SetColumnName.col.label]]", description = "[[SetColumnName.col.description]]", default_value = "name", default_value_type = DataType.STRING)
            @NotEmpty
                    String TypeIndex,

            @Idx(index = "2.1.1", type = TEXT)
            @Pkg(label = "[[SetColumnName.colname.label]]",description = "[[SetColumnName.colname.description]]")
            @NotEmpty
                    String ColumnName,

            @Idx(index = "2.2.1", type = NUMBER)
            @Pkg(label = "[[SetColumnName.colidx.label]]" ,description = "[[SetColumnName.colidx.description]]")
            @NotEmpty
                    Double ColumnIndex,

            @Idx(index = "3", type = TEXT)
            @Pkg(label = "[[SetColumnName.colnew.label]]", description ="[[SetColumnName.colnew.label]]")
            @NotEmpty
                    String NewColumnName

    ) {
        //============================================================ CHECKING COLUMN
        List<Schema> SCHEMAS = new ArrayList<>(Tabela.getSchema());
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);
        List<String> SCHEMA_NAMES = fnd.shemaNames();
        Integer SCHEMA_IDX = null;
        //System.out.println("==================" + Tabela.getRows().get(0).getValues().size());

        if(TypeIndex == "name") {
            if (!fnd.exists(ColumnName)) {
                throw new BotCommandException("Column '" + ColumnName + "' not found!");
            }
            SCHEMA_IDX = fnd.indexSchema(ColumnName);
        }else{
            if((SCHEMA_NAMES.size()-1) >= ColumnIndex.intValue()){
                SCHEMA_IDX = ColumnIndex.intValue();
            }else{
                throw new BotCommandException("Column index '" + ColumnIndex + "' not found!");
            }
        }

        Table tb = new Table(new ArrayList<>(Tabela.getSchema()),new ArrayList<>(Tabela.getRows()));

        fnd.alterSchema(SCHEMA_IDX,NewColumnName);
        tb.setSchema(fnd.schemas);

        TableValue OUTPUT = new TableValue();
        OUTPUT.set(tb);

      return OUTPUT;
    }

}
