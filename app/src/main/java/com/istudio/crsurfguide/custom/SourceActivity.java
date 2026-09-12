package com.istudio.crsurfguide.custom;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;
import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.obj.Constante;
import com.istudio.maps.GPSTracker;

import java.util.ArrayList;

public class SourceActivity extends BaseActivity {

    private Constante cons;
    private GPSTracker Gps;
    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cons = Constante.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
        this.SetButtonsTextSize();
    }

    private void SetButtonsTextSize() {

        Button btnSetSource1 = (Button) this.findViewById(R.id.btnSetSource1);
        btnSetSource1.setTypeface(cons.getFont());

        Button btnSetSource2 = (Button) this.findViewById(R.id.btnSetSource2);
        btnSetSource2.setTypeface(cons.getFont());

        Button btnSetSource3 = (Button) this.findViewById(R.id.btnSetSource3);
        btnSetSource3.setTypeface(cons.getFont());

        Button btnSetSource4 = (Button) this.findViewById(R.id.btnSetSource4);
        btnSetSource4.setTypeface(cons.getFont());

    }

    private String getRes(int pPar) {

        return this.getResources().getString(pPar);

    }

    // Cada uno de estos metodos llaman a iniciar una actividad para recibir un resultado
    // ("traducido literalmente.")

    public void SetSource1(View view) {

        cons.setOrigin(cons.getOriginList().get(0));
        InitActForResult(R.string.sanjose);

    }

    public void SetSource2(View view) {

        cons.setOrigin(cons.getOriginList().get(1));
        InitActForResult(R.string.alajuelaairport);

    }

    public void SetSource3(View view) {

        cons.setOrigin(cons.getOriginList().get(2));
        InitActForResult(R.string.liberiaairport);

    }

    public void SetSource4(View view) {

        cons.setOrigin(cons.getOriginList().get(3));
        this.GetGPS();
        InitActForResult(R.string.mylocation);

    }

    // Metodo generico o funcion. recibe de parametro el entero que referencia el recurso en
    // memoria.

    private void InitActForResult(int pSrc) {

        Intent output = new Intent();
        output.putExtra("coord1", "2015");

        setResult(RESULT_OK, output);
        finish();

        super.onBackPressed();

    }

    private void GetGPS() {

        this.Gps = new GPSTracker(getApplicationContext());

        if (this.Gps.canGetLocation()) {

            // check if GPS enabled
            if (Gps.canGetLocation()) {

                latitude = Gps.getLatitude();
                longitude = Gps.getLongitude();

                // \n is for new line
                String text = getString(R.string.yourlocationis);

                cons.IniciateSuperToast(text + "- \nLat: " + latitude + "\nLong: ",
                        getApplicationContext(), 0, R.drawable.ic_wave, 3000);

            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                Gps.showSettingsAlert();
            }
            cons.getOrigin().setBoundsCoordinates(new ArrayList<LatLng>() {{
                add(new LatLng(latitude, longitude));
            }});
            //   this.CalculateRoute();

        } else {

            cons.IniciateSuperToast(getRes(R.string.cantgetcurrentlocation), getApplicationContext(), 0, R.drawable.ic_wave, 3000);

        }

    }


}
