package br.com.example.weathertest.domain;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by ricardofressa.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Wind implements Serializable {

    private float speed;

    @SerializedName("deg")
    private float degrees;


    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
    }
}
