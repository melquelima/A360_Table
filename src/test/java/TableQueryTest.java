import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.samples.commands.basic.TableQuery;
import com.automationanywhere.botcore.api.dto.AttributeType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class TableQueryTest {
    //@Test
    public void teste(){
        TableQuery a = new TableQuery();

        //TableValue tbv = new TableValue();
        Table searchResult = new Table();
        List<Schema> header = new ArrayList<Schema>();
        List<Row> rows = new ArrayList<Row>();
        List<Value> currentRow = new ArrayList<>();
        Row rw = new Row();

        //CRIA AS COLUNAS

        //new Schema("createdAt");

        //new Schema("id");
        //Schema sc = new Schema();
        header.add(new Schema("TEST"));
        header.add(new Schema("USD"));
        header.add(new Schema("BRL"));
        searchResult.setSchema(header);

        //ADCIONA A LINHA
        currentRow.add(new StringValue("ROW'1COL1"));
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
        currentRow.add(new StringValue(""));
        currentRow.add(new StringValue("4.658,58"));
        rw.setValues(currentRow);
        rows.add(rw);

//
        searchResult.setRows(rows);
//
        //tbv.set(searchResult);
//
        ListValue<String> values = a.action(searchResult,3.0,"TEST|USD|BRL",true,"USD","BRL");

        System.out.println("Expected First Value: " + values.get(0));
    }
}