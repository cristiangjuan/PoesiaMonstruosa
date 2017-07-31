package com.android.poesiamonstruosa.customs;

import android.content.Context;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.poesiamonstruosa.R;
import com.android.poesiamonstruosa.activities.ReadActivity;
import com.android.poesiamonstruosa.activities.ReadActivityStandard;
import com.android.poesiamonstruosa.activities.ReadFragment;
import com.android.poesiamonstruosa.utils.Constants;
import com.android.poesiamonstruosa.utils.ScreenFrame;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;


public class CustomViewPagerStandard extends ViewPager {


	/**
	 * Scroller customizado.
	 */
	private CustomScroller mScroller;
	/**
	 * Número de páginas del ViewPager.
	 */
	private int numPages;
	/**
	 * Es un temporizador que dispara el paso de página al terminar.
	 */
	private Timer nextPageTimer;
	/**
	 * Tarea de paso de página planificada en nextPageTimer
	 */
	private TimerTask nextPageTask;
	/**
	 * Manejador para el hilo del temporizador de paso de página.
	 */
	private Handler mHandler = new Handler();
	/**
	 * Temporizador que se encarga de actualizar la barra de progreso.
	 */
	private CountDownTimer refreshProgressBarCounter;
	/**
	 * Temporizador que se encarga de actualizar la barra de progreso.
	 */
	private CountDownTimer refreshProgressBarInitialCounter;

	/**
	 * Variable para controlar cuando el temporizador ha sido cancelado.
	 */
	private boolean paused = Constants.Autoplay.PAUSADO_INICIO;

	/**
	 * Controla que el desplazamiento de una página sea rápido. Esto es para tener un
	 * deslizamiento rápido como el automático pero activado manualmente (backButton o dobleTap lateral).
	 */
	private boolean swipeFast;
	/**
	 * Distancia del evento MOVE
	 */
	float distance = 0;
	/**
	 * Coordenada de inicio X en touchEvent
	 */
	float startX = 0;
	/**
	 * Coordenada de inicio Y en touchEvent
	 */
	float startY = 0;

	/**
	 * Animación nº de página
	 */
	private Animation hideNumPage;
	/**
	 * Animación nº de página
	 */
	private Animation showNumPage;

	/**
	 * Vista del nº de páginas
	 */
	private TextView numPagesTextView;
	/**
	 * Vista del nº de páginas
	 */
	private TextView numPagesTextView_2;
	/**
	 * Barra de progreso
	 */
	private ProgressBar mProgress;
	/**
	 * Progreso numérico de la barra
	 */
	private int mProgressStatus = Constants.Autoplay.PROGRESSBAR_MAX;
	/**
	 * Contexto
	 */
	private Context mContext;

	/**
	 * Utilizamos para guardar los milisegundos que le quedan al timer al pausarlo para cuando
	 * lo reanudemos.
	 */
	private int millisLeft = Constants.Autoplay.PAGE_JUMP_TIME;
	/**
	 * Controlamos la primera vez que el usuario le da al play en la aplicación
	 * para iniciar la variable millisLeft.
	 */
	private boolean firstResume = true;
	/**
	 * Controla si el desplazamiento de una página está siendo manual o automático
	 */
	private boolean swipeManually = false;
    private CustomView mCurrentView;

    public void setSwipeManually(boolean b) {

		this.swipeManually = b;
	}

	/**
	 * Detecta los gestos que realizamos sobre el pager.
	 */
	private GestureDetector gestureDetector;


	public CustomViewPagerStandard(Context context) {
		super(context);
		mContext = context;
		postInitViewPager();
	}

	public CustomViewPagerStandard(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		postInitViewPager();
	}
	
	/**
	 * Devuelve el nº de páginas del viewpager
	 * @return
	 */
	public int getNumPages() {
		return numPages;
	}

	/**
	 * Setea el número de páginas del viewpager
	 * @param numPages
	 */
	public void setNumPages(int numPages) {
		this.numPages = numPages;
	}

