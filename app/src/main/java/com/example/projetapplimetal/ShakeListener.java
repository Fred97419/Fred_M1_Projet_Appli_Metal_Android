package com.example.projetapplimetal;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.util.Log;

import java.lang.UnsupportedOperationException;

/**
 * Classe qui va écouter si l'on a secoué le telephone en utilisant l'accéléromètre
 */
public class ShakeListener implements SensorEventListener
{
    private static final int FORCE_THRESHOLD = 350; //seuil de force
    private static final int TIME_THRESHOLD = 100; //seuil de temps
    private static final int SHAKE_TIMEOUT = 500; //delai après lequel le nombre de secousse se reinitialise
    private static final int SHAKE_DURATION = 1000; //durée de la secousse
    private static final int SHAKE_COUNT = 3; //nombre de secousse pour activer onShake

    private SensorManager mSensorMgr;
    private float mLastX=-1.0f, mLastY=-1.0f, mLastZ=-1.0f;
    private long mLastTime; //dernier temps enregistré
    private OnShakeListener mShakeListener;
    private Context mContext;
    private int mShakeCount = 0; //compteur de secousse
    private long mLastShake; //dernière secousse enregistré
    private long mLastForce; //dernière force enregistré (accéléromètre)
    private Sensor accelerometre;


    /**
     * Interface avec la methode abstraite onShake()
     */
    public interface OnShakeListener
    {
        public void onShake();
    }


    public ShakeListener(Context context)
    {
        mContext = context;
        resume();
    }

    //
    public void setOnShakeListener(OnShakeListener listener)
    {
        mShakeListener = listener;
    }

    /**
     * Demande au système de pouvoir utiliser l'accéléromètre
     */
    public void resume() {
        mSensorMgr = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorMgr == null) {
            throw new UnsupportedOperationException("Capteur non supporté");
        }
        accelerometre = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean supported = mSensorMgr.registerListener(this, accelerometre, SensorManager.SENSOR_DELAY_GAME);
        if (!supported) {
            mSensorMgr.unregisterListener(this, accelerometre);
            throw new UnsupportedOperationException("Accelerometre non supporté");
        }
    }

    public void pause() {
        if (mSensorMgr != null) {
            mSensorMgr.unregisterListener(this, accelerometre);
            mSensorMgr = null;
        }
    }


    /**
     * Va écouter tous les changements de l'accéléromètre
     *
     * Si l'on a pas secouer depuis un certain temps on remet le compteur de secousse
     * à 0
     *
     * Si on arrive à 3 secousse en dessous d'un certain délai on appelle la methode asbtraite onShake()
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        long now = System.currentTimeMillis();

        //si l'on a pas secouer depuis 500ms le compteur revient à 0
        if ((now - mLastForce) > SHAKE_TIMEOUT) {

            mShakeCount = 0;
        }


        if ((now - mLastTime) > TIME_THRESHOLD) {
            long diff = now - mLastTime;
            float speed = Math.abs(event.values[0] + event.values[1] + event.values[2] - mLastX - mLastY - mLastZ) / diff * 10000;

            // si la vitesse atteint au moins 300
            if (speed > FORCE_THRESHOLD) {
                //si le nombre de secousse est arrivé à 3 et que le dernier shake a été fait il y a moins d'une seconde
                if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
                    mLastShake = now;
                    mShakeCount = 0;
                    if (mShakeListener != null) {
                        //on appelle la methode onShake() de l'interface
                        mShakeListener.onShake();
                    }
                }
                mLastForce = now;
            }

            mLastTime = now;
            mLastX = event.values[0];
            mLastY = event.values[1];
            mLastZ = event.values[2];
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
