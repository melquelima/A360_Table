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
import java.util.Arrays;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.TABLE;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
//import MaskFormatter;

//import java.Math;
//import Math;

@BotCommand
@CommandPkg(
        label = "ReorderColumns",
        name = "ReorderColumns",
        icon = "pkg.svg",
        description = "[[ReorderColumns.description]]",
        node_label = "[[ReorderColumns.node_label]]",
        return_description = "[[ReorderColumns.return_description]]",
        return_type = DataType.TABLE,
        return_required = true
)


public class ReorderColumns {

    @Execute
    public TableValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[ReorderColumns.table.label]]",description = "[[ReorderColumns.table.description]]")
            @NotEmpty
                    Table Tabela,
            @Idx(index = "2", type = TEXT)
            @Pkg(label = "[[ReorderColumns.cols.label]]",description = "[[ReorderColumns.cols.description]]")
            @NotEmpty
                    String colunas
    ) {
        //============================================================ CHECKING COLUMNS
        List<Schema> SCHEMAS = new ArrayList<>(Tabela.getSchema());
        FindInListSchema fnd = new FindInListSchema(SCHEMAS);
        List<String> SCHEMA_NAMES = new ArrayList();

        SCHEMA_NAMES = Arrays.asList(colunas.split("\\|"));
        for (String sc : SCHEMA_NAMES) {
            if (!fnd.exists(sc)) {
                throw new BotCommandException("Column '" + sc + "' not found!");
            }
        }
        List<Integer> SCHEMA_IDX = fnd.indexSchema(SCHEMA_NAMES);


        //============================================================ RUN ORDER
        Table TBL_ORDERED = new Table();
        List<Row> ROWS = new ArrayList();

        for (Row rw : Tabela.getRows()) {
            List<Value> rowList = new ArrayList();
            for (Integer colIdx : SCHEMA_IDX) {
                String val = rw.getValues().get(colIdx).toString();
                rowList.add(new StringValue(val));
            }
            Row newRow = new Row(rowList);
            ROWS.add(newRow);
        }

        FindInListSchema SCHEMA = new FindInListSchema(SCHEMA_NAMES);
        TBL_ORDERED.setSchema(SCHEMA.schemas);
        TBL_ORDERED.setRows(ROWS);

        TableValue OUTPUT = new TableValue();
        OUTPUT.set(TBL_ORDERED);
        return OUTPUT;
    }


}
