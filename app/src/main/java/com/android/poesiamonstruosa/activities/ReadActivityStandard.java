package com.android.poesiamonstruosa.activities;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.graphics.Outline;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.poesiamonstruosa.R;
import com.android.poesiamonstruosa.customs.CustomPagerAdapter;
import com.android.poesiamonstruosa.customs.CustomViewPagerStandard;
import com.android.poesiamonstruosa.utils.Constants;
import com.android.poesiamonstruosa.utils.MusicManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by quayo on 16/01/2017.
 */

public class ReadActivityStandard extends ReadActivity {


    /**
     * Botón de ajustes
     */
    private ImageButton ajustes_btn;
    /**
     * Botón de ajustes
     */
    private ImageButton ajustes_close_btn;
    /**
     * Botón de restart
     */
    private ImageButton restart_btn;

    /**
* The pager widget, which handles animation and allows swiping horizontally to access previous
* and next wizard steps.
*/
    protected CustomViewPagerStandard mPager;
    /**
     * Animación de latido de paso de página
     */
    private ScaleAnimation heartBeatArrowRight;
    /**
     * Animación de latido de paso de página
     */
    private ScaleAnimation heartBeatLike;
    /**
     * Utilizado para mostrar el botón de refresh sobre el de play al terminar el autoplay de una pág
     */
    private TranslateAnimation showRefreshAuto;
    /**
     * Esconde el botón de restart
     */
    private ScaleAnimation hideRestart;
    /**
     * Muestra el botón de restart
     */
    private ScaleAnimation showRestart;
    /**
     * Muestra el botón de restart
     */
    private AnimationSet showLike;
    /**
     * Controla que el autoplay de una pág haya terminado
     */
    private boolean finPagina = false;

    /**
     * Esconde el botón de refresh al hacer click, cuando el autoplay de una pág haya terminado
     */
    private ScaleAnimation clickRefreshFinPagina;
    /**
     * Esconde el botón de refresh al retroceder página, cuando el autoplay de una pág haya terminado
     */
    private ScaleAnimation hideRefreshToRestorePlay;
    /**
     * Muestra el botón de play al retroceder una página, cuando el autoplay de una pág haya terminado
     */
    private ScaleAnimation restorePlayFromRefresh;
    /**
     * Muestra el botón de play despues de refresh, cuando el autoplay de una pág haya terminado
     */
    private ScaleAnimation showPlayFinPagina;
    /**
     * Esconde el botón de play en el fin de página si están los controles visibles
     */
    private ScaleAnimation hidePlayFinPagina;
    /**
     * * Esconde el botón de refresh en el fin de página si están los controles visibles
     */
    private ScaleAnimation showRefreshFinPagina;
    /**
     * Desplaza el botón de play en la animación de ajustes
     */
    private TranslateAnimation translatePlayAjustes;
    /**
     * Desplaza el botón de play en la animación de ajustes
     */
    private TranslateAnimation translatePlayAjustesReverse;
    /**
     * Desplaza la barra de progreso en la animación de ajustes
     */
    private TranslateAnimation translateProgressbarAjustes;
    /**
     * Desplaza la barra de progreso en la animación de ajustes
     */
    private TranslateAnimation translateProgressbarAjustesReverse;
    /**
     * Desplaza el botón de music en la animación de ajustes
     */
    private TranslateAnimation translateMusicAjustes;
    /**
     * Desplaza el botón de music en la animación de ajustes
     */
    private TranslateAnimation translateMusicAjustesReverse;
    /**
     * Desplaza el botón de voice en la animación de ajustes
     */
    private TranslateAnimation translateVoiceAjustes;
    /**
     * Desplaza el botón de voice en la animación de ajustes
     */
    private TranslateAnimation translateVoiceAjustesReverse;
    /**
     * Desplaza el botón de refresh en la animación de ajustes
     */
    private TranslateAnimation translateRefreshAjustes;
    /**
     * Desplaza el botón de refresh en la animación de ajustes
     */
    private TranslateAnimation translateRefreshAjustesReverse;
    /**
     * Esconde el botón de ajustes
     */
    private AnimationSet hideAjustes;
    /**
     * Esconde el botón de ajustes
     */
    private AnimationSet hideAjustesClose;
    /**
     * Esconde el botón de ajustes close
     */
    private AnimationSet showAjustes;
    /**
     * Esconde el botón de ajustes close
     */
    private AnimationSet showAjustesClose;

    /**
     * Indica si estamos en el estado fin de página
     * @return
     */
    public boolean isFinPagina() {

        return finPagina;
    }

    /**
     * Controla la visibilidad de los botones de ajustes
     */
    private boolean areAjustesVisible = false;

    public boolean areAjustesVisible() {

        return areAjustesVisible;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - OnCreate");

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);

        mContext = this;

        mDecorView = getWindow().getDecorView();

        mDecorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {

                boolean visible = (mDecorView.getSystemUiVisibility()
                        & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

                boolean visibleStatus = (mDecorView.getSystemUiVisibility()
                        & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0;

                Log.w(Constants.Log.CONTROLS, "ReadActivityStandard - NavigationBar - "+visible);
                Log.w(Constants.Log.CONTROLS, "ReadActivityStandard - StatusBar - "+visibleStatus);
            }
        });

        setContentView(R.layout.activity_read_standard);
        mainLayout = (FrameLayout) findViewById(R.id.main_layout_read_standard);
        revealLayout = (FrameLayout) findViewById(R.id.reveal_layout);
        controls_top_left = (FrameLayout) findViewById(R.id.controls_top_left);
        controls_top_right = (FrameLayout) findViewById(R.id.controls_top_right);
        controls_bottom = (FrameLayout) findViewById(R.id.controls_bottom);
        //Inicializamos el botón de flecha izquierda
        ajustes_btn = (ImageButton) findViewById(R.id.btn_ajustes);
        //Inicializamos el botón de flecha izquierda
        ajustes_close_btn = (ImageButton) findViewById(R.id.btn_ajustes_close);

