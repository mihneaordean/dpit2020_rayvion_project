//mlc
//fc cfr 1907 cluj
package com.example.dpit2020navem.HomePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dpit2020navem.AddAnObject.Activity.ObjectTypeMenuActivity;
import com.example.dpit2020navem.AddAnObject.Adapter.ObjectListAdapter;
import com.example.dpit2020navem.AddAnObject.Model.ObjectType;
import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.Bluetooth.BluetoothService;
import com.example.dpit2020navem.Database.OwnedObjectsDatabase;
import com.example.dpit2020navem.Database.UsefulStaff;
import com.example.dpit2020navem.Database.UsefulStaffDatabase;
import com.example.dpit2020navem.Help.HelpActivity;
import com.example.dpit2020navem.Intro.IntroActivity;
import com.example.dpit2020navem.Intro.WelcomeActivity;
import com.example.dpit2020navem.ObjectTypeDetailes.ObjectTypeDetailesActivity;
import com.example.dpit2020navem.OwnedObjectsList.OwnedObjectsListActivity;
import com.example.dpit2020navem.R;
import com.example.dpit2020navem.Settings.SettingsActivity;
import com.example.dpit2020navem.UvcInfo.UvcInfoActivity;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements OwnedObjectsListMainPageAdapter.AddButtonListener, ObjectsThatWillBeDisinfectedListMainPageAdapter.RemoveButtonListener{

    Button buttonSideMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView sideMenu;
    OwnedObjectsDatabase database;
    ConstraintLayout layoutOwnedObjectsList;
    Button closeOwnedObjectsList;
    ListView ownedObjectsMainPageListView;
    List<OwnedObject> ownedObjectMainPageList;
    OwnedObjectsListMainPageAdapter ownedObjectsListMainPageAdapter;
    ConstraintLayout layoutSelectedObjectsList;
    Button closeSelectedObjectsList;
    ListView objectsThatWillBeDisinfectedListView;
    List<OwnedObject> objectsThatWillBeDisinfectedList;
    ObjectsThatWillBeDisinfectedListMainPageAdapter objectsThatWillBeDisinfectedListMainPageAdapter;
    ImageView boxStatePicture;
    boolean open;
    TextView timeRemaining;
    Button startButton;
    CountDownTimer countDownTimer;
    long timeLeftMilliseconds = 600000;
    BluetoothService bluetoothService;
    boolean mBounded;
    private NotificationManager notificationManager;
    PendingIntent pendingIntent;
    Handler handlerBluetooth;
    TextView textViewTitle;
    TextView tvInfo;
    String s;
    boolean timerOn;
    boolean timerRunning;
    boolean errorBox;
    UsefulStaffDatabase usefulStaffDatabase;
    UsefulStaff notificationOnOff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        textViewTitle = findViewById(R.id.textViewTitle);
        tvInfo = findViewById(R.id.tvInfo);
        timerOn = false;
        timerRunning = false;
        errorBox = false;

        setUpNotificationOn();
        setUpSideMenu();
        openSideMenu();
        setUpOwnedObjectsListAdapter();
        openCloseOwnedObjectsListAdapter();
        setUpObjectsThatWillBeDisinfectedListAdapter();
        openCloseObjectsThatWillBeDisinfectedListAdapter();
        changeBoxState();
        setUpTimer();
        readBluetooth();

    }

    public void onBackPressed() {
        if(layoutOwnedObjectsList.getVisibility() == View.VISIBLE){
            layoutOwnedObjectsList.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.VISIBLE);
        }else if(layoutSelectedObjectsList.getVisibility() == View.VISIBLE){
             layoutSelectedObjectsList.setVisibility(View.INVISIBLE);
             startButton.setVisibility(View.VISIBLE);
        }else{
            if(bluetoothService.getBtSocket() != null){
                bluetoothService.closeShandrama();
            }
            finish();
        }
    }

    /*public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            bluetoothService.closeShandrama();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    private void setUpNotificationOn(){
        usefulStaffDatabase = new UsefulStaffDatabase(this);
        notificationOnOff = usefulStaffDatabase.getStuffById(1L);

        if(notificationOnOff == null){
            UsefulStaff ceva = new UsefulStaff();
            ceva.setStuffId(1L);
            ceva.setStuff("on");

            usefulStaffDatabase.addToStuffDatabase(ceva);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent mIntent = new Intent(this, BluetoothService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    };

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //Toast.makeText(MainActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            bluetoothService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Toast.makeText(MainActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            BluetoothService.LocalBinder mLocalBinder = (BluetoothService.LocalBinder)service;
            bluetoothService = mLocalBinder.getBluetoothService();
            setUpBluetooth();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    };

    private void setUpBluetooth(){
        bluetoothService.bluetoothConnection(MainActivity.this);

        bluetoothService.connectionBT(MainActivity.this);
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
                    drawerLayout.closeDrawer(sideMenu);
                }else if(id == R.id.addAnObject){
                    Intent intent = new Intent(MainActivity.this, ObjectTypeMenuActivity.class);
                    startActivity(intent);
                    if(bluetoothService.getBtSocket() != null){
                        bluetoothService.closeShandrama();
                    }
                    finish();
                }else if(id == R.id.ownedObjectList) {
                    Intent intent = new Intent(MainActivity.this, OwnedObjectsListActivity.class);
                    startActivity(intent);
                    if(bluetoothService.getBtSocket() != null){
                        bluetoothService.closeShandrama();
                    }
                    finish();
                }else if(id == R.id.objectTypeDetailes) {
                    Intent intent = new Intent(MainActivity.this, ObjectTypeDetailesActivity.class);
                    startActivity(intent);
                    if(bluetoothService.getBtSocket() != null){
                        bluetoothService.closeShandrama();
                    }
                    finish();
                }else if(id == R.id.UVCinfo) {
                    Intent intent = new Intent(MainActivity.this, UvcInfoActivity.class);
                    startActivity(intent);
                    if(bluetoothService.getBtSocket() != null){
                        bluetoothService.closeShandrama();
                    }
                    finish();
                }else if(id == R.id.settings) {
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    if(bluetoothService.getBtSocket() != null){
                        bluetoothService.closeShandrama();
                    }
                    finish();
                }else if(id == R.id.help) {
                    Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                    startActivity(intent);
                    if(bluetoothService.getBtSocket() != null){
                        bluetoothService.closeShandrama();
                    }
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

    private void setUpOwnedObjectsListAdapter(){
        database = new OwnedObjectsDatabase(this);
        ownedObjectsMainPageListView = findViewById(R.id.ownedObjectsListMainPage);
        ownedObjectMainPageList = new ArrayList<>();

        ownedObjectMainPageList = database.getObjectsByIsObjectInBox(0);

        ownedObjectsListMainPageAdapter = new OwnedObjectsListMainPageAdapter(this, R.layout.layout_home_page_owned_objects_list, ownedObjectMainPageList);
        ownedObjectsListMainPageAdapter.setAddButtonListener(MainActivity.this);
        ownedObjectsMainPageListView.setAdapter(ownedObjectsListMainPageAdapter);
    }

    private void openCloseOwnedObjectsListAdapter(){
        TextView openOwnedObjectList = findViewById(R.id.openOwnedObjectList);
        layoutOwnedObjectsList = findViewById(R.id.ownedObjectsLayoutMainPage);
        closeOwnedObjectsList = findViewById(R.id.buttonCloseOwnedObjectsList);


        openOwnedObjectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutOwnedObjectsList.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);
            }
        });

        closeOwnedObjectsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutOwnedObjectsList.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void OnButtonAddClickListener(int position, OwnedObject ownedObjectAdded) {
        ownedObjectsListMainPageAdapter.clear();
        objectsThatWillBeDisinfectedListMainPageAdapter.clear();

        Long ownedObjectId = ownedObjectAdded.getOwnedObjectId();
        OwnedObject ownedObject = database.getObjectsByObjectId(ownedObjectId);
        database.removeObjectFromOwnedObjectsDatabase(ownedObjectId);
        ownedObject.setIsOwnedObjectInBox(1);
        database.addToOwnedObjectsDatabase(ownedObject);

        ownedObjectsListMainPageAdapter.addAll(database.getObjectsByIsObjectInBox(0));
        ownedObjectsListMainPageAdapter.notifyDataSetChanged();
        objectsThatWillBeDisinfectedListMainPageAdapter.addAll(database.getObjectsByIsObjectInBox(1));
        objectsThatWillBeDisinfectedListMainPageAdapter.notifyDataSetChanged();

        setUpTimer();
        Toast.makeText(this, "Object added to box.", Toast.LENGTH_SHORT).show();
    }

    private void setUpObjectsThatWillBeDisinfectedListAdapter(){
        database = new OwnedObjectsDatabase(this);
        objectsThatWillBeDisinfectedListView = findViewById(R.id.objectsThatWillBeDisinfectedListMainPage);
        objectsThatWillBeDisinfectedList = new ArrayList<>();

        objectsThatWillBeDisinfectedList = database.getObjectsByIsObjectInBox(1);

        objectsThatWillBeDisinfectedListMainPageAdapter = new ObjectsThatWillBeDisinfectedListMainPageAdapter(this, R.layout.layout_home_page_objects_that_will_be_disinfected_list, objectsThatWillBeDisinfectedList);
        objectsThatWillBeDisinfectedListMainPageAdapter.setRemoveButtonListener(MainActivity.this);
        objectsThatWillBeDisinfectedListView.setAdapter(objectsThatWillBeDisinfectedListMainPageAdapter);
    }

    private void openCloseObjectsThatWillBeDisinfectedListAdapter(){
        TextView openObjectsThatWillBeDisinfectedList = findViewById(R.id.openObjectsThatWillBeDisinfectedList);
        layoutSelectedObjectsList = findViewById(R.id.objectsThatWillBeDisinfectedLayoutMainPage);
        closeSelectedObjectsList = findViewById(R.id.buttonCloseSelectedObjectsList);


        openObjectsThatWillBeDisinfectedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSelectedObjectsList.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);
            }
        });

        closeSelectedObjectsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSelectedObjectsList.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void OnButtonRemoveClickListener(int position, OwnedObject ownedObjectRemoved) {
        ownedObjectsListMainPageAdapter.clear();
        objectsThatWillBeDisinfectedListMainPageAdapter.clear();

        Long ownedObjectId = ownedObjectRemoved.getOwnedObjectId();
        OwnedObject ownedObject = database.getObjectsByObjectId(ownedObjectId);
        database.removeObjectFromOwnedObjectsDatabase(ownedObjectId);
        ownedObject.setIsOwnedObjectInBox(0);
        database.addToOwnedObjectsDatabase(ownedObject);

        ownedObjectsListMainPageAdapter.addAll(database.getObjectsByIsObjectInBox(0));
        ownedObjectsListMainPageAdapter.notifyDataSetChanged();
        objectsThatWillBeDisinfectedListMainPageAdapter.addAll(database.getObjectsByIsObjectInBox(1));
        objectsThatWillBeDisinfectedListMainPageAdapter.notifyDataSetChanged();

        setUpTimer();
        Toast.makeText(this, "Object removed from box.", Toast.LENGTH_SHORT).show();
    }

    private void changeBoxState(){
        startButton = findViewById(R.id.startButton);
        boxStatePicture = findViewById(R.id.boxStatePicture);
        open = false;
        boxStatePicture.setImageResource(R.drawable.opened_case);
        timeRemaining = findViewById(R.id.timeRemaining);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(open == false) {
                    if(boxDisinfectionTime() == 0){
                        Toast.makeText(MainActivity.this, "The box is empty.", Toast.LENGTH_SHORT).show();
                    }else{
                        open = true;
                        turnOnBox();
                        startButton.setText("PAUSE");

                        timerOn = true;

                    }
                }
                else {
                    timerRunning = false;
                    stopTimer();
                    open = false;
                    setUpTimer();
                    startButton.setText("START");
                    turnOffBox();
                }

                tvInfo.setText("");
            }
        });
    }

    private long boxDisinfectionTime(){
        database = new OwnedObjectsDatabase(this);
        List<OwnedObject> objectsCurentlyDisinfeted = database.getObjectsByIsObjectInBox(1);

        long disinfectionTime = 0;
        for(int i = 0 ; i < objectsCurentlyDisinfeted.size() ; i++){
            if(objectsCurentlyDisinfeted.get(i).getOwnedObjectDisinfectionTime() > disinfectionTime){
                disinfectionTime = objectsCurentlyDisinfeted.get(i).getOwnedObjectDisinfectionTime();
            }
        }

        return  disinfectionTime;
    }

    private void setUpTimer(){
        timeLeftMilliseconds = boxDisinfectionTime();
        updateTimer();
    }

    public void startTimer(Long disinfectionTime) {
        timeLeftMilliseconds = disinfectionTime;
        countDownTimer = new CountDownTimer(timeLeftMilliseconds ,1000) {
            @Override
            public void onTick(long l) {
                timeLeftMilliseconds = l;
                updateTimer();

            }

            @Override
            public void onFinish() {
                setLastDisinfected();
                ownedObjectsListMainPageAdapter.clear();
                objectsThatWillBeDisinfectedListMainPageAdapter.clear();
                ownedObjectsListMainPageAdapter.addAll(database.getObjectsByIsObjectInBox(0));
                ownedObjectsListMainPageAdapter.notifyDataSetChanged();
                objectsThatWillBeDisinfectedListMainPageAdapter.addAll(database.getObjectsByIsObjectInBox(1));
                objectsThatWillBeDisinfectedListMainPageAdapter.notifyDataSetChanged();

                setUpTimer();
                startButton.setText("START");
                sendOnChannel1();
            }
        }.start();


    }

    public void stopTimer() {
        countDownTimer.cancel();
    }

    public void updateTimer() {
        int minutes = (int) timeLeftMilliseconds / 60000;
        int seconds = (int) timeLeftMilliseconds % 60000 / 1000;
        String timeLeftText = "";

        if (minutes < 10) {
            timeLeftText += "0";
        }
        timeLeftText += minutes;
        timeLeftText += ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;

        timeRemaining.setText(timeLeftText);
    }






    private void turnOffBox()
    {
        bluetoothService.writeBluetooth("r");
    }

    private void turnOnBox()
    {
        long disinfectionTime = boxDisinfectionTime();
        int minutes = (int) disinfectionTime / 60000;
        int seconds = (int) disinfectionTime % 60000 / 1000;
        String sMinutes;
        String sSeconds;

        if(minutes < 10){
            sMinutes = "0" + minutes;
        }else{
            sMinutes = String.valueOf(minutes);
        }

        if(seconds < 10){
            sSeconds = "0" + seconds;
        }else{
            sSeconds = String.valueOf(seconds);
        }

        bluetoothService.writeBluetooth("s" + sMinutes + sSeconds);
    }

    public void readBluetooth(){
        handlerBluetooth = new Handler();
        handlerBluetooth.post(new Runnable() {
            @Override
            public void run() {
                s = "";

                if(bluetoothService != null){
                    s = bluetoothService.readBluetooth();
                    //Log.i(null, s);
                }

                if(s != null && s.length() > 0){
                    Log.i(null,s);
                    boxCommands(s);
                }


                handlerBluetooth.postDelayed(this, 50);
            }
        });
    }

    public void boxCommands(String s){
        try{
            if(s.length() == 6){
                if(s.charAt(0) == 'e' && errorBox == false){
                    stopTimer();
                    open = false;
                    errorBox = true;
                    startButton.setVisibility(View.INVISIBLE);
                    startButton.setText("START");
                    timeRemaining.setText("ERROR");
                    tvInfo.setText("There has been a problem. Please check out the help section.");


                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after x seconds
                            errorBox = false;
                            setUpTimer();
                            tvInfo.setText("");
                            startButton.setVisibility(View.VISIBLE);
                        }
                    }, 7000);

                }

                if(s.charAt(1) == '1'){
                    boxStatePicture.setImageResource(R.drawable.closed_case);
                }else if(s.charAt(1) == '0'){
                    boxStatePicture.setImageResource(R.drawable.opened_case);
                }



                if(s.charAt(0) == 's' && s.charAt(1) == '1' && timerOn == true && errorBox == false){
                    timerOn = false;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after x seconds
                            timerRunning = true;
                            startTimer(boxDisinfectionTime());
                        }
                    }, 2000);
                }

                if(s.charAt(0) == 'r' && s.charAt(1) == '0' && timerRunning == true){
                    timerRunning = false;
                    stopTimer();
                    open = false;
                    setUpTimer();
                    startButton.setText("START");
                }

                /*
                if(s.charAt(2) == '0' && s.charAt(3) == '0' && s.charAt(4) == '0' && s.charAt(5) == '0'){
                    setUpTimer();
                }*/
            }

        }catch (Exception e){

            //msg("a crapat");
        }

    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    /*private Long getBoxTimeWhenStart(){
        if(s != null && s.length() > 0){
            if(s.charAt(1) == '1'){
                if(s.charAt(0) == 'r'){
                    Long time;
                    int min, sec;
                    min = (Integer.valueOf(s.charAt(2)) * 10) + Integer.valueOf(s.charAt(3));
                    sec = (Integer.valueOf(s.charAt(4)) * 10) + Integer.valueOf(s.charAt(5));
                    time = Long.valueOf((min *60 + sec) * 1000);

                    return time;
                }
            }
        }
        return null;
    }*/

    private String getCurrentDatetime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        return  dateTime;
    }

    private void setLastDisinfected(){
        for(int i = 0 ; i < objectsThatWillBeDisinfectedList.size() ; i++){
            OwnedObject ownedObject = objectsThatWillBeDisinfectedList.get(i);
            database.removeObjectFromOwnedObjectsDatabase(ownedObject.getOwnedObjectId());
            ownedObject.setIsOwnedObjectInBox(0);
            ownedObject.setLastTimeDisinfected(getCurrentDatetime());
            database.addToOwnedObjectsDatabase(ownedObject);
        }
    }

    public void sendOnChannel1() {
        usefulStaffDatabase = new UsefulStaffDatabase(this);
        notificationOnOff = usefulStaffDatabase.getStuffById(1L);

        if(notificationOnOff.getStuff().equals("on")){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            String channelId = "channel_id_1";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel1 = new NotificationChannel(channelId, "Timer Notification", NotificationManager.IMPORTANCE_DEFAULT);
                channel1.setDescription("Sent when disinfection is completed");
                notificationManager.createNotificationChannel(channel1);
            }

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo_white_notifications)
                    .setContentTitle("Time's up!")
                    .setContentText("You can now take your objects out of the box.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(1, notification.build());
        }
    }

}
