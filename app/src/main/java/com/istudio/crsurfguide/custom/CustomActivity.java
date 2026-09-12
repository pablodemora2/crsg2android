// Author: Pablo Mora Gonzàlez.
// Está es la clase que se usa para desplejar mensajes remplazando al Dialog, que se dispara
// desde el main.
//
//

package com.istudio.crsurfguide.custom;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.obj.Constante;

public class CustomActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        Bundle extras = getIntent().getExtras();
        String msg = extras.getString("key0");
        String msg2 = extras.getString("key1");

        TextView t = (TextView) findViewById(R.id.txtTitle);
        t.setTypeface(Constante.getInstance(this).getFont());
        t.setText(msg);

        TextView t2 = (TextView) findViewById(R.id.txtDet);
        t2.setTypeface(Constante.getInstance(this).getFontBody());
        t2.setText(msg2);

        Button b = (Button) findViewById(R.id.btnOk);
        b.setTypeface(Constante.getInstance(this).getFont());

    }

}
