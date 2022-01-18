import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.botcommand.samples.commands.basic.ColumnToList;
import com.automationanywhere.botcommand.samples.commands.basic.TableToXLSX;
import com.automationanywhere.botcommand.samples.commands.basic.XlsxToTable;
import com.automationanywhere.botcommand.samples.commands.utils.Assets;
import com.automationanywhere.botcommand.samples.commands.utils.FindInListSchema;
import com.automationanywhere.botcommand.samples.commands.utils.WorkbookHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import org.apache.poi.ss.usermodel.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TableToXLSXTest {
    @Test
    public void teste(){
        Table tb = this.tabela();
        XlsxToTable a = new XlsxToTable();

        //========================================= FILE
        String File = "C:\\Users\\melque\\Downloads\\Copy of MR_Easy_Accounts.xlsx";
        //String File = "C:\\Users\\melque\\Documents\\Git\\AA_Packages\\A360-FileConversionPackage\\src\\main\\resources\\test_files\\SampleFilesSource\\SampleExcel.xlsx";
        //String File = "C:\\Users\\melque\\Documents\\teste3.xlsx";

        //========================================= GET SHEET
        String getSheetBy = "index";// "index"
        String sheetName = "AGNIVJU";
        Double sheetIndex = 0.0;
        Boolean RowStartCheck = true;
        Double RowStart = 1.0;
        //======================================== GET RANGE

        //======================================== OTHERS
        Boolean hasHeaders = false;
        String Columns = "A:K";

       TableValue tbs = a.action(File,getSheetBy,sheetName,sheetIndex,Columns,hasHeaders,RowStartCheck,RowStart);
       uteisTest.printTable(tbs.get(),15);
        //System.out.println(tbs.get().getRows().size());
    }

    //@Test
    public void teste2(){
        Table tb = this.tabela();

        uteisTest.printTable(tb,20);
        Table tb2 = this.deleteRows(tb,"range","0|-1");
        uteisTest.printTable(tb2,20);
        //System.out.println(this.getNumbersInRange(1,3));
    }


    public Table deleteRows(Table tabela,String combo1,String strDelLines){

        List<Integer> idxRows = new ArrayList<>();

        switch (combo1){
            case "firstRow":
                idxRows.add(0);
                break;
            case "lastRow":

                break;
            case "range":
                //========================================== AJUSTA INDEXES ==================
                List<String> delLines = Arrays.asList(strDelLines.split("\\|"));

                Integer newId;
                    for(String idx: delLines) {
                        newId = idxArr(Integer.parseInt(idx), tabela.getRows());
                        idxRows.add(newId);
                    }
                break;
        }
        List<Row> newTable = new ArrayList<>();
        Integer idx = 0;
        for(Row rw : tabela.getRows()){
            if(!idxRows.contains(idx)){
                newTable.add(rw);
            }
            idx++;
        }
        tabela.setRows(newTable);
        return tabela;

    }

    private Integer idxArr(Integer idx,List<?> arr){
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        if (idx >= 0) {
            if(idx >= arr.size()){
                throw new BotCommandException("Index '" + idx + "' higher than file rows!");
            }
            return idx;
        }

        for(int i=arr.size()-1;i>-1;i--){
            indexes.add(i);
        }
        if((idx*-1)-1 > (arr.size()-1)){
            throw new BotCommandException("Index '" + idx + "' not found in List object");
        }

        return indexes.get((idx*-1)-1);
    }

    //@Test
    public void TableToXLSX(){
        TableToXLSX a = new TableToXLSX();
        Table tb = this.tabela();

        StringValue ret = a.action(tb,"C:\\Temp\\teste.xlsx");
        System.out.println("==================" + ret.toString());
    }

    private Table tabela(){
        //TableValue tbv = new TableValue();
        Table searchResult = new Table();
        List<Schema> header = new ArrayList<Schema>();
        List<Row> rows = new ArrayList<Row>();
        List<Value> currentRow = new ArrayList<>();
        Row rw = new Row();

        //CRIA AS COLUNAS
        header.add(new Schema("TEST"));
        header.add(new Schema("USD"));
        header.add(new Schema("BRL"));
        searchResult.setSchema(header);

        //ADCIONA A LINHA
        currentRow.add(new StringValue("ROW1COL1"));
        currentRow.add(new StringValue("1,456.25"));
        currentRow.add(new StringValue("12"));
        rw.setValues(currentRow);
        rows.add(rw);

        //SEGUNDA LINHA
        currentRow = new ArrayList<>();
        rw = new Row();
        currentRow.add(new StringValue("ROW2COL1"));
        currentRow.add(new StringValue("25.42"));
        currentRow.add(new StringValue(""));
        rw.setValues(currentRow);
        rows.add(rw);

        //TERCEIRA LINHA
        currentRow = new ArrayList<>();
        rw = new Row();
        currentRow.add(new StringValue("ROW3COL1"));
        currentRow.add(new StringValue("HEHE"));
        currentRow.add(new StringValue("4.658,58"));
        rw.setValues(currentRow);
        rows.add(rw);

        searchResult.setRows(rows);
        return searchResult;
    }

}