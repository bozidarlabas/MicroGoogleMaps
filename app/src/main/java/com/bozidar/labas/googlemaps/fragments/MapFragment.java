package com.bozidar.labas.googlemaps.fragments;

import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Bozidar on 02.11.2015..
 */
public class MapFragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener{

    @Override
    public void onConnected(Bundle bundle) {

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

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}

//ConnectionCallbacks and OnConnectionFailedListener are designed to monitor the state of the GoogleApiClient, which is used in this application for getting the user's current location.

//OnInfoWindowClickListener is triggered when the user clicks on the info window that pops up over a marker on the map.

//OnMapLongClickListener and OnMapClickListener are triggered when the user either taps or holds down on a portion of the map.

//OnMarkerClickListener is called when the user clicks on a marker on the map, which typically also displays the info window for that marker.