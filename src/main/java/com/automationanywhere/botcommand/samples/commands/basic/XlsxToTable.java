package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.commands.utils.FindInListSchema;
import com.automationanywhere.botcommand.samples.commands.utils.WorkbookHelper;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.FileExtension;
import com.automationanywhere.commandsdk.annotations.rules.GreaterThanEqualTo;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.model.DataType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be displayable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "XlsxToTable",
        label = "XlsxToTable",
        icon = "pkg.svg",

        description = "[[XlsxToTable.description]]",
        node_label = "[[XlsxToTable.node_label]]",
        return_description = "[[XlsxToTable.return_description]]",

        return_type = DataType.TABLE,
        return_required = true
)

public class XlsxToTable {
    @Execute
    public TableValue action(
            @Idx(index = "1", type = FILE)
            @Pkg(label = "[[XlsxToTable.file.label]]",description = "[[XlsxToTable.file.description]]")
            @FileExtension("xlsx")
            @NotEmpty
            String file,

            @Idx(index = "2", type = SELECT, options = {
                    @Idx.Option(index = "2.1", pkg = @Pkg(label = "ByName", value = "name")),
                    @Idx.Option(index = "2.2", pkg = @Pkg(label = "ByIndex", value = "index"))})
            @Pkg(label = "[[XlsxToTable.sheetby.label]]",description = "[[XlsxToTable.sheetby.description]]", default_value = "name", default_value_type = DataType.STRING)
            @NotEmpty
            String getSheetBy,

            @Idx(index = "2.1.1", type = TEXT)
            @Pkg(label = "[[XlsxToTable.sheetname.label]]",description = "[[XlsxToTable.sheetname.description]]")
            @NotEmpty
            String sheetName,

            @Idx(index = "2.2.1", type = NUMBER)
            @Pkg(label = "[[XlsxToTable.sheetidx.label]]",description = "[[XlsxToTable.sheetidx.description]]")
            @NotEmpty
            @GreaterThanEqualTo(value = "0")
            Double sheetIndex,

            @Idx(index = "3", type = TEXT)
            @Pkg(label = "[[XlsxToTable.cols.label]]",description = "[[XlsxToTable.cols.description]]")
            @NotEmpty
            String Columns,

            @Idx(index = "4", type = CHECKBOX)
            @Pkg(label = "[[XlsxToTable.header.label]]",description = "[[XlsxToTable.header.description]]",default_value = "false",default_value_type = DataType.BOOLEAN)
            @NotEmpty
            Boolean hasHeaders,

            @Idx(index = "5", type = CHECKBOX)
            @Pkg(label = "[[XlsxToTable.startcheck.label]]",description = "[[XlsxToTable.startcheck.description]]",default_value = "false",default_value_type = DataType.BOOLEAN)
            @NotEmpty
                    Boolean RowStartCheck,

            @Idx(index = "5.1", type = NUMBER)
            @Pkg(label = "[[XlsxToTable.startrow.label]]",description = "[[XlsxToTable.startrow.description]]")
            @NotEmpty
            @GreaterThanEqualTo(value = "1")
                    Double RowStart
) {
            //================================================================= CREATE WORKBOOK OBJECT
            //XSSFWorkbook myWorkBook = WorkbookHelper(file);
            WorkbookHelper wbH = new WorkbookHelper(file);
            //================================================================= VALIDATE RANGE COLUMNS
            List<Integer> colsToreturn = wbH.PatternColumnsToListIndex(Columns);

            //================================================================= GET SHEET
            Sheet mySheet = this.getSheet(getSheetBy,sheetName,sheetIndex,wbH);

            //================================================================= GET ROWS
            List<org.apache.poi.ss.usermodel.Row> ROWS = wbH.getRows(mySheet);
            List<Row> listRows= new ArrayList<>();
            List<String> HEADERS = new ArrayList<>();

            Integer idxs =0;
            for(org.apache.poi.ss.usermodel.Row rw: ROWS){
                System.out.println(idxs++);
                    List<Cell> listCol = wbH.getColumns(rw);
                    //System.out.println(listCol);
                    List<Value> rwValue = new ArrayList<>();

                    if(RowStartCheck && RowStart-- > 1) continue;
                    if(HEADERS.size() == 0) {
                            Integer idx = 0;
                            for (Integer colIdx : colsToreturn) {
                                    if(hasHeaders) {
                                            if(colIdx > (listCol.size()-1)){
                                                HEADERS.add("");
                                            }else {
                                                HEADERS.add(wbH.getCellValue(listCol.get(colIdx)).toString());
                                            }
                                    }else{
                                            HEADERS.add(idx.toString());
                                    }
                                    idx++;
                            }
                            if(hasHeaders) continue;
                    }
                    //System.out.println(HEADERS);

                    for(Integer colIdx: colsToreturn){
                            if(colIdx <= (listCol.size()-1)){
                                    Cell col = listCol.get(colIdx);
                                    rwValue.add(wbH.getCellValue(col));
                                    //System.out.print(wbH.getCellValue(col) + "\t");
                            }else{
                                    rwValue.add(new StringValue(""));
                                    //System.out.print("EMPTY\t");
                            }
                    }
                    listRows.add(new Row(rwValue));
                    //System.out.println("");
            }

            FindInListSchema fnd = new FindInListSchema(HEADERS);

            //System.out.println(HEADERS);
            Table OUTPUT = new Table(fnd.schemas,listRows);

            return new TableValue(OUTPUT);
    }

        private Sheet getSheet(String getSheetBy, String sheetName, Double sheetIndex, WorkbookHelper wbH){
                Sheet mySheet = null;
                if(getSheetBy.equals("name")){
                        if(wbH.sheetExists(sheetName)){
                                wbH.wb.getSheet(sheetName);
                                mySheet = wbH.wb.getSheet(sheetName);
                        }else{
                                throw new BotCommandException("Sheet '" + sheetName + "' not found!");
                        }
                }else {
                        if(wbH.sheetExists(sheetIndex.intValue())){
                                mySheet = wbH.wb.getSheetAt(sheetIndex.intValue());
                        }else{
                                throw new BotCommandException("Sheet index '" + sheetIndex.intValue() + "' not found!");
                        }
                }
                return mySheet;
        }

}
