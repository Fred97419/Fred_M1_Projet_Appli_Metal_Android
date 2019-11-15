package com.example.projetapplimetal;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class ConcertWindowData {

    private String nom;
    private Bitmap image;
    private String date;
    private String duree;
    private String heure;
    private LatLng position;

    public LatLng getPosition(){return this.position;}
    public String getNom(){return this.nom;}
    public Bitmap getImage(){return this.image;}
    public String getDate(){return this.date;}
    public String getDuree(){return this.duree;}
    public String getHeure(){return this.heure;}

    public void setPosition(LatLng p){position=p;}
    public void setNom(String nom){this.nom = nom;}
    public void setImage(Bitmap image){this.image = image;}
    public void setDate(String date){this.date = date;}
    public void setDuree(String duree){this.duree = duree;}
    public void setHeure(String lien){this.heure = lien;}



}
