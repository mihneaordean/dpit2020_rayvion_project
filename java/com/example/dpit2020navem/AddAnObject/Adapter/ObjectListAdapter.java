package com.example.dpit2020navem.AddAnObject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.R;

import java.util.List;

public class ObjectListAdapter extends ArrayAdapter<OwnedObject> {
    private Context mContext;
    int mResource;

    public ObjectListAdapter(Context context, int resource, List<OwnedObject> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String ownedObjectName = getItem(position).getOwnedObjectName();
        Long ownedObjectId = getItem(position).getOwnedObjectId();
        Integer ownedObjectRemainingDisinfectionTime  = getItem(position).getOwnedObjectDisinfectionTime();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);


        TextView tvObjectName = (TextView) convertView.findViewById(R.id.tvObjectName);

        tvObjectName.setText(ownedObjectName);

        return convertView;
    }
}
