package br.com.example.weathertest;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.example.weathertest.business.BaseResponseBO;
import br.com.example.weathertest.business.BusinessException;
import br.com.example.weathertest.domain.BaseResponse;
import br.com.example.weathertest.layout.adapter.TabsAdapter;
import br.com.example.weathertest.util.Util;
import br.com.example.weathertest.util.extras.SlidingTabLayout;
import br.com.example.weathertest.util.task.AppAsyncTask;
import br.com.example.weathertest.util.task.AsyncTaskExecutor;
import br.com.example.weathertest.util.task.AsyncTaskResult;

public class MainActivity extends AppCompatActivity
        implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Toolbar mToolbar;

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    private AsyncTaskExecutor taskExecutor;
    private BaseResponseBO baseResponseBO;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private TabsAdapter tabsFragmentAdapter;

    private boolean isPaused;
    private boolean typeDegress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TOOLBAR
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(mToolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(typeDegress)
                {
                    typeDegress = false;
                    fab.setImageResource(R.drawable.ic_temperature_fahrenheit);
                }
                else{
                    typeDegress = true;
                    fab.setImageResource(R.drawable.ic_temperature_celsius);
                }
                if(mLastLocation != null)
                    showNearCities(mLastLocation, typeDegress);
                else
                    Snackbar.make(view, getString(R.string.location_error), Snackbar.LENGTH_LONG)
                        .setAction("Error", null).show();
            }
        });

        if (!Util.isConnected(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme);
            builder.setTitle(R.string.dialog_title_error)
                    .setMessage(R.string.dialog_error_no_network)
                    .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.show();
        }else {
            this.taskExecutor = new AsyncTaskExecutor();
            this.baseResponseBO = new BaseResponseBO();
        }

        // TABS
        mViewPager = (ViewPager) findViewById(R.id.vp_tabs);
        //mViewPager.setAdapter(new TabsAdapter(getSupportFragmentManager(), this));
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
    }

    @Override
    protected void onResume(){
        super.onResume();
        isPaused = false;
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        if (mGoogleApiClient != null) {
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            } catch (RuntimeException e) {
                Log.e(getString(R.string.app_name), e.getMessage());
            }

        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        showNearCities(mLastLocation, true);
    }

    private void showNearCities(final Location mLastLocation, final boolean typeDegress) {
        taskExecutor.startExecutor(new AppAsyncTask<BaseResponse>() {
            @Override
            public AsyncTaskResult<BaseResponse> onStart() {
                try {
                    return new AsyncTaskResult<>(baseResponseBO.showNearCities(mLastLocation, typeDegress));
                } catch (BusinessException e) {
                    return new AsyncTaskResult<>(e);
                }
            }

            @Override
            public void onFinish(AsyncTaskResult<BaseResponse> result) {
                showDetails(result.response());
            }
        });
    }

    private void showDetails(BaseResponse baseResponse){
        if(!isPaused) {
            tabsFragmentAdapter = new TabsAdapter(getSupportFragmentManager(),
                    getApplicationContext(),
                    baseResponse, mLastLocation);
            mViewPager.setVisibility(View.VISIBLE);
            mViewPager.setAdapter(tabsFragmentAdapter);

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {}

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (mGoogleApiClient != null) {
                        try {
                            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MainActivity.this);
                        } catch (RuntimeException e) {
                            Log.e(getString(R.string.app_name), e.getMessage());
                        }

                    }
                }
            });

            mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.primary));
            mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.icons));
            mSlidingTabLayout.setViewPager(mViewPager);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.DialogTheme);
            builder.setTitle(R.string.dialog_title_error)
                    .setMessage(R.string.main_gps_not_enabled)
                    .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
            builder.show();
        } else {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(20000);
            mLocationRequest.setFastestInterval(20000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.permission_rationale)
                        .setMessage(R.string.permission_rationale_location)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
