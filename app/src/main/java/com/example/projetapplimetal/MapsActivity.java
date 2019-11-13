package com.example.projetapplimetal;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {

    private GoogleMap mMap;
    public final int LOCATION = 1;
    public final int AJOUT_CONCERT=2;
    LocationManager locationManager;
    String locationProvider = LocationManager.GPS_PROVIDER;
    Location location = null;

    ArrayList<ConcertWindowData>  listeConcerts;

    ArrayList<Marker> listeMarker;

    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listeConcerts = new ArrayList<ConcertWindowData>();
        listeMarker = new ArrayList<Marker>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (locationManager != null) {

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {




                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }

            };
        }
    }

    public void addConcertToList(double lat , double lng , String titre , String image , String date , String duree){


        ConcertWindowData concert = new ConcertWindowData();


        concert.setPosition(new LatLng(lat,lng));
        concert.setNom(titre);
        concert.setImage("kal");
        concert.setDate(date);
        concert.setDuree(duree);
        concert.setLien("hello");


        listeConcerts.add(concert);


    }




    private void setConcerts(){

        mMap.clear();
        listeMarker.clear();

        ConcertInfoWindowGoogleMap concertInfoWindow = new ConcertInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(concertInfoWindow);



        for (int i = 0 ; i<listeConcerts.size() ; i++){

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(listeConcerts.get(i).getPosition()).title(listeConcerts.get(i).getNom());
            Marker concertMarker = mMap.addMarker(markerOptions);
            listeMarker.add(concertMarker);
            listeMarker.get(i).setTag(listeConcerts.get(i));
            listeMarker.get(i).showInfoWindow();

        }
    }

    public void rajoutConcert(View v){


        try {
            location = locationManager.getLastKnownLocation(locationProvider);
        }catch (SecurityException e){}
        Intent rajouter_concert = new Intent(this ,AddConcertActivity.class);
        rajouter_concert.putExtra("lat" , location.getLatitude());
        rajouter_concert.putExtra("long" , location.getLongitude());
        startActivityForResult(rajouter_concert , AJOUT_CONCERT );

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==AJOUT_CONCERT && resultCode==RESULT_OK){

            Bundle extras = data.getExtras();

            if(extras !=null){




                addConcertToList(extras.getDouble("lati") , extras.getDouble("longi") , extras.getString("titre") ,
                        "kala" , extras.getString("date") , extras.getString("duree"));


                setConcerts();

                Log.println(Log.ASSERT , "LISTES Concerts" , listeConcerts.toString());
                Log.println(Log.ASSERT , "LISTES MARKER" , listeMarker.toString());

            }



        }




    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] perm = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, perm, LOCATION);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);

        }


    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

}
