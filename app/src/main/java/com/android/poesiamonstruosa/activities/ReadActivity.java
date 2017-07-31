package com.android.poesiamonstruosa.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.poesiamonstruosa.R;
import com.android.poesiamonstruosa.bitmaps.BitMapUtils;
import com.android.poesiamonstruosa.bitmaps.BitmapLoadStoragedPageTask;
import com.android.poesiamonstruosa.bitmaps.BitmapPageTask;
import com.android.poesiamonstruosa.bitmaps.BitmapSavePageTask;
import com.android.poesiamonstruosa.bitmaps.BitmapTextTask;
import com.android.poesiamonstruosa.customs.CustomPagerAdapter;
import com.android.poesiamonstruosa.customs.CustomScaleAnimationSubs;
import com.android.poesiamonstruosa.customs.CustomTransitionDrawable;
import com.android.poesiamonstruosa.customs.CustomView;
import com.android.poesiamonstruosa.utils.Constants;
import com.android.poesiamonstruosa.utils.Page;
import com.android.poesiamonstruosa.utils.Vignette;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


public abstract class ReadActivity extends FragmentActivity {

	/**
	 * Flag que nos indica si es necesario reescalar las páginas y almacenarlas
	 */
	protected static boolean storageNeeded = false;
	
	
	/**
	 * Array con las referencias a las páginas del cuento
	 */
	public final static Integer[] pages = new Integer[] {
	    
		R.drawable.page_010, R.drawable.page_011,
		R.drawable.page_012, R.drawable.page_013
	};

    protected final static String[] pages_names = new String[] {

            "page_010","page_011",
            "page_012","page_013"
    };

	/**
	 * Array con las referencias a las páginas del cuento sin cuadros de texto
	 */
	protected final static Integer[] pages_ct = new Integer[] {

			R.drawable.page_010, R.drawable.page_011,
			R.drawable.page_012, R.drawable.page_013
	};

	/**
	 * Array con las referencias a las páginas del cuento sin cuadros de texto
	 */
	protected final static Integer[] pages_times = new Integer[] {

			6060, 44147,
			47360, 49920
	};

    /**
     * Cuadro de texto compuesto de líneas que contienen palabras.
     */
    protected static int[][] cuadroTexto_001_01 = {

            {R.drawable.ct_001_001_l001_w001, R.drawable.ct_001_001_l001_w002, R.drawable.ct_001_001_l001_w003},
            {R.drawable.ct_001_001_l002_w001, R.drawable.ct_001_001_l002_w002, R.drawable.ct_001_001_l002_w003,
					R.drawable.ct_001_001_l002_w004},
    };

    /**
     * Cuadro de texto compuesto de líneas que contienen palabras.
     */
    protected static int[][] cuadroTexto_001_01_light = {

            {R.drawable.ct_001_001_l001_w001_pink, R.drawable.ct_001_001_l001_w002_pink, R.drawable.ct_001_001_l001_w003_pink},
            {R.drawable.ct_001_001_l002_w001_pink, R.drawable.ct_001_001_l002_w002_pink, R.drawable.ct_001_001_l002_w003_pink,
					R.drawable.ct_001_001_l002_w004_pink},
    };

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_001_02 = {

			{R.drawable.ct_001_002_l001_w001, R.drawable.ct_001_002_l001_w002},
			{R.drawable.ct_001_002_l002_w001, R.drawable.ct_001_002_l002_w002},
	};

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_001_02_light = {

			{R.drawable.ct_001_002_l001_w001_pink, R.drawable.ct_001_002_l001_w002_pink},
			{R.drawable.ct_001_002_l002_w001_pink, R.drawable.ct_001_002_l002_w002_pink},
	};

	/**
	 * Matriz que contiene los cuadros de texto de una página.
	 */
	protected static int[][][] matrizCT_001 = {

			cuadroTexto_001_01, cuadroTexto_001_02
	};

	/**
	 * Matriz que contiene los cuadros de texto de una página.
	 */
	protected static int[][][] matrizCT_001_light = {

			cuadroTexto_001_01_light, cuadroTexto_001_02_light
	};

	/**
	 * Matriz que contiene los cuadros de texto de una página.
	 */
	protected static int[][][] matrizCT_002 = {

	};

