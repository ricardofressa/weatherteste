package br.com.example.weathertest.layout.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.List;

import br.com.example.weathertest.R;
import br.com.example.weathertest.business.BaseResponseBO;
import br.com.example.weathertest.business.BusinessException;
import br.com.example.weathertest.domain.BaseResponse;
import br.com.example.weathertest.util.task.AppAsyncTask;
import br.com.example.weathertest.util.task.AsyncTaskExecutor;
import br.com.example.weathertest.util.task.AsyncTaskResult;

import static android.content.Context.LOCATION_SERVICE;

public class NearCitiesMapsFragment extends BaseTabFragment
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private View mainView;


    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private AsyncTaskExecutor taskExecutor;
    private BaseResponseBO baseResponseBO;

    /**
     * Variable maps
     */
    private GoogleMap mGoogleMap;
    private Location mLastLocation;
    private BaseResponse baseResponse;
    private List<Marker> markers;

    public NearCitiesMapsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_near_cities_maps, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        baseResponse = Parcels.unwrap(getArguments().getParcelable(BaseResponse.EXTRA));
        mLastLocation = Parcels.unwrap(getArguments().getParcelable("Location"));

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.taskExecutor = new AsyncTaskExecutor();
        this.baseResponseBO = new BaseResponseBO();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getContext(), R.string.permission_negate, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private synchronized void buildGoogleApiClient() {
        onLocationChanged();
    }

    public void onLocationChanged() {

        //Place current location marker
        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (markers != null) {
                    CameraPosition position = mGoogleMap.getCameraPosition();
                    Log.i(getClass().getSimpleName(), "onCameraMove " + position.zoom);
                    for (int i = 0; i < markers.size(); i++) {
                        Marker marker = markers.get(i);
                        marker.setVisible(position.zoom > 7);
                    }
                }
            }
        });

//        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//
//                if (currentClinicList != null) {
//                    for (int i = 0; i < currentClinicList.size(); i++) {
//                        Clinic clinic = currentClinicList.get(i);
//                        if (clinic.getNome().equals(marker.getTitle())) {
//                            Intent intent = new Intent(getApplicationContext(), ClinicDetailsActivity.class);
//                            intent.putExtra(Clinic.EXTRA, clinic);
//                            startActivity(intent);
//                            return true;
//                        }
//                    }
//                }
//
//                return false;
//            }
//        });

        showNearCities(mLastLocation);
    }

    private void showNearCities(final Location mLastLocation) {
        taskExecutor.startExecutor(new AppAsyncTask<BaseResponse>() {
            @Override
            public AsyncTaskResult<BaseResponse> onStart() {
                try {
                    return new AsyncTaskResult<>(baseResponseBO.showNearCities(mLastLocation, true));
                } catch (BusinessException e) {
                    return new AsyncTaskResult<>(e);
                }
            }

            @Override
            public void onFinish(AsyncTaskResult<BaseResponse> result) {
                BaseResponse baseResponse = result.response();
                for (int i = 0; i < baseResponse.getCities().size(); i++) {
                    LatLng latLng = new LatLng(
                            baseResponse.getCities().get(i).getCoordinate().getLatitude(),
                            baseResponse.getCities().get(i).getCoordinate().getLongitude());
                    mGoogleMap.addMarker(
                            new MarkerOptions()
                                    .position(latLng)
                                    .title(baseResponse.getCities().get(i).getName())
                                    .snippet(String.valueOf(baseResponse.getCities().get(i).getWeatherData().getTemperature())));
                }
            }
        });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.permission_rationale)
                        .setMessage(R.string.permission_rationale_location)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        LOCATION_PERMISSION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public int getTitle() {
        return R.string.near_cities_maps;
    }
}
