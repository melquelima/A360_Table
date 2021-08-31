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
import com.automationanywhere.commandsdk.annotations.rules.NotEquals;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be displayable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "TableToXLSX",
        label = "TableToXLSX",
        icon = "pkg.svg",

        description = "[[TableToXLSX.description]]",
        node_label = "[[TableToXLSX.node_label]]",
        return_description = "[[TableToXLSX.return_description]]",

        return_type = STRING,
        return_required = false
)

public class TableToXLSX {
    @Execute
    public StringValue action(
            @Idx(index = "1", type = TABLE)
            @Pkg(label = "[[TableToXLSX.table.label]]",description = "[[TableToXLSX.table.description]]")
            @NotEmpty
            Table tabela,

            @Idx(index = "2", type = FILE)
            @Pkg(label = "[[TableToXLSX.output.label]]",description = "[[TableToXLSX.output.description]]")
            @FileExtension("xlsx")
            @NotEmpty
            String outputFile

) {
        //Business logic
        try{

            //Convert to XLSX using Apache POI
            String xlsxFileAddress = outputFile; //xlsx file address
            XSSFWorkbook workBook = new XSSFWorkbook();
            XSSFSheet sheet = workBook.createSheet("Sheet1");


            //======================================================COLUMNS
            int ColNum=0;
            FindInListSchema fnd = new FindInListSchema(tabela.getSchema());
            XSSFRow currentRowCol=sheet.createRow(0);
            for(String schema : fnd.shemaNames()){
                currentRowCol.createCell(ColNum++).setCellValue(schema);
            }


            //======================================================ROWS
            int RowNum=1;
            for(Row rw: tabela.getRows()){
                XSSFRow currentRow=sheet.createRow(RowNum++);
                ColNum = 0;
                for(Value col:rw.getValues()){
                    currentRow.createCell(ColNum++).setCellValue(col.toString());
                }
            }

            FileOutputStream fileOutputStream =  new FileOutputStream(xlsxFileAddress);
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
