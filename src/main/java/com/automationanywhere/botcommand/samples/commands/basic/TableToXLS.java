package com.automationanywhere.botcommand.samples.commands.basic;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.commands.utils.FindInListSchema;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.FileExtension;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;

import static com.automationanywhere.commandsdk.model.AttributeType.FILE;
import static com.automationanywhere.commandsdk.model.AttributeType.TABLE;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be displayable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "TableToXLS",
        label = "TableToXLS",
        icon = "pkg.svg",

        description = "[[TableToXLS.description]]",
        node_label = "[[TableToXLS.node_label]]",
        return_description = "[[TableToXLS.return_description]]",

        return_type = STRING,
        return_required = false

)

public class TableToXLS {
    @Execute
    public StringValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[TableToXLS.table.label]]",description = "[[TableToXLS.table.description]]")
            @NotEmpty
            Table tabela,

            @Idx(index = "2", type = FILE)
            @Pkg(label = "[[TableToXLS.output.label]]",description = "[[TableToXLS.output.description]]")
            @FileExtension("xls")
            @NotEmpty
            String outputFile

) {
        //Business logic
        try{

            //Convert to XLS using Apache POI
            String xlsFileAddress = outputFile; //xls file address
            HSSFWorkbook workBook = new HSSFWorkbook();
            HSSFSheet sheet = workBook.createSheet("Sheet1");



            //======================================================COLUMNS
            int ColNum=0;
            FindInListSchema fnd = new FindInListSchema(tabela.getSchema());
            HSSFRow currentRowCol=sheet.createRow(0);
            for(String schema : fnd.shemaNames()){
                currentRowCol.createCell(ColNum++).setCellValue(schema);
            }


            //======================================================ROWS
            int RowNum=1;
            for(Row rw: tabela.getRows()){
                HSSFRow currentRow=sheet.createRow(RowNum++);
                ColNum = 0;
                for(Value col:rw.getValues()){
                    currentRow.createCell(ColNum++).setCellValue(col.toString());
                }
            }

            FileOutputStream fileOutputStream =  new FileOutputStream(xlsFileAddress);
            workBook.write(fileOutputStream);
            fileOutputStream.close();
            System.out.println("Done");

        } catch (Exception e) {
            throw new BotCommandException("Error occurred during file conversion. Error code: " + e.toString());
        }

        //Return StringValue.
        return new StringValue(outputFile);
    }
}
