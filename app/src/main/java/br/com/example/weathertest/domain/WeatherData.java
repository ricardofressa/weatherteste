package br.com.example.weathertest.domain;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by ricardofressa.
 */
@Parcel(Parcel.Serialization.BEAN)
public class WeatherData implements Serializable {

    @SerializedName("temp")
    private double temperature;

    private float pressure;

    private float humidity;

    @SerializedName("temp_min")
    private double temperatureMinimum;

    @SerializedName("temp_max")
    private double temperatureMaximim;


    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public double getTemperatureMinimum() {
        return temperatureMinimum;
    }

    public void setTemperatureMinimum(double temperatureMinimum) {
        this.temperatureMinimum = temperatureMinimum;
    }

    public double getTemperatureMaximim() {
        return temperatureMaximim;
    }

    public void setTemperatureMaximim(double temperatureMaximim) {
        this.temperatureMaximim = temperatureMaximim;
    }
}
