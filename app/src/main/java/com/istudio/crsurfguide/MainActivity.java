// Autor: Edgar Ríos Cardenas.
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.


// Esta es la clase o actividad que se inicia de primero.
// En el método onCreate inicializa todo.

// Se necesita mejorar donde se localizan las imagenes sobre el mapa.
// todo se necesita hacer una validacion de offlínea para iniciar de primero el modo online
// si no como esta ahora, el mapa estatico.

// Costa Rica Surf Guide:
//  todo        migrar info
//          ejemplo list objective c
//  todo        migrar info iOS
//          implementar listas generadas y pruebas

package com.istudio.crsurfguide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.istudio.animation.AnimationFactory;
import com.istudio.blureffect.Blur;
import com.istudio.blureffect.ImageUtils;
import com.istudio.blureffect.ScrollableImageView;
import com.istudio.crsurfguide.chart.LineChartActivity;
import com.istudio.crsurfguide.custom.BaseActivity;
import com.istudio.crsurfguide.custom.CustomActivity;
import com.istudio.crsurfguide.custom.CustomMessage;
import com.istudio.crsurfguide.custom.ZoneActivity;
import com.istudio.crsurfguide.custom.Top5Activity;
import com.istudio.crsurfguide.gallery.ImageGallery;
import com.istudio.crsurfguide.map.MapActivity;
import com.istudio.crsurfguide.obj.Constante;
import androidx.compose.ui.platform.ComposeView;
import com.istudio.crsurfguide.ui.debug.DebugHudHelper;
import com.istudio.crsurfguide.ui.debug.LogBuffer;

