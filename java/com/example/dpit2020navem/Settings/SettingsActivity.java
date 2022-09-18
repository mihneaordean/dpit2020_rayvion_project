package com.example.dpit2020navem.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dpit2020navem.AddAnObject.Activity.ObjectTypeMenuActivity;
import com.example.dpit2020navem.Database.OwnedObjectsDatabase;
import com.example.dpit2020navem.Database.UsefulStaff;
import com.example.dpit2020navem.Database.UsefulStaffDatabase;
import com.example.dpit2020navem.Help.HelpActivity;
import com.example.dpit2020navem.HomePage.MainActivity;
import com.example.dpit2020navem.Map.MapActivity;
import com.example.dpit2020navem.ObjectTypeDetailes.ObjectTypeDetailesActivity;
import com.example.dpit2020navem.OwnedObjectsList.OwnedObjectsListActivity;
import com.example.dpit2020navem.R;
import com.example.dpit2020navem.UvcInfo.UvcInfoActivity;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class SettingsActivity extends AppCompatActivity {

    Button buttonSideMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView sideMenu;
    TextView buttonDeleteAllObjects;
    OwnedObjectsDatabase database;
    ConstraintLayout transparentLayout;
    ConstraintLayout deleteObject;
    TextView tvYes;
    TextView tvNo;
    TextView tvQuestion;
    Button buttonMap;
    Switch notificationsSwitch;
    UsefulStaffDatabase usefulStaffDatabase;
    UsefulStaff notificationOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        setUpSideMenu();
        setUpDeleteLayout();
        setUpSwitchButtonNotifications();
        openSideMenu();
        cleanOwnedObjectsList();
        changeNotificationsStatus();
        openMap();

    }

    public void onBackPressed() {
        if(deleteObject.getVisibility() == View.VISIBLE){
            deleteObject.setVisibility(View.INVISIBLE);
            transparentLayout = findViewById(R.id.transparentLayout);
            transparentLayout.setVisibility(View.INVISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            buttonSideMenu.setEnabled(true);
        }else{
            finish();
        }
    }

    private void setUpSideMenu(){
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open,R.string.Close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        sideMenu = (NavigationView)findViewById(R.id.sideMenu);
        sideMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public  boolean onNavigationItemSelected(@NonNull MenuItem item){
                int id = item.getItemId();

                if(id == R.id.homePage){
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.addAnObject){
                    Intent intent = new Intent(SettingsActivity.this, ObjectTypeMenuActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.ownedObjectList) {
                    Intent intent = new Intent(SettingsActivity.this, OwnedObjectsListActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.objectTypeDetailes) {
                    Intent intent = new Intent(SettingsActivity.this, ObjectTypeDetailesActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.UVCinfo) {
                    Intent intent = new Intent(SettingsActivity.this, UvcInfoActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.settings) {
                    drawerLayout.closeDrawer(sideMenu);
                }else if(id == R.id.help) {
                    Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void openSideMenu(){
        buttonSideMenu = findViewById(R.id.buttonSideMenu);
        buttonSideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(sideMenu);
            }
        });
    }

    private void setUpDeleteLayout(){
        transparentLayout = findViewById(R.id.transparentLayout);
        transparentLayout.setVisibility(View.INVISIBLE);
        deleteObject = findViewById(R.id.deleteObject);
        deleteObject.setVisibility(View.INVISIBLE);

        tvYes = findViewById(R.id.tvYes);
        tvNo = findViewById(R.id.tvNo);
        tvQuestion = findViewById(R.id.tvQuestion);
    }

    private void cleanOwnedObjectsList(){
        database = new OwnedObjectsDatabase(this);

        buttonDeleteAllObjects = findViewById(R.id.buttonDeleteAllObjects);

        buttonDeleteAllObjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transparentLayout = findViewById(R.id.transparentLayout);
                transparentLayout.setVisibility(View.VISIBLE);
                deleteObject.setVisibility(View.VISIBLE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                buttonSideMenu.setEnabled(false);


                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database.cleanOwnedObjectsDatabase();

                        deleteObject.setVisibility(View.INVISIBLE);

                        transparentLayout = findViewById(R.id.transparentLayout);
                        transparentLayout.setVisibility(View.INVISIBLE);
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        buttonSideMenu.setEnabled(true);

                    }
                });

                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteObject.setVisibility(View.INVISIBLE);

                        transparentLayout = findViewById(R.id.transparentLayout);
                        transparentLayout.setVisibility(View.INVISIBLE);
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        buttonSideMenu.setEnabled(true);
                    }
                });
            }
        });


    }

    private void setUpSwitchButtonNotifications(){
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        usefulStaffDatabase = new UsefulStaffDatabase(this);
        notificationOnOff = usefulStaffDatabase.getStuffById(1L);

        if(notificationOnOff.getStuff().equals("on")){
            notificationsSwitch.setChecked(true);
            notificationsSwitch.setText("Turn off notifications");
        }else{
            notificationsSwitch.setChecked(false);
            notificationsSwitch.setText("Turn on notifications");
        }
    }

    private void changeNotificationsStatus(){
        notificationsSwitch = findViewById(R.id.notificationsSwitch);
        usefulStaffDatabase = new UsefulStaffDatabase(this);

        notificationsSwitch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean on = ((Switch) v).isChecked();
                if(on)
                {
                    notificationOnOff = usefulStaffDatabase.getStuffById(1L);
                    usefulStaffDatabase.removeStuffFromStuffDatabase(notificationOnOff.getStuffId());
                    notificationOnOff.setStuff("on");
                    usefulStaffDatabase.addToStuffDatabase(notificationOnOff);

                    notificationsSwitch.setText("Turn off notifications");
                }
                else
                {
                    notificationOnOff = usefulStaffDatabase.getStuffById(1L);
                    usefulStaffDatabase.removeStuffFromStuffDatabase(notificationOnOff.getStuffId());
                    notificationOnOff.setStuff("off");
                    usefulStaffDatabase.addToStuffDatabase(notificationOnOff);

                    notificationsSwitch.setText("Turn on notifications");
                }
            }
        });
    }


    private void openMap(){
        buttonMap = findViewById(R.id.buttonMap);

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkIfLocationIsTurnedOn(SettingsActivity.this) == true) {
                    Intent intent = new Intent(SettingsActivity.this, MapActivity.class);
                    startActivity(intent);
                }else{
                    //Toast.makeText(SettingsActivity.this, "Location is turned off.", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder notifyLocationServices = new AlertDialog.Builder(SettingsActivity.this);
                    notifyLocationServices.setTitle("Switch on Location Services");
                    notifyLocationServices.setMessage("Location Services must be turned on to complete this action. Also please take note that if on a very weak network connection,  such as 'E' Mobile Data or 'Very weak Wifi-Connections' it may take even 15 mins to load. If on a very weak network connection as stated above, location returned to application may be null or nothing and cause the application to crash.");
                    notifyLocationServices.setPositiveButton("Ok, Open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent openLocationSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            SettingsActivity.this.startActivity(openLocationSettings);
                            finish();
                        }
                    });
                    notifyLocationServices.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    notifyLocationServices.show();

                }
            }
        });
    }

    private boolean checkIfLocationIsTurnedOn(Context context){
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}