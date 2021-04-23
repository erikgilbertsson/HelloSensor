package com.example.myfirstapp;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.*;
import android.util.Log;

public class CompassService extends Service implements SensorEventListener {

    private static final String TAG = "CompassService";

    private SensorManager sensorManager;
    private Sensor accelerometer, magnetometer;
    private float[] accelerometerReading, magnetometerReading, rotationMatrix, orientationAngles;
    private Messenger messenger;

    @Override
    public IBinder onBind(Intent intent) {
        messenger = (Messenger) intent.getExtras().get("messenger");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sensorManager.unregisterListener(this);
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        accelerometerReading = new float[3];
        magnetometerReading = new float[3];
        rotationMatrix = new float[9];
        orientationAngles = new float[3];
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerReading = Util.lowPass(event.values.clone(), accelerometerReading);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerReading = Util.lowPass(event.values.clone(), magnetometerReading);
        }
        updateOrientationAngles();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do something?
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    // Copied from tutorial https://www.raywenderlich.com/10838302-sensors-tutorial-for-android-getting-started
    private void updateOrientationAngles() {
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading);
        float[] orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles);
        double degrees = (Math.toDegrees(orientation[0]) + 360.0) % 360;
        double angle = Math.round(degrees * 100) / 100;
        String direction = Util.getDirection(angle);

        // Sends new data to the CompassActivity handler.
        Bundle bundle = new Bundle();
        bundle.putString("direction", direction);
        bundle.putDouble("angle", angle);
        Message message = new Message();
        message.setData(bundle);
        try {
            messenger.send(message);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
