package br.com.example.weathertest.layout.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.parceler.Parcels;

import br.com.example.weathertest.domain.BaseResponse;
import br.com.example.weathertest.layout.fragment.BaseTabFragment;
import br.com.example.weathertest.layout.fragment.NearCitiesListFragment;
import br.com.example.weathertest.layout.fragment.NearCitiesMapsFragment;


/**
 * Created by ricardofressa.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private Location location;
    private BaseTabFragment[] fragmentTabs;

    public TabsAdapter(FragmentManager fm, Context context, BaseResponse baseResponse, Location location) {
        super(fm);
        this.context = context;
        this.location = location;
        this.fragmentTabs = buildFragments(baseResponse, location);
    }

    private BaseTabFragment[] buildFragments(BaseResponse baseResponse, Location location) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BaseResponse.EXTRA, Parcels.wrap(baseResponse));
        bundle.putParcelable("Location", Parcels.wrap(location));

        BaseTabFragment nearCitiesListFragment = new NearCitiesListFragment();
        nearCitiesListFragment.setArguments(bundle);
        BaseTabFragment nearCitiesMapsFragment = new NearCitiesMapsFragment();
        nearCitiesMapsFragment.setArguments(bundle);

        return new BaseTabFragment[]{nearCitiesListFragment, nearCitiesMapsFragment};
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentTabs[position];
    }

    @Override
    public int getCount() {
        return fragmentTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.context.getString(fragmentTabs[position].getTitle());
    }
}