	/**
	 * Matriz que contiene los cuadros de texto de una página.
	 */
	protected static int[][][] matrizCT_002_light = {

	};

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_003_01 = {

			{R.drawable.ic_play_pause_white_18dp},
			{R.drawable.ct_003_001_l001_w001, R.drawable.ct_003_001_l001_w002, R.drawable.ct_003_001_l001_w003,
					R.drawable.ct_003_001_l001_w004, R.drawable.ct_003_001_l001_w005, R.drawable.ct_003_001_l001_w006},
			{R.drawable.ic_play_pause_white_18dp}
	};

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_003_01_light = {

			{R.drawable.ic_play_pause_white_18dp},
			{R.drawable.ct_003_001_l001_w001_pink, R.drawable.ct_003_001_l001_w002_pink, R.drawable.ct_003_001_l001_w003_pink,
					R.drawable.ct_003_001_l001_w004_pink, R.drawable.ct_003_001_l001_w005_pink, R.drawable.ct_003_001_l001_w006_pink},
			{R.drawable.ic_play_pause_white_18dp}
	};

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_003_02 = {

			{R.drawable.ct_003_002_l001_w001, R.drawable.ct_003_002_l001_w002, R.drawable.ct_003_002_l001_w003,
					R.drawable.ct_003_002_l001_w004, R.drawable.ct_003_002_l001_w005, R.drawable.ct_003_002_l001_w006,
					R.drawable.ct_003_002_l001_w007 },
			{R.drawable.ic_play_pause_white_18dp}
	};

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_003_02_light = {

			{R.drawable.ct_003_002_l001_w001_pink, R.drawable.ct_003_002_l001_w002_pink, R.drawable.ct_003_002_l001_w003_pink,
					R.drawable.ct_003_002_l001_w004_pink, R.drawable.ct_003_002_l001_w005_pink, R.drawable.ct_003_002_l001_w006_pink,
					R.drawable.ct_003_002_l001_w007_pink},
			{R.drawable.ic_play_pause_white_18dp}
	};

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_003_03 = {

			{R.drawable.ct_003_003_l001_w001, R.drawable.ct_003_003_l001_w002, R.drawable.ct_003_003_l001_w003,
					R.drawable.ct_003_003_l001_w004, R.drawable.ct_003_003_l001_w005, R.drawable.ct_003_003_l001_w006,
					R.drawable.ct_003_003_l001_w007, R.drawable.ct_003_003_l001_w008, R.drawable.ct_003_003_l001_w009 },
			{R.drawable.ic_play_pause_white_18dp}
	};

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_003_03_light = {

			{R.drawable.ct_003_003_l001_w001_pink, R.drawable.ct_003_003_l001_w002_pink, R.drawable.ct_003_003_l001_w003_pink,
					R.drawable.ct_003_003_l001_w004_pink, R.drawable.ct_003_003_l001_w005_pink, R.drawable.ct_003_003_l001_w006_pink,
					R.drawable.ct_003_003_l001_w007_pink, R.drawable.ct_003_003_l001_w008_pink, R.drawable.ct_003_003_l001_w009_pink},
			{R.drawable.ic_play_pause_white_18dp}
	};

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_003_04 = {

			{R.drawable.ct_003_004_l001_w001, R.drawable.ct_003_004_l001_w002, R.drawable.ct_003_004_l001_w003,
					R.drawable.ct_003_004_l001_w004, R.drawable.ct_003_004_l001_w005, R.drawable.ct_003_004_l001_w006,
					R.drawable.ct_003_004_l001_w007, R.drawable.ct_003_004_l001_w008, R.drawable.ct_003_004_l001_w009 }
	};

	/**
	 * Cuadro de texto compuesto de líneas que contienen palabras.
	 */
	protected static int[][] cuadroTexto_003_04_light = {

			{R.drawable.ct_003_004_l001_w001_pink, R.drawable.ct_003_004_l001_w002_pink, R.drawable.ct_003_004_l001_w003_pink,
					R.drawable.ct_003_004_l001_w004_pink, R.drawable.ct_003_004_l001_w005_pink, R.drawable.ct_003_004_l001_w006_pink,
					R.drawable.ct_003_004_l001_w007_pink, R.drawable.ct_003_004_l001_w008_pink, R.drawable.ct_003_004_l001_w009_pink}
	};

	/**
	 * Matriz que contiene los cuadros de texto de una página.
	 */
	protected static int[][][] matrizCT_003 = {

			cuadroTexto_003_01, cuadroTexto_003_02, cuadroTexto_003_03, cuadroTexto_003_04
	};

	/**
	 * Matriz que contiene los cuadros de texto de una página.
	 */
	protected static int[][][] matrizCT_003_light = {

			cuadroTexto_003_01_light, cuadroTexto_003_02_light, cuadroTexto_003_03_light,cuadroTexto_003_04_light
	};


	/**
	 * Contiene las posiciones en píxeles de un cuadro de texto.
	 */
	protected static Point[] arrayPositionsCT_001 = {

			new Point(175, 220),
            new Point(671, 774)
	};


	/**
	 * Contiene las posiciones en píxeles de un cuadro de texto.
	 */
	protected static Point[] arrayPositionsCT_002 = {

	};
	/**
	 * Contiene las posiciones en píxeles de un cuadro de texto.
	 */
	protected static Point[] arrayPositionsCT_003 = {

			new Point(300, 970),
			new Point(330, 1160),
			new Point(270, 1330),
			new Point(240, 1510),
	};


	/**
	 * Contiene los tiempos de cada línea dentro de un cuadro de texto
	 */
	protected static int[][][] arrayTimesCT_001 = {

			//La vampira Muriel || y su apestoso Gato
			//2350, 2800, 3300, 3400, 3600, 4000, 4500
			{{350,450,500},{100,200,400,500}},
			//Con gafotas || y aparato
			//4800, 5400, 5600, 6000
			{{300,600},{200,400}}
	};

