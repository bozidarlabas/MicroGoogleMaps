package com.bozidar.labas.googlemaps.fragments;

import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bozidar.labas.googlemaps.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

/**
 * Created by Bozidar on 02.11.2015..
 */
public class MapFragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    //Getting the user's location for initializing the map camera
    private GoogleApiClient googleApiClient;
    private Location currentLocation;

    //Switching between different map display types
    private final int[] MAP_TYPES = {
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE};
    private int curMapTypeIndex;

    /**
     * Creating GoogleApiClient and intializatin of LocationServices in order to get users current location
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        initListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /**
     * The initListeners method binds the interfaces
     */
    private void initListeners() {
        getMap().setOnMarkerClickListener(this);
        getMap().setOnMapLongClickListener(this);
        getMap().setOnInfoWindowClickListener(this);
        getMap().setOnMapClickListener(this);
    }

    /**
     * Whwen the client has connected, grab the users most recently retrieved location and use that for aiming map camera
     *
     * @param bundle
     */
    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = LocationServices
                .FusedLocationApi
                .getLastLocation(googleApiClient);
        initCamera(currentLocation);
    }

    /**
     * Initialize the camera and some basic map properties
     *
     * @param currentLocation
     */
    private void initCamera(Location currentLocation) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .zoom(16f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);

        getMap().setMapType(MAP_TYPES[curMapTypeIndex]);
        getMap().setTrafficEnabled(true);
        getMap().setMyLocationEnabled(true);
        getMap().getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().position(latLng);
        options.title(getAddressFromLatLng(latLng));

        options.icon(BitmapDescriptorFactory.defaultMarker());

        getMap().addMarker(options);

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        MarkerOptions options = new MarkerOptions().position(latLng);
        options.title(getAddressFromLatLng(latLng));

        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)));

        getMap().addMarker(options);
    }


    /**
     * Helper method that takes LatLng and runs it through Geocoder to get a street adress
     * @param latLng
     * @return
     */
    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getActivity());
        String adress = "";

        try {
            adress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1).get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adress;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    /**
     * Method for drawing circle on map
     * @param location
     */
    private void drawCircle(LatLng location){
        CircleOptions options = new CircleOptions();
        options.center(location);

        options.radius(10); //10 meters
        options.fillColor(getResources().getColor(R.color.circle_color));
        options.strokeColor(getResources().getColor(R.color.stroke_color));
        options.strokeWidth(10);
        getMap().addCircle(options);
    }

    /**
     * Inflate main menu
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    /**
     * Get clicked item action from menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_circle:
                drawCircle(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

//ConnectionCallbacks and OnConnectionFailedListener are designed to monitor the state of the GoogleApiClient, which is used in this application for getting the user's current location.

//OnInfoWindowClickListener is triggered when the user clicks on the info window that pops up over a marker on the map.

//OnMapLongClickListener and OnMapClickListener are triggered when the user either taps or holds down on a portion of the map.

//OnMarkerClickListener is called when the user clicks on a marker on the map, which typically also displays the info window for that marker.