        //Inicializamos el botón de flecha izquierda
        arrow_left_btn = (ImageButton) findViewById(R.id.arrow_left_btn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewOutlineProvider outline_left_btn = new ViewOutlineProvider() {

                @Override
                public void getOutline(View view, Outline outline) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int diameter = getResources().getDimensionPixelSize(R.dimen.diameter_tool_btns);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                }
            };
            arrow_left_btn.setOutlineProvider(outline_left_btn);
            arrow_left_btn.setClipToOutline(true);
        }

        //Inicializamos el botón de flecha derecha
        arrow_right_btn = (ImageButton) findViewById(R.id.arrow_right_btn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewOutlineProvider outline_right_btn = new ViewOutlineProvider() {

                @Override
                public void getOutline(View view, Outline outline) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int diameter = getResources().getDimensionPixelSize(R.dimen.diameter_tool_btns);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                }
            };
            arrow_right_btn.setOutlineProvider(outline_right_btn);
            arrow_right_btn.setClipToOutline(true);
        }

        //Inicializamos el botón de actualizar página
        like_btn = (ImageButton) findViewById(R.id.btn_like);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Le damos forma circular
            ViewOutlineProvider outline_like_btn = new ViewOutlineProvider() {

                @Override
                public void getOutline(View view, Outline outline) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int diameter = getResources().getDimensionPixelSize(R.dimen.diameter_divide_btn);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                }
            };
            like_btn.setOutlineProvider(outline_like_btn);
            like_btn.setClipToOutline(true);
        }

        //Inicializamos el botón de flecha derecha
        restart_btn = (ImageButton) findViewById(R.id.btn_restart);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewOutlineProvider outline_right_btn = new ViewOutlineProvider() {

                @Override
                public void getOutline(View view, Outline outline) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int diameter = getResources().getDimensionPixelSize(R.dimen.diameter_play_btn);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                }
            };
            restart_btn.setOutlineProvider(outline_right_btn);
            restart_btn.setClipToOutline(true);
        }

        //Inicializamos el botón de pausa
        play_btn = (ImageButton) findViewById(R.id.btn_play);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            //Le damos forma circular
            ViewOutlineProvider outline_pause_btn = new ViewOutlineProvider() {

                @Override
                public void getOutline(View view, Outline outline) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int diameter = getResources().getDimensionPixelSize(R.dimen.diameter_tool_btns);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                }
            };
            play_btn.setOutlineProvider(outline_pause_btn);
            play_btn.setClipToOutline(true);
        }

        //Inicializamos el botón de cerrar
        close_btn = (ImageButton) findViewById(R.id.btn_close);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            //Le damos forma circular
            ViewOutlineProvider outline_close_btn = new ViewOutlineProvider() {

                @Override
                public void getOutline(View view, Outline outline) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int diameter = getResources().getDimensionPixelSize(R.dimen.diameter_tool_btns);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                }
            };
            close_btn.setOutlineProvider(outline_close_btn);
            close_btn.setClipToOutline(true);
        }

        //Inicializamos el botón de música
        music_btn = (ImageButton) findViewById(R.id.btn_music);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Le damos forma circular
            ViewOutlineProvider outline_music_btn = new ViewOutlineProvider() {

                @Override
                public void getOutline(View view, Outline outline) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int diameter = getResources().getDimensionPixelSize(R.dimen.diameter_tool_btns);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                }
            };
            music_btn.setOutlineProvider(outline_music_btn);
            music_btn.setClipToOutline(true);
        }

        //Inicializamos el botón de voz
        voice_btn = (ImageButton) findViewById(R.id.btn_voice);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Le damos forma circular
            ViewOutlineProvider outline_voice_btn = new ViewOutlineProvider() {

                @Override
                public void getOutline(View view, Outline outline) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int diameter = getResources().getDimensionPixelSize(R.dimen.diameter_tool_btns);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                }
            };
            voice_btn.setOutlineProvider(outline_voice_btn);
            voice_btn.setClipToOutline(true);
        }

        //Inicializamos el botón de actualizar página
        refresh_btn = (ImageButton) findViewById(R.id.btn_refresh);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Le damos forma circular
            ViewOutlineProvider outline_refresh_btn = new ViewOutlineProvider() {

                @Override
                public void getOutline(View view, Outline outline) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int diameter = getResources().getDimensionPixelSize(R.dimen.diameter_tool_btns);
                        outline.setOval(0, 0, diameter, diameter);
                    }
                }
            };
            refresh_btn.setOutlineProvider(outline_refresh_btn);
            refresh_btn.setClipToOutline(true);
        }

        //Inicializamos barra de progreso
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        //Rotamos para que la barra de progreso disminuya en el sentido de las agujas del reloj
        progressBar.setRotationY(180);
        //progressBar.setVisibility(View.INVISIBLE);
        //Inicializamos el texto del nº de página
        numPagTextView = (TextView) findViewById(R.id.tview_numPage);
        numPagTextView_2 = (TextView) findViewById(R.id.tview_numPage_2);

        mAdapter = new CustomPagerAdapter(getSupportFragmentManager(), pages.length);
        mPager = (CustomViewPagerStandard) findViewById(R.id.slide_pager);
        mPager.setVisibility(View.INVISIBLE);
        mPager.setNumPages(pages.length);
        //mPager.setOffscreenPageLimit(2);
        mPager.setAdapter(mAdapter);
        //mPager.setPageTransformer(true, new CustomPageTransformer());
        mPager.setScrollDurationFactor(Constants.SCROLL_FACTOR);
        mPager.initControls(progressBar, numPagTextView, numPagTextView_2);

        resumeShowControls = false;

        prepareMusicAndVoice();
        prepareOnClicks();
        prepareAnimations();

        //Para esperar a que el autoplay se ponga solo
        lockButtons();
        lockTap();


        //Operaciones que tienen que esperar a que el layout esté definido
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                //Asociamos la vista al pager de inicio
                mPager.setCurrentView();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    areControlsVisible = true;

                    //Empezamos la animación inicial
                    startUnvealRead();
                }
                else {

                    areControlsVisible = false;
                    //Empezamos la animación inicial antes de Lollipop
                    startShowLayoutBL();
                }
            }
        });


    }

    private void prepareOnClicks() {

        close_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onClick closeButton");

                if (!areButtonsLocked()) {

                    //El bloqueo de botones lo hacemos dentro de exit.
                    exit();
                }
            }
        });

        voice_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onClick voiceButton");

                if (!areButtonsLocked()) {

                    //Bloqueamos el botón de play y el tap
                    lockButtons();
                    lockTap();

                    //Pausamos el autoplay mostrando refresh
                    pausaAutoplayControles(false);

                    voice_btn.startAnimation(clickVoice);

                    //Programamos desbloqueo
                    programUnlock(clickVoice.getDuration());

                    //Animación transformación voice on/off
                    //On - > Off
                    if (MusicManager.isVoice_on()) {

                        AnimationDrawable frameAnimation;
                        TransitionDrawable transition;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            frameAnimation =
                                    (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_volume_on_off, null);

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_deeppurple_icon_on_off, null);
                        }
                        else {
                            frameAnimation =
                                    (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_volume_on_off);

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_deeppurple_icon_on_off);
                        }

                        voice_btn.setImageDrawable(frameAnimation);
                        voice_btn.setBackground(transition);
                        transition.startTransition(getResources().getInteger(R.integer.tool_btn_color_transition));
                        frameAnimation.start();

                        //Quitamos volumen de la voz
                        MusicManager.setVoiceVolumeOff();
                    }
                    //Play - > Pause
                    else {

                        AnimationDrawable frameAnimation;
                        TransitionDrawable transition;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            frameAnimation =
                                    (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_volume_off_on, null);

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_deeppurple_icon_off_on, null);

                        }
                        else {

                            frameAnimation =
                                    (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_volume_off_on);

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_deeppurple_icon_off_on);
                        }

                        voice_btn.setImageDrawable(frameAnimation);
                        voice_btn.setBackground(transition);
                        transition.startTransition(getResources().getInteger(R.integer.tool_btn_color_transition));
                        frameAnimation.start();

                        //Restablecemos volumen de la voz
                        MusicManager.setVoiceVolumeOn();
                    }
                }
            }
        });

        music_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onClick musicButton");

                if (!areButtonsLocked()) {

                    //Bloqueamos el botón de play y el tap
                    lockButtons();
                    lockTap();

                    //Pausamos el autoplay mostrando refresh
                    pausaAutoplayControles(false);

                    music_btn.startAnimation(clickMusic);

                    //Programamos desbloqueo
                    programUnlock(clickMusic.getDuration());

                    //animación transformación voice on/off
                    //On - > Off
                    if (MusicManager.isMusic_on()) {

                        AnimationDrawable frameAnimation;
                        TransitionDrawable transition;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            frameAnimation =
                                    (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_music_on_off, null);

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_deeppurple_icon_on_off, null);
                        } else {

                            frameAnimation =
                                    (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_music_on_off);

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_deeppurple_icon_on_off);
                        }

                        music_btn.setImageDrawable(frameAnimation);
                        music_btn.setBackground(transition);
                        transition.startTransition(getResources().getInteger(R.integer.tool_btn_color_transition));
                        frameAnimation.start();

                        //Quitamos volumen de la música
                        MusicManager.setMusicVolumeOff();
                    }
                    //Off - > On
                    else {

                        AnimationDrawable frameAnimation;
                        TransitionDrawable transition;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            frameAnimation =
                                    (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_music_off_on, null);

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_deeppurple_icon_off_on, null);
                        }
                        else {

                            frameAnimation =
                                    (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_music_off_on);

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_deeppurple_icon_off_on);
                        }

                        music_btn.setImageDrawable(frameAnimation);
                        music_btn.setBackground(transition);
                        transition.startTransition(getResources().getInteger(R.integer.tool_btn_color_transition));
                        frameAnimation.start();

                        //Restablecemos volumen de la música
                        MusicManager.setMusicVolumeOn();
                    }
                }
            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - play_btn.OnClick");

                if (!areButtonsLocked()) {

                    //Bloqueamos el botón de play y el tap
                    lockButtons();
                    lockTap();

                    //Cancelamos tarea de desvanecimiento
                    cancelDesvanecimientoControles();

                    //Paramos o reanudamos el timer cambiando el icono.
                    //1a parte. Pone en marcha o para el autoplay.
                    //Play - > Pause
                    if (!paused) {

                        mPager.pauseTimer(false);
                        //Pausamos la música
                        MusicManager.pauseMusic();
                    }
                    //Pause - > Play
                    else {

                        mPager.resumeTimer();
                        //Resumimos la música
                        MusicManager.startResumeMusic();
                        //Renovamos el tiempo que están visibles los controles
                        planificarDesvanecimientoControles(true);

                        /** Highlight button deactivated
                         //Escondemos los botones de highlight si estuvieran visibles
                         if (highlightButtonsVisible) {

                         highlightButtonsVisible = false;

                         highlight_btn_close.startAnimation(hideHighlightIconClose);
                         highlight_btn_1.startAnimation(hideHighlight_1);
                         highlight_btn_2.startAnimation(hideHighlight_2);
                         }*/
                    }
                    //Programamos desbloqueo
                    programUnlock(clickPlay.getDuration());
                    startClickPlayAnimations();
                }
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - refreshButton.OnClick");

                if (!areButtonsLocked()) {

                    //Bloqueamos los botones y el tap
                    lockButtons();
                    lockTap();

                    //En caso de haber llegado al final del autoplay, restauramos el botón normal de play
                    if (finCuento) {

                        refresh_btn.startAnimation(clickRefreshFinPagina);

                        //Programamos desbloqueo
                        programUnlock(clickRefreshFinPagina.getDuration()+showPlayFinPagina.getDuration());

                        restart_btn.clearAnimation();
                        restart_btn.startAnimation(hideRestart);
                        like_btn.startAnimation(hideLike);

                        //En la última página tenemos el mediaplayer construido pero el flag a true lo bloquea
                        MusicManager.setVoice_finished(false);
                        finCuento = false;
                        finPagina = false;

                    } else {

                        if (finPagina) {

                            refresh_btn.startAnimation(clickRefreshFinPagina);

                            //Retornamos arrow right a su color original
                            TransitionDrawable transition;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                                transition =
                                        (TransitionDrawable) getResources().getDrawable(R.drawable.transition_finpag_icon_on_off, null);
                            } else {

                                transition =
                                        (TransitionDrawable) getResources().getDrawable(R.drawable.transition_finpag_icon_on_off);
                            }

                            arrow_right_btn.setBackground(transition);
                            transition.startTransition(getResources().getInteger(R.integer.tool_btn_color_transition));

                            //Programamos desbloqueo
                            programUnlock(clickRefreshFinPagina.getDuration()+showPlayFinPagina.getDuration());

                            arrow_right_btn.clearAnimation();
                            finPagina = false;

                        } else {

                            refresh_btn.startAnimation(clickRefresh);

                            //Programamos desbloqueo
                            programUnlock(clickRefresh.getDuration());

                            //Reiniciamos el timer cambiando el icono.
                            mPager.resetProgressBarAnimation();
                        }
                    }

                    timerLleno = true;
                }
            }
        });

        arrow_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - arrow_left_btn.OnClick");

                if (!areButtonsLocked()) {

                    //Comprobamos que no estamos en la primera página
                    if (mPager.getCurrentItem() != 0) {

                        pageBack();
                    }
                }
            }
        });

        arrow_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - arrow_right_btn.OnClick");

                if (!areButtonsLocked()) {

                    //Comprobamos que no estamos en la última página
                    if (mPager.getCurrentItem() != (mPager.getNumPages()-1)) {

                        pageForward();
                    }
                }
            }
        });

        like_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onClick likeButton");

                if (!areButtonsLocked()) {

                    lockButtons();
                    lockTap();

                    //Quitamos música
                    MusicManager.fadeMusic();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        refresh_btn.setElevation(0);
                        like_btn.setElevation(0);
                    }

                    like_btn.clearAnimation();
                    like_btn.startAnimation(hideLikeForLikeTransition);
                }
            }
        });

        restart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onClick restartButton");

                if (!areButtonsLocked()) {

                    //Bloqueamos el botón de play y el tap
                    lockButtons();
                    lockTap();

                    //Interrumpimos animación de heartbeat de autoplay y like
                    restart_btn.clearAnimation();
                    like_btn.clearAnimation();

                    //Quitamos música
                    MusicManager.fadeMusic();

                    //Restablecemos play por refresh
                    refresh_btn.startAnimation(hideRefreshToRestorePlay);

                    //Antes de Lollipop los botones no los tapa el reveal asi que los escondemos
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        restart_btn.startAnimation(preRestartTransition);
                    }
                    else {

                        like_btn.startAnimation(hideLikeForRestartBL);
                        mPager.startAnimation(pagerOutTransition);
                    }

                    timerLleno = true;
                    finPagina = false;
                    finCuento = false;
                }

            }
        });

        ajustes_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onClick ajustesCloseButton");

                if (!areButtonsLocked()) {

                    //Bloqueamos el botón de play y el tap
                    lockButtons();
                    lockTap();

                    if (finPagina) {

                        refresh_btn.startAnimation(translateRefreshAjustesReverse);

                        //Ajustamos la teórica posición del botón de play y la barra de progreso
                        FrameLayout.LayoutParams paramsPlay = (FrameLayout.LayoutParams) play_btn.getLayoutParams();
                        paramsPlay.setMarginEnd((int) (getResources().getDimension(R.dimen.margin_play_tool_btn)));

                        play_btn.setLayoutParams(paramsPlay);

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
                        params.setMarginEnd((int) (getResources().getDimension(R.dimen.margin_tool_progress_bar)));

                        progressBar.setLayoutParams(params);
                    }
                    else {

                        if (refresh_btn.getVisibility() == View.VISIBLE){

                            refresh_btn.startAnimation(translateRefreshAjustesReverse);
                        }
                        else {

                            //Ajustamos la posición sin animación
                            FrameLayout.LayoutParams paramsRefresh = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
                            paramsRefresh.setMarginEnd((int) (paramsRefresh.getMarginEnd()) -
                                    (int) (getResources().getDimension(R.dimen.translate_ajustes)));

                            refresh_btn.setLayoutParams(paramsRefresh);
                        }
                        play_btn.startAnimation(translatePlayAjustesReverse);
                        progressBar.startAnimation(translateProgressbarAjustesReverse);
                    }

                    voice_btn.startAnimation(translateVoiceAjustesReverse);
                    music_btn.startAnimation(translateMusicAjustesReverse);

                    ajustes_close_btn.startAnimation(hideAjustesClose);

                    areAjustesVisible = false;

                    //Programamos desbloqueo
                    programUnlock(hideAjustesClose.getDuration()+showAjustes.getDuration());
                }
            }
        });

        ajustes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onClick ajustesButton");

                if (!areButtonsLocked()) {

                    //Bloqueamos el botón de play y el tap
                    lockButtons();
                    lockTap();

                    if (finPagina) {

                        refresh_btn.startAnimation(translateRefreshAjustes);

                        //Ajustamos la teórica posición del botón de play y la barra de progreso
                        FrameLayout.LayoutParams paramsPlay = (FrameLayout.LayoutParams) play_btn.getLayoutParams();
                        paramsPlay.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes)+
                                paramsPlay.getMarginEnd()));

                        play_btn.setLayoutParams(paramsPlay);

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
                        params.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes)+
                                params.getMarginEnd()));

                        progressBar.setLayoutParams(params);
                    }
                    else {

                        if (refresh_btn.getVisibility() == View.VISIBLE){

                            refresh_btn.startAnimation(translateRefreshAjustes);
                        }
                        else {

                            //Ajustamos la posición de refresh sin animación
                            FrameLayout.LayoutParams paramsRefresh = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
                            paramsRefresh.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes)+
                                    paramsRefresh.getMarginEnd()));

                            refresh_btn.setLayoutParams(paramsRefresh);
                        }
                        play_btn.startAnimation(translatePlayAjustes);
                        progressBar.startAnimation(translateProgressbarAjustes);
                    }

                    voice_btn.startAnimation(translateVoiceAjustes);
                    music_btn.startAnimation(translateMusicAjustes);
                    ajustes_btn.startAnimation(hideAjustes);

                    areAjustesVisible = true;

                    //Programamos desbloqueo
                    programUnlock(hideAjustes.getDuration()+showAjustesClose.getDuration());
                }
            }
        });
    }

    /**
     * Configura las animaciones.
     */
    private void prepareAnimations() {

        prepareStandardAnimations();
        prepareHideShowControlsAnimations();
        prepareAjustesAnimations();
    }

    private void prepareStandardAnimations() {
        //Cargamos la animación de escala para el botón de pausa/play del temporizador
        clickPlay = new AnimationSet(false);

        ScaleAnimation firstClickPlayScale = new ScaleAnimation(1f, 1.4f, 1f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        firstClickPlayScale.setDuration(getResources().getInteger(R.integer.scale_click));
        firstClickPlayScale.setRepeatMode(Animation.REVERSE);
        firstClickPlayScale.setRepeatCount(1);

        ScaleAnimation secondClickPlayScale = new ScaleAnimation(1f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        secondClickPlayScale.setDuration(getResources().getInteger(R.integer.wait_icon_animation));

        clickPlay.addAnimation(firstClickPlayScale);
        clickPlay.addAnimation(secondClickPlayScale);

        clickPlay.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickPlay.onAnimationStart");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickPlay.onAnimationEnd");

                if (!paused) setButtonIconToPause();
            }
        });

        //Cargamos la animación de escala para el botón de pausa/play del temporizador
        clickPlayForPause = new AnimationSet(false);

        ScaleAnimation firstclickPlayForPauseScale = new ScaleAnimation(1f, 1.4f, 1f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        firstclickPlayForPauseScale.setDuration(getResources().getInteger(R.integer.scale_click));
        firstclickPlayForPauseScale.setRepeatMode(Animation.REVERSE);
        firstclickPlayForPauseScale.setRepeatCount(1);

        ScaleAnimation secondclickPlayForPauseScale = new ScaleAnimation(1f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        secondclickPlayForPauseScale.setDuration(getResources().getInteger(R.integer.wait_icon_animation));

        clickPlayForPause.addAnimation(firstclickPlayForPauseScale);
        clickPlayForPause.addAnimation(secondclickPlayForPauseScale);

        clickPlayForPause.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickPlayForPause.onAnimationStart");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickPlayForPause.onAnimationEnd");

                if (paused) setButtonIconToPlay();
            }
        });

        //Cargamos la animación de escala para la barra de progreso
        clickProgressBar = new ScaleAnimation(1f, 1.4f, 1f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        clickProgressBar.setDuration(getResources().getInteger(R.integer.scale_click));
        clickProgressBar.setRepeatMode(Animation.REVERSE);
        clickProgressBar.setRepeatCount(1);

        //Cargamos la animación de escala y rotación para el botón de actualizar
        clickRefresh = new AnimationSet(false);

        /*
        ScaleAnimation clickRefreshScale = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        clickRefreshScale.setRepeatMode(Animation.REVERSE);
        clickRefreshScale.setRepeatCount(1);
        clickRefreshScale.setDuration(getResources().getInteger(R.integer.scale_click));
        clickRefreshScale.setInterpolator(AnimationUtils.loadInterpolator(
                getApplicationContext(), android.R.interpolator.linear));

        RotateAnimation clickRefreshRotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        clickRefreshRotate.setDuration(
                getResources().getInteger(R.integer.rotateRefresh));
        clickRefreshRotate.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        */
        ScaleAnimation scaleRefreshHide = new ScaleAnimation(1, 0.2f, 1, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleRefreshHide.setDuration(getResources().getInteger(R.integer.hide_controls));

        TranslateAnimation clickTranslateRefresh = new TranslateAnimation(0 ,
                getResources().getDimension(R.dimen.margin_refresh_btn) ,0, 0);
        clickTranslateRefresh.setDuration(getResources().getInteger(R.integer.hide_controls));

        ScaleAnimation clickRefreshWait = new ScaleAnimation(1f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        clickRefreshWait.setDuration(getResources().getInteger(R.integer.wait_icon_animation));

        //clickRefresh.addAnimation(clickRefreshScale);
        //clickRefresh.addAnimation(clickRefreshRotate);
        clickRefresh.addAnimation(scaleRefreshHide);
        clickRefresh.addAnimation(clickTranslateRefresh);
        clickRefresh.addAnimation(clickRefreshWait);

        clickRefresh.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickRefresh.onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickRefresh.onAnimationRepeat");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickRefresh.onAnimationEnd");

                refresh_btn.setClickable(false);
                refresh_btn.setVisibility(View.INVISIBLE);
            }
        });

        clickRefreshFinPagina = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        clickRefreshFinPagina.setDuration(getResources().getInteger(R.integer.hide_controls));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            clickRefreshFinPagina.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        clickRefreshFinPagina.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickRefreshFinPagina.onAnimationEnd");

                //Volvemos a colocar el botón refresh en su posición original

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();

                if (areAjustesVisible) {

                    params.setMarginEnd( (int) (getResources().getDimension(R.dimen.margin_refresh_tool_btn) +
                            getResources().getDimension(R.dimen.translate_ajustes)));
                }
                else {

                    params.setMarginEnd( (int) getResources().getDimension(R.dimen.margin_refresh_tool_btn));
                }

                refresh_btn.setLayoutParams(params);

                refresh_btn.setClickable(false);
                refresh_btn.setVisibility(View.INVISIBLE);

                play_btn.startAnimation(showPlayFinPagina);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hideRefreshToRestorePlay = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hideRefreshToRestorePlay.setDuration(getResources().getInteger(R.integer.hide_controls));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideRefreshToRestorePlay.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        hideRefreshToRestorePlay.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideRefreshToRestorePlay.onAnimationEnd");

                //Volvemos a colocar el botón refresh en su posición original
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
                if (areAjustesVisible) {

                    params.setMarginEnd( (int) (getResources().getDimension(R.dimen.margin_refresh_tool_btn) +
                            getResources().getDimension(R.dimen.translate_ajustes)));
                }
                else {

                    params.setMarginEnd( (int) getResources().getDimension(R.dimen.margin_refresh_tool_btn));
                }
                refresh_btn.setLayoutParams(params);

                refresh_btn.setClickable(false);
                refresh_btn.setVisibility(View.INVISIBLE);

                play_btn.startAnimation(restorePlayFromRefresh);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hidePlayFinPagina = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hidePlayFinPagina.setDuration(getResources().getInteger(R.integer.hide_controls));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hidePlayFinPagina.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        hidePlayFinPagina.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hidePlayFinPagina.onAnimationEnd");

                play_btn.setClickable(false);
                play_btn.setVisibility(View.INVISIBLE);

                refresh_btn.startAnimation(showRefreshFinPagina);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        showPlayFinPagina = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showPlayFinPagina.setDuration(getResources().getInteger(R.integer.hide_controls));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showPlayFinPagina.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        showPlayFinPagina.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                setButtonIconToPlay();
                play_btn.setVisibility(View.VISIBLE);
                play_btn.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showPlayFinPagina.onAnimationEnd");

                //En fin de página reseteamos sin animación la barra de progreso y ponemos en marcha el autoplay
                mPager.resetProgressBarWithoutAnimation();
                mPager.resumeTimer();
                progressBar.setVisibility(View.VISIBLE);

                //Resumimos la música
                MusicManager.startResumeMusic();
                //Animación como si hubiéramos hecho click
                startClickPlayAnimations();
                //Renovamos el tiempo que están visibles los controles
                planificarDesvanecimientoControles(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        restorePlayFromRefresh = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        restorePlayFromRefresh.setDuration(getResources().getInteger(R.integer.hide_controls));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            restorePlayFromRefresh.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        restorePlayFromRefresh.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                setButtonIconToPlay();
                play_btn.setVisibility(View.VISIBLE);
                play_btn.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - restorePlayFromRefresh.onAnimationEnd");

                //En fin de página reseteamos sin animación la barra de progreso y ponemos en marcha el autoplay
                mPager.resetProgressBarWithoutAnimation();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        showRefreshFinPagina = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showRefreshFinPagina.setDuration(getResources().getInteger(R.integer.hide_controls));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showRefreshFinPagina.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        showRefreshFinPagina.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                refresh_btn.setVisibility(View.VISIBLE);
                refresh_btn.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showRefreshFinPagina.onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos la animación de escala para el botón de voz de la barra de controles
        clickVoice = new AnimationSet(false);

        ScaleAnimation firstClickVoiceScale = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        firstClickVoiceScale.setDuration(getResources().getInteger(R.integer.scale_click));
        firstClickVoiceScale.setRepeatMode(Animation.REVERSE);
        firstClickVoiceScale.setRepeatCount(1);

        ScaleAnimation secondClickVoiceScale = new ScaleAnimation(1f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        secondClickVoiceScale.setDuration(getResources().getInteger(R.integer.wait_icon_animation));

        clickVoice.addAnimation(firstClickVoiceScale);
        clickVoice.addAnimation(secondClickVoiceScale);

        clickVoice.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickVoice.onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickVoice.onAnimationEnd");

                //animación transformación voice on/off
                //On - > Off
                if (MusicManager.isVoice_on()) {

                    //Quitamos volumen de la voz. Duplicado por si se interrumpe.
                    MusicManager.setVoiceVolumeOff();

                    setVoiceIconOff();
                    MusicManager.setVoice_on(false);
                }
                //Play - > Pause
                else {

                    //Quitamos volumen de la voz. Duplicado por si se interrumpe.
                    MusicManager.setVoiceVolumeOn();

                    setVoiceIconOn();
                    MusicManager.setVoice_on(true);
                }
            }
        });

        //Cargamos la animación de escala para el botón de música de la barra de controles
        clickMusic = new AnimationSet(false);

        ScaleAnimation firstClickMusicScale = new ScaleAnimation(1f, 1.5f, 1f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        firstClickMusicScale.setDuration(getResources().getInteger(R.integer.scale_click));
        firstClickMusicScale.setRepeatMode(Animation.REVERSE);
        firstClickMusicScale.setRepeatCount(1);

        ScaleAnimation secondClickMusicScale = new ScaleAnimation(1f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        secondClickMusicScale.setDuration(getResources().getInteger(R.integer.wait_icon_animation));

        clickMusic.addAnimation(firstClickMusicScale);
        clickMusic.addAnimation(secondClickMusicScale);

        clickMusic.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickMusic.onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - clickMusic.onAnimationEnd");

                //animación transformación voice on/off
                //On - > Off
                if (MusicManager.isMusic_on()) {

                    //Quitamos volumen de la música. Duplicado por si se interrumpe.
                    MusicManager.setMusicVolumeOff();

                    setMusicIconOff();
                    MusicManager.setMusic_on(false);
                }
                //Play - > Pause
                else {

                    //Restablecemos volumen de la música. Duplicado por si se interrumpe.
                    MusicManager.setMusicVolumeOn();

                    setMusicIconOn();
                    MusicManager.setMusic_on(true);
                }
            }
        });
    }

    private void prepareHideShowControlsAnimations() {

        //Cargamos la animación de escala y rotación para el botón close al salir de la app
        hideCloseForReveal = new AnimationSet(false);

        ScaleAnimation preHideClose = new ScaleAnimation(1f, 1.3f, 1f, 1.3f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        preHideClose.setDuration(getResources().getInteger(R.integer.pre_hide_standard));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            preHideClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        RotateAnimation rotateHideClose = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateHideClose.setDuration(
                getResources().getInteger(R.integer.rotateClose));

        ScaleAnimation scaleHideClose = new ScaleAnimation(1.3f, 0, 1.3f, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleHideClose.setDuration(getResources().getInteger(R.integer.hide_standard));
        scaleHideClose.setStartOffset(getResources().getInteger(R.integer.pre_hide_standard));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            scaleHideClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        hideCloseForReveal.addAnimation(preHideClose);
        hideCloseForReveal.addAnimation(rotateHideClose);
        hideCloseForReveal.addAnimation(scaleHideClose);

        hideCloseForReveal.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideCloseForReveal.onAnimationStart");

                close_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideCloseForReveal.onAnimationEnd");

                startRevealReadReturn();
            }
        });

        //Cargamos la animación de aparición del botón de cerrar
        showClose = new TranslateAnimation(0, 0,
                -getResources().getDimension(R.dimen.hide_controls), 0);
        showClose.setDuration(getResources().getInteger(R.integer.hide_controls));
        showClose.setStartOffset(getResources().getInteger(R.integer.delay_show_first));
        showClose.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        //Cargamos la animación de ocultación del botón de cerrar
        hideClose = new TranslateAnimation(0, 0, 0,
                -getResources().getDimension(R.dimen.hide_controls));
        hideClose.setDuration(getResources().getInteger(R.integer.hide_controls));
        hideClose.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        hideClose.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideClose.onAnimationStart");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideClose.onAnimationEnd");

                close_btn.setClickable(false);
                close_btn.setVisibility(View.INVISIBLE);

            }
        });

        //Cargamos la animación de aparición del botón de música
        showMusic = new TranslateAnimation(0, 0,
                -getResources().getDimension(R.dimen.hide_controls), 0);
        showMusic.setDuration(getResources().getInteger(R.integer.hide_controls));
        showMusic.setStartOffset(getResources().getInteger(R.integer.delay_show_second));
        showMusic.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showMusic.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        //Cargamos la animación de ocultación de botón de música
        hideMusic = new TranslateAnimation(0, 0, 0,
                -getResources().getDimension(R.dimen.hide_controls));
        hideMusic.setDuration(getResources().getInteger(R.integer.hide_controls));
        hideMusic.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideMusic.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        hideMusic.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideMusic.onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideMusic.onAnimationEnd");

                music_btn.setClickable(false);
                music_btn.setVisibility(View.INVISIBLE);
            }
        });

        //Cargamos la animación de aparición del botón de voz
        showVoice = new TranslateAnimation(0, 0,
                -getResources().getDimension(R.dimen.hide_controls), 0);
        showVoice.setDuration(getResources().getInteger(R.integer.hide_controls));
        showVoice.setStartOffset(getResources().getInteger(R.integer.delay_show_third));
        showVoice.setFillAfter(true);

        //Lo elegimos como animación para desbloquear los botones después bloquearlos al mostrar controles
        showVoice.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showVoice.onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showVoice.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        //Cargamos la animación de ocultación de botón de voz
        hideVoice = new TranslateAnimation(0, 0, 0,
                -getResources().getDimension(R.dimen.hide_controls));
        hideVoice.setDuration(getResources().getInteger(R.integer.hide_controls));
        hideVoice.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideVoice.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        hideVoice.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideVoice.onAnimationStart");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideVoice.onAnimationEnd");

                voice_btn.setClickable(false);
                voice_btn.setVisibility(View.INVISIBLE);
            }
        });

        /** Highlight button deactivated
         //Cargamos la animación de aparición del botón de voz
         showHighlight = new TranslateAnimation(0, 0,
         -getResources().getDimension(R.dimen.hide_controls), 0);
         showHighlight.setDuration(getResources().getInteger(R.integer.hide_controls));
         showHighlight.setStartOffset(getResources().getInteger(R.integer.delay_show_fourth));
         showHighlight.setFillAfter(true);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         showHighlight.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         //Cargamos la animación de aparición del botón de voz
         showHighlightBackgr = new TranslateAnimation(0, 0,
         -getResources().getDimension(R.dimen.hide_controls), 0);
         showHighlightBackgr.setDuration(getResources().getInteger(R.integer.hide_controls));
         showHighlightBackgr.setStartOffset(getResources().getInteger(R.integer.delay_show_fourth));
         showHighlightBackgr.setFillAfter(true);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         showHighlightBackgr.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         //Lo elegimos como animación para desbloquear los botones después bloquearlos al mostrar controles
         showHighlightBackgr.setAnimationListener(new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showHighlightBackgr.onAnimationEnd");

        unlockButtons();
        unlockTap();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        });

         //Cargamos la animación de ocultación de botón de resaltado
         hideHighlight = new TranslateAnimation(0, 0, 0,
         -getResources().getDimension(R.dimen.hide_controls));
         hideHighlight.setDuration(getResources().getInteger(R.integer.hide_controls));
         hideHighlight.setFillAfter(true);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         hideHighlight.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
         }
         hideHighlight.setAnimationListener(new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlight.onAnimationStart");

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlight.onAnimationEnd");

        highlight_btn.setClickable(false);
        highlight_btn.setVisibility(View.INVISIBLE);
        }
        });

         //Cargamos la animación de ocultación de botón de voz
         hideHighlightClose = new TranslateAnimation(0, 0, 0,
         -getResources().getDimension(R.dimen.hide_controls));
         hideHighlightClose.setDuration(getResources().getInteger(R.integer.hide_controls));
         hideHighlightClose.setFillAfter(true);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         hideHighlightClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
         }
         hideHighlightClose.setAnimationListener(new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlightClose.onAnimationStart");

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlightClose.onAnimationEnd");

        highlight_btn_close.setClickable(false);
        highlight_btn_close.setVisibility(View.INVISIBLE);
        }
        });

         //Cargamos la animación de ocultación de botón de voz
         hideHighlightBackgr = new TranslateAnimation(0, 0, 0,
         -getResources().getDimension(R.dimen.hide_controls));
         hideHighlightBackgr.setDuration(getResources().getInteger(R.integer.hide_controls));
         hideHighlightBackgr.setFillAfter(true);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         hideHighlightBackgr.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
         }
         hideHighlightBackgr.setAnimationListener(new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlightBackgr.onAnimationStart");

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlightBackgr.onAnimationEnd");

        highlight_backgr_btn.setClickable(false);
        highlight_backgr_btn.setVisibility(View.INVISIBLE);
        }
        });

         hideHighlightIcon = new AnimationSet(false);

         RotateAnimation rotateHideHighlightIcon = new RotateAnimation(0, 180,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         rotateHideHighlightIcon.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
         }
         rotateHideHighlightIcon.setDuration(getResources().getInteger(R.integer.change_icon_standard));

         ScaleAnimation scaleHideHighlightIcon = new ScaleAnimation(1f, 0, 1f, 0,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleHideHighlightIcon.setDuration(getResources().getInteger(R.integer.change_icon_standard));
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         scaleHideHighlightIcon.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
         }

         hideHighlightIcon.addAnimation(rotateHideHighlightIcon);
         hideHighlightIcon.addAnimation(scaleHideHighlightIcon);

         hideHighlightIcon.setAnimationListener(new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlightIcon.onAnimationStart");
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlightIcon.onAnimationEnd");

        highlight_btn.setClickable(false);
        highlight_btn.setVisibility(View.INVISIBLE);
        highlight_btn_close.startAnimation(showHighlightClose);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        });

         hideHighlightIconClose = new AnimationSet(false);

         RotateAnimation rotateHideHighlightClose = new RotateAnimation(0, 180,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         rotateHideHighlightClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
         }
         rotateHideHighlightClose.setDuration(getResources().getInteger(R.integer.change_icon_standard));

         ScaleAnimation scaleHideHighlightClose = new ScaleAnimation(1f, 0, 1f, 0,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleHideHighlightClose.setDuration(getResources().getInteger(R.integer.change_icon_standard));
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         scaleHideHighlightClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
         }

         hideHighlightIconClose.addAnimation(rotateHideHighlightClose);
         hideHighlightIconClose.addAnimation(scaleHideHighlightClose);

         hideHighlightIconClose.setAnimationListener(new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlightIconClose.onAnimationEnd");

        highlight_btn_close.setClickable(false);
        highlight_btn_close.setVisibility(View.INVISIBLE);
        highlight_btn.startAnimation(showHighlightIcon);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        });

         showHighlightIcon = new AnimationSet(true);

         ScaleAnimation scaleShowHighlightIcon = new ScaleAnimation(0f, 1f, 0f, 1f,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleShowHighlightIcon.setDuration(getResources().getInteger(R.integer.change_icon_standard));

         RotateAnimation rotateShowHighlightIcon = new RotateAnimation(180, 360,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         rotateShowHighlightIcon.setDuration(getResources().getInteger(R.integer.change_icon_standard));

         showHighlightIcon.addAnimation(scaleShowHighlightIcon);
         showHighlightIcon.addAnimation(rotateShowHighlightIcon);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         }
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         showHighlightIcon.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         showHighlightIcon.setAnimationListener(new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showHighlightIcon.onAnimationStart");

        highlight_btn.setVisibility(View.VISIBLE);
        highlight_btn.setClickable(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showHighlightIcon.onAnimationEnd");

        }
        });

         showHighlightClose = new AnimationSet(true);

         ScaleAnimation scaleShowHighlightClose = new ScaleAnimation(0f, 1f, 0f, 1f,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleShowHighlightClose.setDuration(getResources().getInteger(R.integer.change_icon_standard));

         RotateAnimation rotateShowHighlightClose = new RotateAnimation(180, 360,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         rotateShowHighlightClose.setDuration(getResources().getInteger(R.integer.change_icon_standard));

         showHighlightClose.addAnimation(scaleShowHighlightClose);
         showHighlightClose.addAnimation(rotateShowHighlightClose);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         showHighlightClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         showHighlightClose.setAnimationListener(new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showHighlightClose.onAnimationStart");

        highlight_btn_close.setVisibility(View.VISIBLE);
        highlight_btn_close.setClickable(true);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showHighlightClose.onAnimationEnd");

        }
        });

         //Preparamos animación de mostrar el botón highlight option 1
         showHighlight_1 = new AnimationSet(true);

         ScaleAnimation scaleShowHighlight_1 = new ScaleAnimation(0.4f, 1, 0.4f, 1,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleShowHighlight_1.setDuration(getResources().getInteger(R.integer.show_standard));

         int verticalTranslation = (int) (getResources().getDimension(R.dimen.margin_highlight_option_top) -
         getResources().getDimension(R.dimen.margin_highlight_top));

         int horizontalTranslation = (int) (getResources().getDimension(R.dimen.margin_highlight_option_1) -
         getResources().getDimension(R.dimen.margin_tool_3));

         TranslateAnimation translateShowHighlight_1 = new TranslateAnimation(
         horizontalTranslation, 0, -verticalTranslation, 0);
         translateShowHighlight_1.setDuration(getResources().getInteger(R.integer.show_standard));

         showHighlight_1.addAnimation(scaleShowHighlight_1);
         showHighlight_1.addAnimation(translateShowHighlight_1);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         showHighlight_1.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         showHighlight_1.setAnimationListener(new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showHighlight_1.onAnimationStart);

        highlight_btn_1.setVisibility(View.VISIBLE);
        highlight_btn_1.setClickable(true);
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showHighlight_1.onAnimationEnd");

        unlockButtons();
        unlockTap();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        });

         //Preparamos animación de mostrar el botón de rate
         showHighlight_2 = new AnimationSet(true);

         ScaleAnimation scaleShowHighlight_2 = new ScaleAnimation(0.4f, 1, 0.4f, 1,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleShowHighlight_2.setDuration(getResources().getInteger(R.integer.show_standard));

         TranslateAnimation translateShowHighlight_2 = new TranslateAnimation(
         -horizontalTranslation, 0, -verticalTranslation, 0);
         translateShowHighlight_2.setDuration(getResources().getInteger(R.integer.show_standard));

         showHighlight_2.addAnimation(scaleShowHighlight_2);
         showHighlight_2.addAnimation(translateShowHighlight_2);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         showHighlight_2.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         showHighlight_2.setAnimationListener(new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showHighlight_2.onAnimationStart");

        highlight_btn_2.setVisibility(View.VISIBLE);
        highlight_btn_2.setClickable(true);
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        });

         //Preparamos animación de mostrar el botón highlight option 1
         hideHighlight_1 = new AnimationSet(true);

         ScaleAnimation scaleHideHighlight_1 = new ScaleAnimation( 1, 0.4f, 1, 0.4f,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleHideHighlight_1.setDuration(getResources().getInteger(R.integer.show_standard));

         TranslateAnimation translateHideHighlight_1 = new TranslateAnimation(
         0, horizontalTranslation, 0, -verticalTranslation);
         translateHideHighlight_1.setDuration(getResources().getInteger(R.integer.show_standard));

         hideHighlight_1.addAnimation(scaleHideHighlight_1);
         hideHighlight_1.addAnimation(translateHideHighlight_1);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         hideHighlight_1.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         hideHighlight_1.setAnimationListener(new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlight_1.onAnimationEnd");

        highlight_btn_1.setClickable(false);
        highlight_btn_1.setVisibility(View.INVISIBLE);
        unlockButtons();
        unlockTap();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        });

         //Preparamos animación de mostrar el botón de rate
         hideHighlight_2 = new AnimationSet(true);

         ScaleAnimation scaleHideHighlight_2 = new ScaleAnimation( 1, 0.4f, 1, 0.4f,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleHideHighlight_2.setDuration(getResources().getInteger(R.integer.show_standard));

         TranslateAnimation translateHideHighlight_2 = new TranslateAnimation(
         0, -horizontalTranslation, 0, -verticalTranslation);
         translateHideHighlight_2.setDuration(getResources().getInteger(R.integer.show_standard));

         hideHighlight_2.addAnimation(scaleHideHighlight_2);
         hideHighlight_2.addAnimation(translateHideHighlight_2);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         hideHighlight_2.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         hideHighlight_2.setAnimationListener(new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlight_2.onAnimationEnd");

        highlight_btn_2.setClickable(false);
        highlight_btn_2.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        });

         //Preparamos animación de mostrar el botón highlight option 1
         hideHighlight_1_long = new AnimationSet(true);

         ScaleAnimation scaleHideHighlight_1_long = new ScaleAnimation( 1, 0.4f, 1, 0.4f,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleHideHighlight_1_long.setDuration(getResources().getInteger(R.integer.hide_highlight));

         //Se esconde la suma de las distancias de ambos casos
         TranslateAnimation translateHideHighlight_1_long = new TranslateAnimation(
         0, horizontalTranslation, 0,
         -verticalTranslation-getResources().getDimension(R.dimen.hide_controls));
         translateHideHighlight_1_long.setDuration(getResources().getInteger(R.integer.hide_highlight));

         hideHighlight_1_long.addAnimation(scaleHideHighlight_1_long);
         hideHighlight_1_long.addAnimation(translateHideHighlight_1_long);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         hideHighlight_1_long.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         hideHighlight_1_long.setAnimationListener(new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlight_1_long.onAnimationEnd");

        highlightButtonsVisible = false;
        highlight_btn_1.setClickable(false);
        highlight_btn_1.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        });

         //Preparamos animación de mostrar el botón de rate
         hideHighlight_2_long = new AnimationSet(true);

         ScaleAnimation scaleHideHighlight_2_long = new ScaleAnimation( 1, 0.4f, 1, 0.4f,
         Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
         scaleHideHighlight_2_long.setDuration(getResources().getInteger(R.integer.hide_highlight));

         TranslateAnimation translateHideHighlight_2_long = new TranslateAnimation(
         0, -horizontalTranslation, 0,
         -verticalTranslation-getResources().getDimension(R.dimen.hide_controls));
         translateHideHighlight_2_long.setDuration(getResources().getInteger(R.integer.hide_highlight));

         hideHighlight_2_long.addAnimation(scaleHideHighlight_2_long);
         hideHighlight_2_long.addAnimation(translateHideHighlight_2_long);

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

         hideHighlight_2_long.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
         getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
         }

         hideHighlight_2_long.setAnimationListener(new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideHighlight_2_long.onAnimationEnd");

        highlight_btn_2.setClickable(false);
        highlight_btn_2.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
        });
         */

        //Cargamos la animación de aparición del botón de actualizar página
        showRefresh = new AnimationSet(true);

        ScaleAnimation scaleRefreshShow = new ScaleAnimation(0.4f, 1, 0.4f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleRefreshShow.setDuration(getResources().getInteger(R.integer.show_hide_refresh));

        TranslateAnimation translateRefreshShow = new TranslateAnimation(
                getResources().getDimension(R.dimen.margin_refresh_btn), 0 ,0, 0);
        translateRefreshShow.setDuration(getResources().getInteger(R.integer.show_hide_refresh));

        showRefresh.addAnimation(scaleRefreshShow);
        showRefresh.addAnimation(translateRefreshShow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showRefresh.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        showRefresh.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showRefresh.onAnimationStart");

                refresh_btn.setVisibility(View.VISIBLE);
                refresh_btn.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showRefresh.onAnimationEnd");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        int offset = (int) (getResources().getDimension(R.dimen.margin_refresh_tool_btn) -
                getResources().getDimension(R.dimen.margin_play_tool_btn));

        showRefreshAuto = new TranslateAnimation(0, offset, 0, 0);
        showRefreshAuto.setFillAfter(true);

        showRefreshAuto.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showRefreshAuto.onAnimationStart");

                refresh_btn.setVisibility(View.VISIBLE);
                refresh_btn.setClickable(true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos la animación de ocultación de botón de actualizar página
        hideRefresh = new AnimationSet(true);

        ScaleAnimation scaleRefreshHide = new ScaleAnimation(1, 0.2f, 1, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleRefreshHide.setDuration(getResources().getInteger(R.integer.show_hide_refresh));

        TranslateAnimation translateRefreshHide = new TranslateAnimation(0 ,
                getResources().getDimension(R.dimen.margin_refresh_btn) ,0, 0);
        translateRefreshHide.setDuration(getResources().getInteger(R.integer.show_hide_refresh));

        hideRefresh.addAnimation(scaleRefreshHide);
        hideRefresh.addAnimation(translateRefreshHide);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideRefresh.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }
        hideRefresh.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideRefresh.onAnimationStart");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideRefresh.onAnimationEnd");

                refresh_btn.setClickable(false);
                refresh_btn.setVisibility(View.INVISIBLE);
            }
        });

        //Cargamos la animación de ocultación de botón de actualizar página
        hideRefreshForRestart = new AnimationSet(true);

        ScaleAnimation scaleRefreshHideForRestart = new ScaleAnimation(1, 0.2f, 1, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleRefreshHideForRestart.setDuration(getResources().getInteger(R.integer.show_hide_refresh));

        TranslateAnimation translateRefreshHideForRestart = new TranslateAnimation(0 ,
                getResources().getDimension(R.dimen.margin_refresh_btn) ,0, 0);
        translateRefreshHideForRestart.setDuration(getResources().getInteger(R.integer.show_hide_refresh));

        hideRefreshForRestart.addAnimation(scaleRefreshHideForRestart);
        hideRefreshForRestart.addAnimation(translateRefreshHideForRestart);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideRefreshForRestart.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }
        hideRefreshForRestart.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideRefreshForRestart.onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideRefreshForRestart.onAnimationEnd");

                refresh_btn.setClickable(false);
                refresh_btn.setVisibility(View.INVISIBLE);
            }
        });

        //Cargamos la animación de ocultación de botón de actualizar página
        showRestart = new ScaleAnimation(0f, 1, 0f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showRestart.setDuration(getResources().getInteger(R.integer.show_restart));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showRestart.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }
        showRestart.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showRestart.onAnimationStart");

                restart_btn.setClickable(true);
                restart_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showRestart.onAnimationEnd");

                restart_btn.startAnimation(heartBeatAutoplayEnd);
            }
        });

        //Cargamos la animación de ocultación de botón de actualizar página
        hideRestart = new ScaleAnimation(1, 0f, 1, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hideRestart.setDuration(getResources().getInteger(R.integer.hide_restart));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideRestart.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }
        hideRestart.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideRestart.onAnimationStart");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideRestart.onAnimationEnd");

                restart_btn.setClickable(false);
                restart_btn.setVisibility(View.INVISIBLE);
            }
        });

        //Cargamos la animación de ocultación de botón de like
        showLike = new AnimationSet(true);

        ScaleAnimation scaleLikeShow = new ScaleAnimation(0f, 1, 0f, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleLikeShow.setDuration(getResources().getInteger(R.integer.show_like));

        TranslateAnimation translateLikeShow = new TranslateAnimation(
                -getResources().getDimension(R.dimen.margin_like_btn), 0, 0, 0);
        translateLikeShow.setDuration(getResources().getInteger(R.integer.show_like));

        showLike.addAnimation(scaleLikeShow);
        showLike.addAnimation(translateLikeShow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showLike.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        showLike.setStartOffset(getResources().getInteger(R.integer.delay_like));
        showLike.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {


                like_btn.setClickable(true);
                like_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showLike.onAnimationEnd");

                like_btn.startAnimation(heartBeatLike);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos animación de latido para el botón de like
        heartBeatLike = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        heartBeatLike.setRepeatMode(Animation.REVERSE);
        heartBeatLike.setRepeatCount(Animation.INFINITE);
        heartBeatLike.setDuration(getResources().getInteger(R.integer.heartbeat_autoplay_like));
        //heartBeatLike.setStartOffset(getResources().getInteger(R.integer.heartbeat_autoplay_like));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            heartBeatLike.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        //Cargamos la animación de aparición del botón de actualizar página
        hideLike = new AnimationSet(true);

        ScaleAnimation scaleLikeHide = new ScaleAnimation(1, 0f, 1, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleLikeHide.setDuration(getResources().getInteger(R.integer.hide_like));

        TranslateAnimation translateLikeHide = new TranslateAnimation( 0,
                -getResources().getDimension(R.dimen.margin_like_btn), 0, 0);
        translateLikeHide.setDuration(getResources().getInteger(R.integer.hide_like));

        hideLike.addAnimation(scaleLikeHide);
        hideLike.addAnimation(translateLikeHide);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideLike.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        hideLike.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideLike.onAnimationEnd");

                like_btn.setClickable(false);
                like_btn.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos la animación de aparición del botón de actualizar página
        hideLikeForRestartBL = new AnimationSet(true);

        ScaleAnimation scaleLikeHideBL = new ScaleAnimation(1, 0.2f, 1, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleLikeHideBL.setDuration(getResources().getInteger(R.integer.show_hide_refresh));

        TranslateAnimation translateLikeHideBL = new TranslateAnimation( 0,
                -getResources().getDimension(R.dimen.margin_like_btn), 0, 0);
        translateLikeHideBL.setDuration(getResources().getInteger(R.integer.show_hide_refresh));

        hideLikeForRestartBL.addAnimation(scaleLikeHideBL);
        hideLikeForRestartBL.addAnimation(translateLikeHideBL);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideLikeForRestartBL.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        hideLikeForRestartBL.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideLikeForRestartBL.onAnimationEnd");

                restart_btn.startAnimation(preRestartTransition);

                like_btn.setClickable(false);
                like_btn.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos la animación de aparición de controles inferiores
        showBottomControls = new TranslateAnimation(0, 0,
                getResources().getDimension(R.dimen.hide_controls), 0);
        showBottomControls.setDuration(getResources().getInteger(R.integer.hide_controls));
        showBottomControls.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showBottomControls.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        showBottomControls.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showBottomControls.onAnimationStart");

                controls_bottom.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos la animación de ocultación de los controles inferiores
        hideBottomControls = new AnimationSet(true);

        TranslateAnimation hideBottomControlsTranslate =
                new TranslateAnimation(0, 0, 0, getResources().getDimension(R.dimen.hide_controls));
        hideBottomControlsTranslate.setDuration(getResources().getInteger(R.integer.hide_controls));
        hideBottomControlsTranslate.setFillAfter(true);

        ScaleAnimation waitToUnlock = new ScaleAnimation(1f, 1f, 1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //Tiene la duración del control que más tarda en esconderse
        waitToUnlock.setDuration(getResources().getInteger(R.integer.hide_num_page));

        hideBottomControls.addAnimation(hideBottomControlsTranslate);
        hideBottomControls.addAnimation(waitToUnlock);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideBottomControls.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        hideBottomControls.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideBottomControls.onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideBottomControls.onAnimationEnd");
                controls_bottom.setVisibility(View.INVISIBLE);
            }
        });

        //Cargamos la animación de aparición de controles inferiores
        showTopLeftControls = new TranslateAnimation(0, 0,
                -getResources().getDimension(R.dimen.hide_controls), 0);
        showTopLeftControls.setDuration(getResources().getInteger(R.integer.hide_controls));
        showTopLeftControls.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showTopLeftControls.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        showTopLeftControls.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showTopLeftControls.onAnimationStart");

                controls_top_left.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos la animación de ocultación de los controles inferiores
        hideTopLeftControls = new TranslateAnimation(0, 0, 0, -getResources().getDimension(R.dimen.hide_controls));
        hideTopLeftControls.setDuration(getResources().getInteger(R.integer.hide_controls));
        hideTopLeftControls.setFillAfter(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideTopLeftControls.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        hideTopLeftControls.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideTopLeftControls.onAnimationEnd");

                controls_top_left.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos la animación de aparición de controles inferiores
        showTopRightControls = new TranslateAnimation(0, 0,
                -getResources().getDimension(R.dimen.hide_controls), 0);
        showTopRightControls.setDuration(getResources().getInteger(R.integer.hide_controls));
        showTopRightControls.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showTopRightControls.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }
        showTopRightControls.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideTopRightControls.onAnimationStart");

                controls_top_right.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos la animación de ocultación de los controles inferiores
        hideTopRightControls = new TranslateAnimation(0, 0, 0, -getResources().getDimension(R.dimen.hide_controls));
        hideTopRightControls.setDuration(getResources().getInteger(R.integer.hide_controls));
        hideTopRightControls.setFillAfter(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideTopRightControls.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        hideTopRightControls.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideTopRightControls.onAnimationEnd");

                controls_top_right.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hideProgressBar = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hideProgressBar.setDuration(getResources().getInteger(R.integer.hide_progressbar));

        //Cargamos la animación del botón de play al llegar al final del autoplay
        heartBeatAutoplayEnd = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        heartBeatAutoplayEnd.setDuration(getResources().getInteger(R.integer.heartbeat_autoplay_end));
        heartBeatAutoplayEnd.setRepeatMode(Animation.REVERSE);
        heartBeatAutoplayEnd.setRepeatCount(Animation.INFINITE);
        heartBeatAutoplayEnd.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            heartBeatAutoplayEnd.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        //Cargamos la animación que precede a la transición del botón de like y pantalla de créditos
        hideLikeForLikeTransition = new AnimationSet(false);

        ScaleAnimation preHideLike = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        preHideLike.setDuration(getResources().getInteger(R.integer.pre_hide_standard));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            preHideLike.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        ScaleAnimation scaleHideLike = new ScaleAnimation(1.2f, 0, 1.2f, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleHideLike.setDuration(getResources().getInteger(R.integer.hide_standard));
        scaleHideLike.setStartOffset(getResources().getInteger(R.integer.pre_hide_standard));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            scaleHideLike.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        hideLikeForLikeTransition.addAnimation(preHideLike);
        hideLikeForLikeTransition.addAnimation(scaleHideLike);

        hideLikeForLikeTransition.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideLikeForLikeTransition.onAnimationEnd");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    revealLayout.setBackgroundColor(getResources().getColor(R.color.pink, null));
                } else {
                    revealLayout.setBackgroundColor(getResources().getColor(R.color.pink));
                }

                //Ocultamos el botón para que no se vea antes que el reveal
                like_btn.setClickable(false);
                like_btn.setVisibility(View.INVISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    likeReveal();
                }
                else {

                    startTransitionToLikeBL();
                }
            }
        });

        //Cargamos la animación que precede al reveal del restart
        preRestartTransition = new AnimationSet(false);
        ScaleAnimation preHideRevealRestart = new ScaleAnimation(1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        preHideRevealRestart.setDuration(getResources().getInteger(R.integer.pre_hide_standard));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            preHideRevealRestart.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        ScaleAnimation hideRevealRestart = new ScaleAnimation(1.2f, 0, 1.2f, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hideRevealRestart.setDuration(getResources().getInteger(R.integer.hide_reveal_play));
        hideRevealRestart.setStartOffset(getResources().getInteger(R.integer.pre_hide_standard));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            hideRevealRestart.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        preRestartTransition.addAnimation(preHideRevealRestart);
        preRestartTransition.addAnimation(hideRevealRestart);

        preRestartTransition.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - preRestartTransition.onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - preRestartTransition.onAnimationEnd");

                restart_btn.setVisibility(View.INVISIBLE);
                restart_btn.setClickable(false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    restartReveal();
                }
                else {

                    //Movemos a la 1a página.
                    mPager.toFirstPage();
                    mPager.startAnimation(pagerInTransition);
                }
            }
        });

        showPlayRestart = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showPlayRestart.setDuration(getResources().getInteger(R.integer.show_standard));

        showPlayRestartBL = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showPlayRestartBL.setDuration(getResources().getInteger(R.integer.show_standard));

        showPlayRestartBL.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showPlayRestartBL.onAnimationEnd");

                progressBar.setVisibility(View.VISIBLE);
                //Creamos música
                MusicManager.createMusic(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        pagerInTransition = new TranslateAnimation(
                getResources().getDimension(R.dimen.translate_logos), 0, 0, 0);
        pagerInTransition.setDuration(getResources().getInteger(R.integer.translate_hide_logo));
        pagerInTransition.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            pagerInTransition.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        pagerInTransition.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - pagerInTransition.onAnimationStart");

                //Creamos música
                MusicManager.createMusic(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - pagerInTransition.onAnimationEnd");

                unlockButtons();
                unlockTap();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        pagerInTransitionIntro = new TranslateAnimation(
                getResources().getDimension(R.dimen.translate_logos), 0, 0, 0);
        pagerInTransitionIntro.setDuration(getResources().getInteger(R.integer.translate_hide_logo));
        pagerInTransitionIntro.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            pagerInTransitionIntro.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        pagerOutTransitionExit = new TranslateAnimation(
                0, getResources().getDimension(R.dimen.translate_logos), 0, 0);
        pagerOutTransitionExit.setDuration(getResources().getInteger(R.integer.translate_hide_logo));
        pagerOutTransitionExit.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            pagerOutTransitionExit.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        pagerOutTransitionExit.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - pagerOutTransitionExit.onAnimationEnd");

                ReadActivityStandard.this.setResult(RESULT_OK);
                //Finalizamos la actividad
                ReadActivityStandard.this.finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        pagerOutTransition = new TranslateAnimation(
                0, getResources().getDimension(R.dimen.translate_logos), 0, 0);
        pagerOutTransition.setDuration(getResources().getInteger(R.integer.translate_hide_logo));
        pagerOutTransition.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            pagerOutTransition.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        pagerOutTransitionLike = new TranslateAnimation(
                0, getResources().getDimension(R.dimen.translate_logos), 0, 0);
        pagerOutTransitionLike.setDuration(getResources().getInteger(R.integer.translate_hide_logo));
        pagerOutTransitionLike.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            pagerOutTransitionLike .setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        pagerOutTransitionLike.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - pagerOutTransitionLike.onAnimationEnd");

                /*
                //Damos paso a la actividad
                Intent i = new Intent(mContext, LikeActivity.class);

                //Pasamos la info para identificar a la actividad Like que venimos de Read
                i.putExtra(Constants.LIKE_ACTIVITY_FROM, Constants.LIKE_ACTIVITY_FROM_READ);

                //Lo añadimos para que cuando volvamos a ReadActivityStandard sepamos que venimos de Like
                ReadActivityStandard.this.setResult(RESULT_CANCELED);
                startActivity(i);
                //Lo añadimos para quitar el blink, lo que hace es eliminar animaciones de transición.
                overridePendingTransition(0, 0);

                //Terminamos la actividad
                ReadActivityStandard.this.finish();
                */
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //Cargamos animación de latido para el botón de like
        heartBeatArrowRight = new ScaleAnimation(1f, 1.4f, 1f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        heartBeatArrowRight.setRepeatMode(Animation.REVERSE);
        heartBeatArrowRight.setRepeatCount(Animation.INFINITE);
        heartBeatArrowRight.setDuration(getResources().getInteger(R.integer.heart_beat_next_page));
    }

    private void prepareAjustesAnimations() {

        translatePlayAjustes = new TranslateAnimation(0, -getResources().getDimension(R.dimen.translate_ajustes),
                0, 0);
        translatePlayAjustes.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translatePlayAjustes.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translatePlayAjustes.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        translatePlayAjustes.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translatePlayAjustes");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                play_btn.startAnimation(animation);

                FrameLayout.LayoutParams paramsPlay = (FrameLayout.LayoutParams) play_btn.getLayoutParams();
                paramsPlay.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes)+
                        paramsPlay.getMarginEnd()));

                play_btn.setLayoutParams(paramsPlay);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translatePlayAjustesReverse = new TranslateAnimation(0, getResources().getDimension(R.dimen.translate_ajustes),
                0, 0);
        translatePlayAjustesReverse.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translatePlayAjustesReverse.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translatePlayAjustesReverse.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        translatePlayAjustesReverse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translatePlayAjustesReverse");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                play_btn.startAnimation(animation);

                FrameLayout.LayoutParams paramsPlay = (FrameLayout.LayoutParams) play_btn.getLayoutParams();
                paramsPlay.setMarginEnd((int) (getResources().getDimension(R.dimen.margin_play_tool_btn)));

                play_btn.setLayoutParams(paramsPlay);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        translateRefreshAjustes = new TranslateAnimation(0, -getResources().getDimension(R.dimen.translate_ajustes),
                0, 0);
        translateRefreshAjustes.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translateRefreshAjustes.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateRefreshAjustes.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        translateRefreshAjustes.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translateRefreshAjustes");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                refresh_btn.startAnimation(animation);

                FrameLayout.LayoutParams paramsRefresh = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
                paramsRefresh.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes)+
                        paramsRefresh.getMarginEnd()));

                refresh_btn.setLayoutParams(paramsRefresh);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateRefreshAjustesReverse = new TranslateAnimation(0, getResources().getDimension(R.dimen.translate_ajustes),
                0, 0);
        translateRefreshAjustesReverse.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translateRefreshAjustesReverse.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateRefreshAjustesReverse.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        translateRefreshAjustesReverse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translatePlayAjustesReverse");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                refresh_btn.startAnimation(animation);

                FrameLayout.LayoutParams paramsRefresh = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
                paramsRefresh.setMarginEnd((int) (paramsRefresh.getMarginEnd()) -
                        (int) (getResources().getDimension(R.dimen.translate_ajustes)));

                refresh_btn.setLayoutParams(paramsRefresh);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateProgressbarAjustes = new TranslateAnimation(0, -getResources().getDimension(R.dimen.translate_ajustes),
                0, 0);
        translateProgressbarAjustes.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translateProgressbarAjustes.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateProgressbarAjustes.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        translateProgressbarAjustes.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translateProgressbarAjustes");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                progressBar.startAnimation(animation);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
                params.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes)+
                        params.getMarginEnd()));

                progressBar.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateProgressbarAjustesReverse = new TranslateAnimation(0, getResources().getDimension(R.dimen.translate_ajustes),
                0, 0);
        translateProgressbarAjustesReverse.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translateProgressbarAjustesReverse.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateProgressbarAjustesReverse.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        translateProgressbarAjustesReverse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translateProgressbarAjustesReverse");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                progressBar.startAnimation(animation);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
                params.setMarginEnd((int) (getResources().getDimension(R.dimen.margin_tool_progress_bar)));

                progressBar.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateMusicAjustes = new TranslateAnimation(0, -getResources().getDimension(R.dimen.translate_ajustes_2),
                0, 0);
        translateMusicAjustes.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translateMusicAjustes.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateMusicAjustes.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        translateMusicAjustes.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationStart translateMusicAjustes");

                music_btn.setVisibility(View.VISIBLE);
                music_btn.setClickable(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    music_btn.setElevation(getResources().getDimension(R.dimen.button_higher_elevation));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translateMusicAjustes");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                music_btn.startAnimation(animation);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) music_btn.getLayoutParams();
                params.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes_2)+
                        params.getMarginEnd()));

                music_btn.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateMusicAjustesReverse = new TranslateAnimation(0, getResources().getDimension(R.dimen.translate_ajustes_2),
                0, 0);
        translateMusicAjustesReverse.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translateMusicAjustesReverse.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateMusicAjustesReverse.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        translateMusicAjustesReverse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationStart translateMusicAjustesReverse");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    music_btn.setElevation(0);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translateMusicAjustesReverse");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                music_btn.startAnimation(animation);

                music_btn.setVisibility(View.INVISIBLE);
                music_btn.setClickable(false);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) music_btn.getLayoutParams();
                params.setMarginEnd((int) (getResources().getDimension(R.dimen.margin_right_standard)));

                music_btn.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateVoiceAjustes = new TranslateAnimation(0, -getResources().getDimension(R.dimen.translate_ajustes),
                0, 0);
        translateVoiceAjustes.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translateVoiceAjustes.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateVoiceAjustes.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }
        translateVoiceAjustes.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationStart translateVoiceAjustes");

                voice_btn.setVisibility(View.VISIBLE);
                voice_btn.setClickable(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    voice_btn.setElevation(getResources().getDimension(R.dimen.button_higher_elevation));
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translateVoiceAjustes");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                voice_btn.startAnimation(animation);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) voice_btn.getLayoutParams();
                params.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes)+
                        params.getMarginEnd()));

                voice_btn.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateVoiceAjustesReverse = new TranslateAnimation(0, getResources().getDimension(R.dimen.translate_ajustes),
                0, 0);
        translateVoiceAjustesReverse.setDuration(getResources().getInteger(R.integer.show_ajustes));
        translateVoiceAjustesReverse.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            translateVoiceAjustesReverse.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }
        translateVoiceAjustesReverse.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationStart translateVoiceAjustesReverse");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    voice_btn.setElevation(0);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - onAnimationEnd translateVoiceAjustesReverse");

                //Fix absurdo. Evita flick en el botón al cambiar su posición con setLayoutParams dentro de onAnimationEnd
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 0.0f);
                animation.setDuration(1);
                voice_btn.startAnimation(animation);

                voice_btn.setVisibility(View.INVISIBLE);
                voice_btn.setClickable(false);

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) voice_btn.getLayoutParams();
                params.setMarginEnd((int) (getResources().getDimension(R.dimen.margin_right_standard)));

                voice_btn.setLayoutParams(params);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hideAjustes = new AnimationSet(false);

        RotateAnimation rotateHideAjustes = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            rotateHideAjustes.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        rotateHideAjustes.setDuration(getResources().getInteger(R.integer.change_icon_standard));

        ScaleAnimation scaleHideAjustes = new ScaleAnimation(1f, 0, 1f, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleHideAjustes.setDuration(getResources().getInteger(R.integer.change_icon_standard));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            scaleHideAjustes.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        hideAjustes.addAnimation(rotateHideAjustes);
        hideAjustes.addAnimation(scaleHideAjustes);

        hideAjustes.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideAjustes.onAnimationEnd");

                ajustes_btn.setClickable(false);
                ajustes_btn.setVisibility(View.INVISIBLE);
                ajustes_close_btn.startAnimation(showAjustesClose);
            }
        });

        hideAjustesClose = new AnimationSet(false);

        RotateAnimation rotateHideAjustesClose = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            rotateHideAjustesClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        rotateHideAjustesClose.setDuration(getResources().getInteger(R.integer.change_icon_standard));

        ScaleAnimation scaleHideAjustesClose = new ScaleAnimation(1f, 0, 1f, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleHideAjustesClose.setDuration(getResources().getInteger(R.integer.change_icon_standard));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            scaleHideAjustesClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }

        hideAjustesClose.addAnimation(rotateHideAjustesClose);
        hideAjustesClose.addAnimation(scaleHideAjustesClose);

        hideAjustesClose.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - hideAjustesClose.onAnimationEnd");

                ajustes_close_btn.setClickable(false);
                ajustes_close_btn.setVisibility(View.INVISIBLE);
                ajustes_btn.startAnimation(showAjustes);
            }
        });

        showAjustes =  new AnimationSet(true);

        ScaleAnimation scaleshowAjustes = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleshowAjustes.setDuration(getResources().getInteger(R.integer.change_icon_standard));

        RotateAnimation rotateshowAjustes = new RotateAnimation(180, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateshowAjustes.setDuration(getResources().getInteger(R.integer.change_icon_standard));

        showAjustes.addAnimation(scaleshowAjustes);
        showAjustes.addAnimation(rotateshowAjustes);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showAjustes.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        showAjustes.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showAjustes.onAnimationStart");

                ajustes_btn.setVisibility(View.VISIBLE);
                ajustes_btn.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showAjustes.onAnimationEnd");
            }
        });

        showAjustesClose =  new AnimationSet(true);

        ScaleAnimation scaleshowAjustesClose = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleshowAjustesClose.setDuration(getResources().getInteger(R.integer.change_icon_standard));

        RotateAnimation rotateshowAjustesClose = new RotateAnimation(180, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateshowAjustesClose.setDuration(getResources().getInteger(R.integer.change_icon_standard));

        showAjustesClose.addAnimation(scaleshowAjustesClose);
        showAjustesClose.addAnimation(rotateshowAjustesClose);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            showAjustesClose.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        }

        showAjustesClose.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showAjustesClose.onAnimationStart");

                ajustes_close_btn.setVisibility(View.VISIBLE);
                ajustes_close_btn.setClickable(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - showAjustesClose.onAnimationEnd");
            }
        });
    }

    /**
     * Avanza una página
     */
    private void pageForward() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - pageForward");

        //Bloqueamos el botón de play y el tap para que no se interrumpa la animación de reinicio de autoplay.
        lockButtons();
        lockTap();

        arrow_left_btn.clearAnimation();
        arrow_right_btn.clearAnimation();

        if (!finCuento) {

            //Pausamos el autoplay pero dejamos la música
            pausaAutoplayControles(true);

            if (finPagina) {

                ocultarControles(true);
                nextPagina = true;

                //Resaltamos el color de arrow right
                TransitionDrawable transition;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    transition =
                            (TransitionDrawable) getResources().getDrawable(R.drawable.transition_finpag_icon_on_off, null);
                } else {

                    transition =
                            (TransitionDrawable) getResources().getDrawable(R.drawable.transition_finpag_icon_on_off);
                }

                arrow_right_btn.setBackground(transition);
                transition.startTransition(getResources().getInteger(R.integer.tool_btn_color_transition));
            }
            else {

                //Escondemos el botón refresh si estuviera visible
                ((ReadActivityStandard) mContext).hideRefreshChangePage();
            }
            timerLleno = true;
        }

        mPager.setSwipeManually(false);
        mPager.setCurrentItem(mPager.getCurrentItem()+1, true);
    }

    /**
     * Retrocede una página
     */
    private void pageBack() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - pageBack");

        //Bloqueamos el botón de play y el tap
        lockButtons();
        lockTap();

        arrow_left_btn.clearAnimation();
        arrow_right_btn.clearAnimation();

        //Desbloqueo en SCROLL_STATE_IDLE
        if (!finCuento) {

            //Pausamos el autoplay pero dejamos la música
            pausaAutoplayControles(true);

            if (finPagina) {

                refresh_btn.startAnimation(hideRefreshToRestorePlay);
                finPagina = false;
            }
            else {

                //Escondemos el botón refresh si estuviera visible
                ((ReadActivityStandard) mContext).hideRefreshChangePage();
            }
            timerLleno = true;
        }

        mPager.setSwipeManually(false);
        mPager.setCurrentItem(mPager.getCurrentItem()-1, true);
    }

    private void prepareMusicAndVoice() {

        //Restauramos valor por defecto para que se pare la música en onStop
        continue_music = false;
        //Preparamos la voz para la primera página
        MusicManager.createVoice(0);
        //Recordamos el estado del volumen
        if (MusicManager.wasReadVoiceOn()) {

            MusicManager.setVoiceVolumeOn();
            MusicManager.setVoice_on(true);
            setVoiceIconOn();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                voice_btn.setBackground(getResources().getDrawable(R.drawable.ripple_btn_deeppurple, null));
            }
            else {

                voice_btn.setBackground(getResources().getDrawable(R.drawable.transition_deeppurple_icon_on_off));
            }
        } else {

            MusicManager.setVoiceVolumeOff();
            MusicManager.setVoice_on(false);
            setVoiceIconOff();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                voice_btn.setBackground(getResources().getDrawable(R.drawable.ripple_purple_btn_disabled, null));
            }
            else{

                voice_btn.setBackground(getResources().getDrawable(R.drawable.transition_deeppurple_icon_off_on));
            }
        }
    }

    /**
     * Pausa el autoplay al tocar los controles
     */
    private void pausaAutoplayControles(boolean cambioPagina) {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - pausaAutoplayControles");

        if (!paused) {

            //Cancelamos tarea de desvanecimiento
            cancelDesvanecimientoControles();

            mPager.pauseTimer(cambioPagina);
            //Pausamos la música
            MusicManager.pauseMusic();

            paused = true;

            //Animación click
            play_btn.startAnimation(clickPlayForPause);
            progressBar.startAnimation(clickProgressBar);
            AnimationDrawable frameAnimation;
            //Animación transformación play/pause
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                frameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_pause_play_standard, null);
            }
            else {

                frameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_pause_play_standard);
            }
            play_btn.setImageDrawable(frameAnimation);
            frameAnimation.start();
        }
    }

    /**
     * Pone en marcha las animaciones al hacer click en el botón de play
     */
    private void startClickPlayAnimations() {

        progressBar.startAnimation(clickProgressBar);
        //Animación transformación play/pause
        //Icono Pause - > Icono Play
        //Play - > Pause
        if (!paused) {

            paused = true;
            play_btn.startAnimation(clickPlayForPause);

            AnimationDrawable frameAnimation;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                frameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_pause_play_standard, null);
            }
            else {

                frameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_pause_play_standard);
            }
            play_btn.setImageDrawable(frameAnimation);

            frameAnimation.start();
        }
        //Icono Play - > Icono Pause
        //Pause - > Play
        else {

            paused = false;
            play_btn.startAnimation(clickPlay);

            AnimationDrawable frameAnimation;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                frameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_play_pause_standard, null);
            }
            else {
                frameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_play_pause_standard);
            }

            play_btn.setImageDrawable(frameAnimation);

            frameAnimation.start();
            if (refresh_btn.getVisibility() == View.VISIBLE) refresh_btn.startAnimation(hideRefresh);
        }
    }

    /**
     * Esconde el botón refresh en un paso de página.
     */
    public void hideRefreshChangePage(){

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - hideRefreshChangePage");

        if (refresh_btn.getVisibility() == View.VISIBLE)  {

            refresh_btn.startAnimation(hideRefresh);
        }

        //timerLleno = true;
    }

    /**
     * Muestra el botón de refresh
     */
    public void showRefreshMethod() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - showRefreshMethod");

        refresh_btn.startAnimation(showRefresh);
        timerLleno = false;
    }

    /**
     * Pone en marcha animación Reveal Effect en reverso. Utilizada al entrar en la actividad,
     * después de la primera parte realizada en MainActivity.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startUnvealRead() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - startUnvealRead");

        //Calculamos el centro de la animación
        int cx = (play_btn.getLeft() + play_btn.getRight()) / 2;
        int cy = (play_btn.getTop() + play_btn.getBottom()) / 2;

        //Calculamos el radio de la animación
        //int initialRadius = Math.max(frameLayout.getWidth(), frameLayout.getHeight());
        int initialRadius = (int) Math.sqrt( Math.pow(revealLayout.getWidth(), 2) +
                Math.pow(revealLayout.getHeight(), 2));

        //Creamos la animación
        Animator anim =
                ViewAnimationUtils.createCircularReveal(revealLayout,
                        cx, cy, initialRadius,
                        getResources().getDimension(R.dimen.diameter_logo_btns) / 2);
        anim.setDuration(getResources().getInteger(R.integer.unveal_long));

        //Hacemos visible la vista y empezamos la animación
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityStandard - startUnvealRead.onAnimationStart");

                revealLayout.setVisibility(View.VISIBLE);
                //Mostramos las paginas de cuento
                mPager.setVisibility(View.VISIBLE);
                //play_btn.startAnimation(animPlayButtonScale);
                //play_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityStandard - startUnvealRead.onAnimationEnd");

                revealLayout.setVisibility(View.INVISIBLE);
                play_btn.setElevation(getResources().getDimension(R.dimen.button_higher_elevation));
                progressBar.setVisibility(View.VISIBLE);

                //Creamos la música aquí para dar un poco más de tiempo al fadeMusic
                MusicManager.createMusic(false);
                //Recordamos el estado del volumen
                if (MusicManager.wasReadMusicOn()) {

                    MusicManager.setMusicVolumeOn();
                    MusicManager.setMusic_on(true);
                    setMusicIconOn();
                    music_btn.setBackground(getResources().getDrawable(R.drawable.ripple_purple_btn, null));
                } else {

                    MusicManager.setMusicVolumeOff();
                    MusicManager.setMusic_on(false);
                    setMusicIconOff();
                    music_btn.setBackground(getResources().getDrawable(R.drawable.ripple_purple_btn_disabled, null));
                }

                unlockButtons();
                unlockTap();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        anim.setStartDelay(getResources().getInteger(R.integer.delay_unveal_read));
        anim.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        anim.start();
    }

    private void startShowLayoutBL() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - startShowLayoutBL");

        progressBar.setVisibility(View.VISIBLE);
        //Mostramos las paginas de cuento
        mPager.setVisibility(View.VISIBLE);
        mPager.startAnimation(pagerInTransitionIntro);
        //Programamos desbloqueo
        programUnlock(pagerInTransitionIntro.getDuration());
        //Mostramos controles
        mostrarControles(true);

        //Creamos la música aquí para dar un poco más de tiempo al fadeMusic
        MusicManager.createMusic(false);
        //Recordamos el estado del volumen
        if (MusicManager.wasReadMusicOn()) {

            MusicManager.setMusicVolumeOn();
            MusicManager.setMusic_on(true);
            setMusicIconOn();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                music_btn.setBackground(getResources().getDrawable(R.drawable.ripple_purple_btn, null));
            }
            else {
                music_btn.setBackground(getResources().getDrawable(R.drawable.transition_deeppurple_icon_on_off));
            }
        } else {

            MusicManager.setMusicVolumeOff();
            MusicManager.setMusic_on(false);
            setMusicIconOff();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                music_btn.setBackground(getResources().getDrawable(R.drawable.ripple_purple_btn_disabled, null));
            }
            else {
                music_btn.setBackground(getResources().getDrawable(R.drawable.transition_deeppurple_icon_off_on));
            }
        }
    }

    /**
     * Pone en marcha animación Reveal Effect al pulsar el botón de cerrar.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startRevealReadReturn() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - startRevealReadReturn");

        //Calculamos el centro de la animación
        int cx = (close_btn.getLeft() + close_btn.getRight()) / 2;
        int cy = (close_btn.getTop() + close_btn.getBottom()) / 2;

        //Calculamos el radio de la animación
        int finalRadius = (int) Math.sqrt(Math.pow(revealLayout.getWidth(), 2) +
                Math.pow(revealLayout.getHeight(), 2));

        //Creamos la animación
        Animator anim =
                ViewAnimationUtils.createCircularReveal(revealLayout,
                        cx, cy, 0, finalRadius);
        anim.setDuration(getResources().getInteger(R.integer.reveal_read_return));

        //Hacemos visible la vista y empezamos la animación
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityStandard - startRevealReadReturn.onAnimationStart");

                //Hacemos visible el reveal y le cambiamos el color
                revealLayout.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    revealLayout.setBackgroundColor(getResources().getColor(R.color.pink, null));
                } else {
                    revealLayout.setBackgroundColor(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityStandard - startRevealReadReturn.onAnimationEnd");

                //Indicamos que no se debe interrumpir la música en onStop
                continue_music = true;
                //Guardamos si la música está activa o no
                MusicManager.setWasReadMusicOn(MusicManager.isMusic_on());
                MusicManager.setWasReadVoiceOn(MusicManager.isVoice_on());

                ReadActivityStandard.this.setResult(RESULT_OK);
                //Finalizamos la actividad
                ReadActivityStandard.this.finish();
                overridePendingTransition(0, 0);
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
     * Esconde controles superiores y inferiores - boton play y progressBar
     */
    public void startExitBL() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - startExitBL");

        //Indicamos que no se debe interrumpir la música en onStop
        continue_music = true;
        //Guardamos si la música está activa o no
        MusicManager.setWasReadMusicOn(MusicManager.isMusic_on());
        MusicManager.setWasReadVoiceOn(MusicManager.isVoice_on());

        mPager.startAnimation(pagerOutTransitionExit);

        //Ocultamos los controles superiores e inferiores
        if (areControlsVisible) {

            //Bloqueamos el botón de play y el tap
            lockButtons();
            lockTap();

            controls_top_left.startAnimation(hideTopLeftControls);
            controls_top_right.startAnimation(hideTopRightControls);
            controls_bottom.startAnimation(hideBottomControls);

            /** Highlight button deactivated
             if (highlightButtonsVisible) {

             hideHighlightIcon.cancel();
             showHighlightClose.cancel();
             highlight_btn_close.clearAnimation();
             highlight_btn_close.startAnimation(hideHighlightClose);
             highlight_btn_1.startAnimation(hideHighlight_1_long);
             highlight_btn_2.startAnimation(hideHighlight_2_long);
             }
             else {

             highlight_btn.startAnimation(hideHighlight);
             }
             highlight_backgr_btn.startAnimation(hideHighlightBackgr);
             */
            //actionBar.hide();
            //Aquí no sacamos fuera de las animaciones el setVisibility porque se adelanta a la animacion hideCenterControls

            areControlsVisible = false;
        }
    }

    /**
     * Pone en marcha animación Reveal Effect al reiniciar el autoplay una vez terminado
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void restartReveal() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - restartReveal");

        //Calculamos el centro de la animación
        int cx = (revealLayout.getLeft() + revealLayout.getRight()) / 2;
        int cy = (revealLayout.getTop() + revealLayout.getBottom()) / 2;

        //Calculamos el radio de la animación
        int finalRadius = (int) Math.sqrt( Math.pow(revealLayout.getWidth()/2, 2) +
                Math.pow(revealLayout.getHeight()/2, 2));

        //Creamos la animación
        Animator anim =
                ViewAnimationUtils.createCircularReveal(revealLayout,
                        cx, cy, 0, finalRadius);
        anim.setDuration(getResources().getInteger(R.integer.reveal_standard));

        //Hacemos visible la vista y empezamos la animación
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityStandard - restartReveal.onAnimationStart");

                revealLayout.setVisibility(View.VISIBLE);
                //Cambiamos el tamaño del botón de play para que no haya un cambio brusco después de la animación de transformación del icono.
                //play_btn.setScaleX((float) 1.2);
                //play_btn.setScaleY((float) 1.2);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityStandard - restartReveal.onAnimationEnd");

                //Ponemos en marcha la segunda parte de la animación
                restartUnveal();
                //Movemos a la 1a página.
                mPager.toFirstPage();
                //Escondemos los botones de restart y like
                like_btn.setClickable(false);
                like_btn.setVisibility(View.INVISIBLE);
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
     * Pone en marcha la 2a parte de la animación Reveal Effect al reiniciar el autoplay una vez terminado
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void restartUnveal() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - restartUnveal");

        //Calculamos el centro de la animación
        int cx = (play_btn.getLeft() + play_btn.getRight()) / 2;
        int cy = (play_btn.getTop() + play_btn.getBottom()) / 2;

        //Calculamos el radio de la animación
        //int initialRadius = Math.max(frameLayout.getWidth(), frameLayout.getHeight());
        int initialRadius = (int) Math.sqrt( Math.pow(revealLayout.getWidth(), 2) +
                Math.pow(revealLayout.getHeight(), 2));

        //Creamos la animación
        Animator anim =
                ViewAnimationUtils.createCircularReveal(revealLayout,
                        cx, cy, initialRadius,
                        getResources().getDimension(R.dimen.diameter_logo_btns) / 2);
        anim.setDuration(getResources().getInteger(R.integer.unveal_standard));

        //Hacemos visible la vista y empezamos la animación
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

                Log.v(Constants.Log.METHOD, "ReadActivityStandard - restartUnveal.onAnimationStart");

                //Devolvemos al botón de play su tamaño original y ponemos en marcha un animación de scale a su tamaño.
                /*
                play_btn.setScaleX((float) 1);
                play_btn.setScaleY((float) 1);
                play_btn.startAnimation(autoplayEndReturn);
                */
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                Log.v(Constants.Log.METHOD, "ReadActivityStandard - restartUnveal.onAnimationEnd");

                //Ocultamos la capa revealEffect
                revealLayout.setVisibility(View.INVISIBLE);
                //Creamos música
                MusicManager.createMusic(false);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        anim.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        anim.setStartDelay(getResources().getInteger(R.integer.delay_restart_reveal));
        anim.start();
    }

    /**
     * Pone en marcha animación Reveal Effect al reiniciar el autoplay una vez terminado
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void likeReveal() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - likeReveal");

        //Calculamos el centro de la animación
        int cx = (like_btn.getLeft() + like_btn.getRight()) / 2;
        int cy = (like_btn.getTop() + like_btn.getBottom()) / 2;

        //Calculamos el radio de la animación
        int finalRadius = (int) Math.sqrt( Math.pow(revealLayout.getWidth()/2, 2) +
                Math.pow(revealLayout.getHeight()/2, 2));

        //Creamos la animación
        Animator anim =
                ViewAnimationUtils.createCircularReveal(revealLayout,
                        cx, cy, 0, finalRadius);
        anim.setDuration(getResources().getInteger(R.integer.reveal_standard));

        //Hacemos visible la vista y empezamos la animación
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityStandard - likeReveal.onAnimationStart");

                revealLayout.setVisibility(View.VISIBLE);
                //Cambiamos el tamaño del botón de play para que no haya un cambio brusco después de la animación de transformación del icono.
                //play_btn.setScaleX((float) 1.2);
                //play_btn.setScaleY((float) 1.2);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityStandard - likeReveal.onAnimationEnd");

                /*
                //Indicamos que no se debe interrumpir la música en onStop
                continue_music = true;
                //Guardamos si la música está activa o no
                MusicManager.setWasReadMusicOn(MusicManager.isMusic_on());
                MusicManager.setWasReadVoiceOn(MusicManager.isVoice_on());

                Intent i = new Intent(mContext, LikeActivity.class);

                //Pasamos la info para identificar a la actividad Like que venimos de Read
                i.putExtra(Constants.LIKE_ACTIVITY_FROM, Constants.LIKE_ACTIVITY_FROM_READ);

                //Lo añadimos para que cuando volvamos a MainActivity sepamos que venimos de Like
                ReadActivityStandard.this.setResult(RESULT_CANCELED);
                startActivity(i);
                //Lo añadimos para quitar el blink, lo que hace es eliminar animaciones de transición.
                overridePendingTransition(0, 0);

                //Terminamos la actividad
                ReadActivityStandard.this.finish();
                */
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
     * Transición a Like antes de Lollipop
     */
    private void startTransitionToLikeBL(){

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - startTransitionToLike");

        //Indicamos que no se debe interrumpir la música en onStop
        continue_music = true;
        //Guardamos si la música está activa o no
        MusicManager.setWasReadMusicOn(MusicManager.isMusic_on());
        MusicManager.setWasReadVoiceOn(MusicManager.isVoice_on());

        //Escondemos controles superiores e inferiores
        mPager.startAnimation(pagerOutTransitionLike);

        restart_btn.clearAnimation();
        restart_btn.startAnimation(hideRestart);
        controls_top_left.startAnimation(hideTopLeftControls);
        controls_top_right.startAnimation(hideTopRightControls);
        controls_bottom.startAnimation(hideBottomControls);

        /** Highlight button deactivated
         if (highlightButtonsVisible) {

         hideHighlightIcon.cancel();
         showHighlightClose.cancel();
         highlight_btn_close.clearAnimation();
         highlight_btn_close.startAnimation(hideHighlightClose);
         highlight_btn_1.startAnimation(hideHighlight_1_long);
         highlight_btn_2.startAnimation(hideHighlight_2_long);
         }
         else {

         highlight_btn.startAnimation(hideHighlight);
         }
         highlight_backgr_btn.startAnimation(hideHighlightBackgr);
         */
        //if (refresh_btn.getVisibility() == View.VISIBLE) refresh_btn.startAnimation(hideRefresh);
    }

    /**
     * Hace visibles todos los controles. Si end es true obviamos el bloqueo
     */
    public void mostrarControles(boolean force) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - mostrarControles");

        if (!areButtonsLocked() || force) {

            //Mostramos controles
            if (!areControlsVisible) {

                //Bloqueamos el botón de play y el tap para que no se interrumpa la animación de reinicio de autoplay.
                lockButtons();
                lockTap();

                if (finPagina) {

                    play_btn.setVisibility(View.INVISIBLE);
                    play_btn.setClickable(false);
                    progressBar.setVisibility(View.INVISIBLE);
                    refresh_btn.setVisibility(View.VISIBLE);
                    refresh_btn.setClickable(true);

                    //Colocamos el botón de refresh encima del de play según en función de los ajustes
                    if (areAjustesVisible) {

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
                        params.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes)+
                                (int) getResources().getDimension(R.dimen.margin_play_tool_btn)));
                        refresh_btn.setLayoutParams(params);
                    }
                    else {

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
                        params.setMarginEnd( (int) getResources().getDimension(R.dimen.margin_play_tool_btn));
                        refresh_btn.setLayoutParams(params);
                    }

                    if (!finCuento) {

                        //Resaltamos el color de arrow right
                        TransitionDrawable transition;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_finpag_icon_off_on, null);
                        } else {

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_finpag_icon_off_on);
                        }

                        arrow_right_btn.setBackground(transition);
                        transition.startTransition(getResources().getInteger(R.integer.tool_btn_color_transition));

                        arrow_right_btn.startAnimation(heartBeatArrowRight);
                    }
                }
                //Si el timer no estuviera lleno mostramos refresh
                else if ((refresh_btn.getVisibility() == View.INVISIBLE) && !timerLleno && paused) {

                    refresh_btn.startAnimation(showRefresh);
                }

                controls_top_left.startAnimation(showTopLeftControls);
                controls_top_right.startAnimation(showTopRightControls);
                controls_bottom.startAnimation(showBottomControls);

                //Programamos desbloqueo
                programUnlock(showBottomControls.getDuration());

                //Sacar fuera de las animaciones el setVisibility, a veces no se ejecutan al ser simultáneas 2 animaciones sobre la misma view.
                setClickableButtonsOn();

                areControlsVisible = true;
            }
            else {

                if (finPagina) {

                    //Bloqueamos el botón de play y el tap para que no se interrumpa la animación de reinicio de autoplay.
                    lockButtons();
                    lockTap();

                    //Colocamos el botón de refresh encima del de play según en función de los ajustes
                    if (areAjustesVisible) {

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
                        params.setMarginEnd((int) (getResources().getDimension(R.dimen.translate_ajustes)+
                                (int) getResources().getDimension(R.dimen.margin_play_tool_btn)));
                        refresh_btn.setLayoutParams(params);
                    }
                    else {

                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
                        params.setMarginEnd( (int) getResources().getDimension(R.dimen.margin_play_tool_btn));
                        refresh_btn.setLayoutParams(params);
                    }

                    //En onResume no es necesario hacer la animación
                    if (play_btn.getVisibility() == View.VISIBLE) {

                        //Escondemos el botón de play y mostramos el de refresh
                        progressBar.setVisibility(View.INVISIBLE);
                        play_btn.startAnimation(hidePlayFinPagina);
                    }

                    //Programamos desbloqueo
                    programUnlock(hidePlayFinPagina.getDuration() + showRefreshFinPagina.getDuration());

                    if (!finCuento) {

                        //Retornamos arrow right a su color original
                        TransitionDrawable transition;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_finpag_icon_off_on, null);
                        } else {

                            transition =
                                    (TransitionDrawable) getResources().getDrawable(R.drawable.transition_finpag_icon_off_on);
                        }

                        arrow_right_btn.setBackground(transition);
                        transition.startTransition(getResources().getInteger(R.integer.tool_btn_color_transition));

                        arrow_right_btn.startAnimation(heartBeatArrowRight);
                    }
                    //refresh_btn.startAnimation(showRefreshAuto);
                }
            }
        }
    }

    /**
     * Oculta los controles
     */
    public void ocultarControles(boolean force) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - ocultarControles - force = "+force);

        if (!areButtonsLocked() || force) {

            //Si hemos llegado al final del autoplay no los ocultamos
            if (!finCuento) {

                //Ocultamos los controles
                if (areControlsVisible) {

                    //Bloqueamos el botón de play y el tap
                    lockButtons();
                    lockTap();

                    controls_bottom.startAnimation(hideBottomControls);
                    controls_top_left.startAnimation(hideTopLeftControls);
                    controls_top_right.startAnimation(hideTopRightControls);

                    //Programamos desbloqueo
                    programUnlock(hideBottomControls.getDuration());

                    setClickableButtonsOff();

                    /** Highlight button deactivated
                     if (highlightButtonsVisible) {

                     hideHighlightIcon.cancel();
                     showHighlightClose.cancel();
                     highlight_btn_close.clearAnimation();
                     highlight_btn_close.startAnimation(hideHighlightClose);
                     highlight_btn_1.startAnimation(hideHighlight_1_long);
                     highlight_btn_2.startAnimation(hideHighlight_2_long);
                     }
                     else {

                     highlight_btn.startAnimation(hideHighlight);
                     }
                     highlight_backgr_btn.startAnimation(hideHighlightBackgr);
                     */
                    //if (refresh_btn.getVisibility() == View.VISIBLE && !finPagina) refresh_btn.startAnimation(hideRefresh);
                    //actionBar.hide();
                    //Aquí no sacamos fuera de las animaciones el setVisibility porque se adelanta a la animacion hideCenterControls

                    areControlsVisible = false;
                }
            }
        }
    }

    /**
     * Planifica el desvanecimiento de los controles.
     * 2 modos. Rápido si hemos pulsado el botón de play, lento en otro caso.
     */
    private void planificarDesvanecimientoControles(boolean quick){

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - planificarDesvanecimientoControles");

        //Cancelamos el temporizador y la tarea si ya existieran
        cancelDesvanecimientoControles();

        //Inicializamos temporizadores para el desvanecimiento de los controles
        fadeOutTimer = new Timer();
        fadeOutTimerTask = new TimerTask() {

            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - fadeOutTimerTask");
                        ocultarControles(true);
                    }
                });
            }
        };
        if (quick)
        {
            fadeOutTimer.schedule(fadeOutTimerTask, Constants.Autoplay.FADE_OUT_CONTROLS_QUICK);
        }
        else {

            fadeOutTimer.schedule(fadeOutTimerTask, Constants.Autoplay.FADE_OUT_CONTROLS);
        }
    }

    /**
     * Cancelamos el temporizador y la tarea de planificacion del desvanecimiento de los controles.
     */
    private void cancelDesvanecimientoControles() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - cancelDesvanecimientoControles");

        //Cancelamos el temporizador y la tarea si existen
        if (fadeOutTimer != null) fadeOutTimer.cancel();
        if (fadeOutTimerTask != null) fadeOutTimerTask.cancel();
    }

    /**
     * Muestra los controles al terminar el autoplay de una página
     */
    public void finPagina() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - finPagina");

        //Cancelamos tarea de desvanecimiento
        cancelDesvanecimientoControles();

        paused = true;
        finPagina = true;
        mPager.pauseTimer(true);

        mostrarControles(true);
    }

    /**
     * Muestra los controles al terminar el autoplay de una página
     */
    public void comienzoPagina() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - comienzoPagina");

        paused = false;
        finPagina = false;
        nextPagina = false;
        //timerLleno = true;

        //Volvemos a colocar el botón refresh en su posición original
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) refresh_btn.getLayoutParams();
        if (areAjustesVisible) {

            params.setMarginEnd( (int) (getResources().getDimension(R.dimen.margin_refresh_tool_btn) +
                    getResources().getDimension(R.dimen.translate_ajustes)));
        }
        else {

            params.setMarginEnd( (int) getResources().getDimension(R.dimen.margin_refresh_tool_btn));
        }
        refresh_btn.setLayoutParams(params);

        refresh_btn.setClickable(false);
        refresh_btn.setVisibility(View.INVISIBLE);
        play_btn.setClickable(true);
        play_btn.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        mPager.startTimer();
    }

    /**
     * Muestra el botón con el icono de refresh para reiniciar el autoplay.
     */
    public void finAutoplay() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - finCuento");
        //Paramos el autoplay y marcamos el flag de que hemos llegado al final de él.
        paused = true;
        finCuento = true;
        //También es fin página
        finPagina = true;

        //Cancelamos desvanecimiento
        cancelDesvanecimientoControles();
        //Pausamos autoplay
        mPager.pauseTimer(true);
        //Mostramos controles
        mostrarControles(true);

        //Bloqueamos
        lockButtons();
        lockTap();

        //animación que muestra el icono de reinicio en el botón de play
        restart_btn.startAnimation(showRestart);
        like_btn.startAnimation(showLike);

        //Programamos desbloqueo para cuando se haya mostrado refresh y like
        programUnlock(showRestart.getDuration());
    }

    /**
     * Realizamos animación del botón play y volvemos al menú principal
     */
    private void exit() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityStandard - exit");

        //Bloqueamos el botón para que no se ponga en marcha el autoplay durante la animación inicial
        lockButtons();
        //Bloqueamos el tap para que no se escondan los controles ni se pase de pag. durante la animación
        lockTap();

        cancelDesvanecimientoControles();

        if (!paused) {

            mPager.pauseTimer(true);
            paused = true;

            AnimationDrawable frameAnimation;

            //animación icono pause -> icono play
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                frameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_pause_play_standard, null);
            }
            else{
                frameAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.frame_anim_pause_play_standard);
            }

            play_btn.setImageDrawable(frameAnimation);
            frameAnimation.start();
        }

        if (!areControlsVisible()) {

            play_btn.setVisibility(View.VISIBLE);
        }

        //Quitamos música
        MusicManager.fadeMusic();

        //play_btn.startAnimation(animPlayButtonExit);
        //Sin reveal ocultamos los controles superiores e inferiores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            close_btn.startAnimation(hideCloseForReveal);
        }
        else {

            startExitBL();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Indica si están visibles o no los controles
     * @return
     */
    public boolean areControlsVisible() {

        return areControlsVisible;
    }

    /**
     * Activa la función click de los botones
     */
    private void setClickableButtonsOn() {

        play_btn.setClickable(true);
        ajustes_btn.setClickable(true);
        ajustes_close_btn.setClickable(true);
        refresh_btn.setClickable(true);
        arrow_left_btn.setClickable(true);
        arrow_right_btn.setClickable(true);
        close_btn.setClickable(true);
        music_btn.setClickable(true);
        voice_btn.setClickable(true);
    }

    /**
     * Activa la función click de los botones
     */
    private void setClickableButtonsOff() {

        play_btn.setClickable(false);
        ajustes_btn.setClickable(false);
        ajustes_close_btn.setClickable(false);
        refresh_btn.setClickable(false);
        arrow_left_btn.setClickable(false);
        arrow_right_btn.setClickable(false);
        close_btn.setClickable(false);
        music_btn.setClickable(false);
        voice_btn.setClickable(false);
    }

    /**
     * Pone el icono de pause el botón de play/pause según densidad de pixel.
     */
    private void setButtonIconToPause() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - setButtonIconToPause");

        final int mask = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (mask ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            play_btn.setImageResource(R.drawable.ic_pause_white_18dp);
        }
        else  if (mask ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            play_btn.setImageResource(R.drawable.ic_pause_white_24dp);
        }
        else  if (mask ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            play_btn.setImageResource(R.drawable.ic_pause_white_24dp);
        }
        else {
            play_btn.setImageResource(R.drawable.ic_pause_white_18dp);
        }
    }

    /**
     * Pone el icono de play el botón de play/pause según densidad de pixel.
     */
    private void setButtonIconToPlay() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - setButtonIconToPlay");

        final int mask = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (mask ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            play_btn.setImageResource(R.drawable.ic_play_white_18dp);
        }
        else  if (mask ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            play_btn.setImageResource(R.drawable.ic_play_white_24dp);
        }
        else  if (mask ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            play_btn.setImageResource(R.drawable.ic_play_white_24dp);
        }
        else {
            play_btn.setImageResource(R.drawable.ic_play_white_18dp);
        }
    }

    /**
     * Pone el icono de voz de la barra de controles según densidad de pixel.
     */
    private void setVoiceIconOn() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - setVoiceIconOn");

        final int mask = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (mask ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            voice_btn.setImageResource(R.drawable.ic_volume_high_white_18dp);
        }
        else if (mask ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            voice_btn.setImageResource(R.drawable.ic_volume_high_white_24dp);
        }
        else if (mask ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            voice_btn.setImageResource(R.drawable.ic_volume_high_white_24dp);
        }
        else {
            voice_btn.setImageResource(R.drawable.ic_volume_high_white_18dp);
        }
    }

    /**
     * Pone el icono de voz de la barra de controles según densidad de pixel.
     */
    private void setVoiceIconOff() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - setVoiceIconOff");

        final int mask = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (mask ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            voice_btn.setImageResource(R.drawable.ic_volume_off_white_18dp);
        }
        else  if (mask ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            voice_btn.setImageResource(R.drawable.ic_volume_off_white_24dp);
        }
        else  if (mask ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            voice_btn.setImageResource(R.drawable.ic_volume_off_white_24dp);
        }
        else {
            voice_btn.setImageResource(R.drawable.ic_volume_off_white_18dp);
        }
    }

    /**
     * Pone el icono de música de la barra de controles según densidad de pixel.
     */
    private void setMusicIconOn() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - setMusicIconOn");

        final int mask = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (mask ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            music_btn.setImageResource(R.drawable.ic_music_note_white_18dp);
        }
        else if (mask ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            music_btn.setImageResource(R.drawable.ic_music_note_white_24dp);
        }
        else if (mask ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            music_btn.setImageResource(R.drawable.ic_music_note_white_24dp);
        }
        else {
            music_btn.setImageResource(R.drawable.ic_music_note_white_18dp);
        }
    }

    /**
     * Pone el icono de música de la barra de controles según densidad de pixel.
     */
    private void setMusicIconOff() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - setMusicIconOff");

        final int mask = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
        if (mask ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            music_btn.setImageResource(R.drawable.ic_music_note_off_white_18dp);
        }
        else if (mask ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            music_btn.setImageResource(R.drawable.ic_music_note_off_white_24dp);
        }
        else if (mask ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            music_btn.setImageResource(R.drawable.ic_music_note_off_white_24dp);
        }
        else {
            music_btn.setImageResource(R.drawable.ic_music_note_off_white_18dp);
        }
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {

        Log.w(Constants.Log.METHOD, "ReadActivityStandard - hideSystemUI");

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

    @Override
    protected void onPause() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - OnPause");

        //Hay que cancelar el desvanecimiento ya que sigue en segundo plano
        cancelDesvanecimientoControles();

        if (!paused) {

            mPager.pauseTimer(false);
            paused = true;
            setButtonIconToPlay();
        }
        resumeShowControls = true;

        //Pausamos música
        MusicManager.pauseMusic();

        //Debug.stopMethodTracing();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.v(Constants.Log.METHOD, "ReadActivityStandard - onRestart");

        MusicManager.createMusicRestart(false);
        MusicManager.createVoiceRestart(tiempo);

        super.onRestart();
    }

    @Override
    protected void onStop() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - onStop");

        if (!continue_music) MusicManager.stopMusic();
        MusicManager.stopVoice();

        super.onStop();
    }

    @Override
    protected void onDestroy() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - OnDestroy");

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.v(Constants.Log.METHOD, "ReadActivityStandard - OnResume");

        //Escondemos barras
        hideSystemUI();

        //Mostrar los controles con el autoplay parado.
        if (resumeShowControls) {

            //Desbloqueamos siempre por si hubiera lock fallido
            unlockForced();

            mostrarControles(false);
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {

        Log.v(Constants.Log.METHOD, "ReadActivityStandard - onBackPressed");

        //Desbloqueamos siempre por si hubiera lock fallido
        unlockForced();

        if (mPager.getCurrentItem() == 0) {
            Log.v(Constants.Log.METHOD, "ReadActivityStandard - onBackPressed - Primera página");
            exit();

        } else {
            Log.v(Constants.Log.METHOD, "ReadActivityStandard - onBackPressed - Retrocede página");
            //Mostramos si estuvieran escondidos
            mostrarControles(true);
            // Retrocedemos una página
            pageBack();
        }
    }
}
