package br.com.example.weathertest.domain;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by ricardofressa.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Coordinate implements Serializable {

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lon")
    private double longitude;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
