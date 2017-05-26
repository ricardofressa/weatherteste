package br.com.example.weathertest.domain;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ricardofressa.
 */
@Parcel(Parcel.Serialization.BEAN)
public class BaseResponse implements Serializable {

    public static final String EXTRA = "EXTRA_BASE_RESPONSE";

    private String message;

    @SerializedName("cod")
    private int responseCode;

    @SerializedName("count")
    private int aroundCities;

    @SerializedName("list")
    private List<City> cities;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getAroundCities() {
        return aroundCities;
    }

    public void setAroundCities(int aroundCities) {
        this.aroundCities = aroundCities;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}
