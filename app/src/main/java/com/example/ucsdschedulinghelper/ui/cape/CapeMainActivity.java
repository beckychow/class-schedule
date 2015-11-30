package com.example.ucsdschedulinghelper.ui.cape;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.ucsdschedulinghelper.R;
import com.example.ucsdschedulinghelper.database.CoursesCollectionContract;
import com.example.ucsdschedulinghelper.parser.CapeParser;
import com.example.ucsdschedulinghelper.parser.OnDataSendToActivity;

import java.util.List;

public class CapeMainActivity extends AppCompatActivity
                                implements OnDataSendToActivity, ScrollViewListener {
    static final String DEBUG_TAG = "CapeMainActivity";
    static final String CAPE_MAIN_PAGE = "http://cape.ucsd.edu/responses/Results.aspx?courseNumber=";
    static final String HEADER_LINK = "Course";
    static final int COLOR_TABLE = Color.WHITE;
    static final int COLOR_HEADER = Color.CYAN;
    static final int COLOR_ENTRY = Color.WHITE;
    List<List<String>> tableContent;
    String department, code;
    ObservableHorizontalScrollView headerScroll;
    ObservableHorizontalScrollView tableScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cape_main);
        Intent intent = getIntent();
        department = intent.getStringExtra(CoursesCollectionContract.Course.COLUMN_DEPARTMENT);
        code = intent.getStringExtra(CoursesCollectionContract.Course.COLUMN_CODE);
        new CapeParser(this, CAPE_MAIN_PAGE + department + "+" + code);
        // set scroll listener
        headerScroll = (ObservableHorizontalScrollView) findViewById(R.id.cape_header_scroll);
        tableScroll =  (ObservableHorizontalScrollView) findViewById(R.id.cape_table_scroll);
        tableScroll.setScrollViewListener(this);
    }

    public void onScrollChanged(ObservableHorizontalScrollView view, int x, int y, int oldx, int oldy) {
            headerScroll.scrollTo(x, y);
    }

    public void sendData(List table) {
        tableContent = table;
        addTableView();
    }

    private void addTableView() {
        setTable();
    }

    private void setTable() {
        Log.d(DEBUG_TAG, department + "+" + code);

        // set layout parameters
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1f);
        TableRow.LayoutParams itemParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        // create table layout
        TableLayout fixedHeaderLayout = (TableLayout) findViewById(R.id.cape_header_fixed);
        TableLayout headerLayout = (TableLayout) findViewById(R.id.cape_header);
        TableLayout fixedTableLayout = (TableLayout) findViewById(R.id.cape_table_fixed);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.cape_table);
        tableLayout.setBackgroundColor(COLOR_TABLE);

        // fill table entries
        Log.d(DEBUG_TAG, "Table Size: " + tableContent.size());
        //header
        int headerLinkCol = 0;
        {
            // fixed column
            TableRow fixedTableRow = new TableRow(this);
            fixedTableRow.setLayoutParams(rowParams);
            // other content
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(rowParams);
            for (int col = 0; col < tableContent.get(0).size(); ++col) {
                if (!tableContent.get(0).get(col).equals(HEADER_LINK)) {
                    TextView textView = new TextView(this);
                    textView.setLayoutParams(itemParams);
                    textView.setBackgroundColor(COLOR_HEADER);
                    textView.setText(tableContent.get(0).get(col));
                    textView.setPadding(30, 10, 0, 0);
                    textView.measure(0, 0);
                    //textView.setWidth(textView.getMeasuredWidth());
                    if (col == 0)
                        fixedTableRow.addView(textView);
                    else tableRow.addView(textView);
                    //Log.d(DEBUG_TAG, textView.getText().toString());
                } else headerLinkCol = col;
            }
            fixedHeaderLayout.addView(fixedTableRow);
            headerLayout.addView(tableRow);
        }

        // entries
        for (int row = 1; row < tableContent.size(); ++row) {
            // fixed column
            TableRow fixedTableRow = new TableRow(this);
            fixedTableRow.setLayoutParams(rowParams);
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(rowParams);
            for (int col = 0; col < tableContent.get(row).size(); ++col) {
                if (col != headerLinkCol) {
                    TextView textView = new TextView(this);
                    textView.setLayoutParams(itemParams);
                    textView.setBackgroundColor(COLOR_ENTRY);
                    textView.setText(tableContent.get(row).get(col));
                    textView.setPadding(30, 10, 0, 0);
                    textView.measure(0, 0);
                    // add view to textView and set header width
                    if (col == 0) {
                        fixedTableRow.addView(textView);
                        int currentIdx = fixedTableRow.getChildCount() - 1;
                        TableRow headerRow = (TableRow) fixedHeaderLayout.getChildAt(0);
                        TextView headerView = (TextView) headerRow.getChildAt(currentIdx);
                        if (textView.getMeasuredWidth() > headerView.getMeasuredWidth()) {
                            headerView.setWidth(textView.getMeasuredWidth());
                            headerView.measure(0,0);
                        }
                        else textView.setWidth(headerView.getMeasuredWidth());
                        //Log.d(DEBUG_TAG, String.format("header:%d", headerView.getMeasuredWidth()));
                        //Log.d(DEBUG_TAG, String.format("content:%d", textView.getMeasuredWidth()));
                    } else {
                        tableRow.addView(textView);
                        int currentIdx = tableRow.getChildCount() - 1;
                        TableRow headerRow = (TableRow) headerLayout.getChildAt(0);
                        TextView headerView = (TextView) headerRow.getChildAt(currentIdx);
                        if (textView.getMeasuredWidth() > headerView.getMeasuredWidth()) {
                            headerView.setWidth(textView.getMeasuredWidth());
                            headerView.measure(0,0);
                        }
                        else textView.setWidth(headerView.getMeasuredWidth());
                    }
                    // set header width

                }
            }
            fixedTableLayout.addView(fixedTableRow);
            tableLayout.addView(tableRow);
        }
        fixedHeaderLayout.requestLayout();
        headerLayout.requestLayout();
        fixedTableLayout.requestLayout();
        tableLayout.requestLayout();
    }

}
