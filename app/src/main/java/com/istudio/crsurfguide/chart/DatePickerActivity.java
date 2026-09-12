// author : Pablo Mora G.
// This activity will ask for a custom date choosed by user.
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.

package com.istudio.crsurfguide.chart;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.custom.BaseActivity;
import com.istudio.crsurfguide.obj.Constante;

public class DatePickerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);

        this.CustomText();
    }


    private void CustomText(){


        TextView text1 = (TextView) this.findViewById(R.id.txtTitle);
        text1.setTypeface(Constante.getInstance(this).getFont());
        text1.setTextSize(this.getApplicationContext().getResources().getDimension(R.dimen.text_size_title));

        Button btn1 = (Button) this.findViewById(R.id.btnOk);
        btn1.setTypeface(Constante.getInstance(this).getFont());
        btn1.setTextSize(this.getApplicationContext().getResources().getDimension(R.dimen.text_size_title));


    }

    public void exit(View view) {


        DatePicker d = (DatePicker) findViewById(R.id.datePicker);

        Intent output = new Intent();
        output.putExtra(getString(R.string.year), d.getYear());
        output.putExtra(getString(R.string.month), d.getMonth());
        output.putExtra(getString(R.string.day), d.getDayOfMonth());

        setResult(RESULT_OK, output);
        finish();

        super.onBackPressed();

    }
}
