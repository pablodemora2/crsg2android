// author : Pablo Mora G.
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.
// Esta es la actividad papa de cualquier otra actividad si se quiere que esta herede atributos
// globales
// como que cambie de color, etc.


package com.istudio.crsurfguide.custom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.istudio.crsurfguide.R;


public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.Custom();
    }

    protected void Custom() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void exit(View view) {

        super.onBackPressed();

    }

}
