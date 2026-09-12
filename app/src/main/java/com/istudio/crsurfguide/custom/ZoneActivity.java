package com.istudio.crsurfguide.custom;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.list.List;
import com.istudio.crsurfguide.obj.Constante;

public class ZoneActivity extends BaseActivity {


    private Constante cons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone_dialog);
        cons = Constante.getInstance(this);
        SetButtonsTextSize();
    }

    public void exit(String coord1) {

        cons.setZone("");

        Intent output = new Intent();
        output.putExtra("coord1", coord1);

        setResult(RESULT_OK, output);
        finish();

        super.onBackPressed();

    }

    public void goToZone1(View view) {


        this.cons.setZone(this.getRes(R.string.northpacific));

        if(cons.getMap()){

            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();


        }else {

            Intent i = new Intent(this, List.class);
            startActivity(i);
            finish();

        }

    }

    public void goToZone2(View view) {

        this.cons.setZone(this.getRes(R.string.centralpacific));

        if(cons.getMap()){

            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();

        }else {

            Intent i = new Intent(this, List.class);
            startActivity(i);
            finish();

        }

    }

    public void goToZone3(View view) {

        this.cons.setZone(this.getRes(R.string.southpacific));

        if(cons.getMap()){

            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();

        }else {

            Intent i = new Intent(this, List.class);
            startActivity(i);
            finish();

        }

    }

    public void goToZone4(View view) {

        this.cons.setZone(this.getRes(R.string.southcaribbean));

        if(cons.getMap()){

            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();

        }else {

            Intent i = new Intent(this, List.class);
            startActivity(i);
            finish();

        }

    }

    public void goToZone5(View view) {

        this.cons.setZone(this.getRes(R.string.all_zones));

        if(cons.getMap()){

            Intent output = new Intent();
            setResult(RESULT_OK, output);
            finish();

        }else {

            Intent i = new Intent(this, List.class);
            startActivity(i);
            finish();

        }

    }
    private void SetButtonsTextSize() {

        Button btnGoToZone1 = (Button) this.findViewById(R.id.btnGoToZone1);
        btnGoToZone1.setTypeface(cons.getFont());

        Button btnGoToZone2 = (Button) this.findViewById(R.id.btnGoToZone2);
        btnGoToZone2.setTypeface(cons.getFont());

        Button btnGoToZone3 = (Button) this.findViewById(R.id.btnGoToZone3);
        btnGoToZone3.setTypeface(cons.getFont());

        Button btnGoToZone4 = (Button) this.findViewById(R.id.btnGoToZone4);
        btnGoToZone4.setTypeface(cons.getFont());

        Button btnGoToZone5 = (Button) this.findViewById(R.id.btnGoToZone5);
        btnGoToZone5.setTypeface(cons.getFont());

    }

    private String getRes(int pPar) {

        return this.getResources().getString(pPar);

    }

}
