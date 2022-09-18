package com.example.dpit2020navem.ObjectTypeDetailes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dpit2020navem.R;

import java.util.HashMap;
import java.util.List;

public class ObjectTypeDetailesListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<ListHeader> listType;
    private HashMap< ListHeader , List<String> > listDetailes;

    public ObjectTypeDetailesListAdapter(Context context, List<ListHeader> listType, HashMap< ListHeader , List<String> > listDetailes) {
        this.context = context;
        this.listType = listType;
        this.listDetailes = listDetailes;
    }
    @Override
    public int getGroupCount() {
        return listType.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listDetailes.get(listType.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listType.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listDetailes.get(listType.get(i)).get(i1); // i = Group Item , i1 = ChildItem
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)listType.get(i).getType();
        Integer headerPicture = (Integer) listType.get(i).getPicture();
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_object_type_detailes_list_group,null);
        }
        TextView listHeaderType = (TextView)view.findViewById(R.id.type);
        listHeaderType.setText(headerTitle);
        ImageView listHeaderPicture = (ImageView)view.findViewById(R.id.picture);
        listHeaderPicture.setImageResource(headerPicture);

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
            view = inflater.inflate(R.layout.layout_object_type_detailes_list_item,null);
        }

        TextView listChildDetail = (TextView)view.findViewById(R.id.detailes);
        listChildDetail.setText(childText);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
