package com.example.myfirstapp;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.w3c.dom.Text;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float[] accelerometerReadings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometerReadings = new float[3];
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        updateTextValues(Util.lowPass(event.values.clone(), accelerometerReadings));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateTextValues(float[] values) {
        TextView xField = this.findViewById(R.id.x);
        TextView yField = this.findViewById(R.id.y);
        TextView zField = this.findViewById(R.id.z);
        TextView infoTxt = this.findViewById(R.id.infoTxt);
        View container = this.findViewById(R.id.container);
        xField.setText("X: " + values[0]);
        yField.setText("Y: " + values[1]);
        zField.setText("Z: " + values[2]);

        if (values[2] <= -9) {
            infoTxt.setText("Screen orientation: Upside down.");
            container.setBackgroundColor(Color.BLUE);
        } else if (values[2] >= 9) {
            infoTxt.setText("Screen orientation: Upside up.");
            container.setBackgroundColor(Color.GREEN);
        } else if (values[2] <= 1 && values[2] >= -1) {
            infoTxt.setText("Screen orientation: Sideways!");
            container.setBackgroundColor(Color.YELLOW);
        } else {
            infoTxt.setText("Screen orientation:");
            container.setBackgroundColor(Color.WHITE);
        }
    }
}
