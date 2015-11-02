package com.example.user.ucsdschedulinghelper;

import android.app.Activity;
import android.util.Log;


/**
 * Created by Huayin Zhou on 10/24/15.
 * Abstract class MyHtmlParser is to parse html url of course description, information or cape.
 */

// TODO: use Async Task for this class
abstract public class MyHtmlParser {
    String urlToParse;
    Activity myActivity;
    String content;
    FetchDataFromHttp fetchDataFromHttp;

    public MyHtmlParser(Activity activity, String url) {
        urlToParse = url;
        myActivity = activity;
        try {
            fetchDataFromHttp = new FetchDataFromHttp(myActivity.getApplicationContext(), urlToParse);
            content = fetchDataFromHttp.getResults();
        } catch (Exception e)
        {
            Log.e("CourseDescriptionParser", "parseContentToDB() " + e);
        }
    }

    abstract void parseContentToDB();
}
