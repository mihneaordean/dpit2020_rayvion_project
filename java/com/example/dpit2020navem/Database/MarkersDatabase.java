package com.example.dpit2020navem.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.Map.MyMarker;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class MarkersDatabase  extends SQLiteAssetHelper {
    public static final String DB_NAME = "MarkersDatabase.db";
    public static final int DB_VER = 1;

    public MarkersDatabase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<MyMarker> getMarkers() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"MarkerId", "MarkerLatitude", "MarkerLongitude", "MarkerName", "MarkerPicture"};
        String sqlTable = "MarkersDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        List<MyMarker> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                MyMarker myMarker = extractMarkersFromCursor(c);

                result.add(myMarker);
            } while (c.moveToNext());
        }

        return result;
    }

    private MyMarker extractMarkersFromCursor(Cursor c) {

        MyMarker myMarker = new MyMarker();
        myMarker.setMarkerId(c.getLong(c.getColumnIndex("MarkerId")));
        myMarker.setMarkerLatitude(c.getDouble(c.getColumnIndex("MarkerLatitude")));
        myMarker.setMarkerLongitude(c.getDouble(c.getColumnIndex("MarkerLongitude")));
        myMarker.setMarkerName(c.getString(c.getColumnIndex("MarkerName")));
        myMarker.setMarkerPicture(c.getInt(c.getColumnIndex("MarkerPicture")));

        return myMarker;
    }

    public void addToMarkersDatabase(MyMarker myMarker) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO MarkersDetail(MarkerId,MarkerLatitude,MarkerLongitude,MarkerName,MarkerPicture) VALUES('%s','%s','%s','%s','%s');",
                myMarker.getMarkerId(),
                myMarker.getMarkerLatitude(),
                myMarker.getMarkerLongitude(),
                myMarker.getMarkerName(),
                myMarker.getMarkerPicture());
        db.execSQL(query);
    }

    public void cleanMarkersDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM MarkersDetail");
        db.execSQL(query);
    }

    public void removeMarkerFromMarkersDatabase(Long markerId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM MarkersDetail where MarkerId=%d", markerId);
        db.execSQL(query);
    }

    public MyMarker getMarkersByMarkerId(Long MarkerId) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"MarkerId", "MarkerLatitude", "MarkerLongitude", "MarkerName", "MarkerPicture"};
        String sqlTable = "MarkersDetail";

        qb.setTables(sqlTable);
        String selection = String.format("MarkerId=%d", MarkerId);

        Cursor c = qb.query(db, sqlSelect, selection, null, null, null, null);

        MyMarker myMarker = null;
        if (c.moveToFirst()) {

            myMarker = extractMarkersFromCursor(c);

        }

        return myMarker;
    }
}
