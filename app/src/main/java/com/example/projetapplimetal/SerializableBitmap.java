package com.example.projetapplimetal;

import android.graphics.Bitmap;
import java.io.Serializable;

/**
 * Classe représentant une image Bitmap mais qui elle est sérialisable.
 *
 * Du au problème que un objet Bitmap n'est pas de base sérialisable.
 *
 * Elle transforme alors l'image Bitmap en tableau de pixels
 */
public class SerializableBitmap implements Serializable {
    private final int[] pixels;
    private final int width, height;

    /**
     *
     * @param bitmap L'image bitmap à transformer
     */
    public SerializableBitmap(Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
    }

    /**
     * Transforme le tableau de pixel en image Bitmap
     *
     * @return l'image Bitmap
     */
    public Bitmap getBitmap() {
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);

    }

}

