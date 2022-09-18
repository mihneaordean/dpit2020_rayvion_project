package com.example.dpit2020navem.HomePage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.Database.OwnedObjectsDatabase;
import com.example.dpit2020navem.R;

import java.util.List;

public class OwnedObjectsListMainPageAdapter extends ArrayAdapter<OwnedObject> {

    private Context mContext;
    int mResource;
    OwnedObjectsDatabase database;
    AddButtonListener addButtonListener;


    public OwnedObjectsListMainPageAdapter(Context context, int resource, List<OwnedObject> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        database = new OwnedObjectsDatabase(mContext);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String ownedObjectName = getItem(position).getOwnedObjectName();
        Integer ownedObjectDisinfectionTime  = getItem(position).getOwnedObjectDisinfectionTime();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);


        TextView ownedObjectsListMainPageName = (TextView) convertView.findViewById(R.id.ownedObjectsListMainPageName);
        TextView ownedObjectsListMainPageDetailes = (TextView) convertView.findViewById(R.id.ownedObjectsListMainPageDetailes);
        Button ownedObjectsListMainPageAdd = convertView.findViewById(R.id.ownedObjectsListMainPageAdd);

        ownedObjectsListMainPageName.setText(ownedObjectName);
        ownedObjectsListMainPageDetailes.setText("Disinfection time: " + calculateMinutesSeconds(ownedObjectDisinfectionTime));

        final OwnedObject ownedObjectAdded = getItem(position);
        ownedObjectsListMainPageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addButtonListener != null){
                    addButtonListener.OnButtonAddClickListener(position, ownedObjectAdded);
                }
            }
        });

        return convertView;
    }

    private String calculateMinutesSeconds(Integer disinfectionTime){
        String minutesSeconds = "";
        Integer minutes, seconds;

        disinfectionTime /= 1000;

        minutes = disinfectionTime / 60;
        seconds = disinfectionTime % 60;


        if(seconds != 0 && minutes != 0){
            minutesSeconds = minutes + "m " + seconds + "s";
        }else if(seconds != 0 && minutes == 0){
            minutesSeconds = seconds + "s";
        }else if(seconds == 0 && minutes != 0){
            minutesSeconds = minutes + "m";
        }

        return  minutesSeconds;
    }

    public interface AddButtonListener
    {
        void OnButtonAddClickListener(int position, OwnedObject ownedObjectAdded);
    }

    public void setAddButtonListener(AddButtonListener listener)
    {
        this.addButtonListener = listener;
    }
}
