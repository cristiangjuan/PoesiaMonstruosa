package com.android.poesiamonstruosa.activities;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.android.poesiamonstruosa.R;
import com.android.poesiamonstruosa.utils.Constants;
import com.android.poesiamonstruosa.utils.MusicManager;
import com.android.poesiamonstruosa.utils.ScreenFrame;

public class MainActivity extends AppCompatActivity {


    private FrameLayout mainLayout;
    private FrameLayout revealLayout;
    private ImageButton manual_btn;
    private ImageButton auto_btn;
    private Context mContext;

    /**
     * Controla que la música siga sonando al terminar la actividad. En caso de pasar a
     * otro menú seguirá sonando.
     */
    private boolean continue_music = false;

    /**
     * Vista para gestionar los cambios de visibilidad de las barras de status y nav
     */
    private View mDecorView;
    /**
     * Control de gesto para esconder barras de status y navegación.
     */
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.activity_main);

        mainLayout = (FrameLayout) findViewById(R.id.main_layout);
        revealLayout = (FrameLayout) findViewById(R.id.reveal_layout);
        //manual_btn = (ImageButton) findViewById(R.id.manual_btn);
        auto_btn = (ImageButton) findViewById(R.id.auto_btn);

        MusicManager.build(mContext);

        //Inicializamos el detector de gestos
        gestureDetector = new GestureDetector(mContext, new GestureListener());

        mDecorView = getWindow().getDecorView();

        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {

                boolean visible = (mDecorView.getSystemUiVisibility()
                        & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

                boolean visibleStatus = (mDecorView.getSystemUiVisibility()
                        & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0;

                Log.w(Constants.Log.CONTROLS, "MainActivity - NavigationBar - " + visible);
                Log.w(Constants.Log.CONTROLS, "MainActivity - StatusBar - " + visibleStatus);
            }
        });

        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ScreenFrame.setFrame(mainLayout.getMeasuredWidth(), mainLayout.getMeasuredHeight());
            }
        });

        /*
        manual_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, ReadActivityManual.class);

                startActivity(i);
            }
        });
        */

        auto_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startReveal();
            }
        });

    }

    /**
     * Pone en marcha animación Reveal que da inicio a la actividad principal
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startReveal() {

        Log.v(Constants.Log.METHOD, "MainActivity - startReveal ");

        //Calculamos el centro de la animación
        int cx = (auto_btn.getLeft() + auto_btn.getRight()) / 2;
        int cy = (auto_btn.getTop() + auto_btn.getBottom()) / 2;

        //Calculamos el radio de la animación
        int finalRadius = (int) Math.sqrt(Math.pow(revealLayout.getWidth(), 2) +
                Math.pow(revealLayout.getHeight(), 2));

        //Creamos la animación
        Animator anim =
                ViewAnimationUtils.createCircularReveal(revealLayout,
                        cx, cy, 0, finalRadius);
        anim.setDuration(getResources().getInteger(R.integer.reveal_standard));

        //Hacemos visible la vista y empezamos la animación
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(Constants.Log.METHOD, "MainActivity - startReveal.onAnimationStart ");

                //Hacemos visible el reveal y le cambiamos el color
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    revealLayout.setBackgroundColor(getResources().getColor(R.color.pink, null));
                } else {
                    revealLayout.setBackgroundColor(getResources().getColor(R.color.pink));
                }
                revealLayout.setVisibility(View.VISIBLE);
                auto_btn.setElevation(0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(Constants.Log.METHOD, "MainActivity - startReveal.onAnimationEnd ");

                //Guardamos si la música está activa o no
                MusicManager.setWasMainMusicOn(MusicManager.isMusic_on());

                Intent i = new Intent(MainActivity.this, ReadActivityStandard.class);

                startActivityForResult(i, Constants.ACTIVITY_REQUEST_CODE_INFO);
                //Lo añadimos para quitar el blink, lo que hace es eliminar animaciones de transición.
                overridePendingTransition(0, 0);

                //Indicamos que no se debe interrumpir la música en onStop
                continue_music = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        anim.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        anim.start();
    }

    /**
     * Pone en marcha laanimación Reveal de retorno de la actividad principal.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startUnvealPlayReturn() {

        Log.v(Constants.Log.METHOD, "MainActivity - startUnvealPlayReturn ");

        //Calculamos el centro de la animación
        int cx = (auto_btn.getLeft() + auto_btn.getRight()) / 2;
        int cy = (auto_btn.getTop() + auto_btn.getBottom()) / 2;

        //Calculamos el radio de la animación
        int initialRadius = (int) Math.sqrt(Math.pow(revealLayout.getWidth(), 2) +
                Math.pow(revealLayout.getHeight(), 2));

        //Creamos la animación
        Animator anim =
                ViewAnimationUtils.createCircularReveal(revealLayout,
                        cx, cy, initialRadius, 0);
        anim.setDuration(getResources().getInteger(R.integer.unveal_long));

        //Hacemos visible la vista y empezamos la animación
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(Constants.Log.METHOD, "MainActivity - startUnvealPlayReturn.onAnimationStart ");

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(Constants.Log.METHOD, "MainActivity - startUnvealPlayReturn.onAnimationEnd ");

                revealLayout.setVisibility(View.INVISIBLE);

                //Restauramos la elevación
                auto_btn.setElevation(getResources().getDimension(R.dimen.button_higher_elevation));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        //anim.setStartDelay(getResources().getInteger(R.integer.delay_show_logo));
        anim.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        anim.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.v(Constants.Log.METHOD, "MainActivity - onActivityResult ");

        startUnvealPlayReturn();
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {

        Log.w(Constants.Log.METHOD, "MainActivity - hideSystemUI ");

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * Escondemos las barras de status y nav al coger foco
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {

            boolean visible = (mDecorView.getSystemUiVisibility()
                    & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

            if (visible) {

                hideSystemUI();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    /**
     * Controlamos con el singletap esconder las barras de status y nav
     *
     * @author quayo
     */
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            boolean visible = (mDecorView.getSystemUiVisibility()
                    & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

            if (visible) {

                hideSystemUI();
            }

            return super.onSingleTapUp(e);
        }
    }
}
