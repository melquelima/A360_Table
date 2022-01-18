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
import org.apache.poi.ss.formula.functions.T;

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
        //List<String> SCHEMA_NAMES = new ArrayList();

        List<String> SCHEMA_INPUT = new ArrayList();

        SCHEMA_INPUT = Arrays.asList(colunas.split("\\|"));
        List<Integer> SCHEMA_IDX = new ArrayList<>();
        List<String> SCHEMA_NAMES = new ArrayList<>();

        System.out.println(SCHEMA_INPUT);
        for (int idx=0;idx<SCHEMA_INPUT.size();idx++) {
            String sc = SCHEMA_INPUT.get(idx);
            if(isNumeric(sc)) {
                Integer idxInt = Integer.parseInt(sc);
                Integer idx2 = idxReverse(fnd.schemas,idxInt);
                System.out.println("IDX2:" + idx2 + " INT:" + idxInt);
                if(idx2 >= fnd.schemas.size()){
                    throw new BotCommandException("Column index '" + sc + "' not found!");
                }
                SCHEMA_INPUT.set(idx,fnd.shemaNames().get(idx2));
                SCHEMA_IDX.add(idx2);
                SCHEMA_NAMES.add(fnd.shemaNames().get(idx2));
            }else{
                if (!fnd.exists(sc)) {
                    throw new BotCommandException("Column '" + sc + "' not found!");
                }
                List<Integer> idxs = fnd.indexSchema(Arrays.asList(new String[] { sc }));
                System.out.println("IDS:" + idxs);
                SCHEMA_IDX.addAll(idxs);

                for(int i:idxs){
                    SCHEMA_NAMES.add(sc);
                }
            }
        }

        System.out.println("SCHEMA NAMES:" + SCHEMA_INPUT);
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
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Integer idxReverse(List<?> arr,Integer idx){
        if(idx >=0){
            return idx;
        }else{
            int idx2 = Math.abs(idx);
            return arr.size()-idx2;
        }

    }

}
