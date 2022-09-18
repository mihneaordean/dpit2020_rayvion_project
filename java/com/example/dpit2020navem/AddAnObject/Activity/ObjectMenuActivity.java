package com.example.dpit2020navem.AddAnObject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.ColorUtils;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dpit2020navem.AddAnObject.Adapter.ObjectListAdapter;
import com.example.dpit2020navem.AddAnObject.Model.ObjectType;
import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.Database.OwnedObjectsDatabase;
import com.example.dpit2020navem.Help.HelpActivity;
import com.example.dpit2020navem.HomePage.MainActivity;
import com.example.dpit2020navem.ObjectTypeDetailes.ObjectTypeDetailesActivity;
import com.example.dpit2020navem.OwnedObjectsList.OwnedObjectsListActivity;
import com.example.dpit2020navem.R;
import com.example.dpit2020navem.Settings.SettingsActivity;
import com.example.dpit2020navem.UvcInfo.UvcInfoActivity;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ObjectMenuActivity extends AppCompatActivity {

    Button buttonSideMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView sideMenu;
    ListView ownedObjectsListView;
    List<OwnedObject> ownedObjectList;
    ObjectType objectType;
    Bundle bundle;
    Button buttonAddAnObject;
    OwnedObjectsDatabase database;
    OwnedObject ownedObject;
    Context context = this;
    List<OwnedObject> allObjects;
    ConstraintLayout layoutCreateObjectName;
    EditText etObjectName;
    Button buttonAddAnObjectWithName;
    TextView warning;
    Long newObjectId;
    String newObjectName;
    Handler handler;
    TextView tvObjectType;
    ConstraintLayout transparentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_menu);

        setUpSideMenu();
        openSideMenu();
        setUpObjectListAdapter();
        setUpLayoutCreateObjectName();
        nameTheObject();
        removeWarningIfTyping();

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
                    Intent intent1 = new Intent("finish");
                    sendBroadcast(intent1);
                    Intent intent = new Intent(ObjectMenuActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.addAnObject){
                    drawerLayout.closeDrawer(sideMenu);
                }else if(id == R.id.ownedObjectList) {
                    Intent intent1 = new Intent("finish");
                    sendBroadcast(intent1);
                    Intent intent = new Intent(ObjectMenuActivity.this, OwnedObjectsListActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.objectTypeDetailes) {
                    Intent intent1 = new Intent("finish");
                    sendBroadcast(intent1);
                    Intent intent = new Intent(ObjectMenuActivity.this, ObjectTypeDetailesActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.UVCinfo) {
                    Intent intent1 = new Intent("finish");
                    sendBroadcast(intent1);
                    Intent intent = new Intent(ObjectMenuActivity.this, UvcInfoActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.settings) {
                    Intent intent1 = new Intent("finish");
                    sendBroadcast(intent1);
                    Intent intent = new Intent(ObjectMenuActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.help) {
                    Intent intent1 = new Intent("finish");
                    sendBroadcast(intent1);
                    Intent intent = new Intent(ObjectMenuActivity.this, HelpActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });

        transparentLayout = findViewById(R.id.transparentLayout);
        transparentLayout.setVisibility(View.INVISIBLE);
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

    private void setUpObjectListAdapter(){
        database = new OwnedObjectsDatabase(this);
        tvObjectType = findViewById(R.id.objectType);
        ownedObjectsListView = findViewById(R.id.lvObjectList);
        ownedObjectList = new ArrayList<>();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            objectType = (ObjectType) bundle.get("Object");
            ownedObjectList = database.getObjectsByObjectType(objectType.getObjectTypeName());
        }

        tvObjectType.setText(objectType.getObjectTypeName() + " Addition");

        ObjectListAdapter adapter = new ObjectListAdapter(this, R.layout.layout_object_menu, ownedObjectList);
        ownedObjectsListView.setAdapter(adapter);
    }

    private void setUpLayoutCreateObjectName(){
        layoutCreateObjectName = findViewById(R.id.layoutCreateObjectName);
        layoutCreateObjectName.setVisibility(View.INVISIBLE);
    }

    private Long createNewObjectId(){
        allObjects = new ArrayList<>();
        allObjects = database.getOwnedObjects();
        Long max = -1L;

        for(int i = 0 ; i < allObjects.size() ; i++){
            if(allObjects.get(i).getOwnedObjectId() > max) {
                max = allObjects.get(i).getOwnedObjectId();
            }
        }

        Long objectId = Long.valueOf(max + 1L);
        return  objectId;
    }

    private String createNewObjectName(){
        etObjectName = findViewById(R.id.etObjectName);

        String objectName = etObjectName.getText().toString();
        etObjectName.setText("");
        return objectName;
    }

    private boolean validateName(String name){
        boolean valid = true;

        for(int i = 0 ; i < allObjects.size() ; i++){
            if(allObjects.get(i).getOwnedObjectName().equals(name)){
                valid = false;
            }
        }

        return valid;
    }


    private void nameTheObject(){
        buttonAddAnObject = findViewById(R.id.buttonAddAnObject);
        buttonAddAnObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warning = findViewById(R.id.warning);
                etObjectName = findViewById(R.id.etObjectName);

                warning.setText("");
                etObjectName.setText("");


                transparentLayout = findViewById(R.id.transparentLayout);
                transparentLayout.setVisibility(View.VISIBLE);
                layoutCreateObjectName.setVisibility(View.VISIBLE);
                buttonAddAnObject.setVisibility(View.INVISIBLE);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                buttonSideMenu.setEnabled(false);

                buttonAddAnObjectWithName = findViewById(R.id.buttonAddAnObjectWithName);

                addAnObject();
            }
        });
    }

    private String getCurrentDatetime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmm");
        String dateTime = simpleDateFormat.format(calendar.getTime());
        return  dateTime;
    }

    private void addAnObject(){
        buttonAddAnObjectWithName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newObjectId = createNewObjectId();
                newObjectName = createNewObjectName();
                boolean valid = validateName(newObjectName);

                warning.setText("");

                etObjectName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        warning.setText("");
                    }
                });

                if(newObjectName.equals("")){
                    warning.setText("Please name the object.");
                }else if(valid == false){
                    warning.setText("This name already exist.");
                } else{
                    warning.setText("");

                    ownedObject = new OwnedObject();
                    ownedObject.setOwnedObjectId(newObjectId);
                    ownedObject.setOwnedObjectType(objectType.getObjectTypeName());
                    ownedObject.setOwnedObjectName(newObjectName);
                    ownedObject.setOwnedObjectDisinfectionTime(objectType.getObjectTypeDisinfectionTime());
                    ownedObject.setIsOwnedObjectInBox(0);
                    ownedObject.setLastTimeDisinfected("0");

                    database.addToOwnedObjectsDatabase(ownedObject);

                    ownedObjectList = database.getObjectsByObjectType(objectType.getObjectTypeName());
                    ObjectListAdapter adapter = new ObjectListAdapter(context, R.layout.layout_object_menu, ownedObjectList);
                    ownedObjectsListView.setAdapter(adapter);

                    closeKeyboard();
                    layoutCreateObjectName.setVisibility(View.INVISIBLE);
                    buttonAddAnObject.setVisibility(View.VISIBLE);
                    transparentLayout = findViewById(R.id.transparentLayout);
                    transparentLayout.setVisibility(View.INVISIBLE);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    buttonSideMenu.setEnabled(true);
                }

            }
        });
    }

    private void removeWarningIfTyping(){
        handler = new Handler();
        etObjectName = findViewById(R.id.etObjectName);
        warning = findViewById(R.id.warning);

        handler.post(new Runnable() {
            @Override
            public void run() {
                String name = etObjectName.getText().toString();
                if(!name.equals("")){
                    warning.setText("");
                }
                handler.postDelayed(this, 100);
            }
        });

    }

    public void onBackPressed() {
        if(layoutCreateObjectName.getVisibility() == View.VISIBLE){
            layoutCreateObjectName.setVisibility(View.INVISIBLE);
            buttonAddAnObject.setVisibility(View.VISIBLE);
            closeKeyboard();
            transparentLayout = findViewById(R.id.transparentLayout);
            transparentLayout.setVisibility(View.INVISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            buttonSideMenu.setEnabled(true);
        }else{
            finish();
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}