	/**
	 * Inicializamos el scroller y el detector de gestos.
	 */
	private void postInitViewPager() {

		Log.v(Constants.Log.METHOD, "CustomViewPagerStandard - postInitViewPager");
		
		
		try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new CustomScroller(mContext,
                    new DecelerateInterpolator());
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
		
		//Inicializamos el detector de gestos
		gestureDetector = new GestureDetector(mContext, new GestureListener());

		/**
		 * Controlamos eventos al pasar de página o al ir deslizandola
		 */
		addOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * Controlamos cuando estamos en la siguiente página y
			 * cuando dejamos de arrastrar una página o hacer swipe.
			 */
			@Override
			public void onPageSelected(int arg0) {
				
				Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - OnPageChangeListener.onPageSelected");

                setCurrentView();

				//Reseteamos las animaciones por si volviéramos a la página por 2a vez
				resetFullTextFramesAnimation();
				
				//Actualizamos el nº de página
				numPagesTextView.startAnimation(hideNumPage);
				
				//Actualizamos el valor de la barra de progreso y del tiempo de autoplay al reanudar
				mProgressStatus = Constants.Autoplay.PROGRESSBAR_MAX;
				mProgress.setProgress(mProgressStatus);
				millisLeft = getTiempoAutoplay();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

				//Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - OnPageChangeListener.onPageScrolled");

			}
			
			/*
			 * Utilizamos este evento para resumir la barra de progreso después
			 * de haberla parado cuando estamos haciendo swipe.
			 * Estados:
			 * 0 - página asentada
			 * 1 - Arrastrando o Swipe.
			 * 2 - Asentandose.
			 * (non-Javadoc)
			 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
			 */
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
				//Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - OnPageChangeListener.onPageScrollStateChanged");
				/*
				switch (arg0) {
				
					case ViewPager.SCROLL_STATE_IDLE: 
						Log.v(Constants.Log.TIMER, "OnPageChangeListener.onPageScrollStateChanged IDLE");
						break;
					case ViewPager.SCROLL_STATE_DRAGGING: 
						Log.v(Constants.Log.TIMER, "OnPageChangeListener.onPageScrollStateChanged DRAGGING");
						break;
					case ViewPager.SCROLL_STATE_SETTLING: 
						Log.v(Constants.Log.TIMER, "OnPageChangeListener.onPageScrollStateChanged SETTLING");
						break;
				}
				*/
				
				/*
				 * El estado ViewPager.SCROLL_STATE_IDLE indica que la página se ha asentado que
				 * es cuando queremos poner en marcha el autoplay y las animaciones.
				 * 
				 */
				if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
					Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - OnPageChangeListener.onPageScrollStateChanged STATE = IDLE");

                    //Desbloqueamos los controles ya que fueron bloqueados al pulsar el botón de siguiente página
                    ((ReadActivityStandard)mContext).unlockButtons();
                    ((ReadActivityStandard)mContext).unlockTap();

					// Si el paso de página es natural después del fin de página, iniciamos automáticamente
					if (((ReadActivityStandard)mContext).isNextPagina()) {

                        ((ReadActivityStandard)mContext).comienzoPagina();
					}
				}
			}
		});
	}

    /**
     * Asocia al pager la vista que está mostrando.
     */
	public void setCurrentView(){

        //Tenemos en memoria la vista de la página actual
        ReadFragment f = (ReadFragment) (CustomViewPagerStandard.this.getAdapter()
                .instantiateItem(CustomViewPagerStandard.this, getCurrentItem()));
        mCurrentView = f.getCustomView();
    }
	
	/**
	 * Recibimos la barra de progreso y el contador de páginas y los inicializamos.
	 * @param progressBar
	 * @param numPages
	 */
	public void initControls(ProgressBar progressBar, TextView numPages, TextView numPages_2) {
		
		Log.v(Constants.Log.METHOD, "CustomViewPagerStandard - initControls");
		
		mProgress = progressBar;
		mProgress.setMax(Constants.Autoplay.PROGRESSBAR_MAX);
		//Iniciamos la barra de progreso al 100%
		mProgressStatus = Constants.Autoplay.PROGRESSBAR_MAX;
		mProgress.setProgress(mProgressStatus);
		//Ponemos millisLeft al valor inicial (lo hacemos en resume con firtResume)
		//millisLeft = getTiempoAutoplay();
		numPagesTextView = numPages;
		numPagesTextView_2 = numPages_2;
		//Inicializamos el nº de página y las animaciones
		hideNumPage = new TranslateAnimation(0, 0, 0,
				getResources().getDimension(R.dimen.hide_num_page));
		hideNumPage.setDuration(getResources().getInteger(R.integer.hide_num_page));
		hideNumPage.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				numPagesTextView.setText("" + (CustomViewPagerStandard.this.getCurrentItem() + 1));
				numPagesTextView.startAnimation(showNumPage);
			}
		});
		showNumPage = new TranslateAnimation(0, 0,
				-getResources().getDimension(R.dimen.hide_num_page), 0);
		showNumPage.setDuration(getResources().getInteger(R.integer.show_num_page));
		showNumPage.setInterpolator(new BounceInterpolator());
		numPagesTextView.setText(""+(CustomViewPagerStandard.this.getCurrentItem()+1));
		numPagesTextView_2.setText(( " / "+ CustomViewPagerStandard.this.getNumPages()));
		nextPageTimer = new Timer();
	}
	
	
	/**
	 * Pone en marcha el timer.
	 * Lo llamamos al pasar de página. En caso de que sea por un paso de página manual paramos los hilos
	 * del autoplay, en caso de automático no haría falta ya que los hilos habrían terminado su ejecución.
	 */
	public void startTimer() {
		
		Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - startTimer");

		//Ponemos en marcha los hilos
		int tiempo = getTiempoAutoplay();
		startHilosTimers(tiempo, tiempo);
		//Empezamos las animaciones de los cuadros de texto
		startTextFramesAnimation();
        
        paused=false;
	}

	/**
	 * Pausamos el timer si no estuviera ya pausado por el usuario. Igual que
	 * stopTimer pero guardando los milisegundos restantes del temporizador 
	 * para la reanudación.
	 */
	public void pauseTimer(boolean notRefresh){
		
		Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - pauseTimer");

		//Mostramos el botón refresh si el temporizador no estuviera completo
		if (mProgressStatus != Constants.Autoplay.PROGRESSBAR_MAX && !notRefresh)
			((ReadActivityStandard) mContext).showRefreshMethod();

		millisLeft = mProgressStatus * getTiempoAutoplay() / Constants.Autoplay.PROGRESSBAR_MAX;
		//Log.v(Constants.Log.TIMER, "Progress: "+mProgressStatus );
		//Log.v(Constants.Log.TIMER, "Secs left: "+millisLeft );

		pararHilosTimers();
		cancelTextFramesAnimation(millisLeft);
		paused = true;
	}

	/**
	 * Resumimos el timer
	 */
	public void resumeTimer(){
	
		Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - resumeTimer");

        //Inicializamos millisLeft aquí ya que en initControls no da tiempo a que el fragmento haya calculado el tiempo
        if (firstResume) {
            Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - "+" FirstResume: "+millisLeft+" millisLeft");
            millisLeft = getTiempoAutoplay();
            firstResume = false;
        }

		Log.v(Constants.Log.TIMER, "Progress: "+mProgressStatus );
		Log.v(Constants.Log.TIMER, "Secs left: "+millisLeft );

		//El proceso es el mismo que startTimer pero con los milisegundos que quedaban
		if (paused) {
			
			pararHilosTimers(); 
			//Ponemos en marcha los hilos
			startHilosTimers(getTiempoAutoplay(), millisLeft);
			//Empezamos las animaciones de los cuadros de texto
			resumeTextFramesAnimation(millisLeft);
			paused=false;
		}
	}
	
	/**
	 * Da comienzo a los hilos del temporizador y la barra de progreso.
	 * @param tiempoTotal
	 */
	private void startHilosTimers(int tiempoTotal, int tiempoRestante) {
		
		Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - startHilosTimers");

		//Planificamos la ejecución de la tarea de paso de página
		nextPageTask = new AutoplayTask();
		//Creamos el contador asociado a la barra de progreso
		crearRefreshProgressBarTimer(tiempoTotal, tiempoRestante);
		//Iniciamos los hilos
		nextPageTimer.schedule(nextPageTask, tiempoRestante);
		refreshProgressBarCounter.start();
	}
	
	/**
	 * Cancela el hilo del temporizador y de la barra de progreso. 
	 */
	private void pararHilosTimers() {
		
		Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - cancelarHilosTimers");
		
		if (nextPageTask != null) nextPageTask.cancel();
		if (refreshProgressBarCounter != null) refreshProgressBarCounter.cancel();
	}
	
	/**
	 * Creamos el hilo que actualiza la barra de progreso
	 * @param tiempoTotal Tiempo total que dura la cuenta atrás.
	 * @param tiempoRestante Tiempo restante de la cuenta atrás. Cuando lo hemos parado.
	 */
	private void crearRefreshProgressBarTimer(final int tiempoTotal, int tiempoRestante){
		
		Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - crearRefreshProgressBarCounter");
		
		//En el caso de que no lo hayamos cancelado antes, ej: AutoplayTask
		if (refreshProgressBarCounter != null) refreshProgressBarCounter.cancel();
		if (refreshProgressBarInitialCounter != null) refreshProgressBarInitialCounter.cancel();
		
		//Creamos el hilo.
		//Hay que asegurase de que se crea en el hilo principal ya que accede a la barra de progreso.
		refreshProgressBarCounter = new CountDownTimer(tiempoRestante, Constants.Autoplay.PROGRESSBAR_REFRESH_INTERVAL) {
			
			/* 
			 * Actualiza la barra de progreso.
			 * (non-Javadoc)
			 * @see android.os.CountDownTimer#onTick(long)
			 */
			@Override
			public void onTick(long millisUntilFinished) {
				
				mProgressStatus = (int) ((double) millisUntilFinished / (double ) tiempoTotal 
						* Constants.Autoplay.PROGRESSBAR_MAX);
				//Log.v(Constants.Log.TIMER, "CountDownTimer.onTick CustomViewPagerStandard - ProgressBar);
				//Log.v(Constants.Log.TIMER, "MillisUntilFinished: "+millisUntilFinished);
				//Log.v(Constants.Log.TIMER, "TiempoRestante: "+tiempoRestante);
				//Log.v(Constants.Log.TIMER, "ProgressBarStatus: "+mProgressStatus);
				//Update the progress bar
                mProgress.setProgress(mProgressStatus);
			}
			
			@Override
			public void onFinish() {
				
				Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - CountDownTimer.onFinish - ProgressBar finished");
				
				/*
				mProgressStatus = 0;
				mProgress.setProgress(mProgressStatus);
				*/
			}
		};
	}

    /**
     * Reseteamos barra de progreso sin animación y ponemos en marcha autoplay
     */
    public void resetProgressBarWithoutAnimation() {

        Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - resetProgressBarWithoutAnimation");

        //Reiniciamos las animaciones
        resetTextFramesAnimation();

        mProgressStatus = Constants.Autoplay.PROGRESSBAR_MAX;
        mProgress.setProgress(mProgressStatus);
        //Ponemos millisLeft al valor inicial
        millisLeft = getTiempoAutoplay();
        //Reseteamos el estado de las animaciones
        resetStateTextFramesAnimation();
    }

	/**
	 * Animación en restart de la barra de progreso
	 */
	public void resetProgressBarAnimation(){
		
		Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - resetProgressBarAnimation");

		//Reiniciamos las animaciones
		resetTextFramesAnimation();
		
		//Creamos el hilo.
		//Hay que asegurase de que se crea en el hilo principal ya que accede a la barra de progreso.
		refreshProgressBarInitialCounter = new CountDownTimer(Constants.Autoplay.PROGRESSBAR_INITIAL_TIME_ANIMATION -
				(int) ( ((double) millisLeft / (double) getTiempoAutoplay()) *  Constants.Autoplay.PROGRESSBAR_INITIAL_TIME_ANIMATION), 
				Constants.Autoplay.PROGRESSBAR_REFRESH_INTERVAL) {
			
			/* 
			 * Actualiza la barra de progreso.
			 * (non-Javadoc)
			 * @see android.os.CountDownTimer#onTick(long)
			 */
			@Override
			public void onTick(long millisUntilFinished) {
				
				mProgressStatus = Constants.Autoplay.PROGRESSBAR_MAX - (int) ((double) millisUntilFinished / (double ) Constants.Autoplay.PROGRESSBAR_INITIAL_TIME_ANIMATION 
						* Constants.Autoplay.PROGRESSBAR_MAX);
                //Log.w(Constants.Log.TIMER, "resetProgressBarAnimation.onTick CustomViewPagerStandard - ProgressBar );
				//Log.w(Constants.Log.TIMER, "MillisUntilFinished: "+millisUntilFinished);
				//Log.w(Constants.Log.TIMER, "TiempoRestante: "+tiempoRestante);
				//Log.w(Constants.Log.TIMER, "ProgressBarStatus: "+mProgressStatus);
				//Update the progress bar
                mProgress.setProgress(mProgressStatus);
			}
			
			@Override
			public void onFinish() {
				
				Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - resetProgressBarAnimation.onFinish");
				
				mProgressStatus = Constants.Autoplay.PROGRESSBAR_MAX;
				mProgress.setProgress(mProgressStatus);
				//Ponemos millisLeft al valor inicial
				millisLeft = getTiempoAutoplay();
				//Reseteamos el estado de las animaciones
				resetStateTextFramesAnimation();
				//((ReadActivityStandard) mContext).unlockButtons();
				//((ReadActivityStandard) mContext).unlockTap();
			}
		};
		refreshProgressBarInitialCounter.start();
	}
	
	/**
	 * Devuelve el tiempo de autoplay del temporizador llamando al fragmento.
	 * @return
	 */
	private int getTiempoAutoplay() {
		
		ReadFragment f = (ReadFragment) (CustomViewPagerStandard.this.getAdapter()
				.instantiateItem(CustomViewPagerStandard.this, getCurrentItem()));
		
		Log.w(Constants.Log.TIMER, "CustomViewPagerStandard - getTiempoAutoplay - Fragmento: "+f.getPage());
		
		return f.getTiempo();
	}
	
	/**
	 * Ponemos en marcha las animaciones de los cuadros de texto
	 */
	private void startTextFramesAnimation() {
		
		ReadFragment f = (ReadFragment) (CustomViewPagerStandard.this.getAdapter()
				.instantiateItem(CustomViewPagerStandard.this, getCurrentItem()));
		
		Log.w(Constants.Log.TIMER, "CustomViewPagerStandard - startTextFramesAnimation - Fragmento: "+f.getPage());
		
		f.startAnimations();
	}
	
	/**
	 * Pausamos las animaciones de los cuadros de texto
	 */
	private void cancelTextFramesAnimation(int timeLeft) {
		
		ReadFragment f = (ReadFragment) (CustomViewPagerStandard.this.getAdapter()
				.instantiateItem(CustomViewPagerStandard.this, getCurrentItem()));
		
		Log.w(Constants.Log.TIMER, "CustomViewPagerStandard - cancelTextFramesAnimation - Fragmento: "+f.getPage());
		
		f.cancelAnimations(timeLeft);
	}
	
	/**
	 * Reseteamos las animaciones de los cuadros de texto con una Animación de retorno
	 */
	private void resetTextFramesAnimation() {
		
		ReadFragment f = (ReadFragment) (CustomViewPagerStandard.this.getAdapter()
				.instantiateItem(CustomViewPagerStandard.this, getCurrentItem()));
		
		Log.w(Constants.Log.TIMER, "CustomViewPagerStandard - resetTextFramesAnimation - Fragmento: "+f.getPage());
		
		f.resetAnimations();
	}
	
	/**
	 * Reinicia el estado de las animaciones.
	 */
	private void resetStateTextFramesAnimation() {
		
		ReadFragment f = (ReadFragment) (CustomViewPagerStandard.this.getAdapter()
				.instantiateItem(CustomViewPagerStandard.this, getCurrentItem()));
		
		Log.w(Constants.Log.TIMER, "CustomViewPagerStandard - resetStateTextFramesAnimation - Fragmento: "+f.getPage());
		
		f.resetStateAnimations();
	}
	
	/**
	 * Reinicia el estado de las animaciones y la animación en sí, utilizado entrar en la página.
	 */
	private void resetFullTextFramesAnimation() {
		
		ReadFragment f = (ReadFragment) (CustomViewPagerStandard.this.getAdapter()
				.instantiateItem(CustomViewPagerStandard.this, getCurrentItem()));
		
		Log.w(Constants.Log.TIMER, "CustomViewPagerStandard - resetFullTextFramesAnimation - Fragmento: "+f.getPage());
		
		f.resetFullHighlights();
	}
	
	/**
	 * Reanudamos las animaciones de los cuadros de texto
	 */
	private void resumeTextFramesAnimation(int timeLeft) {
		
		ReadFragment f = (ReadFragment) (CustomViewPagerStandard.this.getAdapter()
				.instantiateItem(CustomViewPagerStandard.this, getCurrentItem()));
		
		Log.w(Constants.Log.TIMER, "CustomViewPagerStandard - resumeTextFramesAnimation - Fragmento: "+f.getPage());
		
		f.resumeAnimations(timeLeft);
	}
	
	/**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }
    
    /**
     * @author quayo
     *
     * Tarea que se ejecuta periódicamente para poner en marcha el hilo que controla
     * el salto de página.
     */
    private class AutoplayTask extends TimerTask {

		@Override
		public void run() {
			
			mHandler.post(new Runnable() {
                public void run() {
                	
                	Log.v(Constants.Log.TIMER, "CustomViewPagerStandard - AutoplayTask.run");
                	
                	//Si es la última página no hacemos nada
                    if (CustomViewPagerStandard.this.getCurrentItem() == numPages - 1) {
                    	//CustomViewPager.this.setCurrentItem(0, false);
                    	//Mostramos botón de play con icono de actualizar para volver a empezar
                    	//Tenemos que poner pause a true en ScreenActivityStandard
                    	((ReadActivityStandard) mContext).finAutoplay();
                    }
                    else {

                    	//Mostramos controles
						((ReadActivityStandard) mContext).finPagina();
                    }
                }
			});
		}
    }

	/**
	 * Intercepta el evento
	 */
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

		Log.v(Constants.Log.TOUCH, "CustomViewPagerStandard - onInterceptTouchEvent");

		//Al dejarlo comentado conseguimos que no se pare al transición de pág al hacer tap en mitad de ella.
		//super.onInterceptTouchEvent(ev);

		return true;
    }


	/**
	 * Controlamos que se pare el temporizador al mover la página
	 * (non-Javadoc)
	 * @see ViewPager#onTouchEvent(MotionEvent)
	 */
	/* (non-Javadoc)
	 * @see android.support.v4.view.ViewPager#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		Log.v(Constants.Log.TOUCH, "CustomViewPagerStandard - onTouchEvent");

		if (!((ReadActivityStandard) mContext).isTapLocked()) {

			switch (event.getAction() & MotionEvent.ACTION_MASK) {

				case MotionEvent.ACTION_MOVE:
					Log.d(Constants.Log.TOUCH, "MOVE");

					swipeManually = true;
					break;
			}
			gestureDetector.onTouchEvent(event);

			//Solo permitimos el desliz de página manual si está pausado
			if (!paused) {

				return true;
			}
			else {
				return super.onTouchEvent(event);
			}
		}

		//Si estamos en lock no permitimos desliz manual
		return true;
	}
	
	public float getDistance(float startX, float startY, MotionEvent ev) {
	     
		 float distanceSum = 0;
	     final int historySize = ev.getHistorySize();
	     for (int h = 0; h < historySize; h++) {
	         // historical point
	         float hx = ev.getHistoricalX(0, h);
	         float hy = ev.getHistoricalY(0, h);
	         // distance between startX,startY and historical point
	         float dx = (hx-startX);
	         float dy = (hy-startY);
	         distanceSum += Math.sqrt(dx*dx+dy*dy);
	         // make historical point the start point for next loop iteration
	         startX = hx;
	         startY = hy;
	     }
	     // add distance from last historical point to event's point
	     float dx = (ev.getX(0)-startX);
	     float dy = (ev.getY(0)-startY);
	     distanceSum += Math.sqrt(dx*dx+dy*dy);
	     
	     return distanceSum;        
	 }

	/**
	 * Vuelve a la primera página
	 */
	public void toFirstPage() {
		
		Log.v(Constants.Log.METHOD, "CustomViewPagerStandard - toFirstPage");
		
		setCurrentItem(0, false);
	}

	/**
	 * Controlamos con el singletap que aparezacan los controles en pantalla
	 * @author quayo
	 *
	 */
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {

            Log.v(Constants.Log.TOUCH, "CustomViewPagerStandard - GestureListener.onDoubleTap");

            //Comprobamos si la imagen está aumentada o no
            if (mCurrentView.isInZoom()) {

                Log.v(Constants.Log.SIZE, "CustomView onDoubleTap - inZoom");
                //view.resetMatrix();
                mCurrentView.resetAnimation();
            } else if (Constants.Options.ZOOM) {
                Log.v(Constants.Log.SIZE, "CustomView onDoubleTap - notZoom");

                //toZoomActivity(e);
                mCurrentView.zoomDoubleTap(e.getRawX(), e.getRawY());
            }

			return super.onDoubleTap(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			
			Log.v(Constants.Log.TOUCH, "CustomViewPagerStandard - GestureListener.onSingleTapConfirmed");

            boolean visible = (CustomViewPagerStandard.this.getSystemUiVisibility()
                    & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

            if (visible) {

                hideSystemUI();
            }

			if (!((ReadActivityStandard) mContext).isTapLocked()) {
				
				int coordX = (int) e.getX();
				int coordY = (int) e.getY();

                //Controles visibles
				if (((ReadActivityStandard) mContext).areControlsVisible()) {

                    if (((ReadActivityStandard) mContext).areAjustesVisible()) {

                        //Si los ajustes están visibles la esquina superior derecha es más amplia
                        if (( (coordY > (ScreenFrame.getFrameHeight() * 0.20)) ||
                                (coordX > (ScreenFrame.getFrameWidth() * 0.20)) ) &&

                                ( (coordY > (ScreenFrame.getFrameHeight() * 0.20)) ||
                                        (coordX < (ScreenFrame.getFrameWidth() * 0.50)) ) &&

                                ( (coordY < (ScreenFrame.getFrameHeight() * 0.80)) ||
                                        (coordX > (ScreenFrame.getFrameWidth() * 0.20)) ) &&

                                ((coordY < (ScreenFrame.getFrameHeight() * 0.80)) ||
                                        (coordX < (ScreenFrame.getFrameWidth() * 0.80)) )) {

                            ((ReadActivityStandard) mContext).ocultarControles(false);
                        }
                    }
                    else {

                        if (( (coordY > (ScreenFrame.getFrameHeight() * 0.20)) ||
                                (coordX > (ScreenFrame.getFrameWidth() * 0.20)) ) &&

                                ( (coordY > (ScreenFrame.getFrameHeight() * 0.20)) ||
                                        (coordX < (ScreenFrame.getFrameWidth() * 0.80)) ) &&

                                ( (coordY < (ScreenFrame.getFrameHeight() * 0.80)) ||
                                        (coordX > (ScreenFrame.getFrameWidth() * 0.20)) ) &&

                                ((coordY < (ScreenFrame.getFrameHeight() * 0.80)) ||
                                        (coordX < (ScreenFrame.getFrameWidth() * 0.80)) )) {

                            ((ReadActivityStandard) mContext).ocultarControles(false);
                        }
                    }
				}
                //Controles no visibles
				else {

                    ((ReadActivityStandard) mContext).mostrarControles(false);
				}
			}
			
			return super.onSingleTapConfirmed(e);
		}
	}

    // This snippet hides the system bars.
    private void hideSystemUI() {

        Log.w(Constants.Log.METHOD, "CustomViewPagerStandard - hideSystemUI");

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        this.setSystemUiVisibility(
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
        this.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * Escondemos las barras de status y nav al coger foco
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {

            boolean visible = (this.getSystemUiVisibility()
                    & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

            if (visible) {

                hideSystemUI();
            }
        }
    }
	
	/**
	 * Scroller customizado.
	 * @author quayo
	 *
	 */
	private class CustomScroller extends Scroller {
		
		private double mScrollFactor = 1;
		
		public CustomScroller(Context context) {
			super(context);
		}
		
		public CustomScroller(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }

	    public CustomScroller(Context context, Interpolator interpolator, boolean flywheel) {
	        super(context, interpolator, flywheel);
	    }

	    /**
	     * Set the factor by which the duration will change
	     */
	    public void setScrollDurationFactor(double scrollFactor) {
	        mScrollFactor = scrollFactor;
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {

			if (swipeManually){
				Log.v(Constants.Log.SCROLL, "Manually ");
				Log.v(Constants.Log.SCROLL, "startX="+startX+" startY="+startY+" dx="+dx+" dy="+dy+" duration="+duration+" scroollFactor="+mScrollFactor);
				super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
			}
			else {
				Log.v(Constants.Log.SCROLL, "CustomViewPagerStandard - startScroll Auto");
				Log.v(Constants.Log.SCROLL, "startX=" + startX + " startY=" + startY + " dx=" + dx + " dy=" + dy + " duration=" + duration);
				super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor * 3));
			}
	    }
		
	}
}
