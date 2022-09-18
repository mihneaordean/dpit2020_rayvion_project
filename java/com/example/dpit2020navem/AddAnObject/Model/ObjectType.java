package com.example.dpit2020navem.AddAnObject.Model;

import android.graphics.drawable.Drawable;
import android.media.Image;

import java.io.Serializable;
import java.util.List;

public class ObjectType implements Serializable {
    private String objectTypeName;
    private int objectTypePicture;
    private Integer objectTypeDisinfectionTime;//seconds
    private List<OwnedObject> ownedObjectList;

    public ObjectType(String objectTypeName, int objectTypePicture, Integer objectTypeDisinfectionTime, List<OwnedObject> ownedObjectList) {
        this.objectTypeName = objectTypeName;
        this.objectTypePicture = objectTypePicture;
        this.objectTypeDisinfectionTime = objectTypeDisinfectionTime;
        this.ownedObjectList = ownedObjectList;
    }

    public String getObjectTypeName() {
        return objectTypeName;
    }

    public void setObjectTypeName(String objectTypeName) {
        this.objectTypeName = objectTypeName;
    }

    public int getObjectTypePicture() {
        return objectTypePicture;
    }

    public void setObjectTypePicture(int objectTypePicture) {
        this.objectTypePicture = objectTypePicture;
    }

    public Integer getObjectTypeDisinfectionTime() {
        return objectTypeDisinfectionTime;
    }

    public void setObjectTypeDisinfectionTime(Integer objectTypeDisinfectionTime) {
        this.objectTypeDisinfectionTime = objectTypeDisinfectionTime;
    }

    public List<OwnedObject> getOwnedObjectList() {
        return ownedObjectList;
    }

    public void setOwnedObjectList(List<OwnedObject> ownedObjectList) {
        this.ownedObjectList = ownedObjectList;
    }
}
