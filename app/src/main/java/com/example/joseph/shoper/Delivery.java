package com.example.joseph.shoper;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TabHost;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Delivery extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CHECK_SETTINGS = 304;
    Toolbar mToolBar;
    GoogleApiClient mGoogleApiClient;
    GoogleMap mGoogleMap;
    Location mLastKnownLocation;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    double lat, lon;
    private boolean mRequestLocationUpdates;
    //marker refferance
    Marker mMarker;

    //location call backs
    private LocationCallback mLocationCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallBack=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //get a location from the result
                mLastKnownLocation=locationResult.getLastLocation();
                Log.w("Location",mLastKnownLocation.toString());
                updateMapMarker();

            }
        };
        createLocationRequest();
        startLocationUpdates();
        SupportMapFragment supportMapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.order_location);
        supportMapFragment.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Log.w("Location",mLastKnownLocation.toString());

    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(settingsBuilder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //settings are ok so initialize location requests here.
                getLastKnownLocation();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        //show a user dialog to turn location on like they do i google maps ;=)
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                            resolvableApiException.startResolutionForResult(Delivery.this, CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //if you get here probably something is realy wrong with device.You cant fix in code :-(
                        break;
                }
            }
        });
    }

    public void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        } else {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.w("Getting last location", "====");
                    if (location != null) {
                        mLastKnownLocation = location;
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==LOCATION_REQUEST_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                createLocationRequest();
                startLocationUpdates();
            }else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if(mLastKnownLocation!=null){
            LatLng position=new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            mMarker= mGoogleMap.addMarker(new MarkerOptions().position(position));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }
    }
    public void updateMapMarker(){
        if(mLastKnownLocation!=null){
            LatLng position=new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            //check if the marker has been added
            if(mMarker==null){
                //adds new marker if none has already been added
                mMarker= mGoogleMap.addMarker(new MarkerOptions().position(position));
            }else {
                //just update the marker position
                mMarker.setPosition(position);
            }
            //update the camera so marker be arround the centre of the focus
            CameraPosition cameraPosition=new CameraPosition.Builder().target(position).zoom(13).tilt(90).build();
            //adds the zooming animation :-)
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastKnownLocation=location;
    }
    //get location updates frequently
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //permision to access location not granted :-( ask user to grant
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            return;
        }else {
            //location is on so get updates
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallBack, null);
            mRequestLocationUpdates=true;
        }

    }
    //stop the updates when no longer needed
    //good candidate being when the app is being paused
    protected void stopLocationUpdates(){
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallBack);
        mRequestLocationUpdates=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mRequestLocationUpdates){
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
}
