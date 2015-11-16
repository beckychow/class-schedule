package com.example.ucsdschedulinghelper.ui.fourYearPlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Freddy on 11/14/2015.
 */
public class fypData {

    public static HashMap<String, List<String>> getInfo(){
        HashMap<String, List<String>> classDetails = new HashMap<String, List<String>>();
        //Add the classes you need for the four year plan for each quarter. As of now add some test
        //data
        List<String> fall_classes = new ArrayList<String>();
        fall_classes.add("Test");

        List<String> winter_classes = new ArrayList<String>();
        winter_classes.add("Test");

        List<String> spring_classes = new ArrayList<String>();
        spring_classes.add("Test");

        List<String> fall2_classes = new ArrayList<String>();
        fall2_classes.add("Test");

        List<String> winter2_classes = new ArrayList<String>();
        winter2_classes.add("Test");

        List<String> spring2_classes = new ArrayList<String>();
        spring2_classes.add("Test");

        List<String> fall3_classes = new ArrayList<String>();
        fall3_classes.add("Test");

        List<String> winter3_classes = new ArrayList<String>();
        winter3_classes.add("Test");

        List<String> spring3_classes = new ArrayList<String>();
        spring3_classes.add("Test");

        List<String> fall4_classes = new ArrayList<String>();
        fall4_classes.add("Test");

        List<String> winter4_classes = new ArrayList<String>();
        winter4_classes.add("Test");

        List<String> spring4_classes = new ArrayList<String>();
        spring4_classes.add("Test_1");
        spring4_classes.add("Test_2");

        //Put everything in the hashmap, shouldn't need to mess with this later
        classDetails.put("Freshman Fall", fall_classes);
        classDetails.put("Freshman Winter", winter_classes);
        classDetails.put("Freshman Spring", spring_classes);

        classDetails.put("Sophomore Fall", fall2_classes);
        classDetails.put("Sophomore Winter", winter2_classes);
        classDetails.put("Sophomore Spring", spring2_classes);

        classDetails.put("Junior Fall", fall3_classes);
        classDetails.put("Junior Winter", winter3_classes);
        classDetails.put("Junior Spring", spring3_classes);

        classDetails.put("Senior Fall", fall4_classes);
        classDetails.put("Senior Winter", winter4_classes);
        classDetails.put("Senior Spring", spring4_classes);

        return classDetails;
    }
}
