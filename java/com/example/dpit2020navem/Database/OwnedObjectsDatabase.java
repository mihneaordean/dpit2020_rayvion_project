package com.example.dpit2020navem.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;


public class OwnedObjectsDatabase extends SQLiteAssetHelper {

    public static final String DB_NAME = "OwnedObjectsDatabase.db";
    public static final int DB_VER = 1;

    public OwnedObjectsDatabase(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<OwnedObject> getOwnedObjects() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ObjectId", "ObjectType", "ObjectName", "ObjectDisinfectionTime", "IsObjectInBox", "LastTimeDisinfected"};
        String sqlTable = "OwnedObjectsDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        List<OwnedObject> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                OwnedObject ownedObject = extractOwnedObjectsFromCursor(c);

                result.add(ownedObject);
            } while (c.moveToNext());
        }

        return result;
    }

    private OwnedObject extractOwnedObjectsFromCursor(Cursor c) {

        OwnedObject ownedObject = new OwnedObject();
        ownedObject.setOwnedObjectId(c.getLong(c.getColumnIndex("ObjectId")));
        ownedObject.setOwnedObjectType(c.getString(c.getColumnIndex("ObjectType")));
        ownedObject.setOwnedObjectName(c.getString(c.getColumnIndex("ObjectName")));
        ownedObject.setOwnedObjectDisinfectionTime(c.getInt(c.getColumnIndex("ObjectDisinfectionTime")));
        ownedObject.setIsOwnedObjectInBox(c.getInt(c.getColumnIndex("IsObjectInBox")));
        ownedObject.setLastTimeDisinfected(c.getString(c.getColumnIndex("LastTimeDisinfected")));

        return ownedObject;
    }

    public void addToOwnedObjectsDatabase(OwnedObject ownedObject) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OwnedObjectsDetail(ObjectId,ObjectType,ObjectName,ObjectDisinfectionTime,IsObjectInBox,LastTimeDisinfected) VALUES('%s','%s','%s','%s','%s','%s');",
                ownedObject.getOwnedObjectId(),
                ownedObject.getOwnedObjectType(),
                ownedObject.getOwnedObjectName(),
                ownedObject.getOwnedObjectDisinfectionTime(),
                ownedObject.getIsOwnedObjectInBox(),
                ownedObject.getLastTimeDisinfected());
        db.execSQL(query);
    }

    public void cleanOwnedObjectsDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OwnedObjectsDetail");
        db.execSQL(query);
    }

    public void removeObjectFromOwnedObjectsDatabase(Long ownedObjectId) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OwnedObjectsDetail where ObjectId=%d", ownedObjectId);
        db.execSQL(query);
    }

    public OwnedObject getObjectsByObjectId(Long ObjectId) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ObjectId", "ObjectType", "ObjectName", "ObjectDisinfectionTime","IsObjectInBox","LastTimeDisinfected"};
        String sqlTable = "OwnedObjectsDetail";

        qb.setTables(sqlTable);
        String selection = String.format("ObjectId=%d", ObjectId);

        Cursor c = qb.query(db, sqlSelect, selection, null, null, null, null);

        OwnedObject ownedObject = null;
        if (c.moveToFirst()) {

            ownedObject = extractOwnedObjectsFromCursor(c);

        }

        return ownedObject;
    }

    public List<OwnedObject> getObjectsByObjectType(String ownedObjectType) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ObjectId", "ObjectType", "ObjectName", "ObjectDisinfectionTime","IsObjectInBox","LastTimeDisinfected"};
        String sqlTable = "OwnedObjectsDetail";

        qb.setTables(sqlTable);
        String selection = String.format("ObjectType='%s'", ownedObjectType);

        Cursor c = qb.query(db, sqlSelect, selection, null, null, null, null);

        List<OwnedObject> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                OwnedObject ownedObject = extractOwnedObjectsFromCursor(c);

                result.add(ownedObject);
            } while (c.moveToNext());
        }

        return result;
    }

    public List<OwnedObject> getObjectsByIsObjectInBox(Integer isObjectInBox) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ObjectId", "ObjectType", "ObjectName", "ObjectDisinfectionTime","IsObjectInBox","LastTimeDisinfected"};
        String sqlTable = "OwnedObjectsDetail";

        qb.setTables(sqlTable);
        String selection = String.format("IsObjectInBox=%d", isObjectInBox);

        Cursor c = qb.query(db, sqlSelect, selection, null, null, null, null);

        List<OwnedObject> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                OwnedObject ownedObject = extractOwnedObjectsFromCursor(c);

                result.add(ownedObject);
            } while (c.moveToNext());
        }

        return result;
    }

}
