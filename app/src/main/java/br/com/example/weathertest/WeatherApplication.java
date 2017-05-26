package br.com.example.weathertest;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by ricardofressa.
 */

public class WeatherApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        Fresco.initialize(this);
    }
}