import org.shredzone.commons.suncalc.MoonIllumination;
import org.shredzone.commons.suncalc.MoonTimes;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends BaseActivity {

    private static final String BLURRED_IMG_PATH = "blurred_image2.png";
    private static final int TOP_HEIGHT = 700;
    private Constante Constante;
    private String[] ParentItems;
    private ViewAnimator viewAnimator;
    private ViewAnimator viewAnimator2;
    private ViewAnimator viewAnimator3;
    private ViewAnimator viewAnimator4;
    private ViewAnimator viewAnimator5;
    private ViewAnimator viewAnimator6;
    private ViewAnimator viewAnimator7;
    private ViewAnimator viewAnimator8;
    private View headerView;
    private ScrollableImageView mBlurredImageHeader;

    // Metodo inicial
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            LogBuffer.INSTANCE.e("CRITICAL", "Uncaught Exception in MainActivity", throwable);
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
        });
        com.istudio.crsurfguide.ui.debug.LogBuffer.INSTANCE.d("MainActivity", "onCreate: Iniciando...");
        super.Custom();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.Constante = com.istudio.crsurfguide.obj.Constante.getInstance(this);
        
        try {
            ComposeView debugHudView = findViewById(R.id.debug_hud_view);
            if (debugHudView != null) {
                androidx.lifecycle.ViewTreeLifecycleOwner.set(debugHudView, this);
                androidx.savedstate.ViewTreeSavedStateRegistryOwner.set(debugHudView, this);
                DebugHudHelper.attachHud(debugHudView);
            }
        } catch (Exception e) {
            LogBuffer.INSTANCE.e("MainActivity", "Error al inyectar DebugHud", e);
        }

        this.ParentItems = new String[this.Constante.getZoneList().size()];

        this.LoadArrays();
        this.SetButtonsStyle();
        this.InitAnimations();


        startIntent2(getResources().getString(R.string.intro) + "\n" + this.Constante
                .calculateTide());

        mBlurredImageHeader = (ScrollableImageView) findViewById(R.id.blurred_image_header);
        initBlurHeader(R.id.blurred_image_header, headerView, mBlurredImageHeader);

        InitAd();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private String calcularIluminacionLuna(){

        String result="";
        Calendar now = GregorianCalendar.getInstance();
        MoonIllumination.Parameters parameters = MoonIllumination.compute()
                .on(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));


            long percent = Math.round(parameters.execute().getFraction() * 100.0);
            result += getString(R.string.moon_today_prefix) + now.get(Calendar.DAY_OF_MONTH) + getString(R.string.moon_today_mid) + String.format(getString(R.string.moon_today_suffix), percent);

        return result;
    }

    private String calcularLunaAlta() {

        final double[] sj = new double[] { 9.93333, -84.08333 };
        String result = "";
        MoonTimes.Parameters parameters = MoonTimes.compute()
                .at(sj)
                .midnight();

        MoonTimes today = parameters.execute();
        result += getString(R.string.moon_rises_sj) + today.getRise().toString().substring(11, 19) + "\n\n";

        parameters.tomorrow();
        MoonTimes tomorrow = parameters.execute();
        result += getString(R.string.moon_tomorrow_rise) + tomorrow.getRise().toString().substring(11, 19)+"\n";

        return result;

    }

    private void InitAd() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // SDK Initialized
                AdView adView = (AdView) findViewById(R.id.adView);
                adView.setVisibility(View.VISIBLE);
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
        });
    }

    // Iniciar intencion 2
    private void startIntent2(String pMsj1) {

        Intent intent = new Intent(this, CustomMessage.class);
        intent.putExtra("key0", pMsj1);
        startActivity(intent);

    }

    // Iniciar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.actionMain1) {
            startIntent(getRes(R.string.intro), this.Constante.calculateTide());
        }
        return true;
    }

    // Iniciar vista n
    public void sendMessage1(View view) {

        this.startIntent(getString(R.string.generalinfo), getString(R.string.generalinfodetail));

    }

    public void sendMessage2(View view) {

        this.Constante.setMap(false);
        Intent intent = new Intent(this, ZoneActivity.class);
        startActivity(intent);

    }

    public void sendMessage3(View view) {
        // Do something in response to button

        try {

            this.Constante.setMap(true);
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);

        } catch (Exception ex) {

        }

    }

    public void sendMessage4(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ImageGallery.class);
        this.Constante.setActivity(this);
        startActivity(intent);

    }

    public void sendMessage6() {

        Intent intent = new Intent(this, Top5Activity.class);
        startActivity(intent);

    }

    public void sendMessage7() {

        Intent intent = new Intent(this, LineChartActivity.class);
        startActivity(intent);

    }

    // Inicializar data
    private void LoadArrays() {

        for (int i = 0; i < this.Constante.getZoneList().size(); ++i) {

            ParentItems[i] = getRes(this.Constante.getZoneList().get(i).getName());

        }

    }

    // Iniciar intencion
    private void startIntent(String pMsj1, String pMsj2) {

        Intent intent = new Intent(this, CustomActivity.class);
        intent.putExtra("key0", pMsj1);
        intent.putExtra("key1", pMsj2);
        startActivity(intent);

    }

    // Iniciar animaciones
    private void InitAnimations() {

        final Animation animBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        final ViewFlipper image1 = (ViewFlipper) findViewById(R.id.viewFlipper3);
        final ViewFlipper image2 = (ViewFlipper) findViewById(R.id.viewFlipper4);
        final ViewFlipper image3 = (ViewFlipper) findViewById(R.id.viewFlipper5);
        final ViewFlipper image4 = (ViewFlipper) findViewById(R.id.viewFlipper6);
        final ViewFlipper image5 = (ViewFlipper) findViewById(R.id.viewFlipper7);

        image1.startAnimation(animBounce);
        image2.startAnimation(animBounce);
        image3.startAnimation(animBounce);
        image4.startAnimation(animBounce);
        image5.startAnimation(animBounce);

        final ViewFlipper image6 = (ViewFlipper) findViewById(R.id.viewFlipper2);
        image6.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));

        viewAnimator = (ViewAnimator) this.findViewById(R.id.viewFlipper);
        viewAnimator2 = (ViewAnimator) this.findViewById(R.id.viewFlipper2);
        viewAnimator3 = (ViewAnimator) this.findViewById(R.id.viewFlipper3);
        viewAnimator4 = (ViewAnimator) this.findViewById(R.id.viewFlipper4);
        viewAnimator5 = (ViewAnimator) this.findViewById(R.id.viewFlipper5);
        viewAnimator6 = (ViewAnimator) this.findViewById(R.id.viewFlipper6);
        viewAnimator7 = (ViewAnimator) this.findViewById(R.id.viewFlipper7);
        viewAnimator8 = (ViewAnimator) this.findViewById(R.id.viewFlipper8);

        this.findViewById(R.id.imgView1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator, AnimationFactory.FlipDirection
                        .LEFT_RIGHT);
                sendMessage7();
            }

        });

        this.findViewById(R.id.imgView2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator, AnimationFactory.FlipDirection
                        .RIGHT_LEFT);
                sendMessage7();
            }

        });

        InitFlip(R.id.viewFlipper2);
        InitFlip3(R.id.viewFlipper3);
        InitFlip4(R.id.viewFlipper4);
        InitFlip5(R.id.viewFlipper5);
        InitFlip6(R.id.viewFlipper6);
        InitFlip7(R.id.viewFlipper7);
        InitFlip8(R.id.viewFlipper8);

    }

    private void InitFlip(int pImg) {

        this.findViewById(R.id.imgView21).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator2, AnimationFactory.FlipDirection
                        .LEFT_RIGHT);

                sendMessage6();
            }

        });

        this.findViewById(R.id.imgView22).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator2, AnimationFactory.FlipDirection
                        .RIGHT_LEFT);

                sendMessage6();
            }

        });
    }

    private void InitFlip3(int pImg) {

        this.findViewById(R.id.imgView31).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator3, AnimationFactory.FlipDirection
                        .LEFT_RIGHT);


                startIntent(getString(R.string.centralpacific), getString(R.string
                        .centralpacificinfo));
            }

        });

        this.findViewById(R.id.imgView32).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator3, AnimationFactory.FlipDirection
                        .RIGHT_LEFT);


                startIntent(getString(R.string.centralpacific), getString(R.string
                        .centralpacificinfo));
            }

        });
    }

    private void InitFlip4(int pImg) {

        this.findViewById(R.id.imgView41).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator4, AnimationFactory.FlipDirection
                        .LEFT_RIGHT);

                startIntent(getString(R.string.northpacific), getString(R.string.northpacificinfo));
            }

        });

        this.findViewById(R.id.imgView42).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator4, AnimationFactory.FlipDirection
                        .RIGHT_LEFT);

                startIntent(getString(R.string.northpacific), getString(R.string.northpacificinfo));
            }

        });
    }

    private void InitFlip5(int pImg) {


        this.findViewById(R.id.imgView51).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator5, AnimationFactory.FlipDirection
                        .LEFT_RIGHT);

                startIntent(getString(R.string.southcaribbean), getString(R.string
                        .southcaribbeaninfo));

            }

        });

        this.findViewById(R.id.imgView52).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator5, AnimationFactory.FlipDirection
                        .RIGHT_LEFT);
                startIntent(getString(R.string.southcaribbean), getString(R.string
                        .southcaribbeaninfo));

            }

        });
    }

    private void InitFlip6(int pImg) {


        this.findViewById(R.id.imgView61).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator6, AnimationFactory.FlipDirection
                        .LEFT_RIGHT);
                startIntent(getString(R.string.southpacific), getString(R.string.southpacificinfo));

            }

        });

        this.findViewById(R.id.imgView62).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator6, AnimationFactory.FlipDirection
                        .RIGHT_LEFT);
                startIntent(getString(R.string.southpacific), getString(R.string.southpacificinfo));

            }

        });
    }

    private void InitFlip7(int pImg) {

        this.findViewById(R.id.imgView71).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator7, AnimationFactory.FlipDirection
                        .LEFT_RIGHT);
                startIntent(getString(R.string.skate), getString(R.string.skateinfo));

            }

        });

        this.findViewById(R.id.imgView72).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator7, AnimationFactory.FlipDirection
                        .RIGHT_LEFT);
                startIntent(getString(R.string.skate), getString(R.string.skateinfo));

            }

        });
    }

    private void InitFlip8(int pImg) {

        this.findViewById(R.id.imgMoon1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator8, AnimationFactory.FlipDirection
                        .LEFT_RIGHT);
                String result = calcularIluminacionLuna() + "\n" + calcularLunaAlta();
                startIntent(getString(R.string.moon_desc), result);

            }

        });

        this.findViewById(R.id.imgMoon2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is all you need to do to 3D flip
                AnimationFactory.flipTransition(viewAnimator8, AnimationFactory.FlipDirection
                        .RIGHT_LEFT);
                String result = calcularIluminacionLuna() + "\n" + calcularLunaAlta();
                startIntent(getString(R.string.moon_desc), result);

            }

        });
    }

    // Setear el tama;o de los textos de los botones
    private void SetButtonsStyle() {

        Button btnInfo = (Button) this.findViewById(R.id.button1);
        btnInfo.setTypeface(this.Constante.getFont());

        Button btnZones = (Button) this.findViewById(R.id.button2);
        btnZones.setTypeface(this.Constante.getFont());

        Button btnOnlineMap = (Button) this.findViewById(R.id.button3);
        btnOnlineMap.setTypeface(this.Constante.getFont());

        Button btnGallery = (Button) this.findViewById(R.id.button4);
        btnGallery.setTypeface(this.Constante.getFont());

        TextView txtMoonState = (TextView) this.findViewById((R.id.txtMoonState));
        txtMoonState.setTypeface(this.Constante.getFont());

    }

    // Metodo para obtener recursos por referencia
    private String getRes(int pPar) {

        return this.getResources().getString(pPar);

    }

    private void initBlurHeader(int pImage, View pView, final ScrollableImageView pImg) {

        final int screenWidth = ImageUtils.getScreenWidth(this);

        pImg.setScreenWidth(screenWidth);

        // Try to find the blurred image
        final File blurredImage = new File(getFilesDir() + BLURRED_IMG_PATH);
        if (!blurredImage.exists()) {

            // launch the progressbar in ActionBar
            setProgressBarIndeterminateVisibility(true);

            new Thread(new Runnable() {

                @Override
                public void run() {

                    // No image found => let's generate it!
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable
                            .background, options);
                    Bitmap newImg = Blur.fastblur(MainActivity.this, image, 12);
                    ImageUtils.storeImage(newImg, blurredImage);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            updateView(screenWidth, pImg);

                            // And finally stop the progressbar
                            setProgressBarIndeterminateVisibility(false);
                        }
                    });

                }
            }).start();

        } else {

            // The image has been found. Let's update the view
            updateView(screenWidth, pImg);

        }

        pView = new View(this);
        pView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                TOP_HEIGHT));

    }

    private void updateView(final int screenWidth, final ScrollableImageView pImg) {
        try {
            Bitmap bmpBlurred = BitmapFactory.decodeFile(getFilesDir() + BLURRED_IMG_PATH);
            if (bmpBlurred != null) {
                bmpBlurred = Bitmap.createScaledBitmap(bmpBlurred, screenWidth, (int) (bmpBlurred
                        .getHeight()
                        * ((float) screenWidth) / (float) bmpBlurred.getWidth()), false);
                pImg.setoriginalImage(bmpBlurred);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Obsolete AsyncTask replaced with modern background Thread structure to prevent build/deprecation issues
    public class MyCustomAsyncTask2 {

        private Context context;

        public MyCustomAsyncTask2(Context context) {
            this.context = context;
        }

        public void execute() {
            // Pre-execute code on main thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Background work here
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Post-execute code on main thread
                            AnimationFactory.flipTransition(viewAnimator, AnimationFactory.FlipDirection
                                    .LEFT_RIGHT);
                        }
                    });
                }
            }).start();
        }
    }

}
