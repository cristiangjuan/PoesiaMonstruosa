package com.android.poesiamonstruosa.customs;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.poesiamonstruosa.R;
import com.android.poesiamonstruosa.activities.ReadActivity;
import com.android.poesiamonstruosa.activities.ReadActivityManual;
import com.android.poesiamonstruosa.utils.Constants;
import com.android.poesiamonstruosa.utils.ScreenFrame;

import java.lang.reflect.Field;

public class CustomViewPagerManual extends ViewPager {

	
	/**
	 * Scroller customizado.
	 */
	private CustomScroller mScroller;
	/**
	 * Número de páginas del ViewPager.
	 */
	private int numPages;
	/**
	 * Manejador para el hilo del temporizador de paso de página.
	 */
	private Handler mHandler = new Handler();
    /**
     * Controla si el desplazamiento de una página está siendo manual o automático
     */
    private boolean swipeManually = false;
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
	 * Detecta los gestos que realizamos sobre el pager.
	 */
	private GestureDetector gestureDetector;


	public CustomViewPagerManual(Context context) {
		super(context);
		mContext = context;
		postInitViewPager();
	}
	
	public CustomViewPagerManual(Context context, AttributeSet attrs) {
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

		Log.v(Constants.Log.METHOD, "CustomViewPagerManual - postInitViewPager");
		
		
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
				
				Log.v(Constants.Log.TIMER, "CustomViewPagerManual - OnPageChangeListener.onPageSelected");
				
				//Actualizamos el nº de página
				numPagesTextView.startAnimation(hideNumPage);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
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
				
				//Log.v(Constants.Log.TIMER, "CustomViewPagerManual - OnPageChangeListener.onPageScrollStateChanged");
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
				 * Esto puede ocurrir al pasar de página o al mover la página actual sin llegar
				 * a pasar a la siguiente. Esto lo distinguiremos con el flag pageChanged que se 
				 * pone a true al pasar de página con el evento onPageSelected.
				 * 
				 */
				if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
					Log.v(Constants.Log.TIMER, "CustomViewPagerManual - OnPageChangeListener.onPageScrollStateChanged STATE = IDLE");

					//Desbloqueamos los controles ya que fueron bloqueados al pulsar el botón de siguiente página
					((ReadActivityManual)mContext).unlockButtons();
					((ReadActivityManual)mContext).unlockTap();
				}
			}
		});
	}
	
	/**
	 * Recibimos la barra de progreso y el contador de páginas y los inicializamos.
	 * @param progressBar
	 * @param numPages
	 */
	public void initControls(ProgressBar progressBar, TextView numPages, TextView numPages_2) {
		
		Log.v(Constants.Log.METHOD, "CustomViewPagerManual - initControls");
		
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

				numPagesTextView.setText("" + (CustomViewPagerManual.this.getCurrentItem() + 1));
				numPagesTextView.startAnimation(showNumPage);
			}
		});
		showNumPage = new TranslateAnimation(0, 0,
				-getResources().getDimension(R.dimen.hide_num_page), 0);
		showNumPage.setDuration(getResources().getInteger(R.integer.show_num_page));
		showNumPage.setInterpolator(new BounceInterpolator());
		numPagesTextView.setText(""+(CustomViewPagerManual.this.getCurrentItem()+1));
		numPagesTextView_2.setText(( " / "+ CustomViewPagerManual.this.getNumPages()));
	}
	
	/**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }
    
	/**
	 * Intercepta el evento
	 */
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
		  
		Log.v(Constants.Log.TOUCH, "CustomViewPagerManual - onInterceptTouchEvent");
		
		//((ScreenSlidePagerAutoActivity) mContext).showHideControls(false);
		super.onInterceptTouchEvent(ev);

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
		
		Log.v(Constants.Log.TOUCH, "CustomViewPagerManual - onTouchEvent");

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:

                startX = event.getX();
                startY = event.getY();

                Log.d(Constants.Log.TOUCH, "StartX = "+startX);
                Log.d(Constants.Log.TOUCH, "StartY = "+startY);

                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(Constants.Log.TOUCH, "MOVE");

			/*
			 * Controlamos que no se muestren/oculten los controles 2 veces seguidas
			 * en un intervalo muy corto de tiempo debido a un evento MOVE y uno SingleTap
			 * en el mismo gesto.
			 */
			/*
			distance = getDistance(startX, startY, event);
			Log.d(Constants.Log.TOUCH, "onTouchEvent distance = "+distance+" CustomViewPagerAuto ");
			if (distance > Constants.PIXELS_TO_MOVE)
			((ReadActivityManual) mContext).showHideControls(true);
			*/

			/*
			 * El evento move se repite muchas veces, con esto evitamos
			 * proceso innecesario.
			 */
			/*
			if (!paused){

				pauseTimer();
			}
			*/
                swipeManually = true;
                break;

            case MotionEvent.ACTION_UP:
                //Log.d(Constants.Log.TOUCH, "onTouchEvent CustomViewPagerAuto - "+"ACTION_UP");
                //if (!pausedAsynchronously && paused) resumeTimer();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //Log.d(Constants.Log.TOUCH, "onTouchEvent CustomViewPagerAuto - "+"ACTION_POINTER_UP");
                //startTimer();
                break;
        }

        gestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
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
		
		Log.v(Constants.Log.METHOD, "CustomViewPagerManual - toFirstPage");
		
		setCurrentItem(0, false);
	}

	/**
	 * Controlamos con el singletap que aparezacan los controles en pantalla
	 * @author quayo
	 *
	 */
	private class GestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			
			Log.v(Constants.Log.TOUCH, "CustomViewPagerManual - GestureListener.onDoubleTap");
			//Log.w(Constants.Log.TOUCH, "X: "+e.getX()+" Y: "+e.getY());
			
			if (!((ReadActivityManual) mContext).isTapLocked()) {
				
				switch (e.getAction() & MotionEvent.ACTION_MASK) {
				
				case MotionEvent.ACTION_UP:
					
					int coordX = (int) e.getX();
					int coordY = (int) e.getY();
					
					if ((coordY > (ScreenFrame.getFrameHeight() * 0.20)) &&
							(coordX < (ScreenFrame.getFrameWidth() * 0.35))) {

                        //Comprobamos que no estamos en la primera página
                        if (getCurrentItem() != 0) {

                            ((ReadActivityManual) mContext).pageBack();
                        }

					}
					if ((coordY > (ScreenFrame.getFrameHeight() * 0.20)) &&
							(coordX > (ScreenFrame.getFrameWidth() * 0.65))) {

                        //Comprobamos que no estamos en la última página
                        if (getCurrentItem() != (getNumPages()-1)) {

                            ((ReadActivityManual) mContext).pageForward();
                        }
					}
					break;
				}
			}
			
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			
			Log.v(Constants.Log.TOUCH, "CustomViewPagerManual - GestureListener.onSingleTapConfirmed");

            boolean visible = (CustomViewPagerManual.this.getSystemUiVisibility()
                    & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;

            if (visible) {

                hideSystemUI();
            }

			if (!((ReadActivityManual) mContext).isTapLocked()) {
				
				int coordX = (int) e.getX();
				int coordY = (int) e.getY();

				if (((ReadActivityManual) mContext).areControlsVisible()) {

					if (( (coordY > (ScreenFrame.getFrameHeight() * 0.20)) ||
							(coordX > (ScreenFrame.getFrameWidth() * 0.20)) ) &&

							( (coordY > (ScreenFrame.getFrameHeight() * 0.20)) ||
									(coordX < (ScreenFrame.getFrameWidth() * 0.80)) ) &&

							( (coordY < (ScreenFrame.getFrameHeight() * 0.80)) ||
									(coordX > (ScreenFrame.getFrameWidth() * 0.20)) ) &&

							((coordY < (ScreenFrame.getFrameHeight() * 0.80)) ||
									(coordX < (ScreenFrame.getFrameWidth() * 0.80)) )) {

						((ReadActivityManual) mContext).ocultarControles(false);
					}
				}
				else {

						((ReadActivityManual) mContext).mostrarControles(false);
				}
			}
			
			return super.onSingleTapConfirmed(e);
		}
	}

    // This snippet hides the system bars.
    private void hideSystemUI() {

        Log.w(Constants.Log.METHOD, "CustomViewPagerManual - hideSystemUI");

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
	    	

			Log.v(Constants.Log.SCROLL, "CustomViewPagerManual - startScroll Auto");
            if (swipeManually){
                Log.v(Constants.Log.SCROLL, "Manually ");
                Log.v(Constants.Log.SCROLL, "startX="+startX+" startY="+startY+" dx="+dx+" dy="+dy+" duration="+duration+" scroollFactor="+mScrollFactor);
                super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
            }
            else {
                Log.v(Constants.Log.SCROLL, "Auto");
                Log.v(Constants.Log.SCROLL, "startX="+startX+" startY="+startY+" dx="+dx+" dy="+dy+" duration="+duration);
                super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor * 6));
            }
	    }
		
	}
}
