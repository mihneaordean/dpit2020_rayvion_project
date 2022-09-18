package com.example.dpit2020navem.OwnedObjectsList;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dpit2020navem.AddAnObject.Model.ObjectType;
import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.Database.OwnedObjectsDatabase;
import com.example.dpit2020navem.HomePage.MainActivity;
import com.example.dpit2020navem.HomePage.OwnedObjectsListMainPageAdapter;
import com.example.dpit2020navem.Intro.IntroActivity;
import com.example.dpit2020navem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OwnedObjectsListSubItemAdapter extends RecyclerView.Adapter<OwnedObjectsListSubItemAdapter.SubItemViewHolder> {

    private List<OwnedObject> ownedObjectsList;
    Context context;
    OwnedObjectsDatabase database;
    DeleteButtonListener deleteButtonListener;
    String dateTime;
    int day, month, year, hour, minute, currentDay, currentMonth, currentYear, currentHour, currentMinute;
    int yearsBetween, monthsBetween, daysBetween, hoursBetween, minutesBetween;


    OwnedObjectsListSubItemAdapter(List<OwnedObject> ownedObjectsList, Context context) {
        this.ownedObjectsList = ownedObjectsList;
        this.context = context;
        this.database = new OwnedObjectsDatabase(context);
    }



    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_owned_objects_list_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubItemViewHolder subItemViewHolder,final int i) {
        final OwnedObject ownedObjectDeleted = ownedObjectsList.get(i);
        subItemViewHolder.ownedObjectName.setText(ownedObjectDeleted.getOwnedObjectName());
        subItemViewHolder.lastTimeDisinfected.setText(setLastTimeDisinfected(i));

        subItemViewHolder.buttonDeleteObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deleteButtonListener != null){
                    deleteButtonListener.OnButtonDeleteClickListener(i, ownedObjectDeleted);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return ownedObjectsList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder  {
        TextView ownedObjectName;
        TextView lastTimeDisinfected;
        Button buttonDeleteObject;
        RecyclerView ownedObjectsListUpdate;

        SubItemViewHolder(View itemView) {
            super(itemView);
            ownedObjectName = itemView.findViewById(R.id.ownedObjectName);
            lastTimeDisinfected = itemView.findViewById(R.id.lastTimeDisinfected);
            buttonDeleteObject = itemView.findViewById(R.id.buttonDeleteObject);
            ownedObjectsListUpdate = itemView.findViewById(R.id.ownedObjectsList);
        }

    }

    public interface DeleteButtonListener
    {
        void OnButtonDeleteClickListener(int position, OwnedObject ownedObjectDeleted);
    }

    public void setDeleteButtonListener(OwnedObjectsListSubItemAdapter.DeleteButtonListener listener)
    {
        this.deleteButtonListener = listener;
    }

    private String setLastTimeDisinfected(int i){
        final OwnedObject ownedObjectDeleted = ownedObjectsList.get(i);
        dateTime = ownedObjectDeleted.getLastTimeDisinfected();



        if(dateTime.equals("0")){
            return "Disinfected: never";
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                initializeDateTime();
            }

            if(yearsBetween > 0){
                return "Disinfected: more than a year";
            }else{
                if(monthsBetween > 0){
                    if(monthsBetween == 1){
                        return "Disinfected: " + monthsBetween + " month ago";
                    }else{
                        return "Disinfected: " + monthsBetween + " months ago";
                    }

                }else{
                    if(daysBetween >= 1){
                        if(daysBetween == 1){
                            return "Disinfected: " + daysBetween + " day ago";
                        }else{
                            return "Disinfected: " + daysBetween + " days ago";
                        }
                    }else{
                        if(hoursBetween >= 1){
                            if(hoursBetween == 1){
                                return "Disinfected: " + hoursBetween + " hour ago";
                            }else{
                                return "Disinfected: " + hoursBetween + " hours ago";
                            }
                        }else{
                            if(minutesBetween >= 1){
                                if(minutesBetween == 1){
                                    return "Disinfected: " + minutesBetween + " minute ago";
                                }else{
                                    return "Disinfected: " + minutesBetween + " minutes ago";
                                }
                            }else{
                                return "Disinfected: few seconds ago";
                            }
                        }
                    }
                }
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeDateTime(){
        day = (dateTime.charAt(0) - '0')* 10 + (dateTime.charAt(1)- '0');
        month = (dateTime.charAt(2) - '0')* 10 + (dateTime.charAt(3)- '0');
        year = (dateTime.charAt(4) - '0')* 1000 + (dateTime.charAt(5) - '0')* 100 + (dateTime.charAt(6) - '0')* 10 + (dateTime.charAt(7)- '0');
        hour = (dateTime.charAt(8) - '0')* 10 + (dateTime.charAt(9) - '0');
        minute = (dateTime.charAt(10) - '0')* 10 + (dateTime.charAt(11) - '0');

        String currentDateTime = getCurrentDatetime();
        currentDay = (currentDateTime.charAt(0) - '0')* 10 + (currentDateTime.charAt(1) - '0');
        currentMonth = (currentDateTime.charAt(2) - '0')* 10 + (currentDateTime.charAt(3) - '0');
        currentYear = (currentDateTime.charAt(4) - '0')* 1000 + (currentDateTime.charAt(5) - '0')* 100 + (currentDateTime.charAt(6) - '0')* 10 + (currentDateTime.charAt(7) - '0');
        currentHour = (currentDateTime.charAt(8) - '0')* 10 + (currentDateTime.charAt(9) - '0');
        currentMinute = (currentDateTime.charAt(10) - '0')* 10 + (currentDateTime.charAt(11) - '0');

        LocalDate dateBefore = LocalDate.of(year, month, day);
        LocalDate dateAfter = LocalDate.of(currentYear, currentMonth, currentDay);
        yearsBetween = (int) ChronoUnit.YEARS.between((Temporal) dateBefore, dateAfter);
        monthsBetween = (int) ChronoUnit.MONTHS.between(dateBefore, dateAfter);
        daysBetween = (int) ChronoUnit.DAYS.between(dateBefore, dateAfter);

        calculateTimeBetween(dateTime,currentDateTime);
    }

    private void calculateTimeBetween(String start_date, String end_date){

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmm");

        try {

            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            int difference_In_Time = (int) (d2.getTime() - d1.getTime());

            minutesBetween = (difference_In_Time / (1000 * 60)) % 60;
            hoursBetween = (difference_In_Time / (1000 * 60 * 60)) % 24;

        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }



    private String getCurrentDatetime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        return  dateTime;
    }

}