package com.example.dpit2020navem.Help;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.dpit2020navem.R;

import java.util.HashMap;
import java.util.List;

public class QuestionsAnswearsListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listQuestions;
    private HashMap<String, List<String>> listAnswers;

    public QuestionsAnswearsListAdapter(Context context, List<String> listQuestions, HashMap<String, List<String>> listAnswers) {
        this.context = context;
        this.listQuestions = listQuestions;
        this.listAnswers = listAnswers;
    }

    @Override
    public int getGroupCount() {
        return listQuestions.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listAnswers.get(listQuestions.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listQuestions.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listAnswers.get(listQuestions.get(i)).get(i1); // i = Group Item , i1 = ChildItem
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_help_activity_list_group,null);
        }
        TextView listHeaderQuestion = (TextView)view.findViewById(R.id.question);
        listHeaderQuestion.setText(headerTitle);

        if(b){
            view.setBackgroundResource(R.color.gray);
        }else{
            view.setBackgroundResource(R.color.white);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childText = (String)getChild(i,i1);
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_help_activity_list_item,null);
        }

        TextView listChildAnswear = (TextView)view.findViewById(R.id.answer);
        listChildAnswear.setText(childText);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
