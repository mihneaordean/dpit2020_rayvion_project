package com.example.dpit2020navem.ObjectTypeDetailes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.example.dpit2020navem.AddAnObject.Activity.ObjectTypeMenuActivity;
import com.example.dpit2020navem.Help.HelpActivity;
import com.example.dpit2020navem.Help.QuestionsAnswearsListAdapter;
import com.example.dpit2020navem.HomePage.MainActivity;
import com.example.dpit2020navem.OwnedObjectsList.OwnedObjectsListActivity;
import com.example.dpit2020navem.R;
import com.example.dpit2020navem.Settings.SettingsActivity;
import com.example.dpit2020navem.UvcInfo.UvcInfoActivity;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectTypeDetailesActivity extends AppCompatActivity {

    Button buttonSideMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView sideMenu;
    private ExpandableListView objectTypeDetailesListView;
    private ObjectTypeDetailesListAdapter objectTypeDetailesListAdapter;
    private List<ListHeader> listType;
    private HashMap< ListHeader , List<String> > listDetailes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_type_detailes);

        setUpSideMenu();
        openSideMenu();
        setUpObjectTypeDetailesListView();

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
                    Intent intent = new Intent(ObjectTypeDetailesActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.addAnObject){
                    Intent intent = new Intent(ObjectTypeDetailesActivity.this, ObjectTypeMenuActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.ownedObjectList) {
                    Intent intent = new Intent(ObjectTypeDetailesActivity.this, OwnedObjectsListActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.objectTypeDetailes) {
                    drawerLayout.closeDrawer(sideMenu);
                }else if(id == R.id.UVCinfo) {
                    Intent intent = new Intent(ObjectTypeDetailesActivity.this, UvcInfoActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.settings) {
                    Intent intent = new Intent(ObjectTypeDetailesActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                }else if(id == R.id.help) {
                    Intent intent = new Intent(ObjectTypeDetailesActivity.this, HelpActivity.class);
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

    private void setUpObjectTypeDetailesListView(){
        objectTypeDetailesListView = (ExpandableListView)findViewById(R.id.objectTypeDetailesList);
        initializeObjectTypeDetailesList();
        objectTypeDetailesListAdapter = new ObjectTypeDetailesListAdapter(this,listType,listDetailes);
        objectTypeDetailesListView.setAdapter(objectTypeDetailesListAdapter);
    }

    private void initializeObjectTypeDetailesList(){
        listType = new ArrayList<>();
        listDetailes = new HashMap<>();

        listType.add(new ListHeader("Phones",R.drawable.phone));
        listType.add(new ListHeader("Wallets",R.drawable.wallet));
        listType.add(new ListHeader("Keys",R.drawable.keys));
        listType.add(new ListHeader("Glasses",R.drawable.glasses));
        listType.add(new ListHeader("Watches",R.drawable.watch));
        listType.add(new ListHeader("Laptops",R.drawable.laptops));
        listType.add(new ListHeader("Cameras",R.drawable.cameras));
        listType.add(new ListHeader("Headphones",R.drawable.headphones));
        listType.add(new ListHeader("Mice",R.drawable.mice));
        listType.add(new ListHeader("Chargers/Cables",R.drawable.chargers_cables));
        listType.add(new ListHeader("Remotes/Joysticks",R.drawable.remotes_joysticks));
        listType.add(new ListHeader("Accesories",R.drawable.accesories));
        listType.add(new ListHeader("Books",R.drawable.books));
        listType.add(new ListHeader("Pens",R.drawable.pens));

        List<String> list1 = new ArrayList<>();
        list1.add("- Disinfection time: 50 seconds\n" +
                "- Material: metal, glass, plastic\n" +
                "- Efficiency: 99.9999%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 3 phones can be disinfected in 30 seconds, where we add 10 seconds at the start and 10 at the end for losses, errors and inefficiency.");

        List<String> list2 = new ArrayList<>();
        list2.add("- Disinfection time: 30 seconds\n" +
                "- Material: leather, polyesther\n" +
                "- Efficiency: 99%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 3 wallets can be disinfected in 20 seconds, where we add 5 seconds at the start and 5 at the end for losses, errors and inefficiency. In case it is exposed too long, it can lead to a distaining.");

        List<String> list3 = new ArrayList<>();
        list3.add("- Disinfection time: 35 seconds\n" +
                "- Material: metal\n" +
                "- Efficiency: 99%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 10 keys can be disinfected in 15 seconds, where we add 10 seconds at the start and 10 at the end for losses, errors and inefficiency.");

        List<String> list4 = new ArrayList<>();
        list4.add("- Disinfection time: 30 seconds\n" +
                "- Material: metal, plastic, glass\n" +
                "- Efficiency: 98%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 5 pairs of glasses can be disinfected in 20 seconds, where we add 5 seconds at the start and 5 at the end for losses, errors and inefficiency. Its inefficiency is due to the material of the lenses.");

        List<String> list5 = new ArrayList<>();
        list5.add("- Disinfection time: 30 seconds\n" +
                "- Material: metal, plastic, rubber, leather, glass\n" +
                "- Efficiency: 98.5%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 7 watches can be disinfected in 20 seconds, where we add 5 seconds at the start and 5 at the end for losses, errors and inefficiency. Its inefficiency is due to the material of the screen. In case it is exposed too long, it can lead to a distaining.");

        List<String> list6 = new ArrayList<>();
        list6.add("- Disinfection time: 75 seconds\n" +
                "- Material: metal, plastic, glass\n" +
                "- Efficiency: 96%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, one laptop can be disinfected in 55 seconds, where we add 10 seconds at the start and 10 at the end for losses, errors and inefficiency. Its inefficiency is due to the impossibility of leaving the laptop open. In case it is exposed too long, it can lead to a distaining.");

        List<String> list7 = new ArrayList<>();
        list7.add("- Disinfection time: 55 seconds\n" +
                "- Material: metal, plastic, glass\n" +
                "- Efficiency: 98%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 2 cameras can be disinfected in 35 seconds, where we add 10 seconds at the start and 10 at the end for losses, errors and inefficiency. Its inefficiency is due to the material of the lenses and the screen. In case it is exposed too long, it can lead to a distaining.");

        List<String> list8 = new ArrayList<>();
        list8.add("- Disinfection time: 25 seconds\n" +
                "- Material: metal, plastic, rubber\n" +
                "- Efficiency: 99,98%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 7 sets of headphones can be disinfected in 15 seconds, where we add 5 seconds at the start and 5 at the end for losses, errors and inefficiency. In case it is exposed too long, it can lead to a distaining.");

        List<String> list9 = new ArrayList<>();
        list9.add("- Disinfection time: 40 seconds\n" +
                "- Material: metal, plastic\n" +
                "- Efficiency: 99,6%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 4 mice can be disinfected in 20 seconds, where we add 10 seconds at the start and 10 at the end for losses, errors and inefficiency. In case it is exposed too long, it can lead to a distaining.");

        List<String> list10 = new ArrayList<>();
        list10.add("- Disinfection time: 20 seconds\n" +
                "- Material: plastic, ruber, polyesther\n" +
                "- Efficiency: 99,9%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 7 chargers/cables can be disinfected in 10 seconds, where we add 5 seconds at the start and 5 at the end for losses, errors and inefficiency. In case it is exposed too long, it can lead to a distaining.");

        List<String> list11 = new ArrayList<>();
        list11.add("- Disinfection time: 50 seconds\n" +
                "- Material: plastic, rubber\n" +
                "- Efficiency: 99,5%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 4 remotes/joysticks can be disinfected in 30 seconds, where we add 10 seconds at the start and 10 at the end for losses, errors and inefficiency. In case it is exposed too long, it can lead to a distaining.");

        List<String> list12 = new ArrayList<>();
        list12.add("- Disinfection time: 20 seconds\n" +
                "- Material: plastic, rubber, metal, glass, polyesther etc.\n" +
                "- Efficiency: 98%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 10 accesories can be disinfected in 10 seconds, where we add 5 seconds at the start and 5 at the end for losses, errors and inefficiency. In case it is exposed too long, it can lead to a distaining.");

        List<String> list13 = new ArrayList<>();
        list13.add("- Disinfection time: 30 seconds\n" +
                "- Material: paper, leather\n" +
                "- Efficiency: 99,99%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 4 books can be disinfected in 20 seconds, where we add 5 seconds at the start and 5 at the end for losses, errors and inefficiency. In case it is exposed too long, it can lead to a distaining.");

        List<String> list14 = new ArrayList<>();
        list14.add("- Disinfection time: 15 seconds\n" +
                "- Material: plastic, metal, wood\n" +
                "- Efficiency: 99,9%\n" +
                "- Explanation: Needed about 25 mJ/cm2  generated within 25 seconds, the leds can keep up to 20J for a second. With that known, up to 30 pens can be disinfected in 5 seconds, where we add 5 seconds at the start and 5 at the end for losses, errors and inefficiency. In case it is exposed too long, it can lead to a distaining.");

        listDetailes.put(listType.get(0),list1);
        listDetailes.put(listType.get(1),list2);
        listDetailes.put(listType.get(2),list3);
        listDetailes.put(listType.get(3),list4);
        listDetailes.put(listType.get(4),list5);
        listDetailes.put(listType.get(5),list6);
        listDetailes.put(listType.get(6),list7);
        listDetailes.put(listType.get(7),list8);
        listDetailes.put(listType.get(8),list9);
        listDetailes.put(listType.get(9),list10);
        listDetailes.put(listType.get(10),list11);
        listDetailes.put(listType.get(11),list12);
        listDetailes.put(listType.get(12),list13);
        listDetailes.put(listType.get(13),list14);

    }
}