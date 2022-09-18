package com.example.dpit2020navem.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.dpit2020navem.Map.MyMarker;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class UsefulStaffDatabase extends SQLiteAssetHelper {

    public static final String DB_NAME = "UsefulStuffDatabase.db";
    public static final int DB_VER = 1;

    public UsefulStaffDatabase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<UsefulStaff> getStuff() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Id", "Stuff"};
        String sqlTable = "UsefulStuff";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        List<UsefulStaff> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                UsefulStaff usefulStaff = extractStuffFromCursor(c);

                result.add(usefulStaff);
            } while (c.moveToNext());
        }

        return result;
    }

    private UsefulStaff extractStuffFromCursor(Cursor c) {

        UsefulStaff usefulStaff = new UsefulStaff();
        usefulStaff.setStuffId(c.getLong(c.getColumnIndex("Id")));
        usefulStaff.setStuff(c.getString(c.getColumnIndex("Stuff")));

        return usefulStaff;
    }

    public void addToStuffDatabase(UsefulStaff usefulStaff) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO UsefulStuff(Id,Stuff) VALUES('%s','%s');",
                usefulStaff.getStuffId(),
                usefulStaff.getStuff());
        db.execSQL(query);
    }

    public void cleanStuffDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM UsefulStuff");
        db.execSQL(query);
    }

    public void removeStuffFromStuffDatabase(Long id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM UsefulStuff where Id=%d", id);
        db.execSQL(query);
    }

    public UsefulStaff getStuffById(Long id) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"Id", "Stuff"};
        String sqlTable = "UsefulStuff";

        qb.setTables(sqlTable);
        String selection = String.format("Id=%d", id);

        Cursor c = qb.query(db, sqlSelect, selection, null, null, null, null);

        UsefulStaff usefulStaff = null;
        if (c.moveToFirst()) {

            usefulStaff = extractStuffFromCursor(c);

        }

        return usefulStaff;
    }

}
