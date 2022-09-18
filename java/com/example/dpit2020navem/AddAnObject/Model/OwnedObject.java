package com.example.dpit2020navem.AddAnObject.Model;

public class OwnedObject {
    private Long ownedObjectId;
    private String ownedObjectType;
    private String ownedObjectName;
    private Integer ownedObjectDisinfectionTime;
    private Integer isOwnedObjectInBox;
    private String lastTimeDisinfected;


    public Long getOwnedObjectId() {
        return ownedObjectId;
    }

    public void setOwnedObjectId(Long ownedObjectId) {
        this.ownedObjectId = ownedObjectId;
    }

    public String getOwnedObjectType() {
        return ownedObjectType;
    }

    public void setOwnedObjectType(String ownedObjectType) {
        this.ownedObjectType = ownedObjectType;
    }

    public String getOwnedObjectName() {
        return ownedObjectName;
    }

    public void setOwnedObjectName(String ownedObjectName) {
        this.ownedObjectName = ownedObjectName;
    }

    public Integer getOwnedObjectDisinfectionTime() {
        return ownedObjectDisinfectionTime;
    }

    public void setOwnedObjectDisinfectionTime(Integer ownedObjectDisinfectionTime) {
        this.ownedObjectDisinfectionTime = ownedObjectDisinfectionTime;
    }

    public Integer getIsOwnedObjectInBox() {
        return isOwnedObjectInBox;
    }

    public void setIsOwnedObjectInBox(Integer ownedObjectInBox) {
        isOwnedObjectInBox = ownedObjectInBox;
    }

    public String getLastTimeDisinfected() {
        return lastTimeDisinfected;
    }

    public void setLastTimeDisinfected(String lastTimeDisinfected) {
        this.lastTimeDisinfected = lastTimeDisinfected;
    }
}
