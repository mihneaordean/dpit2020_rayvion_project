package com.example.dpit2020navem.Map;

public class MyMarker {
    private Long markerId;
    private Double markerLatitude;
    private Double markerLongitude;
    private String markerName;
    private int markerPicture;


    public Long getMarkerId() {
        return markerId;
    }

    public void setMarkerId(Long markerId) {
        this.markerId = markerId;
    }

    public Double getMarkerLatitude() {
        return markerLatitude;
    }

    public void setMarkerLatitude(Double markerLatitude) {
        this.markerLatitude = markerLatitude;
    }

    public Double getMarkerLongitude() {
        return markerLongitude;
    }

    public void setMarkerLongitude(Double markerLongitude) {
        this.markerLongitude = markerLongitude;
    }

    public String getMarkerName() {
        return markerName;
    }

    public void setMarkerName(String markerName) {
        this.markerName = markerName;
    }

    public int getMarkerPicture() {
        return markerPicture;
    }

    public void setMarkerPicture(int markerPicture) {
        this.markerPicture = markerPicture;
    }
}
