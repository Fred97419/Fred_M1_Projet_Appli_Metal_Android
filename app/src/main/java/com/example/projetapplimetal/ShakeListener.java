package com.example.projetapplimetal;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.util.Log;

import java.lang.UnsupportedOperationException;

/**
 * Classe qui va écouter si l'on a secoué le telephone
 */
public class ShakeListener implements SensorEventListener
{
    private static final int FORCE_THRESHOLD = 350;
    private static final int TIME_THRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 1000;
    private static final int SHAKE_COUNT = 3;

    private SensorManager mSensorMgr;
    private float mLastX=-1.0f, mLastY=-1.0f, mLastZ=-1.0f;
    private long mLastTime;
    private OnShakeListener mShakeListener;
    private Context mContext;
    private int mShakeCount = 0;
    private long mLastShake;
    private long mLastForce;
    private Sensor accelerometre;


    /**
     * Interface avec la methode abstraite onShake()
     */
    public interface OnShakeListener
    {
        public void onShake();
    }

    /**
     * Classe qui va écouter si on a eu une secousse
     * en utilisant l'accéléromètre
     * @param context
     */
    public ShakeListener(Context context)
    {
        mContext = context;
        Log.println(Log.ASSERT , "SHAKE IT " , "SHAKE IT" );
        resume();
    }

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

        if ((now - mLastForce) > SHAKE_TIMEOUT) {

            mShakeCount = 0;
        }

        if ((now - mLastTime) > TIME_THRESHOLD) {
            long diff = now - mLastTime;
            float speed = Math.abs(event.values[0] + event.values[1] + event.values[2] - mLastX - mLastY - mLastZ) / diff * 10000;
            if (speed > FORCE_THRESHOLD) {
                if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
                    mLastShake = now;
                    mShakeCount = 0;
                    if (mShakeListener != null) {
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
