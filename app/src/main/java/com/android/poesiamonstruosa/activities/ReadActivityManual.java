package com.android.poesiamonstruosa.activities;


import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewAnimationUtils;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.poesiamonstruosa.R;
import com.android.poesiamonstruosa.customs.CustomPagerAdapter;
import com.android.poesiamonstruosa.customs.CustomViewPagerManual;
import com.android.poesiamonstruosa.utils.Constants;

import java.util.Timer;
import java.util.TimerTask;


public class ReadActivityManual extends ReadActivity {

    /**
* The pager widget, which handles animation and allows swiping horizontally to access previous
* and next wizard steps.
*/
    protected FrameLayout frameLayout;
    protected CustomViewPagerManual mPager;
    protected Animation hideCenterControls;
    protected Animation showCenterControls;
    /**
     * Control de gesto para esconder barras de status y navegación.
     */
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.v(Constants.Log.METHOD, "ReadActivityManual - OnCreate");

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

                Log.w(Constants.Log.CONTROLS, "ReadActivityManual - NavigationBar - "+visible);
                Log.w(Constants.Log.CONTROLS, "ReadActivityManual - StatusBar - "+visibleStatus);
            }
        });

        setContentView(R.layout.activity_read_manual);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        revealLayout = (FrameLayout) findViewById(R.id.reveal_layout);
        controls_bottom = (FrameLayout) findViewById(R.id.controls_bottom);
        controls_bottom.setVisibility(View.INVISIBLE);

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

        //Invisible al iniciar la app. Con los otros botones ocultamos el layout.
        close_btn.setClickable(false);
        close_btn.setVisibility(View.INVISIBLE);

      	//Inicializamos el texto del nº de página
      	numPagTextView = (TextView) findViewById(R.id.tview_numPage);
      	numPagTextView_2 = (TextView) findViewById(R.id.tview_numPage_2);

        mAdapter = new CustomPagerAdapter(getSupportFragmentManager(), pages.length);
        mPager = (CustomViewPagerManual) findViewById(R.id.slide_pager);
        mPager.setVisibility(View.INVISIBLE);
        mPager.setNumPages(pages.length);
        //mPager.setOffscreenPageLimit(2);
        mPager.setAdapter(mAdapter);
        //mPager.setPageTransformer(true, new CustomPageTransformer());
        mPager.setScrollDurationFactor(Constants.SCROLL_FACTOR);
        mPager.initControls(progressBar, numPagTextView, numPagTextView_2);

        resumeShowControls = false;

        prepareOnClicks();
        prepareAnimations();
        /** Highlight button deactivated
        prepareHighlightSelection();
        */

        //Bloqueamos el botón para que no se ponga en marcha el autoplay durante la animación inicial
        lockButtons();
        //Bloqueamos el tap para que no se escondan los controles ni se pase de pag. durante la animación
        lockTap();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Empezamos la animación inicial
            startUnvealRead();
        }
        else {
            //Empezamos la animación inicial antes de Lollipop
            startShowLayoutBL();
        }

    }

    private void prepareOnClicks() {

        close_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityManual - onClick closeButton");

                if (!areButtonsLocked()) {

                    //El bloqueo de botones lo hacemos dentro de exit.
                    exit();
                }
            }
        });
    }

    /**
     * Configura las animaciones.
     */
    private void prepareAnimations() {

        prepareHideShowControlsAnimations();
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

        hideCloseForReveal.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityManual - hideCloseForReveal.onAnimationStart");

                close_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityManual - hideCloseForReveal.onAnimationEnd");

                startRevealReadReturn();
            }
        });

        //Cargamos la animación de aparición del botón de cerrar
        showClose = new TranslateAnimation(0, 0,
                -getResources().getDimension(R.dimen.hide_controls), 0);
        showClose.setDuration(getResources().getInteger(R.integer.hide_controls));
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
        hideClose.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityManual - hideClose.onAnimationStart");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityManual - hideClose.onAnimationEnd");

                close_btn.setClickable(false);
                close_btn.setVisibility(View.INVISIBLE);

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
        showBottomControls.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityManual - showBottomControls.onAnimationEnd");
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
        hideBottomControls.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityManual - hideBottomControls.onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v(Constants.Log.CONTROLS, "ReadActivityManual - hideBottomControls.onAnimationEnd");
                controls_bottom.setVisibility(View.INVISIBLE);
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
        pagerOutTransitionExit.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.CONTROLS, "ReadActivityManual - pagerOutTransitionExit.onAnimationEnd");

                ReadActivityManual.this.setResult(RESULT_OK);
                //Finalizamos la actividad
                ReadActivityManual.this.finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Avanza una página
     */
    public void pageForward() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityManual - pageForward");

        //Bloqueamos el botón de play y el tap para que no se interrumpa la animación de reinicio de autoplay.
        lockButtons();
        lockTap();

        mPager.setCurrentItem(mPager.getCurrentItem()+1, true);
    }

    /**
     * Retrocede una página
     */
    public void pageBack() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityManual - pageBack");

        //Bloqueamos el botón de play y el tap
        lockButtons();
        lockTap();

        mPager.setCurrentItem(mPager.getCurrentItem()-1, true);
    }

    /**
	 * Pone en marcha animación Reveal Effect en reverso. Utilizada al entrar en la actividad,
	 * después de la primera parte realizada en MainActivity.
	 */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void startUnvealRead() {
		
		Log.v(Constants.Log.METHOD, "ReadActivityManual - startUnvealRead");
		
		revealLayout.addOnLayoutChangeListener(new OnLayoutChangeListener() {
			
			@Override
			public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
				
				v.removeOnLayoutChangeListener(this);

				//Calculamos el centro de la animación
				int cx = (revealLayout.getLeft() + revealLayout.getRight()) / 2;
				int cy = (revealLayout.getTop() + revealLayout.getBottom()) / 2;
				
				//Calculamos el radio de la animación
				//int initialRadius = Math.max(frameLayout.getWidth(), frameLayout.getHeight());
				int initialRadius = (int) Math.sqrt( Math.pow(revealLayout.getWidth(), 2) +
						Math.pow(revealLayout.getHeight(), 2));
				
				//Creamos la animación
				Animator anim =
				    ViewAnimationUtils.createCircularReveal(revealLayout,
				    		cx, cy, initialRadius, 0);
				anim.setDuration(getResources().getInteger(R.integer.unveal_long));
		        
				//Hacemos visible la vista y empezamos la animación
				anim.addListener(new AnimatorListener() {
					
					@Override
					public void onAnimationStart(Animator animation) {
						Log.v(Constants.Log.METHOD, "ReadActivityManual - startUnvealRead.onAnimationStart");
						
						revealLayout.setVisibility(View.VISIBLE);
						//Mostramos las paginas de cuento
						mPager.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onAnimationRepeat(Animator animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animator animation) {
						Log.v(Constants.Log.METHOD, "ReadActivityManual - startUnvealRead.onAnimationEnd");
						
						revealLayout.setVisibility(View.INVISIBLE);

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
		});
	}

    private void startShowLayoutBL() {

        Log.v(Constants.Log.METHOD, "ReadActivityManual - startShowLayoutBL");

        //Mostramos las paginas de cuento
        mPager.setVisibility(View.VISIBLE);
        mPager.startAnimation(pagerInTransitionIntro);
        //Programamos desbloqueo
        programUnlock(pagerInTransitionIntro.getDuration());
    }

    /**
     * Pone en marcha animación Reveal Effect al pulsar el botón de cerrar.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startRevealReadReturn() {

        Log.v(Constants.Log.METHOD, "ReadActivityManual - startRevealReadReturn");

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
        anim.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityManual - startRevealReadReturn.onAnimationStart");

                //Hacemos visible el reveal y le cambiamos el color
                revealLayout.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    revealLayout.setBackgroundColor(getResources().getColor(R.color.color_primary, null));
                } else {
                    revealLayout.setBackgroundColor(getResources().getColor(R.color.color_primary));
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(Constants.Log.METHOD, "ReadActivityManual - startRevealReadReturn.onAnimationEnd");

                ReadActivityManual.this.setResult(RESULT_OK);
                //Finalizamos la actividad
                ReadActivityManual.this.finish();
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

        Log.v(Constants.Log.CONTROLS, "ReadActivityManual - startExitBL");

        mPager.startAnimation(pagerOutTransitionExit);

        //Ocultamos los controles superiores e inferiores
        if (areControlsVisible) {

            //Bloqueamos el botón de play y el tap
            lockButtons();
            lockTap();

            close_btn.startAnimation(hideClose);
            controls_bottom.startAnimation(hideBottomControls);
            //Aquí no sacamos fuera de las animaciones el setVisibility porque se adelanta a la animacion hideCenterControls

            areControlsVisible = false;
        }
    }

	/**
	 * Muestra o esconde los controles del autoplay, según están visibles o no. Respuesta al evento Single Tap.
	 */
	public void showOrHideControls() {
		
		Log.v(Constants.Log.METHOD, "ReadActivityManual - showOrHideControls");
		
		//Controles visibles
		if (areControlsVisible) {
			
			Log.v(Constants.Log.CONTROLS, "ReadActivityManual - showOrHideControls Desvanecer ");
			
			//Escondemos los controles
			//Primero cancelamos la tarea de desvanecimiento.
			cancelDesvanecimientoControles();
			ocultarControles(false);
		}
		//Controles no visibles
		else {
			
			Log.v(Constants.Log.CONTROLS, "ReadActivityManual - showOrHideControls Mostrar");
			
			//Mostramos controles
	        mostrarControles(false);
		}
	}
	
	/**
	 * Hace visibles todos los controles. Si end es true obviamos el bloqueo
	 */
	public void mostrarControles(boolean force) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityManual - mostrarControles");

        if (!areButtonsLocked() || force) {

            //Planificamos el desvanecimiento de los controles solo si está en marcha el autoplay
            if (!paused) planificarDesvanecimientoControles(false);

            //Mostramos controles
            if (!areControlsVisible) {

                //Bloqueamos el botón de play y el tap para que no se interrumpa la animación de reinicio de autoplay.
                lockButtons();
                lockTap();

                //Desbloqueamos en mostrarControlesSupInf
                mostrarControlesSupInf();
            }
        }
	}

	/**
	 * Muestra controles superiores y inferiores - boton play y progressBar
	 */
	public void mostrarControlesSupInf() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityManual - mostrarControlesSupInf");

		close_btn.startAnimation(showClose);

        //Programamos desbloqueo
        programUnlock(showBottomControls.getDuration());

		controls_bottom.startAnimation(showBottomControls);
		
		//Sacar fuera de las animaciones el setVisibility, a veces no se ejecutan al ser simultáneas 2 animaciones sobre la misma view.
		close_btn.setVisibility(View.VISIBLE);
        close_btn.setClickable(true);
		controls_bottom.setVisibility(View.VISIBLE);
		
    	areControlsVisible = true;
	}

	/**
	 * Oculta los controles
	 */
	public void ocultarControles(boolean auto) {

        Log.v(Constants.Log.CONTROLS, "ReadActivityManual - ocultarControles");

        if (!areButtonsLocked() || auto) {

            //Si hemos llegado al final del autoplay no los ocultamos
            if (!finCuento) {

                //Ocultamos los controles
                if (areControlsVisible) {

                    //Bloqueamos el botón de play y el tap
                    lockButtons();
                    lockTap();

                    close_btn.startAnimation(hideClose);
                    controls_bottom.startAnimation(hideBottomControls);

                    //Desbloqueamos
                    programUnlock(hideBottomControls.getDuration());

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
		
		Log.v(Constants.Log.METHOD, "ReadActivityManual - planificarDesvanecimientoControles");
		
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
						
						Log.v(Constants.Log.CONTROLS, "ReadActivityManual - fadeOutTimerTask");
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
		
		Log.v(Constants.Log.METHOD, "ReadActivityManual - cancelDesvanecimientoControles");
		
		//Cancelamos el temporizador y la tarea si existen
		if (fadeOutTimer != null) fadeOutTimer.cancel();
		if (fadeOutTimerTask != null) fadeOutTimerTask.cancel();
	}

	/**
	 * Realizamos animación del botón play y volvemos al menú principal
	 */
	private void exit() {

        Log.v(Constants.Log.CONTROLS, "ReadActivityManual - exit");

		//Bloqueamos el botón para que no se ponga en marcha el autoplay durante la animación inicial
        lockButtons();
        //Bloqueamos el tap para que no se escondan los controles ni se pase de pag. durante la animación
        lockTap();

		cancelDesvanecimientoControles();

        //Sin reveal ocultamos los controles superiores e inferiores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            close_btn.startAnimation(hideCloseForReveal);
        }
        else {

            startExitBL();
        }
	}

    /**
     * Indica si están visibles o no los controles
     * @return
     */
    public boolean areControlsVisible() {

        return areControlsVisible;
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {

        Log.w(Constants.Log.METHOD, "ReadActivityManual - hideSystemUI");

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
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    @Override
	protected void onPause() {

    	Log.v(Constants.Log.METHOD, "ReadActivityManual - OnPause");

        //Hay que cancelar el desvanecimiento ya que sigue en segundo plano
        cancelDesvanecimientoControles();

    	resumeShowControls = true;

    	//Debug.stopMethodTracing();
		super.onPause();
	}

    @Override
    protected void onRestart() {
        Log.v(Constants.Log.METHOD, "ReadActivityManual - onRestart");

        super.onRestart();
    }

    @Override
    protected void onStop() {

        Log.v(Constants.Log.METHOD, "ReadActivityManual - onStop");

        super.onStop();
    }

	@Override
	protected void onDestroy() {
		
		Log.v(Constants.Log.METHOD, "ReadActivityManual - OnDestroy");
		
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Log.v(Constants.Log.METHOD, "ReadActivityManual - OnResume");

        //Escondemos barras
        hideSystemUI();

		//Mostrar los controles con el autoplay parado.
		if (resumeShowControls) {

            //Desbloqueamos siempre por si hubiera lock fallido
            unlockButtons();
            unlockTap();

            mostrarControles(false);
        }

		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.v(Constants.Log.METHOD, "ReadActivityManual - onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}
	
    @Override
    public void onBackPressed() {
    	
    	Log.v(Constants.Log.METHOD, "ReadActivityManual - onBackPressed");

        //Desbloqueamos siempre por si hubiera lock fallido
        unlockForced();

    	if (!areButtonsLocked()) {
    		
    		if (mPager.getCurrentItem() == 0) {
            	Log.v(Constants.Log.METHOD, "ReadActivityManual - onBackPressed - Primera página");
                exit();
                
            } else {
            	Log.v(Constants.Log.METHOD, "ReadActivityManual - onBackPressed - Retrocede página ");
                //Mostramos si estuvieran escondidos
                mostrarControles(true);
                // Retrocedemos una página
            	pageBack();
            }
    	}
    }

}
