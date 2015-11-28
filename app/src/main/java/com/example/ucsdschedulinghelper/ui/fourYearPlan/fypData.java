package com.example.ucsdschedulinghelper.ui.fourYearPlan;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.ucsdschedulinghelper.provider.DbContentProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Freddy on 11/14/2015.
 */
public class fypData {

    public static LinkedHashMap<String, List<String>> getInfo(Context c){
        LinkedHashMap<String, List<String>> classDetails = new LinkedHashMap<String, List<String>>();
        //Add the classes you need for the four year plan for each quarter. As of now add some test
        //data


        Cursor cr = c.getContentResolver().query(Uri.withAppendedPath(DbContentProvider.CONTENT_PLAN_URI,
                DbContentProvider.ALL_PATH_AUX), null, null, null, null);


        List<String> fall_classes = new ArrayList<String>();
        //Iterate over the cursor
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
           if(cr.getString(2).equals("1") && cr.getString(3).equals("1")){
                fall_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> winter_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("1") && cr.getString(3).equals("2")){
                winter_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> spring_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("1") && cr.getString(3).equals("3")){
                spring_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> fall2_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("2") && cr.getString(3).equals("1")){
                fall2_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> winter2_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("2") && cr.getString(3).equals("2")){
                winter2_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> spring2_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("2") && cr.getString(3).equals("3")){
                spring2_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> fall3_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("3") && cr.getString(3).equals("1")){
                fall3_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> winter3_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("3") && cr.getString(3).equals("2")){
                winter3_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> spring3_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("3") && cr.getString(3).equals("3")){
                spring3_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> fall4_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("4") && cr.getString(3).equals("1")){
                fall4_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> winter4_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("4") && cr.getString(3).equals("2")){
                winter4_classes.add(cr.getString(1));
            }
        }
        cr.moveToFirst();

        List<String> spring4_classes = new ArrayList<String>();
        while(cr.moveToNext()){
            //If you should take this class in your first year,first quarter then add to the plan
            if(cr.getString(2).equals("4") && cr.getString(3).equals("3")){
                spring4_classes.add(cr.getString(1));
            }
        }
        cr.close();

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
