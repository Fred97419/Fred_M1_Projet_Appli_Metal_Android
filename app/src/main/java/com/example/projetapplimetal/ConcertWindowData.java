package com.example.projetapplimetal;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Objet qui représente notre concert, elle implémente l'interface
 * Serializable pour pouvoir être envoyé via des Intent.
 *
 * ** POINT IMPORTANT **
 *
 * Ici l'attribut image est normalement de type Bitmap or un objet
 * Bitmap n'est pas sérialisable du coup on utlise un objet SerializableBitmap
 * qui permet de contourner le problème
 *
 */
public class ConcertWindowData implements Serializable {

    private String nom;


    /**
     * Classe qui permet d'envoyer un BitMap car la classe BitMap n'est pas sérialisable
     *
     * de même pour l'objet LatLng où l'on utilise deux valeurs "double" à la place.
     *
     * @see SerializableBitmap
     *
     */
    private SerializableBitmap image;
    private String date;
    private String duree;
    private String heure;

    private double lat;
    private double lng;



    public double getLat(){return this.lat;}
    public double getLng(){return this.lng;}

    public String getNom(){return this.nom;}
    public SerializableBitmap getImage(){return this.image;}
    public String getDate(){return this.date;}
    public String getDuree(){return this.duree;}
    public String getHeure(){return this.heure;}


    public void setLat(double lat) { this.lat = lat;}
    public void setLng(double lng) {this.lng = lng;}

    public void setNom(String nom){this.nom = nom;}
    public void setImage(SerializableBitmap image){this.image = image;}
    public void setDate(String date){this.date = date;}
    public void setDuree(String duree){this.duree = duree;}
    public void setHeure(String lien){this.heure = lien;}



}
