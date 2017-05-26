package br.com.example.weathertest.layout.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.parceler.Parcels;

import br.com.example.weathertest.R;
import br.com.example.weathertest.domain.BaseResponse;
import br.com.example.weathertest.layout.adapter.ForecastAdapter;

import static br.com.example.weathertest.R.id.weather_icon;

public class NearCitiesListFragment extends BaseTabFragment {

    private View mainView;

    private RecyclerView mRecyclerView;
    private ForecastAdapter adapter;

    private BaseResponse baseResponse;

    public NearCitiesListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_near_cities_list, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        baseResponse = Parcels.unwrap(getArguments().getParcelable(BaseResponse.EXTRA));
        adapter = new ForecastAdapter(baseResponse);

        mRecyclerView = (RecyclerView) mainView.findViewById(R.id.forecast_list_recycler);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public int getTitle() {
        return R.string.near_cities_list;
    }
}
