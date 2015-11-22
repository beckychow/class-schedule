package com.example.ucsdschedulinghelper.ui.cape;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ucsdschedulinghelper.R;
import com.example.ucsdschedulinghelper.parser.CapeParser;
import com.example.ucsdschedulinghelper.parser.OnDataSendToActivity;

import java.util.List;

public class CapeMainActivity extends AppCompatActivity implements OnDataSendToActivity {
    static final String DEBUG_TAG = "CapeMainActivity";
    static final String CAPE_MAIN_PAGE = "http://cape.ucsd.edu/responses/Results.aspx?courseNumber=";
    static final String HEADER_LINK = "Course";
    static final int COLOR_TABLE = Color.WHITE;
    static final int COLOR_HEADER = Color.CYAN;
    static final int COLOR_ENTRY = Color.WHITE;
    List<List<String>> tableContent;
    String department, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_cape_main);
        Intent intent = getIntent();
        department = intent.getStringExtra("department");
        code = intent.getStringExtra("code");
        CapeParser capeParser = new CapeParser(this, CAPE_MAIN_PAGE + department + "+" + code);
    }

    public void sendData(List table) {
        tableContent = table;
        addTableView();
    }

    private void addTableView() {
        // create a table layout
        TableLayout tableView = createTable();

        // add table layout to the existing layout
        //LinearLayout layout = (LinearLayout) findViewById(R.id.activity_cape_main);
        //layout.addView(tableView);
        setContentView(tableView);
    }

    private TableLayout createTable() {
        Log.d(DEBUG_TAG, department + "+" + code);

        // set layout parameters
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1f);
        TableLayout.LayoutParams itemParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f);

        // create table layout
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(tableParams);
        tableLayout.setBackgroundColor(COLOR_TABLE);

        // fill table entries
        Log.d(DEBUG_TAG, "Table Size: " + tableContent.size());
        // header
        //if (tableContent != null) {
            int headerLinkCol = 0;
            {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(rowParams);
                for (int col = 0; col < tableContent.get(0).size(); ++col)
                    if (!tableContent.get(0).get(col).equals(HEADER_LINK)) {
                        TextView textView = new TextView(this);
                        textView.setLayoutParams(itemParams);
                        textView.setBackgroundColor(COLOR_HEADER);
                        textView.setText(tableContent.get(0).get(col));
                        tableRow.addView(textView);
                        Log.d(DEBUG_TAG, textView.getText().toString());
                    } else headerLinkCol = col;
                tableLayout.addView(tableRow);
            }
            // entries
            for (int row = 1; row < tableContent.size(); ++row) {
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(rowParams);
                for (int col = 0; col < tableContent.get(row).size(); ++col)
                    if (col != headerLinkCol) {
                        TextView textView = new TextView(this);
                        textView.setLayoutParams(itemParams);
                        textView.setBackgroundColor(COLOR_ENTRY);
                        textView.setText(tableContent.get(row).get(col));
                        tableRow.addView(textView);
                        Log.d(DEBUG_TAG, textView.getText().toString());
                    }
                tableLayout.addView(tableRow);
            }
        //}
        return tableLayout;
    }

}
