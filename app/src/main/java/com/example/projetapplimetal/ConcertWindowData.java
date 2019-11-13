package com.example.projetapplimetal;

import com.google.android.gms.maps.model.LatLng;

public class ConcertWindowData {

    private String nom;
    private String image;
    private String date;
    private String duree;
    private String lien;
    private LatLng position;

    public LatLng getPosition(){return this.position;}
    public String getNom(){return this.nom;}
    public String getImage(){return this.image;}
    public String getDate(){return this.date;}
    public String getDuree(){return this.duree;}
    public String lien(){return this.lien;}

    public void setPosition(LatLng p){position=p;}
    public void setNom(String nom){this.nom = nom;}
    public void setImage(String image){this.image = image;}
    public void setDate(String date){this.date = date;}
    public void setDuree(String duree){this.duree = duree;}
    public void setLien(String lien){this.lien = lien;}



}
