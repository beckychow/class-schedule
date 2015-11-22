package com.example.ucsdschedulinghelper.parser;

import android.app.Activity;
import android.util.Log;

import com.example.ucsdschedulinghelper.ui.cape.CapeMainActivity;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.Connection.Response;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Huayin Zhou on 11/21/15.
 */
public class CapeParser extends MyHtmlParser {
    static final String DEBUG_TAG = "CapeParser";
    List capeResult;

    public CapeParser (Activity activity, String url) {
        myActivity = activity;
        urlToParse = url;
        parseContent();
    }

    public void parseContent() {
        Log.d(DEBUG_TAG, "parsing data... " + urlToParse);
        new ParseContentToDisplayTask().execute();
    }

    public List getResult() {
        return capeResult;
    }

    private class ParseContentToDisplayTask extends ParseTask {
        List<List<String>> result;

        @Override
        public void onPreExecute(){

        }

        @Override
        public Void doInBackground(FetchDataFromHttp... params) {
            result = new ArrayList<List<String>>();
            try {
                //Document mainCapePage = Jsoup.parse(params[0].getResults());
                Response response = Jsoup.connect(urlToParse)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .referrer("http://www.google.com")
                        .timeout(15000)
                        .followRedirects(true)
                        .execute();
                Document mainCapePage = response.parse();
                Element table = mainCapePage.select("table").get(0);
                Elements rows = table.select("tr");

                // get header of the table
                try {
                    Elements row = rows.get(0).select("th");
                    List<String> entries = new ArrayList<String>();
                    for (Element entry : row) {
                        entries.add(entry.text());
                        //Log.d(DEBUG_TAG, entry.text());
                    }
                    result.add(entries);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(DEBUG_TAG, e.toString());
                }

                // content of the table
                for (int i = 1; i < rows.size(); ++i) {
                    Elements row = rows.get(i).select("td");
                    List<String> entries = new ArrayList<String>();
                    for (Element entry : row) {
                        if (entry.hasAttr("href"))
                            entries.add(entry.attr("href"));
                        else entries.add(entry.text());
                        //Log.d(DEBUG_TAG, entry.text());
                    }
                    result.add(entries);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void ret){
            OnDataSendToActivity onDataSendToActivity = (OnDataSendToActivity) myActivity;
            onDataSendToActivity.sendData(result);
        }
    }
}
