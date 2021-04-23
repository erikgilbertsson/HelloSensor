package com.example.myfirstapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.*;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CompassActivity extends AppCompatActivity {

    private static final String TAG = "CompassActivity";

    private static Vibrator vibrator;
    private static AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        Intent compassService = new Intent(this, CompassService.class);

        Handler handler = new Handler(Looper.myLooper()) //Handler given to CompassService for sending compass updates.
        {
            @Override
            public void handleMessage(Message msg)
            {
                Bundle data = msg.getData();
                String direction = data.getString("direction");
                Double angle = data.getDouble("angle");
                updateCompass(direction, angle);
            }
        };
        Messenger messenger = new Messenger(handler);
        compassService.putExtra("messenger", messenger);

        bindService(compassService, conn, BIND_AUTO_CREATE);
    }

    private void updateCompass(String direction, double angle) {
        ImageView imageView = findViewById(R.id.compassImg);
        imageView.setRotation((float) angle);
        TextView textView = findViewById(R.id.directionTxt);
        textView.setText(angle + " " + direction);
        if(direction == "N") {
            textView.setTextColor(Color.RED);
            // vibrator.vibrate(VibrationEffect.EFFECT_CLICK);  // annoying
            // audioManager.playSoundEffect(SoundEffectConstants.CLICK);
        } else {
            textView.setTextColor(Color.BLACK);
        }
    }
}
