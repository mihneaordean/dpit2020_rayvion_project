package com.example.dpit2020navem.AddAnObject.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dpit2020navem.AddAnObject.Model.ObjectType;
import com.example.dpit2020navem.R;

import java.util.List;

public class ObjectTypeListAdapter extends ArrayAdapter<ObjectType> {
    private Context mContext;
    int mResource;

    public ObjectTypeListAdapter(Context context, int resource, List<ObjectType> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int objectTypePicture = getItem(position).getObjectTypePicture();
        String objectTypeName = getItem(position).getObjectTypeName();
        Integer objectTypeDisinfectionTime  = getItem(position).getObjectTypeDisinfectionTime();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        ImageView ivObjectTypePicture = (ImageView) convertView.findViewById(R.id.ivObjectTypePicture);
        TextView tvObjectTypeName = (TextView) convertView.findViewById(R.id.tvObjectTypeName);
        TextView tvObjectTypeDisinfectionTime = (TextView) convertView.findViewById(R.id.tvObjectTypeDisinfectionTime);

        ivObjectTypePicture.setImageResource(objectTypePicture);
        tvObjectTypeName.setText(objectTypeName);
        tvObjectTypeDisinfectionTime.setText("Disinfection time: " + calculateMinutesSeconds(objectTypeDisinfectionTime));

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
}