	/**
	 * Contiene los tiempos de cada línea dentro de un cuadro de texto
	 */
	protected static int[][][] arrayTimesCT_002 = {

	};

	/**
	 * Contiene los tiempos de cada línea dentro de un cuadro de texto
	 */
	protected static int[][][] arrayTimesCT_003 = {

			//          Hay    que    ver    que    pinta  llevas!
			//          31450, 31700, 31850, 32100, 32300, 32600, 33100
			{{0, 29450},{250,  150,   250,   200,   300,   500}, {0, 1100}},
			//Contigo así   no     querrá bailar  ni    "elTato"
			//34200, 34800, 35000, 35300, 35700, 36100, 36250, 36900
			{{600,   200,   300,   400,   400,   150,   650}, {0, 2950}},
			//Me     da     cosa,   me     da    miedo,  me     da    grima!
			//39850, 40050, 40200, 40650, 40900, 41050, 41450, 41600, 41800, 42300
			{{200,   150,   450,   250,   150,   400,   150,   200,   500   },{0, 2950}},
			//Yo     me     quedo   en     mi    casa,   con    mi     gato!
			//45250, 45450, 45650, 45950, 46100, 46250, 46550, 46800, 46900, 47300
			{{200,   200,   300,   150,   150,   300,   250,   100,   400   }},

	};


	protected static int [][][][] matrizCT = {

            matrizCT_001, matrizCT_002, matrizCT_003
	};

	protected static int [][][][] matrizCT_light = {

            matrizCT_001_light, matrizCT_002_light, matrizCT_003_light
	};

	protected static Point [][] matrizPositionsCT = {

            arrayPositionsCT_001, arrayPositionsCT_002, arrayPositionsCT_003
	};

	protected static int [][][][] matrizTiemposCT = {

            arrayTimesCT_001, arrayTimesCT_002, arrayTimesCT_003
	};

    public static Integer[][] matrixPagVignette = new Integer[][]{
            {R.drawable.page_010_v001},
            {R.drawable.page_011_v001, R.drawable.page_011_v002, R.drawable.page_011_v003},
            {R.drawable.page_012_v001, R.drawable.page_012_v002, R.drawable.page_012_v003},
            {R.drawable.page_013_v001, R.drawable.page_013_v002, R.drawable.page_013_v003}
    };

	public static final HashMap<String, Vignette> mapaVignettes = new HashMap<>();
	public static final HashMap<String, Page> mapaPages = new HashMap<>();

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    protected CustomPagerAdapter mAdapter;
    
    /**
     * Texto nº de página 1
     */
    protected TextView numPagTextView;
    
    /**
     * Texto nº de página 2
     */
    protected TextView numPagTextView_2;
    
    protected ImageButton close_btn;
    
    //Animaciones
    protected Animation animFadeOutLong;
	protected AnimationSet showPlay;
    protected ScaleAnimation showProgressBar;
	protected AnimationSet clickPlay;
	protected AnimationSet clickPlayForPause;
    protected Animation clickProgressBar;
    protected AnimationSet hideCloseForReveal;
    protected AnimationSet hideCloseBL;
    protected AnimationSet hideLikeForLikeTransition;
    protected AnimationSet clickRefresh;
    protected AnimationSet clickVoice;
    protected AnimationSet clickMusic;
    protected Animation showBottomControls;
    protected AnimationSet hideBottomControls;
    protected TranslateAnimation showTopLeftControls;
    protected TranslateAnimation hideTopLeftControls;
    protected TranslateAnimation showTopRightControls;
    protected TranslateAnimation hideTopRightControls;
	protected AnimationSet hidePlay;
	protected ScaleAnimation hideProgressBar;
    protected Animation hideClose;
    protected Animation hideMusic;
    protected Animation hideVoice;
	protected Animation hideHighlight;
	protected Animation hideHighlightClose;
    protected Animation hideHighlightBackgr;
    protected AnimationSet hideHighlightIcon;
    protected AnimationSet hideHighlightIconClose;
    protected AnimationSet hideHighlight_1;
    protected AnimationSet hideHighlight_2;
	protected AnimationSet hideHighlight_1_long;
	protected AnimationSet hideHighlight_2_long;
    protected AnimationSet hideRefresh;
	protected AnimationSet hideRefreshForRestart;
    protected Animation showClose;
    protected Animation showMusic;
    protected Animation showVoice;
	protected Animation showHighlight;
	protected Animation showHighlightBackgr;
    protected AnimationSet showHighlightIcon;
    protected AnimationSet showHighlightClose;
	protected AnimationSet showHighlight_1;
    protected AnimationSet showHighlight_2;
    protected AnimationSet showRefresh;
	protected AnimationSet showLikeSet;
    protected AnimationSet hideLike;
    protected AnimationSet hideLikeForRestartBL;
    protected ScaleAnimation heartBeatAutoplayEnd;
    protected Animation autoplayEndReturn;
	protected AnimationSet preRestartTransition;
	protected AnimationSet hidePlayForLikeTransition;
    protected ScaleAnimation showPlayRestart;
    protected ScaleAnimation showPlayRestartBL;
    protected TranslateAnimation pagerInTransition;
    protected TranslateAnimation pagerInTransitionIntro;
	protected TranslateAnimation pagerOutTransition;
    protected TranslateAnimation pagerOutTransitionExit;
	protected TranslateAnimation pagerOutTransitionLike;
    
