package com.example.myfirstapp;

public class Util {

    private static final float ALPHA = 0.25f;

    // Copied from tutorial https://www.built.io/blog/applying-low-pass-filter-to-android-sensor-s-readings
    protected static float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    // Copied from tutorial https://www.raywenderlich.com/10838302-sensors-tutorial-for-android-getting-started
    protected static String getDirection(Double angle) {
        String direction = "";

        if (angle >= 350 || angle <= 10)
            direction = "N";
        if (angle < 350 && angle > 280)
            direction = "NW";
        if (angle <= 280 && angle > 260)
            direction = "W";
        if (angle <= 260 && angle > 190)
            direction = "SW";
        if (angle <= 190 && angle > 170)
            direction = "S";
        if (angle <= 170 && angle > 100)
            direction = "SE";
        if (angle <= 100 && angle > 80)
            direction = "E";
        if (angle <= 80 && angle > 10)
            direction = "NE";

        return direction;
    }
}
