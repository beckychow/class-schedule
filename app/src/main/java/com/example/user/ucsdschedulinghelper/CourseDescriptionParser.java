package com.example.user.ucsdschedulinghelper;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Huayin Zhou on 10/24/15.
 * To parse html url of course description.
 * example: "http://www.ucsd.edu/catalog/courses/CSE.html"
 */

//TODO: in parseContentToDB()

public class CourseDescriptionParser extends MyHtmlParser {

    public CourseDescriptionParser(Activity activity, String url) {
        super(activity, url);
    }

    void parseContentToDB() {

        final String DIVISION_TAG = "<h2 class=\"course-subhead-1\">";
        final String ID_TAG = "<a id=\"";
        final String NAME_TAG = "<p class=\"course-name\">";
        final String DESCRIPTION_TAG = "<p class=\"course-descriptions\">";


        // Lower division, Upper division, Graduate
        int idx = content.indexOf(DIVISION_TAG);
        final String[] division = {"Lower", "Upper", "Graduate"};
        int divi = 0;
        while (idx != -1) {
            int next_idx = content.indexOf(DIVISION_TAG, idx + DIVISION_TAG.length());
            // get description for each class in one division
            while (idx < next_idx) {
                int tmp_idx;
                String id, name, full_name, description;
                int unit;

                // get course id
                idx = content.indexOf(ID_TAG, idx) + ID_TAG.length();
                tmp_idx = content.indexOf('"', idx);
                id = content.substring(idx, tmp_idx);
                // get name
                idx = content.indexOf(NAME_TAG, idx) + NAME_TAG.length();
                tmp_idx = content.indexOf('.', idx);
                name = content.substring(idx, tmp_idx);
                name = removeExtraSpace(name);
                // get full name
                idx = tmp_idx + 2;
                tmp_idx = content.indexOf('(', idx);
                full_name = content.substring(idx,tmp_idx - 1);
                full_name = removeExtraSpace(full_name);
                // get unit
                // TODO: resolve issue of varied unit in same course
                idx = tmp_idx + 1;
                tmp_idx = content.indexOf(')', idx);
                unit = Integer.parseInt(content.substring(idx, tmp_idx));
                // get description
                idx = content.indexOf(DESCRIPTION_TAG, idx) + DESCRIPTION_TAG.length();
                tmp_idx = content.indexOf("<strong", idx) - 1;
                description = content.substring(idx, tmp_idx);
                description = removeExtraSpace(description);
                // TODO: get prerequisites
                // TODO: division category
                // test
                changeText(id, name, full_name, description, unit);
            }
            idx = next_idx;
        }
    }

    private String removeExtraSpace(String s) {
        boolean space = false;
        char[] charArray = new char[s.length()];
        int len = 0;
        for (int i = 0; i < s.length(); ++i)
        {
            int tmp = s.charAt(i);
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


    private void changeText(String id, String name, String full_name,
                            String description, int unit) {
        final TextView textView = (TextView) myActivity.findViewById(R.id.message);
        String newString = textView.getText() + "\n" + id + " " + name + " " + full_name + " "
                + description + " " + Integer.toString(unit);
        textView.setText(newString);
    }
}
