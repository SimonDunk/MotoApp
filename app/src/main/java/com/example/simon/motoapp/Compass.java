package com.example.simon.motoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

public class Compass
{
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currentAzimuth = 0f;
    private float azimuthFix;

    private SensorManager mSensorManager;
    private Sensor gSensor;
    private Sensor mSensor;

    private Compass.CompassListener listener;
    private MainActivity mMainActivity;

    private ImageView navi;
    private TextView dis;

    Compass(Context a, ImageView i, TextView t)
    {
        mMainActivity = (MainActivity) a;
        navi = i;
        dis = t;

        mSensorManager = (SensorManager)a.getSystemService(Context.SENSOR_SERVICE);
        gSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        SetupCompass();
    }

    public interface CompassListener {
        void onNewAzimuth(float azimuth);
    }

    private void SetupCompass()
    {
        CompassListener cl = new CompassListener() {

            @Override
            public void onNewAzimuth(float azimuth) {
                adjustArrow();
            }
        };
        setListener(cl);
    }

    //enables sensors
    public void StartCompass(NavigationFragment f)
    {
        mSensorManager.registerListener((SensorEventListener) f, gSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener((SensorEventListener) f, mSensor, SensorManager.SENSOR_DELAY_GAME);
        setDistance();
    }

    public void StopCompass(NavigationFragment f)
    {
        mSensorManager.unregisterListener(f);
    }

    private void setAzimuthFix(float fix) {
        azimuthFix = fix;
    }

    public void resetAzimuthFix() {
        setAzimuthFix(0);
    }

    private void setListener(CompassListener l) {
        listener = l;
    }

    public void onSensorChanged(SensorEvent event)
    {
        final float alpha = 0.97f;
        synchronized (this)
        {
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                mGravity[0] = alpha*mGravity[0]+(1-alpha)*event.values[0];
                mGravity[1] = alpha*mGravity[1]+(1-alpha)*event.values[1];
                mGravity[2] = alpha*mGravity[2]+(1-alpha)*event.values[2];
            }
            if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            {
                mGeomagnetic[0] = alpha*mGeomagnetic[0]+(1-alpha)*event.values[0];
                mGeomagnetic[1] = alpha*mGeomagnetic[1]+(1-alpha)*event.values[1];
                mGeomagnetic[2] = alpha*mGeomagnetic[2]+(1-alpha)*event.values[2];
            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic);
            if(success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R,orientation);
                azimuth = (float)Math.toDegrees(orientation[0]);
                azimuth = (azimuth + azimuthFix +360)%360;
                //
                if(mMainActivity.getLocation() != null && mMainActivity.getDestination() != null)
                {
                    azimuth -= bearing(mMainActivity.getLocation().latitude,mMainActivity.getLocation().longitude, mMainActivity.getDestination().latitude, mMainActivity.getDestination().longitude);
                    setDistance();
                    // Log.d("!!!", "azimuth (deg): " + azimuth);
                }
                //
                if (listener != null)
                {
                    listener.onNewAzimuth(azimuth);
                }
            }
        }
    }

    private void adjustArrow()
    {
        Animation anim = new RotateAnimation(-currentAzimuth,-azimuth, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        currentAzimuth = azimuth;
        anim.setDuration(500);
        anim.setRepeatCount(0);
        anim.setFillAfter(true);

        navi.startAnimation(anim);
    }

    private void setDistance()
    {
        dis.setText(formatNumber(computeDistance()));
        //Log.d("!!!", formatNumber(computeDistance()) + "/" + mMainActivity.getLocation() + "/" + mMainActivity.getDestination());
    }

    //calculate distance between 2 points
    private double computeDistance()
    {
        LatLng aa = mMainActivity.getLocation();
        LatLng b = mMainActivity.getDestination();
        if(aa != null && b != null)
        {
            return SphericalUtil.computeDistanceBetween(aa, b);
        }
        else
        {
            return 0;
        }
    }

    @SuppressLint("DefaultLocale")
    private String formatNumber(double distance)
    {
        String unit = "m";
        if (distance < 1)
        {
            distance *= 1000;
            unit = "mm";
        } else if (distance > 1000)
        {
            distance /= 1000;
            unit = "km";
        }

        return String.format("%4.3f%s", distance, unit);
    }

    //bearing direction of arrow
    private double bearing(double startLat, double startLng, double endLat, double endLng)
    {
        double latitude1 = Math.toRadians(startLat);
        double latitude2 = Math.toRadians(endLat);
        double longDiff= Math.toRadians(endLng-startLng);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }
}
