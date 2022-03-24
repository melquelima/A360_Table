import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.samples.commands.basic.ColumnToList;
import com.automationanywhere.botcommand.samples.commands.conditionals.HasAllHeaders;
import com.automationanywhere.botcommand.samples.commands.utils.FindInListSchema;
import com.automationanywhere.botcommand.samples.commands.utils.WorkbookHelper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ColumnToListTest {
    //@Test
    public void teste() throws IOException {
        String inputFile = "C:\\Users\\melque\\Documents\\teste2.xlsx";
        //XSSFWorkbook myWorkBook = WorkbookHelper(inputFile);
        WorkbookHelper wbH = new WorkbookHelper(inputFile);
        Workbook myWorkBook = wbH.wb;


//        CellStyle unlockedCellStyle = myWorkBook.createCellStyle();
//        unlockedCellStyle.setLocked(true);

        Sheet sht = myWorkBook.getSheet("Plano C.C.2020 NOVO");
        CellReference cr = new CellReference("B2");
        org.apache.poi.ss.usermodel.Row row = sht.getRow(cr.getRow());

        Cell cell = row.getCell(cr.getCol());

        CellStyle unlockedCellStyle = cell.getCellStyle();
        unlockedCellStyle.setLocked(true);

        cell.setCellStyle(unlockedCellStyle);

        try (FileOutputStream out = new FileOutputStream(inputFile)) {
            myWorkBook.write(out);
        } catch (FileNotFoundException ex) {
            System.out.println("Expected First Value: " + ex.getMessage());
        }

        //wbH.getColumns()

//        ColumnToList a = new ColumnToList();
//        Table tb = this.tabela();
//
//        ListValue ret = a.action(tb,"TEST");
//        System.out.println("==================" + ret.get(0));
    }
    //@Test
    public void HasAllHeaders(){
        HasAllHeaders a = new HasAllHeaders();
        Table tb = this.tabela();

        List<Value> list = new ArrayList<>();
        list.add(new StringValue("TEST"));
        list.add(new StringValue("USD"));
        list.add(new StringValue("BRL "));

        ListValue<Value> l = new ListValue<>();
        l.set(list);


        //boolean ret = a.validate(tb,l,true);
        //System.out.println(ret);

    }

    //@Test
    public void col2list(){
        ColumnToList a = new ColumnToList();
        Table tb = this.tabela();

        ListValue ret = a.action(tb,"index","TESTs",3.0);
        System.out.println(ret.get());

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