package com.example.dpit2020navem.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dpit2020navem.Database.MarkersDatabase;
import com.example.dpit2020navem.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 18f;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap map;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    Location currentLocation;
    Button buttonSelectHomeLocation;
    Button buttonSelectHomeCurrentLocation;
    Button buttonCancelSelection;
    MarkerOptions homeLocationPoint;
    Marker homeLocation;
    MarkerOptions settedHomeLocation;
    Marker settedHome;
    LatLng homeLatLng;
    MarkersDatabase markersDatabase;
    List<MyMarker> markersList;
    TextView distance;
    LatLng location;
    Handler handler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        markersDatabase = new MarkersDatabase(this);

        setUpButtons();
        getLocationPermission();


    }

    private void setUpButtons() {
        buttonSelectHomeLocation = findViewById(R.id.buttonSelectHomeLocation);
        buttonSelectHomeLocation.setVisibility(View.INVISIBLE);
        buttonCancelSelection = findViewById(R.id.buttonCancelSelection);
        buttonCancelSelection.setVisibility(View.INVISIBLE);
        buttonSelectHomeCurrentLocation = findViewById(R.id.buttonSelectHomeCurrentLocation);
        buttonSelectHomeCurrentLocation.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        map = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);


            selectHomeLocation();
            selectHomeCurrentLocation();
            addMapMarkers();
            setUpDistanceTextView();

        }
    }

    private void selectHomeLocation(){
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(final LatLng selectedPoint) {

                homeLocationPoint = new MarkerOptions().position(selectedPoint).title("Selcted point");
                if(homeLocation != null){
                    homeLocation.remove();
                }
                homeLocation = map.addMarker(homeLocationPoint);


                buttonSelectHomeLocation.setVisibility(View.VISIBLE);
                buttonCancelSelection.setVisibility(View.VISIBLE);
                buttonSelectHomeCurrentLocation.setVisibility(View.INVISIBLE);

                buttonSelectHomeLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), selectedPoint.toString(), Toast.LENGTH_SHORT).show();
                        map.clear();
                        markersDatabase.removeMarkerFromMarkersDatabase(1L);

                        int height = 100;
                        int width = 100;
                        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.home_map_icon);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);

                        settedHomeLocation = new MarkerOptions().position(selectedPoint)
                                .title("Home").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                        settedHome = map.addMarker(settedHomeLocation);

                        MyMarker myMarker = new MyMarker();
                        myMarker.setMarkerId(1L);
                        myMarker.setMarkerLatitude(selectedPoint.latitude);
                        myMarker.setMarkerLongitude(selectedPoint.longitude);
                        myMarker.setMarkerName("Home");
                        myMarker.setMarkerPicture(R.id.homePage);
                        markersDatabase.addToMarkersDatabase(myMarker);


                        buttonSelectHomeLocation.setVisibility(View.INVISIBLE);
                        buttonCancelSelection.setVisibility(View.INVISIBLE);
                        buttonSelectHomeCurrentLocation.setVisibility(View.VISIBLE);
                    }
                });

                buttonCancelSelection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homeLocation.remove();
                        buttonSelectHomeLocation.setVisibility(View.INVISIBLE);
                        buttonCancelSelection.setVisibility(View.INVISIBLE);
                        buttonSelectHomeCurrentLocation.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void selectHomeCurrentLocation(){
        buttonSelectHomeCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                map.clear();
                markersDatabase.removeMarkerFromMarkersDatabase(1L);

                int height = 100;
                int width = 100;
                BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.home_map_icon);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);

                settedHomeLocation = new MarkerOptions().position(currentLatLng)
                        .title("Home").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                settedHome = map.addMarker(settedHomeLocation);

                MyMarker myMarker = new MyMarker();
                myMarker.setMarkerId(1L);
                myMarker.setMarkerLatitude(currentLatLng.latitude);
                myMarker.setMarkerLongitude(currentLatLng.longitude);
                myMarker.setMarkerName("Home");
                myMarker.setMarkerPicture(R.id.homePage);
                markersDatabase.addToMarkersDatabase(myMarker);
            }
        });
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();

                            if(currentLocation != null){
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);
                            }

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            //Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void getDeviceLocation2(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();


                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            //Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;

                    initMap();
                }
            }
        }
    }

    private void addMapMarkers(){
        markersList = markersDatabase.getMarkers();

        for(int i = 0 ; i <markersList.size() ; i++){
            int height = 100;
            int width = 100;
            BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.home_map_icon);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, width, height, false);

            MyMarker myMarker = markersList.get(i);
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(myMarker.getMarkerLatitude(),myMarker.getMarkerLongitude()))
                    .title(myMarker.getMarkerName()).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            map.addMarker(markerOptions);
        }

    }

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return meter * 1000;
    }

    public void setUpDistanceTextView(){
        distance = findViewById(R.id.distance);

        final MyMarker homeMarker = markersDatabase.getMarkersByMarkerId(1L);


        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                MyMarker homeMarker = markersDatabase.getMarkersByMarkerId(1L);
                if(homeMarker != null){
                    if(currentLocation != null){
                        final LatLng home = new LatLng(homeMarker.getMarkerLatitude(), homeMarker.getMarkerLongitude());

                        getDeviceLocation2();
                        location = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        distance.setText(String.valueOf(calculationByDistance(home, location)));
                    }
                }


                handler.postDelayed(this, 100);
            }
        });

    }



}