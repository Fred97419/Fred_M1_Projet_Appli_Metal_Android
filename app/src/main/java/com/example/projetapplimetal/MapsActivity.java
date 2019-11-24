package com.example.projetapplimetal;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 * Classe MapActivity : Classe principale de l'application
 * Elle affiche la carte via l'api de Google avec les différents marqueurs
 * représentant chaque concert.
 *
 */

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback  {


    /*-----------[Déclaration des variables]--------------*/

    private GoogleMap mMap;
    public final int LOCATION = 1;
    public final int AJOUT_CONCERT=2;
    public final int LISTE_CONCERT=3;

    public final String PROX_ALERT_INTENT = "com.example.projetapplimetal.ProximityAlert";


    LocationManager locationManager;
    String locationProvider = LocationManager.GPS_PROVIDER;
    Location location = null;

    ShakeListener shaker;

    ArrayList<ConcertWindowData>  listeConcerts; //ArrayList stockant les différents concerts de la classe ConcertWindowData

    ArrayList<Marker> listeMarker;               //ArrayList des différents marqueurs via GoogleMap

    LocationListener locationListener;

    ProximityBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Charge le fragment GoogleMap ---------------------

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //----------------------------------------------------

        listeConcerts = new ArrayList<ConcertWindowData>();
        listeMarker = new ArrayList<Marker>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        receiver = new ProximityBroadcastReceiver();

        /**
         * Synchronise la liste des concerts avec ce qui est
         * sauvegardé dans l'objet SharedPreferences
         *
         * @see SharedPreferences
         */
        if(getListeFromJson("liste_concert") != null){

            listeConcerts = getListeFromJson("liste_concert");
        }



        /**
         * Classe qui detecte si il y a une secousse et lance
         * l'activité pour rajouter un concert si detecté
         *
         * @see ShakeListener
         *
         */
        shaker = new ShakeListener(this);
        shaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {

                envoieLocalisationToActivityAddConcert();

            }
        });





        /**
         * Sauvegarde de la liste des concerts dans le bundle pour éviter la perte de données
         * en cas de changement de configuration
         *
         */
        if(savedInstanceState != null){

            listeConcerts = (ArrayList<ConcertWindowData>) savedInstanceState.get("liste_concert_save");
        }



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


        initialiseReceiver();

    }




    /**
     *  Sauvegarde la liste des concerts identifié par une
     *  clé dans l'objet SharedPreferences de l'application.
     *  Elle est sauvegardé en format JSON.
     *
     * @see SharedPreferences
     * @see Gson
     *
     * @param list la liste de concerts à sauvegarder
     * @param key  le chaine de caractère pour l'identifier
     */
    private void saveListeToJson(ArrayList<ConcertWindowData> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);

        editor.apply();
    }

    /**
     * Récupère la liste identifié par la clé. Elle est récupérée
     * en format JSON pour ensuite être transformé en liste de
     * concerts.
     *
     *
     * @param key Identifiant
     * @return La liste des concerts enregistrés
     */
    private ArrayList<ConcertWindowData> getListeFromJson(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);

        Type type = new TypeToken<ArrayList<ConcertWindowData>>() {}.getType();
        return gson.fromJson(json, type);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("liste_concert_save" , listeConcerts);


    }

    /**
     * Va rajouter un objet ConcertWindowData représentant un concert
     * dans la liste des concerts.
     * @param lat latitude
     * @param lng longitude
     * @param titre titre du concert
     * @param image image représentant le concert
     * @param date  la date
     * @param heure l'heure où commence le concert
     * @param duree la durée du concert en heure
     *
     * @see ConcertWindowData
     */
    public void addConcertToList(double lat , double lng , String titre , SerializableBitmap image , String date , String heure, String duree){


        ConcertWindowData concert = new ConcertWindowData();


        concert.setLat(lat);
        concert.setLng(lng);
        concert.setNom(titre);

        if (image!=null) concert.setImage(image);
        concert.setDate(date);
        concert.setDuree(duree);
        concert.setHeure(heure);


        listeConcerts.add(concert);


    }


    /**
     * Methode qui va parcourir la liste des concerts et pour
     * chacun d'entre eux créé un marker googleMap et le rajoute
     * à la carte.
     * La fenêtre du marqueur est décrite avec la classe ConcertInfoWindowGoogleMap
     *
     * Puis sauvegarde la liste
     */
    private void setConcerts(){


        mMap.clear();
        listeMarker.clear();
        supprimerProximityAlert();


        ConcertInfoWindowAdapter concertInfoWindow = new ConcertInfoWindowAdapter(this);
        mMap.setInfoWindowAdapter(concertInfoWindow);

        for (int i = 0 ; i<listeConcerts.size() ; i++){

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(listeConcerts.get(i).getLat() , listeConcerts.get(i).getLng())).title(listeConcerts.get(i).getNom());
            Marker concertMarker = mMap.addMarker(markerOptions);
            listeMarker.add(concertMarker);
            listeMarker.get(i).setTag(listeConcerts.get(i));
            listeMarker.get(i).showInfoWindow();


        }
        ajouterProximityAlert();

        saveListeToJson(listeConcerts , "liste_concert");
    }

    /**
     * Methode qui démarre l'activité représentant la ListView des concerts
     * via un bouton.
     * En lui envoyant la liste des concerts
     * @param v
     *
     * @see ListeConcertActivity
     */
    public void voirListeConcerts(View v){

        Bundle extras = new Bundle();

        for (int i = 0 ; i<listeConcerts.size() ; i++){
            extras.putSerializable("concert"+i , listeConcerts.get(i));
        }
        extras.putInt("longueur" , listeConcerts.size());
        Intent forListeConcertsActivity = new Intent(this ,ListeConcertActivity.class);

        forListeConcertsActivity.putExtras(extras);


       startActivityForResult(forListeConcertsActivity , LISTE_CONCERT);



    }

    /**
     * Methode qui va appeler la fonction envoieLocalisationToActivity()
     * via un bouton.
     *
     * @param v
     */
    public void rajoutConcert(View v){
        envoieLocalisationToActivityAddConcert();
    }

    /**
     * Démarre l'activité pour rajouter un nouveau concert.
     * En lui envoyant la longitude et la latitude courante
     * via un Intent.
     *
     * @see AddConcertActivity
     */
    private void envoieLocalisationToActivityAddConcert(){

        try {
            location = locationManager.getLastKnownLocation(locationProvider);
        }catch (SecurityException e){}
        Intent rajouter_concert = new Intent(this ,AddConcertActivity.class);
        rajouter_concert.putExtra("lat" , location.getLatitude());
        rajouter_concert.putExtra("long" , location.getLongitude());
        startActivityForResult(rajouter_concert , AJOUT_CONCERT );

    }


    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);


    }

    public void onResume(){

        super.onResume();
        initialiseReceiver();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * Récupère toutes les informations renseigné par l'utilisateur sur un
         * concert. Puis le rajoute à la liste des concerts.
         */
        if(requestCode==AJOUT_CONCERT && resultCode==RESULT_OK){

            Bundle extras = data.getExtras();

            if(extras !=null){

                Bitmap image , image_compress;
                SerializableBitmap serializableImage = null;


                //Si l'image n'est pas récupéré on donne une image de base
                if(extras.get("image") == null ){
                    image = BitmapFactory.decodeResource(getResources() , R.drawable.kal);
                    image_compress = scaleDownBitmap(image , 100 , this);

                    serializableImage = new SerializableBitmap(image_compress);
                }

                else {
                    serializableImage = (SerializableBitmap) extras.get("image");
                }
                /**
                 *  Utilisation de la classe SerializableBitmap, car ls images BitMap n'étant pas "Serializable"
                 *  ne peuvent donc pas être passées par des Intent
                 */
                addConcertToList(extras.getDouble("lati") , extras.getDouble("longi") , extras.getString("titre") ,
                        serializableImage , extras.getString("date") ,extras.getString("heure"), extras.getString("duree"));


                setConcerts();


            }
        }

        /**
         * Met à jour la liste des concerts et
         * se déplace vers le marqueur si l'utilisateur à cliqué sur un bouton
         *
         */
        if(requestCode==LISTE_CONCERT && resultCode==RESULT_OK){

            Bundle extras = data.getExtras();

            listeConcerts.clear();

            int longueur = extras.getInt("listeViewLong");

            for (int i =0 ; i<longueur ; i++){

                listeConcerts.add((ConcertWindowData) extras.getSerializable("listeView"+i));

            }

            double lng = extras.getDouble("go_long");
            double lat = extras.getDouble("go_lat");

            setConcerts();



            if(lng != -1 && lat !=-1) goToMarker(lat , lng);

        }
    }

    /**
     * Se déplace vers le Marqueur via sa longitude et latitude
     *
     * @param lat
     * @param lng
     */
    public void goToMarker(double lat , double lng){

        LatLng pos = new LatLng(lat , lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(pos));

    }

    /**
     * Rajoute pour chaque concerts un alerte de proximité
     *
     */
    private void ajouterProximityAlert() {

        if(listeConcerts.size()>0) {
            for (int i = 0; i < listeConcerts.size(); i++) {

                Intent intent = new Intent(PROX_ALERT_INTENT);

                PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                try {

                    locationManager.addProximityAlert(
                            listeConcerts.get(i).getLat(),
                            listeConcerts.get(i).getLng(),
                            10000.0f,
                            -1,
                            proximityIntent
                    );


                } catch (SecurityException e) {
                    Log.println(Log.ASSERT , "ERREUR_PROXIMITY" , e.getMessage());
                }
            }

            initialiseReceiver();

        }

    }

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    /**
     * Pour chaque concert supprime les alerte de proximité
     */
    public void supprimerProximityAlert(){

        if(listeConcerts.size()>0) {
            for (int i = 0; i < listeConcerts.size() ; i++){
                Intent intent = new Intent(PROX_ALERT_INTENT);
                PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), i , intent, PendingIntent.FLAG_UPDATE_CURRENT);
                locationManager.removeProximityAlert(proximityIntent);


            }

        }
    }

    /**
     * Initialise le recepteur, qui agit en fonction de si on rentre dans la zone
     *
     * @see ProximityBroadcastReceiver
     */
    private void initialiseReceiver()
    {
        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(receiver, filter);
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
            setConcerts();

        }

    }



    @Override
    public void onMyLocationClick(@NonNull Location location) {}

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }





}
