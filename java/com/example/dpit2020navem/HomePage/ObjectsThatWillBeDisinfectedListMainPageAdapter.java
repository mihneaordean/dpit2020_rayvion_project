package com.example.dpit2020navem.HomePage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.Database.OwnedObjectsDatabase;
import com.example.dpit2020navem.R;

import java.util.List;

public class ObjectsThatWillBeDisinfectedListMainPageAdapter extends ArrayAdapter<OwnedObject> {

    private Context mContext;
    int mResource;
    OwnedObjectsDatabase database;
    RemoveButtonListener removeButtonListener;

    public ObjectsThatWillBeDisinfectedListMainPageAdapter(Context context, int resource, List<OwnedObject> objects) {
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


        TextView ownedObjectsListMainPageName = (TextView) convertView.findViewById(R.id.objectsThatWillBeDisinfectedListMainPageName);
        TextView ownedObjectsListMainPageDetailes = (TextView) convertView.findViewById(R.id.objectsThatWillBeDisinfectedListMainPageDetailes);
        Button objectsThatWillBeDisinfectedListMainPageRemove = convertView.findViewById(R.id.objectsThatWillBeDisinfectedListMainPageRemove);

        ownedObjectsListMainPageName.setText(ownedObjectName);
        ownedObjectsListMainPageDetailes.setText("Disinfection time: " + calculateMinutesSeconds(ownedObjectDisinfectionTime));

        final OwnedObject ownedObjectRemoved = getItem(position);
        objectsThatWillBeDisinfectedListMainPageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(removeButtonListener != null){
                    removeButtonListener.OnButtonRemoveClickListener(position, ownedObjectRemoved);
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

    public interface RemoveButtonListener
    {
        void OnButtonRemoveClickListener(int position, OwnedObject ownedObjectRemoved);
    }

    public void setRemoveButtonListener(ObjectsThatWillBeDisinfectedListMainPageAdapter.RemoveButtonListener listener)
    {
        this.removeButtonListener = listener;
    }
}
