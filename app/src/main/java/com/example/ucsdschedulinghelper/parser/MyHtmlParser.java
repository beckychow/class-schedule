package com.example.ucsdschedulinghelper.parser;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;


/**
 * Created by Huayin Zhou on 10/24/15.
 * Abstract class MyHtmlParser is to parse html url of course description, information or cape.
 */

abstract public class MyHtmlParser{
    String urlToParse;
    Activity myActivity;
    FetchDataFromHttp fetchDataFromHttp;

    public MyHtmlParser() {}

    public MyHtmlParser(Activity activity, String url) {
        urlToParse = url;
        myActivity = activity;
        try {
            fetchDataFromHttp = new FetchDataFromHttp(myActivity.getApplicationContext(), urlToParse);
            //content = fetchDataFromHttp.getResults();
        } catch (Exception e)
        {
            Log.e("CourseDescriptionParser", "parseContentToDB() " + e);
        }
    }

    abstract protected class ParseTask extends AsyncTask<FetchDataFromHttp, Void, Void>
    {
        @Override
        abstract public Void doInBackground(FetchDataFromHttp... params);

        protected String removeExtraSpaces(String s) {
            boolean space = false;
            char[] charArray = new char[s.length()];
            int len = 0;
            for (int i = 0; i < s.length(); ++i)
            {
                if (s.charAt(i) <= ' ' ||
                        s.charAt(i) > '~')
                {
                    if (space)
                        continue;
                    space = true;
                    charArray[len++] = ' ';
                } else
                {
                    space = false;
                    charArray[len++] = s.charAt(i);
                }
            }
            return String.valueOf(charArray, 0, len);
        }

        protected String removeInsideTags(String s) {
            char[] charArray = new char[s.length()];
            int len = 0;
            boolean insideTag = false;
            for (int i = 0; i < s.length(); ++i) {
                if (s.charAt(i) == '<') insideTag = true;
                else if (s.charAt(i) == '>') insideTag = false;
                else if (!insideTag) {
                    charArray[len++] = s.charAt(i);
                }
            }
            return String.valueOf(charArray, 0, len);
        }
    }

    abstract public void parseContent();
}
