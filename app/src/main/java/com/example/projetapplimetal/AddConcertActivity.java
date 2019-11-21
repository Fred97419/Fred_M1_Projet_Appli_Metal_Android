package com.example.projetapplimetal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Activité qui sert à ajouter un concert en renseignant
 *  -Le titre du concert
 *  -La date
 *  -La durée du concert
 *  -L'heure où celui-ci commence
 *  -Sa position
 */
public class AddConcertActivity extends AppCompatActivity {

    EditText nom;
    EditText date;
    EditText duree;
    EditText heure;
    EditText lng;
    EditText lat;
    Bitmap image_to_send;

    MediaPlayer player;

    static final int DEMANDER_IMAGE= 1;
    static final int DEMANDER_GALLERIE=2;


    boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_concert);

        nom = findViewById(R.id.nom_concert);
        date = findViewById(R.id.edit_DATE);
        heure = findViewById(R.id.edit_heure);
        duree = findViewById(R.id.edit_duree);
        lng = findViewById(R.id.lng_edit);
        lat = findViewById(R.id.lat_edit);

        Intent result = getIntent();

        lng.setText(Double.toString(result.getDoubleExtra("long" , 0)));
        lat.setText(Double.toString(result.getDoubleExtra("lat" , 0)));
        /**
         * Si il y a changement de configuration
         */
        if (savedInstanceState!=null){
            nom.setText(savedInstanceState.getString("nom_to_send"));
            date.setText(savedInstanceState.getString("date_to_send"));
            heure.setText(savedInstanceState.getString("heure_to_send"));
            duree.setText(savedInstanceState.getString("duree_to_send"));
            lng.setText(Double.toString(savedInstanceState.getDouble("lng_to_send")));
            lat.setText(Double.toString(savedInstanceState.getDouble("lat_to_send")));

            SerializableBitmap img_serializable = (SerializableBitmap) savedInstanceState.getSerializable("image_to_send");
            if(img_serializable!=null){

                image_to_send = img_serializable.getBitmap();
            }

        }
    }

    /**
     * Detecte si on swipe vers le bas 
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        if (action == MotionEvent.ACTION_DOWN){
            finish();
            return true;
        }

        else {
            return super.onTouchEvent(event);
        }

    }

    /**
     * Va sauvegarder nos différents champs renseignés si il y a eu
     * un changement de configuration
     *
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("nom_to_send" , nom.getText().toString());
        savedInstanceState.putString("date_to_send" , date.getText().toString());
        savedInstanceState.putString("heure_to_send" ,heure.getText().toString());
        savedInstanceState.putString("duree_to_send",duree.getText().toString());
        savedInstanceState.putDouble("lng_to_send",Double.valueOf(lng.getText().toString()));
        savedInstanceState.putDouble("lat_to_send",Double.valueOf(lat.getText().toString()));

        if(image_to_send!=null) {
            savedInstanceState.putSerializable("image_to_send" , new SerializableBitmap(image_to_send));
        }


    }



    /**
     * Bouton valider, si l'on appuie dessus appelle la methode finish.
     * En jouant également un son
     * @param v
     */
    public void valider(View v){

        validate = true;
        player = MediaPlayer.create(this , R.raw.newalert);
        player.setVolume(1.0f , 1.0f);
        player.start();
        finish();



    }

    /**
     *
     * @param v
     */
    public void annuler(View v){

        validate = false;
        finish();

    }

    /**
     * Lance un intent vers l'appareil photo pour demander une image via
     * un bouton
     * @param v
     */
    public void prendrePhoto(View v){

        Intent prendrePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (prendrePhoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(prendrePhoto, DEMANDER_IMAGE);
        }


    }

    public void prendreGallerie(View v){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK , android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, DEMANDER_GALLERIE);

    }

    /**
     *  Récupère l'image BitMap via l'intent envoyé lorsque l'on appuie
     *  sur le bouton "Prendre Photo"
     *
     * @param requestCode
     * @param resultCode
     * @param data la photo à récupéré
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

            Log.println(Log.ASSERT , "BLA" , "TESSSSST");


            if (requestCode == DEMANDER_IMAGE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap image = (Bitmap) extras.get("data");
                image_to_send= scaleDownBitmap(image, 1000 , this);



            }

            if(requestCode == DEMANDER_GALLERIE && resultCode==RESULT_OK){

                try {

                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(imageStream);
                    image_to_send= scaleDownBitmap(image, 100 , this);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Une erreur s'est produite",Toast.LENGTH_LONG).show();

                }


            }

    }

    /**
     * Termine l'application, en envoyant toutes les informations renseignées
     * via un Intent à l'activité principale MapActivity si l'on a appuyé sur le
     * bouton "Valider" sinon on finit l'application sans rien envoyer.
     *
     * @see MapsActivity
     */
    @Override
    public void finish(){

        Intent intent = new Intent(this, MapsActivity.class);

        if(!validate){

            setResult(RESULT_CANCELED);
            super.finish();

        }

        if(validate){

            intent.putExtra("titre" , nom.getText().toString());
            Log.println(Log.ASSERT , "valeur_titre" , nom.getText().toString());
            intent.putExtra("date" , date.getText().toString());
            intent.putExtra("heure" , heure.getText().toString());
            intent.putExtra("duree" , duree.getText().toString());
            intent.putExtra("longi" , Double.valueOf(lng.getText().toString()));
            intent.putExtra("lati" , Double.valueOf(lat.getText().toString()));

            if (image_to_send!=null) intent.putExtra("image" , new SerializableBitmap(image_to_send));

            setResult(RESULT_OK , intent);
            super.finish();


        }




    }

    /**
     * Reduit la résolution d'une image Bitmap
     *
     * @param photo l'image bitmap à reduire
     * @param newHeight nouvelle taille
     * @param context
     * @return L'image Bitmap reduite
     */
    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }


}