    /**
   	 * Es un temporizador que dispara el desvanecimiento de los controles. 
   	 */
   	protected Timer fadeOutTimer;
   	/**
   	 * Tarea que dispasar el temporizador anterior. Realizará el desvanecimiento de los controles.
   	 */
   	protected TimerTask fadeOutTimerTask;
   	/**
   	 * Manejador para el hilo del temporizador de desvanecimiento de controles.
   	 */
   	protected Handler mHandler = new Handler();

    /**
     * Layout de los controles medios
     */
    protected FrameLayout controlsLayout;
    /**
     * Layout para el efecto reveal
     */
    protected FrameLayout revealLayout;
    /**
     * Layout general
     */
    protected FrameLayout mainLayout;
    /**
     * Layout de barra de color inferior
     */
    protected RelativeLayout colorBarLayout;
    
    /**
     * Layout de controles inferiores
     */
    protected FrameLayout controls_bottom;

	/**
	 * Layout de controles superiores derecha
	 */
	protected FrameLayout controls_top_left;
    /**
     * Layout de controles superiores derecha
     */
    protected FrameLayout controls_top_right;

    /**
     * Controla que solo mostremos los controles al venir de un onPause(). 
     */
    protected boolean resumeShowControls = false;
  
    /**
     * Controla si los controles están visibles
     */
    protected boolean areControlsVisible = false;
	/**
     * Contexto
     */
	protected Context mContext;
	/**
     * Vista para gestionar los cambios de visibilidad de las barras de status y nav
     */
	protected View mDecorView;
	/**
     * Botón de pausa/play
     */
	protected ImageButton play_btn;
	/**
     * Botón de audio
     */
	protected ImageButton music_btn;
	/**
     * Botón de voz
     */
	protected ImageButton voice_btn;
	/**
     * Botón de actualizar página
     */
	protected ImageButton refresh_btn;
	/**
     * Barra de progreso
     */
	protected ProgressBar progressBar;
	/**
     * Controla que la música siga sonando al terminar la actividad. En caso de volver
     * a la actividad anterior seguirá sonando.
     */
	protected boolean continue_music = false;
	/**
     * Constante que controla si el autoplay está parado o no.
     */
	protected boolean paused = Constants.Autoplay.PAUSADO_INICIO;
	/**
	 * Constante que controla hemos llegado al final del autoplay.
	 */
	protected boolean finCuento = false;
	/**
     * Constante que controla que se está reiniciando el autoplay.
     * Para no interrumpir la animación.
     */
	protected boolean btn_locked = false;
	/**
     * Constante que controla que se está reiniciando el autoplay.
     * Para no interrumpir la animación.
     */
	protected boolean tap_locked = false;
	/**
	 * Si es mayor que cero se aplica el lock
	 */
	private int lockButtons = 0;
	/**
	 * Si es mayor que cero se aplica el lock
	 */
	private int lockTap = 0;
	/**
     * Indica si el timer está al 100%. Para controlar cuando mostrar refresh_btn.
     */
	protected boolean timerLleno = true;
	/**
     * Botón de like
     */
	protected ImageButton like_btn;
	/**
     * Millis que han transcurrido del autoplay
     * Lo utilizamos para saber donde reniciar la voz al hacer onRestart
     */
	protected int tiempo;
	/**
     * Botón de anterior página
     */
	protected ImageButton arrow_left_btn;
	/**
     * Botón de siguiente página
     */
	protected ImageButton arrow_right_btn;
	/**
     * Controla que el autoplay de una pág haya terminado
     */
	protected boolean nextPagina = false;

	public void setTiempo(int tiempo) {

        Log.v(Constants.Log.METHOD, "ReadActivity - setTiempo");

        this.tiempo = tiempo;
    }

