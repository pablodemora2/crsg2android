// Author: Edgar Ríos Cardenas.
// Está es la clase representando el description place.
// author : Pablo Mora G.
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.

package com.istudio.crsurfguide.list;

/**
 * @author Edgar Ríos
 * <p/>
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.custom.BaseActivity;
import com.istudio.crsurfguide.obj.Constante;

public class DescriptionPlace extends BaseActivity {


    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_description);


        this.InitAd();

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(List.EXTRA_MESSAGE);

        String colores = message;

        String[] arrayColores = colores.split("-");

        TextView texto_descripcion = (TextView) this.findViewById(R.id.txtVwDescription2);
        texto_descripcion.setTypeface(Constante.getInstance(this).getFontBody());

        texto_descripcion.setText(arrayColores[7]);
        texto_descripcion.setMovementMethod(new ScrollingMovementMethod());

        TextView texto_locacion = (TextView) this.findViewById(R.id.txtVwLocation2);
        texto_locacion.setText("" + arrayColores[0]);
        texto_locacion.setTypeface(Constante.getInstance(this).getFontBody());

        TextView texto_bestSwell = (TextView) this.findViewById(R.id.txtVwBestSwell2);
        texto_bestSwell.setText("" + arrayColores[2]);
        texto_bestSwell.setTypeface(Constante.getInstance(this).getFontBody());

        this.setTitle(arrayColores[6]);

        TextView texto_playa = (TextView) this.findViewById(R.id.txtVwBeach2);
        texto_playa.setText("" + arrayColores[6]);
        texto_playa.setTypeface(Constante.getInstance(this).getFontBody());

        TextView texto_besttide = (TextView) this.findViewById(R.id.txtVwBestTide2);
        texto_besttide.setText("" + arrayColores[3]);
        texto_besttide.setTypeface(Constante.getInstance(this).getFontBody());

        TextView texto_Dangers = (TextView) this.findViewById(R.id.txtVwDangers2);
        texto_Dangers.setText("" + arrayColores[4]);
        texto_Dangers.setTypeface(Constante.getInstance(this).getFontBody());

        ImageView imagen = (ImageView) this.findViewById(R.id.imageView9);

        int imag = Integer.parseInt(arrayColores[1]);

        // If null, then generic.
        if (imag != -1) {

            imagen.setImageResource(imag);

        } else {

            imagen.setImageResource(R.drawable.generic);
        }

        int imageBackground = com.istudio.R.drawable.shape_photo_black;
        imagen.setBackgroundResource(imageBackground);

        imagen.setScaleType(ImageView.ScaleType.FIT_XY);
        imagen.setPadding(7, 7, 7, 7);

        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar1);

        String text = arrayColores[5]; // example String
        double value = Double.parseDouble(text);

        float varFloat = (float) value;

        ratingBar.setNumStars(5);
        ratingBar.setRating(varFloat);

        InitFonts();

    }

    private void InitFonts() {

        TextView txtDet1 = (TextView) this.findViewById(R.id.txtVwBeach);
        txtDet1.setTypeface(Constante.getInstance(this).getFont());

        TextView txtDet2 = (TextView) this.findViewById(R.id.txtVwLocation);
        txtDet2.setTypeface(Constante.getInstance(this).getFont());

        TextView txtDet3 = (TextView) this.findViewById(R.id.txtVwRating);
        txtDet3.setTypeface(Constante.getInstance(this).getFont());

        TextView txtDet4 = (TextView) this.findViewById(R.id.txtVwDangers);
        txtDet4.setTypeface(Constante.getInstance(this).getFont());

        TextView txtDet5 = (TextView) this.findViewById(R.id.txtVwBestSwell);
        txtDet5.setTypeface(Constante.getInstance(this).getFont());

        TextView txtDet6 = (TextView) this.findViewById(R.id.txtVwBestTide);
        txtDet6.setTypeface(Constante.getInstance(this).getFont());

        TextView txtDet7 = (TextView) this.findViewById(R.id.txtVwDescription);
        txtDet7.setTypeface(Constante.getInstance(this).getFont());

    }

//
//    private void InitAd() {
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
//
//        AdRequest adRequest2 = new AdRequest.Builder().build();
//        mInterstitialAd.loadAd(adRequest2);
//
//
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                requestNewInterstitial();
//                beginSecondActivity();
//            }
//        });
//
//        requestNewInterstitial();
//
//    }
//
//    private void ShowAd() {
//
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        } else {
//            beginSecondActivity();
//        }
//
//    }
//    private void requestNewInterstitial() {
//        AdRequest adRequest;
//        if (Constante.getInstance(this).getDebug()) {
//            adRequest = new AdRequest.Builder()
//                    .build();
//        } else {
//            adRequest = new AdRequest.Builder().build();
//        }
//
//        String adUnitId = "34CBD345E26521469292B95D7F4556BE"; // Pon tu ID de bloque de anuncios de AdMob
//
//        com.google.android.gms.ads.interstitial.InterstitialAd.load(
//                this,
//                adUnitId,
//                adRequest,
//                new com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
//                        mInterstitialAd = interstitialAd;
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull com.google.android.gms.ads.LoadAdError loadAdError) {
//                        mInterstitialAd = null;
//                    }
//                });
//    }
    private void InitAd() {
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();

        // Aquí puedes usar tu ID de prueba de AdMob o el ID que tenías antes
        String adUnitId = "ca-app-pub-3940256099942544/1033173712";

        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                this,
                adUnitId,
                adRequest,
                new com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;

                        // Configuramos el callback para cuando se cierre el anuncio (equivalente a tu antiguo AdListener)
                        mInterstitialAd.setFullScreenContentCallback(new com.google.android.gms.ads.FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Código que se ejecuta cuando el usuario cierra el anuncio
                                requestNewInterstitial();
                                beginSecondActivity();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                mInterstitialAd = null;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull com.google.android.gms.ads.LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    private void ShowAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        } else {
            beginSecondActivity();
        }
    }
//    private void requestNewInterstitial() {
//
//        AdRequest adRequest;
//
//        if (Constante.getInstance(this).getDebug()) {
//
//            adRequest = new AdRequest.Builder()
//                    .addTestDevice("34CBD345E26521469292B95D7F4556BE")
//                    .build();
//
//        }else{
//
//            adRequest = new AdRequest.Builder().build();
//
//        }
//        mInterstitialAd.loadAd(adRequest);
//
//    }
//    // [END request_new_interstitial]
//
    private void beginSecondActivity() {

        requestNewInterstitial();
    }

    @Override
    public void onBackPressed() {
        ShowAd();
        super.onBackPressed();
        return;
    }


}
