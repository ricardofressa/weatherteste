package br.com.example.weathertest.layout.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;
import java.util.List;

import br.com.example.weathertest.R;
import br.com.example.weathertest.domain.BaseResponse;
import br.com.example.weathertest.domain.City;

/**
 * Created by ricardofressa.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.WeatherViewHolder> {

    private Context ctx;
    private BaseResponse baseResponse;
    private String urlImage = "http://openweathermap.org/img/w/";

    public ForecastAdapter(BaseResponse baseResponse){
        this.baseResponse = baseResponse;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_forecast, viewGroup, false);
        WeatherViewHolder pvh = new WeatherViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder weatherViewHolder, int i) {
        Uri uri = Uri.parse(urlImage+baseResponse.getCities().get(i).getWeather().get(0).getIcon()+".png");
        weatherViewHolder.weather_icon.setImageURI(uri);
        weatherViewHolder.city_name.setText(baseResponse.getCities().get(i).getName());
        weatherViewHolder.condition.setText(baseResponse.getCities().get(i).getWeather().get(0).getDescription());
        weatherViewHolder.degrees.setText(String.valueOf(baseResponse.getCities().get(i).getWeatherData().getTemperature() + "°"));
    }

    @Override
    public int getItemCount() {
        return baseResponse.getCities().size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        SimpleDraweeView weather_icon;
        TextView city_name;
        TextView condition;
        TextView degrees;


        WeatherViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.recycler_id);
            weather_icon = (SimpleDraweeView) itemView.findViewById(R.id.weather_icon);
            city_name = (TextView) itemView.findViewById(R.id.tv_city_name);
            condition = (TextView) itemView.findViewById(R.id.tv_condition);
            degrees = (TextView) itemView.findViewById(R.id.tv_degrees);

        }
    }
}
