package com.example.dpit2020navem.OwnedObjectsList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dpit2020navem.AddAnObject.Activity.ObjectMenuActivity;
import com.example.dpit2020navem.AddAnObject.Activity.ObjectTypeMenuActivity;
import com.example.dpit2020navem.AddAnObject.Adapter.ObjectTypeListAdapter;
import com.example.dpit2020navem.AddAnObject.Model.ObjectType;
import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.Database.OwnedObjectsDatabase;
import com.example.dpit2020navem.Help.HelpActivity;
import com.example.dpit2020navem.HomePage.MainActivity;
import com.example.dpit2020navem.ObjectTypeDetailes.ObjectTypeDetailesActivity;
import com.example.dpit2020navem.R;
import com.example.dpit2020navem.Settings.SettingsActivity;
import com.example.dpit2020navem.UvcInfo.UvcInfoActivity;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OwnedObjectsListActivity extends AppCompatActivity implements OwnedObjectsListSubItemAdapter.DeleteButtonListener{

    Button buttonSideMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView sideMenu;
    List<ObjectType> ownedObjectsListByType;
    OwnedObjectsDatabase database;
    OwnedObjectsListItemAdapter ownedObjectsListItemAdapter;
    ConstraintLayout transparentLayout;
    ConstraintLayout deleteObject;
    TextView tvYes;
    TextView tvNo;
    TextView tvQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_objects_list);

        setUpSideMenu();
        openSideMenu();
        setUpOwnedObjectsList();
        setUpDeleteLayout();

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
                    Intent intent = new Intent(OwnedObjectsListActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.addAnObject){
                    Intent intent = new Intent(OwnedObjectsListActivity.this, ObjectTypeMenuActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.ownedObjectList) {
                    drawerLayout.closeDrawer(sideMenu);
                }else if(id == R.id.objectTypeDetailes) {
                    Intent intent = new Intent(OwnedObjectsListActivity.this, ObjectTypeDetailesActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.UVCinfo) {
                    Intent intent = new Intent(OwnedObjectsListActivity.this, UvcInfoActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.settings) {
                    Intent intent = new Intent(OwnedObjectsListActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.help) {
                    Intent intent = new Intent(OwnedObjectsListActivity.this, HelpActivity.class);
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

    private void  setUpOwnedObjectsList(){
        RecyclerView ownedObjectsList = findViewById(R.id.ownedObjectsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(OwnedObjectsListActivity.this);
        ownedObjectsListItemAdapter = new OwnedObjectsListItemAdapter(createOwnedObjectsList(),OwnedObjectsListActivity.this);
        ownedObjectsList.setAdapter(ownedObjectsListItemAdapter);
        ownedObjectsList.setLayoutManager(layoutManager);
    }

    private List<ObjectType> createOwnedObjectsList() {
        database = new OwnedObjectsDatabase(this);
        ownedObjectsListByType = new ArrayList<>();

        List<OwnedObject> ownedObjectsList1 = database.getObjectsByObjectType("Phones");
        if(!ownedObjectsList1.isEmpty()){
            ObjectType objectType1 = new ObjectType("Phones",R.drawable.phone,3000,ownedObjectsList1);
            ownedObjectsListByType.add(objectType1);
        }
        List<OwnedObject> ownedObjectsList2 = database.getObjectsByObjectType("Wallets");
        if(!ownedObjectsList2.isEmpty()){
            ObjectType objectType2 = new ObjectType("Wallets",R.drawable.wallet,3000,ownedObjectsList2);
            ownedObjectsListByType.add(objectType2);
        }
        List<OwnedObject> ownedObjectsList3 = database.getObjectsByObjectType("Keys");
        if(!ownedObjectsList3.isEmpty()){
            ObjectType objectType3 = new ObjectType("Keys",R.drawable.keys,3000,ownedObjectsList3);
            ownedObjectsListByType.add(objectType3);
        }
        List<OwnedObject> ownedObjectsList4 = database.getObjectsByObjectType("Glasses");
        if(!ownedObjectsList4.isEmpty()){
            ObjectType objectType4 = new ObjectType("Glasses",R.drawable.glasses,3000,ownedObjectsList4);
            ownedObjectsListByType.add(objectType4);
        }
        List<OwnedObject> ownedObjectsList5 = database.getObjectsByObjectType("Watches");
        if(!ownedObjectsList5.isEmpty()){
            ObjectType objectType5 = new ObjectType("Watches",R.drawable.watch,3000,ownedObjectsList5);
            ownedObjectsListByType.add(objectType5);
        }
        List<OwnedObject> ownedObjectsList6 = database.getObjectsByObjectType("Laptops");
        if(!ownedObjectsList6.isEmpty()){
            ObjectType objectType6 = new ObjectType("Laptops",R.drawable.laptops,3000,ownedObjectsList6);
            ownedObjectsListByType.add(objectType6);
        }
        List<OwnedObject> ownedObjectsList7 = database.getObjectsByObjectType("Cameras");
        if(!ownedObjectsList7.isEmpty()){
            ObjectType objectType7 = new ObjectType("Cameras",R.drawable.cameras,3000,ownedObjectsList7);
            ownedObjectsListByType.add(objectType7);
        }
        List<OwnedObject> ownedObjectsList8 = database.getObjectsByObjectType("Headphones");
        if(!ownedObjectsList8.isEmpty()){
            ObjectType objectType8 = new ObjectType("Headphones",R.drawable.headphones,3000,ownedObjectsList8);
            ownedObjectsListByType.add(objectType8);
        }
        List<OwnedObject> ownedObjectsList9 = database.getObjectsByObjectType("Mice");
        if(!ownedObjectsList9.isEmpty()){
            ObjectType objectType9 = new ObjectType("Mice",R.drawable.mice,3000,ownedObjectsList9);
            ownedObjectsListByType.add(objectType9);
        }
        List<OwnedObject> ownedObjectsList10 = database.getObjectsByObjectType("Chargers/Cables");
        if(!ownedObjectsList10.isEmpty()){
            ObjectType objectType10 = new ObjectType("Chargers/Cables",R.drawable.chargers_cables,3000,ownedObjectsList10);
            ownedObjectsListByType.add(objectType10);
        }
        List<OwnedObject> ownedObjectsList11 = database.getObjectsByObjectType("Remotes/Joysticks");
        if(!ownedObjectsList11.isEmpty()){
            ObjectType objectType11 = new ObjectType("Remotes/Joysticks",R.drawable.remotes_joysticks,3000,ownedObjectsList11);
            ownedObjectsListByType.add(objectType11);
        }
        List<OwnedObject> ownedObjectsList12 = database.getObjectsByObjectType("Accesories");
        if(!ownedObjectsList12.isEmpty()){
            ObjectType objectType12 = new ObjectType("Accesories",R.drawable.accesories,3000,ownedObjectsList12);
            ownedObjectsListByType.add(objectType12);
        }
        List<OwnedObject> ownedObjectsList13 = database.getObjectsByObjectType("Books");
        if(!ownedObjectsList13.isEmpty()){
            ObjectType objectType13 = new ObjectType("Books",R.drawable.books,3000,ownedObjectsList13);
            ownedObjectsListByType.add(objectType13);
        }
        List<OwnedObject> ownedObjectsList14 = database.getObjectsByObjectType("Pens");
        if(!ownedObjectsList14.isEmpty()){
            ObjectType objectType14 = new ObjectType("Pens",R.drawable.pens,3000,ownedObjectsList14);
            ownedObjectsListByType.add(objectType14);
        }

        return  ownedObjectsListByType;
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

    @Override
    public void OnButtonDeleteClickListener(int position, OwnedObject ownedObjectDeleted) {
        if(deleteObject.getVisibility() == View.INVISIBLE){
            transparentLayout = findViewById(R.id.transparentLayout);
            transparentLayout.setVisibility(View.VISIBLE);
            deleteObject.setVisibility(View.VISIBLE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            buttonSideMenu.setEnabled(false);

            tvQuestion.setText("Are you sure you want to delete " + ownedObjectDeleted.getOwnedObjectName() + "?");

            final Long ownedObjectId = ownedObjectDeleted.getOwnedObjectId();

            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.removeObjectFromOwnedObjectsDatabase(ownedObjectId);

                    setUpOwnedObjectsList();
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
    }
}