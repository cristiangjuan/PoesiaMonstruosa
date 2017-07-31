package com.android.poesiamonstruosa.utils;

import android.graphics.Point;

/**
 * Created by cgj on 12/07/2017.
 */

public class ScreenFrame {


    /**
     * Ancho del marco que engloba la imagen
     */
    public static int frameWidth;
    /**
     * Alto del marco que engloba la imagen
     */
    public static int frameHeight;


    public static void setFrame(int width, int height) {

        frameWidth = width;
        frameHeight = height;
    }

    public static int getFrameWidth(){

        return frameWidth;
    }

    public static int getFrameHeight(){

        return frameHeight;
    }

    public static Point getFrameSize(){

        return new Point(frameWidth, frameHeight);
    }
}
