package com.istudio.crsurfguide.custom;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.obj.Constante;

public class Top5Activity extends BaseActivity {


    Constante constante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top5);
        constante = Constante.getInstance(this);
        InitFonts();
    }

    private void InitFonts() {

        TextView txtTitle = (TextView) this.findViewById(R.id.txtTitle);
        txtTitle.setTypeface(this.constante.getFont());

        TextView txtDet = (TextView) this.findViewById(R.id.txtDet);
        txtDet.setTypeface(this.constante.getFontBody());


        TextView txtDet1 = (TextView) this.findViewById(R.id.txtDet1);
        txtDet1.setTypeface(this.constante.getFont());

        TextView txtDet2 = (TextView) this.findViewById(R.id.txtDet2);
        txtDet2.setTypeface(this.constante.getFontBody());


        TextView txtDet3 = (TextView) this.findViewById(R.id.txtDet3);
        txtDet3.setTypeface(this.constante.getFont());

        TextView txtDet4 = (TextView) this.findViewById(R.id.txtDet4);
        txtDet4.setTypeface(this.constante.getFontBody());


        TextView txtDet5 = (TextView) this.findViewById(R.id.txtDet5);
        txtDet5.setTypeface(this.constante.getFont());

        TextView txtDet6 = (TextView) this.findViewById(R.id.txtDet6);
        txtDet6.setTypeface(this.constante.getFontBody());


        TextView txtDet7 = (TextView) this.findViewById(R.id.txtDet7);
        txtDet7.setTypeface(this.constante.getFont());

        TextView txtDet8 = (TextView) this.findViewById(R.id.txtDet8);
        txtDet8.setTypeface(this.constante.getFontBody());

    }


}
