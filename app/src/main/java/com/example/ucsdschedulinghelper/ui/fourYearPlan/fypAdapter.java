package com.example.ucsdschedulinghelper.ui.fourYearPlan;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.ucsdschedulinghelper.R;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Freddy on 11/14/2015.
 */
public class fypAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private HashMap<String, List<String>> classes_cat;
    private List<String> classes_list;

    public fypAdapter(Context ctx, HashMap classes_cat, List classes_list){
        this.ctx = ctx;
        this.classes_cat = classes_cat;
        this.classes_list = classes_list;
    }

    @Override
    public int getGroupCount() {
        return classes_list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return classes_cat.get(classes_list.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return classes_list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return classes_cat.get(classes_list.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String group_title = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fyp_parent_layout, parent, false);
        }
        TextView parent_textview = (TextView) convertView.findViewById(R.id.parent_txt);
        parent_textview.setTypeface(null, Typeface.BOLD);
        parent_textview.setText(group_title);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String child_title = (String) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fyp_child_layout, parent, false);
        }
        TextView child_textview = (TextView) convertView.findViewById(R.id.child_txt);
        child_textview.setText(child_title);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
