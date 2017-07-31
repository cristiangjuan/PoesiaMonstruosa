package com.android.poesiamonstruosa.customs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.android.poesiamonstruosa.activities.ReadActivity;
import com.android.poesiamonstruosa.bitmaps.BitMapUtils;
import com.android.poesiamonstruosa.utils.Constants;
import com.android.poesiamonstruosa.utils.Page;
import com.android.poesiamonstruosa.utils.Vignette;


public class CustomView extends ImageView {

    private Point viewDimensions;
    private float ratioViewImage;
    private Page pag;
    private Vignette lastVignette;
    private int lastVignetteIndex;
    private float lastScaleVigPag;
    private int centerPageX;
    private int centerPageY;
    private float imageRatio;
    private float viewRatio;
    private int xOffset;
    private int yOffset;

	private boolean inZoom = false;

	private Context mContext;
	
	/**
	 * Número de página correspondiente a la vista.
	 */
	private int pageNum = 0;

	public CustomView(Context context) {
        this(context, null, 0);
    }
    
    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public CustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        Log.v(Constants.Log.METHOD, "CustomView - new()");
        
        mContext = context;

        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6,
                                       int i7) {

                view.removeOnLayoutChangeListener(this);

                viewDimensions = new Point(view.getMeasuredWidth(), view.getMeasuredHeight());
                Log.v(Constants.Log.SIZE, "CustomView - ViewDimen - "+
                        view.getMeasuredWidth() + ", " + view.getMeasuredHeight());

                pag = ReadActivity.mapaPages
                        .get(String.valueOf(ReadActivity.pages[pageNum]));

                viewRatio = (float) viewDimensions.x /  (float) viewDimensions.y;
                Log.v(Constants.Log.SIZE, "CustomView - ViewRatio " + viewRatio);
                imageRatio =
                        (float) pag.getResolution().x / (float) pag.getResolution().y;
                Log.v(Constants.Log.SIZE, "CustomView - ImageRatio " + imageRatio);

                centerPageX = pag.getResolution().x / 2;
                centerPageY = pag.getResolution().y / 2;

                Log.d(Constants.Log.SIZE, "CustomView - centerX = "+centerPageX);
                Log.d(Constants.Log.SIZE, "CustomView - centerY = "+centerPageY);

                  /*
                    Comprobamos si nuestra referencia para la constante de relacion es el ancho o el alto.
                    Desplazamos una de las coordenadas ya que la imagen habitualmente no cuadra con la pantalla
                    siempre sobrará algo de ancho o de alto. Entonces consideraremos desplazaramos el valor 0 del
                    alto o el ancho a el punto donde comienza el alto o el ancho de la imagen.

                   */
                //Ancho
                int newWidthOrHeight;
                Log.v(Constants.Log.SIZE, "CustomView - RatioViewImage, " + ratioViewImage);
                //Ancho
                if (imageRatio > viewRatio) {
                    ratioViewImage =
                            (float) viewDimensions.x / (float) pag.getResolution().x;
                    newWidthOrHeight = (int) (pag.getResolution().y * ratioViewImage);
                    yOffset -= (viewDimensions.y - newWidthOrHeight) / 2;
                    Log.v(Constants.Log.SIZE, "CustomView - Y offset, " + yOffset);
                }
                //Alto
                else {
                    ratioViewImage =
                            (float) viewDimensions.y / (float) pag.getResolution().y;
                    newWidthOrHeight = (int) (pag.getResolution().x * ratioViewImage);
                    xOffset -= (viewDimensions.x - newWidthOrHeight) / 2;
                    Log.v(Constants.Log.SIZE, "CustomView - New X ofsset, " + xOffset);
                }
                Log.v(Constants.Log.SIZE, "CustomView - RatioViewImage, " + ratioViewImage);

            }
        });

    }
    
    public void setPage(int p) {
    	pageNum = p;
    }

   
	public boolean isInZoom() {
		
		Log.v(Constants.Log.TOUCH, "CustomView - isInZoom");
		
		return inZoom;
	}

    public void zoomDoubleTap(float x, float y) {

        Log.v(Constants.Log.SIZE, "CustomView zoomDoubleTap");

        Vignette vig;

        Log.v(Constants.Log.SIZE, "CustomView Tap(x,y) = "+x + ", " + y);

        //int ratioConst = BitMapUtils.calculateRatioFromRes(getResources(), resId);
        //Calcular resolucion del bitmap y hacer el mapeo entre las coordenadas de la viñeta y las del tap
        if (ReadActivity.pages == null) {
            Log.e(Constants.Log.DEBUG, "CustomView - Pages array static es null!!");
        }

        if (ReadActivity.mapaPages == null) {
            Log.e(Constants.Log.DEBUG, "CustomView - MapPages es null!!");
        }

      /*
        Comprobamos si nuestra referencia para la constante de relacion es el ancho o el alto.
        Desplazamos una de las coordenadas ya que la imagen habitualmente no cuadra con la pantalla
        siempre sobrará algo de ancho o de alto. Entonces consideraremos desplazaramos el valor 0 del
        alto o el ancho a el punto donde comienza el alto o el ancho de la imagen.
       */
        //Ancho
        if (imageRatio > viewRatio) {
            y += yOffset;
            Log.v(Constants.Log.SIZE, "CustomView - New Y, " + y);
        }
        //Alto
        else {
            x += xOffset;
            Log.v(Constants.Log.SIZE, "CustomView - New X, " + x);
        }

        Log.v(Constants.Log.SIZE, "CustomView - RatioViewImage, " + ratioViewImage);

        //Comprobamos que la página actual se encuentra en la matriz que relaciona viñetas con páginas
        if (pageNum < ReadActivity.matrixPagVignette.length) {
            //Recorrer viñetas asociadas a página
            for (int i = 0; i < ReadActivity.matrixPagVignette[pageNum].length; i++) {

                vig = ReadActivity.mapaVignettes
                        .get(String.valueOf(ReadActivity.matrixPagVignette[pageNum][i]));
                if (vig.insideVignnette(x, y, ratioViewImage)) {
                    Log.v(Constants.Log.SIZE, "CustomView - Inside Viñeta: " + x + ", " + y);

                    lastVignette = vig;
                    lastVignetteIndex = i;

                    zoomAnimation(vig);
                }
            }
        }
    }

    private void zoomAnimation(Vignette vig) {

        float scaleVigPag = BitMapUtils.calculateScale(pag.getResolution(), vig.getResolution());

        //Guardamos la escala para restaurar.
        lastScaleVigPag = scaleVigPag;

        Log.d(Constants.Log.SIZE, "CustomView - ScaleVigPag = "+scaleVigPag);

        int centerX = pag.getResolution().x / 2;
        int centerY = pag.getResolution().y / 2;
        int centerXVig = vig.getAbsolutCenter().x;
        int centerYVig = vig.getAbsolutCenter().y;

        float translationX = (centerX - centerXVig) * ratioViewImage;
        float translationY = (centerY - centerYVig) * ratioViewImage;

        Log.d(Constants.Log.SIZE, "CustomView - centerX = "+centerX);
        Log.d(Constants.Log.SIZE, "CustomView - centerY = "+centerY);
        Log.d(Constants.Log.SIZE, "CustomView - centerXVig = "+centerXVig);
        Log.d(Constants.Log.SIZE, "CustomView - centerYVig = "+centerYVig);

        AnimationSet anim = new AnimationSet(true);

        TranslateAnimation translateAnimation = new TranslateAnimation(
                0, translationX,
                0, translationY);
        translateAnimation.setDuration(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateAnimation.setInterpolator(AnimationUtils.loadInterpolator(
                    getContext(), android.R.interpolator.fast_out_slow_in));
        }

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, scaleVigPag, 1f, scaleVigPag,
                Animation.RELATIVE_TO_PARENT,
                0.5f,
                Animation.RELATIVE_TO_PARENT,
                0.5f);
        scaleAnimation.setDuration(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            scaleAnimation.setInterpolator((AnimationUtils.loadInterpolator(
                    getContext(), android.R.interpolator.fast_out_slow_in)));
        }

        anim.addAnimation(translateAnimation);
        anim.addAnimation(scaleAnimation);
        anim.setFillAfter(true);

        CustomView.this.startAnimation(anim);

        inZoom = true;
    }

    public void resetAnimation() {

        Log.v(Constants.Log.SIZE, "CustomView resetAnimation");

        AnimationSet anim = new AnimationSet(true);

        int centerXVig = lastVignette.getAbsolutCenter().x;
        int centerYVig = lastVignette.getAbsolutCenter().y;

        float translationX = (centerPageX - centerXVig) * ratioViewImage;
        float translationY = (centerPageY - centerYVig) * ratioViewImage;

        Log.d(Constants.Log.SIZE, "CustomView - centerXVig = "+centerXVig);
        Log.d(Constants.Log.SIZE, "CustomView - centerYVig = "+centerYVig);

        TranslateAnimation translateAnimation = new TranslateAnimation(
                translationX, 0,
                translationY, 0);
        translateAnimation.setDuration(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateAnimation.setInterpolator(AnimationUtils.loadInterpolator(
                    getContext(), android.R.interpolator.fast_out_slow_in));
        }

        ScaleAnimation scaleAnimation = new ScaleAnimation(lastScaleVigPag, 1f, lastScaleVigPag, 1f,
                Animation.RELATIVE_TO_PARENT,
                0.5f,
                Animation.RELATIVE_TO_PARENT,
                0.5f);
        scaleAnimation.setDuration(1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            scaleAnimation.setInterpolator((AnimationUtils.loadInterpolator(
                    getContext(), android.R.interpolator.fast_out_slow_in)));
        }

        anim.addAnimation(translateAnimation);
        anim.addAnimation(scaleAnimation);
        anim.setFillAfter(true);

        CustomView.this.startAnimation(anim);

        inZoom = false;
    }

	
	/**
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        
    	
		@Override
        public boolean onScale(ScaleGestureDetector detector) {
            
    		oldScaleFactor = newScaleFactor;
    		newScaleFactor *= detector.getScaleFactor();
    		 Log.v("ScaleZoom","Pure Scale: "+newScaleFactor);
            
            // Limitamos coeficiente del zoom.
            newScaleFactor = Math.max(1.f, Math.min(newScaleFactor, 3.0f));
            
            if (newScaleFactor == 1.f){
            	inZoom=false;
            }
            else {
            	inZoom=true;
            }

            return true;
        }
    }
    */
}
