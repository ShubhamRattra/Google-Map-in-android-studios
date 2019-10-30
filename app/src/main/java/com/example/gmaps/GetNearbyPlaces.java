package com.example.gmaps;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object, String, String> {

    private String googleplaceData, url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects)
    {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try
        {
            googleplaceData = downloadUrl.ReadTheUrl(url);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return googleplaceData;
    }

    @Override
    protected void onPostExecute(String s)
    {
        List<HashMap<String, String>> nearbyByPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyByPlacesList = dataParser.parse(s);

        DisplayNearbyPlaces(nearbyByPlacesList);
    }

    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearbyByPlacesList)
    {
        for (int i=0; i<nearbyByPlacesList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> googleNearbyPlace = nearbyByPlacesList.get(i);
            String nameOfPlace = googleNearbyPlace.get("place_name");
            String vicinity = googleNearbyPlace.get("vicinity");
            double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
            double lng = Double.parseDouble(googleNearbyPlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + " ; " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        }
    }
}
