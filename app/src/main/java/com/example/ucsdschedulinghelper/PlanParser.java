package com.example.ucsdschedulinghelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Huayin Zhou on 11/15/15.
 */
public class PlanParser extends MyHtmlParser {

    public PlanParser(Activity activity, String url) {
        super(activity, url);
    }

    void parseContentToDB() { new ParseContentToDBTask().execute(fetchDataFromHttp); }

    private class ParseContentToDBTask extends ParseTask {
        @Override
        public void onPreExecute() {

        }

        @Override
        public Void doInBackground(FetchDataFromHttp... params) {
            try {
                JSONArray jsonArray = new JSONArray(fetchDataFromHttp.getResults());
                JSONObject plan = jsonArray.getJSONObject(0);
                JSONArray courseList = plan.getJSONArray("courses");
                for (int i_courseList = 0; i_courseList < courseList.length(); ++i_courseList) {
                    JSONArray yearList = courseList.getJSONArray(i_courseList);
                    for (int i_yearList = 0; i_yearList < yearList.length(); ++i_yearList) {
                        JSONArray quarterList = yearList.getJSONArray(i_yearList);
                        for (int i_quarterList = 0; i_quarterList < yearList.length(); ++i_quarterList) {
                            // get one course info
                            JSONObject course = quarterList.getJSONObject(i_quarterList);
                            String course_name = course.getString("course_name");
                            String year_taken = course.getString("year_taken");
                            String quarter_taken = course.getString("quarter_taken");
                            // test
                            changeText(course_name, year_taken, quarter_taken);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private void changeText(String course_name, String year_taken, String quarter_taken) {
        final TextView textView = (TextView) myActivity.findViewById(R.id.message);
        textView.setMovementMethod(new ScrollingMovementMethod());
        String newString = textView.getText() + "\n" +
                " -- course_name: " + course_name + "\n" +
                " -- year_taken: " + year_taken + "\n" +
                " -- quarter_taken: " + quarter_taken + "\n" +
                "  <<-->>\n";
        textView.setText(newString);
    }
}
