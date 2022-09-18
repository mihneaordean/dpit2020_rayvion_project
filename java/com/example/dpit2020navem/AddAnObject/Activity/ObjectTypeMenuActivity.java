package com.example.dpit2020navem.AddAnObject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.dpit2020navem.AddAnObject.Adapter.ObjectTypeListAdapter;
import com.example.dpit2020navem.AddAnObject.Model.ObjectType;
import com.example.dpit2020navem.Help.HelpActivity;
import com.example.dpit2020navem.HomePage.MainActivity;
import com.example.dpit2020navem.ObjectTypeDetailes.ObjectTypeDetailesActivity;
import com.example.dpit2020navem.OwnedObjectsList.OwnedObjectsListActivity;
import com.example.dpit2020navem.Database.OwnedObjectsDatabase;
import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.R;
import com.example.dpit2020navem.Settings.SettingsActivity;
import com.example.dpit2020navem.UvcInfo.UvcInfoActivity;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ObjectTypeMenuActivity extends AppCompatActivity {

    Button buttonSideMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView sideMenu;
    List<ObjectType> objectTypeList;
    Context context = this;
    ListView objectTypesListView;
    OwnedObjectsDatabase database;

    List<OwnedObject> phonesList;
    List<OwnedObject> walletsList;
    List<OwnedObject> keysList;
    List<OwnedObject> glassesList;
    List<OwnedObject> watchesList;
    List<OwnedObject> laptopsList;
    List<OwnedObject> camerasList;
    List<OwnedObject> headphonesList;
    List<OwnedObject> miceList;
    List<OwnedObject> chargersList;
    List<OwnedObject> remotesList;
    List<OwnedObject> accesoriesList;
    List<OwnedObject> booksList;
    List<OwnedObject> pensList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_type_menu);

        setUpSideMenu();
        openSideMenu();
        finishActivityFromAnotherActivity();
        createObjectTypeListMenu();
        setUpObjectTypeListAdapter();

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
                    Intent intent = new Intent(ObjectTypeMenuActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.addAnObject){
                    drawerLayout.closeDrawer(sideMenu);
                }else if(id == R.id.ownedObjectList) {
                    Intent intent = new Intent(ObjectTypeMenuActivity.this, OwnedObjectsListActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.objectTypeDetailes) {
                    Intent intent = new Intent(ObjectTypeMenuActivity.this, ObjectTypeDetailesActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.UVCinfo) {
                    Intent intent = new Intent(ObjectTypeMenuActivity.this, UvcInfoActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.settings) {
                    Intent intent = new Intent(ObjectTypeMenuActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.help) {
                    Intent intent = new Intent(ObjectTypeMenuActivity.this, HelpActivity.class);
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

    private void finishActivityFromAnotherActivity(){
        BroadcastReceiver broadcast_reciever = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish")) {
                    finish();
                }
            }
        };
        registerReceiver(broadcast_reciever, new IntentFilter("finish"));
    }

    private void createOwnedObjectList(){
        database = new OwnedObjectsDatabase(this);

        phonesList = new ArrayList<>();
        walletsList = new ArrayList<>();
        keysList = new ArrayList<>();
        glassesList = new ArrayList<>();
        watchesList = new ArrayList<>();
        laptopsList = new ArrayList<>();
        camerasList = new ArrayList<>();
        headphonesList = new ArrayList<>();
        miceList = new ArrayList<>();
        chargersList = new ArrayList<>();
        remotesList = new ArrayList<>();
        accesoriesList = new ArrayList<>();
        booksList = new ArrayList<>();
        pensList = new ArrayList<>();

    }

    private void createObjectTypeListMenu(){
        createOwnedObjectList();

        objectTypeList = new ArrayList<>();
        objectTypeList.add(new ObjectType("Phones",R.drawable.phone,50000,phonesList));
        objectTypeList.add(new ObjectType("Wallets",R.drawable.wallet,30000,walletsList));
        objectTypeList.add(new ObjectType("Keys",R.drawable.keys,35000,keysList));
        objectTypeList.add(new ObjectType("Glasses",R.drawable.glasses,30000,glassesList));
        objectTypeList.add(new ObjectType("Watches",R.drawable.watch,30000,watchesList));
        objectTypeList.add(new ObjectType("Laptops",R.drawable.laptops,75000,laptopsList));
        objectTypeList.add(new ObjectType("Cameras",R.drawable.cameras,55000,camerasList));
        objectTypeList.add(new ObjectType("Headphones",R.drawable.headphones,25000,headphonesList));
        objectTypeList.add(new ObjectType("Mice",R.drawable.mice,40000,miceList));
        objectTypeList.add(new ObjectType("Chargers/Cables",R.drawable.chargers_cables,20000,chargersList));
        objectTypeList.add(new ObjectType("Remotes/Joysticks",R.drawable.remotes_joysticks,50000,remotesList));
        objectTypeList.add(new ObjectType("Accesories",R.drawable.accesories,20000,accesoriesList));
        objectTypeList.add(new ObjectType("Books",R.drawable.books,30000,booksList));
        objectTypeList.add(new ObjectType("Pens",R.drawable.pens,15000,pensList));

    }

    private void setUpObjectTypeListAdapter(){
        ObjectTypeListAdapter adapter = new ObjectTypeListAdapter(context, R.layout.layout_object_type_menu, objectTypeList);
        objectTypesListView = findViewById(R.id.lvObjectTypeList);
        objectTypesListView.setAdapter(adapter);

        objectTypesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ObjectTypeMenuActivity.this, ObjectMenuActivity.class);
                ObjectType selectedItem = (ObjectType) objectTypesListView.getItemAtPosition(position);

                intent.putExtra("Object", (Serializable) selectedItem);
                startActivity(intent);
            }
        });
    }


}