package br.com.example.weathertest.domain;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by ricardofressa.
 */
@Parcel(Parcel.Serialization.BEAN)
public class Country implements Serializable {

    @SerializedName("country")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