	/**
     * Indica si estamos en el estado fin de página
     * @return
     */
    public boolean isNextPagina() {

        return nextPagina;
    }

	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);

        loadPages();
        loadVignettes();
	}

    /**
     * Carga información sobre las páginas, su resolución.
     */
    private void loadPages() {

        Log.v(Constants.Log.METHOD, "ReadActivity loadPages");
        mapaPages.put("" + R.drawable.page_010, new Page(
                BitMapUtils.getResolution(getResources(), R.drawable.page_010, 1)));
        mapaPages.put("" + R.drawable.page_011, new Page(
                BitMapUtils.getResolution(getResources(), R.drawable.page_011, 1)));
        mapaPages.put("" + R.drawable.page_012, new Page(
                BitMapUtils.getResolution(getResources(), R.drawable.page_012, 1)));
        mapaPages.put("" + R.drawable.page_013, new Page(
                BitMapUtils.getResolution(getResources(), R.drawable.page_013, 1)));
    }

    /**
     * Carga las coordenadas de las viñetas
     */
    private void loadVignettes() {
        Log.v(Constants.Log.METHOD, "ScreenSlidePagerActivity loadVignettes");

        int vigSeq = 0;
        int vigPag = 1;

        mapaVignettes.put("" + R.drawable.page_010_v001, new Vignette(200, 580, 1060, 1410,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_010_v001, 1)));
        vigPag++;

        mapaVignettes.put("" + R.drawable.page_011_v001, new Vignette(170, 60, 990, 560,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_011_v001, 1)));
        mapaVignettes.put("" + R.drawable.page_011_v002, new Vignette(170, 570, 990, 1160,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_011_v002, 1)));
        mapaVignettes.put("" + R.drawable.page_011_v003, new Vignette(170, 1150, 990, 1650,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_011_v003, 1)));
        vigPag++;

        mapaVignettes.put("" + R.drawable.page_012_v001, new Vignette(155, 50, 1055, 580,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_012_v001, 1)));
        mapaVignettes.put("" + R.drawable.page_012_v002, new Vignette(155, 570, 1055, 940,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_012_v002, 1)));
        mapaVignettes.put("" + R.drawable.page_012_v003, new Vignette(155, 920, 1055, 1640,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_012_v003, 1)));
        vigPag++;

        mapaVignettes.put("" + R.drawable.page_013_v001, new Vignette(110, 50, 1090, 280,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_013_v001, 1)));
        mapaVignettes.put("" + R.drawable.page_013_v002, new Vignette(110, 300, 1090, 770,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_013_v002, 1)));
        mapaVignettes.put("" + R.drawable.page_013_v003, new Vignette(110, 770, 1090, 1620,
                vigPag, vigSeq++,
                BitMapUtils.getResolution(getResources(), R.drawable.page_013_v003, 1)));
        vigPag++;
    }

    /**
     * Decodifica, escala y almacena las páginas
     *
     */
    public static void decodeScaleAndSaveAllPages(int frameWidth, int frameHeight, Context context){

        Log.v(Constants.Log.METHOD, "ReadActivity - decodeScaleAndSaveAllPages");

				//Marcamos que se van a almacenar las páginas para que ReadFragment las recupere
				storageNeeded = true;

        Bitmap bitmap;

        for (int i=0; i<pages.length; i++) {

            File file = new File(context.getFilesDir(), pages_names[i]);
            if (file == null) {
              Log.e(Constants.Log.STORE,
                  "Error creating media file, check storage permissions: ");// e.getMessage());
              return;
            }
            //Si ya existiera no decodificamos y almacenamos
            if (!file.exists()) {

              //Calculamos el coeficiente para reescalar las imagenes y ajustarlas a la pantalla del dispositivo
              float scale = BitMapUtils.calculateScale(
                  new Point(frameWidth, frameHeight),
                  BitMapUtils.getResolution(context.getResources(), pages[i], 1));
              Log.v(Constants.Log.SIZE, "ReadActivity Page "+i+" - Scale: "+scale);

              savePageBitmap(pages[i], context, scale, pages_names[i]);
            }
        }
    }

	/**
     * Carga una imagen en la página.
     * @param resId
     * @param imageView
     */
    public void loadPageBitmap(int resId, CustomView imageView, Point frameRes, float scale) {
    	
    	Log.v(Constants.Log.METHOD, "ReadActivity - loadPageBitmap");
    	/*
	 	final String imageKey = String.valueOf(resId);

	    final Bitmap bitmap = getBitmapFromMemCache(imageKey);
	    if (bitmap != null) {
	    	Log.i("II", "Caché!!");
	    	imageView.setImageBitmap(bitmap);
	    } else {
	    	Log.i("II", "Not found in cache: "+imageKey);
	    	//imageView.setImageResource(R.drawable.image_placeholder);
	        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
	        task.execute(resId);
	    }
	    */
    	
	 	BitmapPageTask task = new BitmapPageTask(imageView, getResources(), frameRes, scale);
        task.execute(resId);
    }

    /**
     * Carga una imagen en la página guardada previamente.
     * @param resId
     * @param imageView
     */
    public void loadSavedPageBitmap(String resId, CustomView imageView) {

        Log.v(Constants.Log.METHOD, "ReadActivity - loadSavedPageBitmap");

        BitmapLoadStoragedPageTask task = new BitmapLoadStoragedPageTask(imageView, mContext);
        task.execute(resId);
    }

    /**
     * Carga una imagen en la página.
     * @param resId
     */
    public static void savePageBitmap(int resId, Context context, float scale, String pageName) {

        Log.v(Constants.Log.METHOD, "ReadActivity - loadPageBitmap");

        BitmapSavePageTask task = new BitmapSavePageTask(context, scale, pageName);
        task.execute(resId);
    }
    
    /**
     * Carga los cuadros de texto o subtitulado.
     * Genera la vistas de los cuadros de texto y las añade al marco de la página (frame),
     * así como un array con las referencias a todas las vistas de las palabras (arrayVistas) para animarlas después.
     * @param frame Marco de la página
     * @param arrayTransiciones Array con todas las vistas o palabras de los cuadros de texto.
     * @param pageNum página en la que nos encontramos.
     */
    public void loadTextFrames (int resId, RelativeLayout frame, Point frameRes,
                                ArrayList<CustomTransitionDrawable> arrayTransiciones,
                                int pageNum) {
		
    	Log.v(Constants.Log.METHOD, "ReadActivity - loadTextFrames");
    	
    	//Protegemos de un fallo de configuración, en caso de que haya más páginas que entradas en la matriz
    	if (pageNum >= matrizCT.length ||
				pageNum >= matrizCT_light.length ||
    			pageNum >= matrizPositionsCT.length ||
    			pageNum >= matrizTiemposCT.length) {
    		
    		Log.e(Constants.Log.SUBS, "Cuadros texto sin definir");
    		Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" >= MatrizCT.lenght = "+ matrizCT.length);
			Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" >= MatrizCT_light.lenght = "+ matrizCT_light.length);
    		Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" >= matrizPositionsCT.lenght = "+ matrizPositionsCT.length);
    		Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" >= matrizTiemposCT.lenght = "+ matrizTiemposCT.length);
    	}
		else if (matrizCT[pageNum].length == 0 ||
					matrizCT_light[pageNum].length == 0 ||
					matrizPositionsCT[pageNum].length == 0 ||
					matrizTiemposCT[pageNum].length == 0 ) {

			Log.e(Constants.Log.SUBS, "Cuadros texto sin definir");
			Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" en MatrizCT sin definir");
			Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" en MatrizCT_light sin definir");
			Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" en matrizPositionsCT sin definir");
			Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" en matrizTiemposCT sin definir");
		}
    	else {
    		
    		BitmapTextTask task = new BitmapTextTask(this, getResources(), frame, frameRes,
    				arrayTransiciones, 
    				matrizCT[pageNum], matrizCT_light[pageNum],
    				matrizTiemposCT[pageNum], matrizPositionsCT[pageNum]);
            task.execute(resId);
    	}
	}
    
    /**
     * Cancela todas las animaciones, lo utilizamos al pausar el autoplay
     * @param arrayAnimaciones array con todas las animaciones
     */
    public void cancelAnimations(ArrayList<CustomScaleAnimationSubs> arrayAnimaciones) {
    	
    	Log.v(Constants.Log.METHOD, "ReadActivity - cancelAnimations");
    	
    	Iterator<CustomScaleAnimationSubs> iteratorAnimaciones = arrayAnimaciones.iterator();
		
		while (iteratorAnimaciones.hasNext()) {
			
			iteratorAnimaciones.next().cancel();
		}
    }
    
    /**
     * Cancela todas las animaciones de resaltado, lo utilizamos al pausar el autoplay
     * @param arrayHighlights array con todas las animaciones
     */
    public void cancelHighlights(ArrayList<CustomTransitionDrawable> arrayHighlights) {
    	
    	Log.v(Constants.Log.METHOD, "ReadActivity - cancelHighlights");
    	
    	Iterator<CustomTransitionDrawable> iteratorHighlights = arrayHighlights.iterator();
		
		while (iteratorHighlights.hasNext()) {
			
			mHandler.removeCallbacks(iteratorHighlights.next());
		}
    }
    
    /**
     * Reinicia todas las animaciones de resaltado con una animación de retorno, lo utilizamos al pulsar el botón refresh.
     * @param arrayHighlights array con todas las animaciones
     */
    public void resetHighlights(ArrayList<CustomTransitionDrawable> arrayHighlights) {
    	
    	Log.v(Constants.Log.METHOD, "ReadActivity - resetHighlights");
    	
    	Iterator<CustomTransitionDrawable> iteratorHighlights = arrayHighlights.iterator();
		
		while (iteratorHighlights.hasNext()) {
			
			CustomTransitionDrawable transition = iteratorHighlights.next();
			
			if (transition.isFirstStepDone() && !transition.isFinished()) {
				
				mHandler.post(transition);
			}
		}
    }
    
    /**
     * Reinicia el estado de las animaciones, lo utilizamos al pulsar el botón refresh.
     * Se tiene que ejecutar después de que se empiecen a reiniciar las animaciones por eso lo separamos.
     * @param arrayHighlights array con todas las animaciones
     */
    public void resetStateHighlights(ArrayList<CustomTransitionDrawable> arrayHighlights) {
    	
    	Log.v(Constants.Log.METHOD, "ReadActivity - resetStateHighlights");
    	
    	Iterator<CustomTransitionDrawable> iteratorHighlights = arrayHighlights.iterator();
		
		while (iteratorHighlights.hasNext()) {
			
			iteratorHighlights.next().reset();
		}
    }
    
    /**
     * Reinicia el estado de las animaciones y la animación en sí,
     * lo utilizamos al entrar en un página por si volviéramos después de haber estado en ella antes.
     * @param arrayHighlights array con todas las animaciones
     */
    public void resetFullHighlights(ArrayList<CustomTransitionDrawable> arrayHighlights) {
    	
    	Log.w(Constants.Log.METHOD, "ReadActivity - resetFullHighlights");
    	
    	Iterator<CustomTransitionDrawable> iteratorHighlights = arrayHighlights.iterator();
		
		while (iteratorHighlights.hasNext()) {
			
			CustomTransitionDrawable transition = iteratorHighlights.next();
			
			transition.resetTransition();
			transition.reset();
		}
    }
    
    /**
     * Reanuda o empieza las animaciones
     * @param arrayAnimaciones arrayAnimaciones array con todas las animaciones
     * @param tiempoTranscurrido tiempo que ha pasado del autoplay
     */
    public void startResumeAnimations(ArrayList<CustomScaleAnimationSubs> arrayAnimaciones,
    		int tiempoTranscurrido) {
    	
    	Log.v(Constants.Log.METHOD, "ReadActivity - startResumeAnimations");
    	
    	Iterator<CustomScaleAnimationSubs> iteratorAnimaciones = arrayAnimaciones.iterator();
    	
    	//Variable para llevar el offset trancurrido en general
		long startTime = 0;
    	long tiempoResume = 0;
		long tiempoPalabra = 0;
    	
		while (iteratorAnimaciones.hasNext()) {
			
			CustomScaleAnimationSubs anim = iteratorAnimaciones.next();
			
			//Tiempo de la animación de la palabra
			tiempoPalabra = anim.getDuration();
			//tiempoResume seria el time offset para las animaciones que vamos a resumir
			tiempoResume = startTime - tiempoTranscurrido;

			//Log.w(Constants.Log.SUBS, "ReadActivity - startResumeAnimations - tiempoResume = "+tiempoResume);
			
			// Si es mayor entramos en las animaciones a resumir
			if (tiempoResume >= 0) {
				
				anim.setStartOffset(tiempoResume);
				//Pasarle el TransitionDrawable y que lo empiece en onStart
				anim.startAnimation();
			}
			
			//Actualizamos el offset general
			startTime += tiempoPalabra * Constants.Subs.CF_START_OFFSET;
		}
    }
    
    /**
     * Reanuda o empieza las animaciones
     * @param arrayHighlights arrayAnimaciones array con todas las animaciones
     * @param tiempoTranscurrido tiempo que ha pasado del autoplay
     */
    public void startResumeHighlights(ArrayList<CustomTransitionDrawable> arrayHighlights,
    		int tiempoTranscurrido) {
    	
    	Log.v(Constants.Log.METHOD, "ReadActivity - startResumeHighlights");
    	
    	Iterator<CustomTransitionDrawable> iteratorHighlights = arrayHighlights.iterator();
    	
    	//Variable para llevar el offset trancurrido en general
		long startTime = Constants.Subs.INITIAL_DELAY;
    	long tiempoResume = 0;
		long tiempoPalabra = 0;
		int index = 0;
    	
		while (iteratorHighlights.hasNext()) {
			
			CustomTransitionDrawable transition = iteratorHighlights.next();
			
			//Tiempo de la animación de la palabra
			tiempoPalabra = transition.getDuration();
			//tiempoResume seria el time offset para las animaciones que vamos a resumir
			tiempoResume = startTime - tiempoTranscurrido;
			
			Log.w(Constants.Log.SUBS, "ReadActivity - startResumeHighlights - tiempoTranscurrido = "+tiempoTranscurrido);
			Log.w(Constants.Log.SUBS, "startResumeHighlights - Animacion "+(++index)+" = "+tiempoResume);
			Log.w(Constants.Log.SUBS, "startResumeHighlights - Animacion "+(index)+" Retorno = "
					+(tiempoPalabra+Constants.Subs.BETWEEN_DELAY)+" ReadActivity");
			
			
			//Comprobamos en que estado está la animación
			if (!transition.isFinished()) {
				
				if (!transition.isFirstStepDone()) {
					
					mHandler.postDelayed(transition, tiempoResume);
					mHandler.postDelayed(transition, tiempoResume+tiempoPalabra+Constants.Subs.BETWEEN_DELAY);
				} 
				//No se ha producido la anim retorno por lo que la planificamos
				else {
					
					mHandler.postDelayed(transition, tiempoResume+tiempoPalabra+Constants.Subs.BETWEEN_DELAY);
				}
			}
			
			//Actualizamos el offset general
			startTime += tiempoPalabra;
		}
    }
    
    /**
     * Calcula el tiempo de autoplay de una página en base a los tiempos introducidos en configuración.
     * @param pageNum Nº de página.
     * @return
     */
    public int getTiempoAutoPlay(int pageNum) {
    	
    	Log.v(Constants.Log.METHOD, "ReadActivity - getTiempoAutoPlay");

		//Protegemos de un fallo de configuración, en caso de que haya más páginas que entradas en la matriz
		if (pageNum >= matrizCT.length ||
				pageNum >= matrizCT_light.length ||
				pageNum >= matrizPositionsCT.length ||
				pageNum >= matrizTiemposCT.length) {

			Log.e(Constants.Log.SUBS, "Cuadros texto sin definir");
			Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" >= MatrizCT.lenght = "+ matrizCT.length);
			Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" >= MatrizCT_light.lenght = "+ matrizCT_light.length);
			Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" >= matrizPositionsCT.lenght = "+ matrizPositionsCT.length);
			Log.e(Constants.Log.SUBS, "Nº pag = "+pageNum+" >= matrizTiemposCT.lenght = "+ matrizTiemposCT.length);
    		
    		return pages_times[pageNum];
    	}
    	else {
    	
	    	int startTime = Constants.Subs.INITIAL_DELAY;
	    	int numCT = matrizCT[pageNum].length;
	    	int numPalabras = 0;
	    	int tiempoPalabra = 0;

			//Si no hay tiempos de highlight para una página devolvemos tiempo total
			if (numCT == 0) return  pages_times[pageNum];
	    	
	    	for (int ctIndex=0; ctIndex<numCT; ctIndex++) {
	    		
	    		//Recorremos líneas
	    		for (int lineaIndex=0; lineaIndex<matrizCT[pageNum][ctIndex].length; lineaIndex++) {

					//Comprobamos que no sea un silencio
					if (matrizTiemposCT[pageNum][ctIndex][lineaIndex][0] == 0) {

						startTime += matrizTiemposCT[pageNum][ctIndex][lineaIndex][1];
					}
					else{

						numPalabras = matrizCT[pageNum][ctIndex][lineaIndex].length;

						//Recorremos palabras en cuadro de texto
						for (int wordIndex=0; wordIndex<numPalabras; wordIndex++) {

							startTime += matrizTiemposCT[pageNum][ctIndex][lineaIndex][wordIndex];
						}
					}
	    		}
	    	}
	    	startTime += tiempoPalabra;
	    	startTime += Constants.Subs.FINAL_DELAY;
	    	return startTime;
    	}
    }

	/**
	 * Nos dice si es necesario almacenar las páginas.
	 * @return
	 */
	public static boolean isStorageNeeded() {
		return storageNeeded;
	}


	/**
	 * Bloquea el botón de play
	 */
	public void lockTap() {

		lockTap++;
		Log.i(Constants.Log.METHOD, "ReadActivity - lockTap = "+lockTap);

		//tap_locked = true;
	}
	/**
	 * Desbloquea el botón de play
	 */
	public void unlockTap() {

		lockTap--;
		Log.i(Constants.Log.METHOD, "ReadActivity - unlockTap = "+lockTap);
		//Protegemos que no baje de cero
		if (lockTap < 0) lockTap = 0;

		//tap_locked = false;
	}

	/**
	 * Comprueba si el botón está bloqueado o no
	 * @return
	 */
	public boolean isTapLocked() {

		Log.w(Constants.Log.METHOD, "ReadActivity - isTapLocked - "+(lockTap > 0));

		return (lockTap > 0)? true : false;
		//return tap_locked;
	}

	/**
	 * Bloquea el botón de play
	 */
	public void lockButtons() {

		lockButtons++;
		Log.i(Constants.Log.METHOD, "ReadActivity - lockButtons = "+lockButtons);

		//btn_locked = true;
	}

	/**
	 * Desbloquea el botón de play
	 */
	public void unlockButtons() {

		lockButtons--;
		Log.i(Constants.Log.METHOD, "ReadActivity - unlockButtons = "+lockButtons);
		//Protegemos que no baje de cero
		if (lockButtons < 0) lockButtons = 0;

		//btn_locked = false;
	}

	/**
	 * Desbloqueo forzado
	 */
	public void unlockForced(){

		Log.i(Constants.Log.METHOD, "ReadActivity - unlockForced - btns = "+lockButtons+" - tap = "+lockTap);

		lockButtons = 0;
		lockTap = 0;
	}

	/**
	 * Comprueba si el botón está bloqueado o no
	 * @return
	 */
	public boolean areButtonsLocked() {

		Log.v(Constants.Log.METHOD, "ReadActivity - areButtonsLocked: "+(lockButtons > 0));

		return (lockButtons > 0)? true : false;
		//return btn_locked;
	}

    /**
     * Programa desbloqueo dentro de "duration" ms.
     * @param duration
     */
    protected void programUnlock(long duration) {

        Log.i(Constants.Log.METHOD, "ReadActivity - programUnlock: "+duration);

        //Programamos desbloqueo
        new Handler().postDelayed(new Runnable() {
            public void run() {

                unlockButtons();
                unlockTap();
            }
        }, duration);
    }

}
