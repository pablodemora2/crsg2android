package com.istudio.crsurfguide.custom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.istudio.animation.AnimationFactory;
import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.obj.Constante;

public class CustomMessage extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_message);

        Bundle extras = getIntent().getExtras();
        String msg = extras.getString("key0");
        TextView t = (TextView) findViewById(R.id.message_textView);
        t.setTypeface(Constante.getInstance(this).getFontBody());
        t.setText(msg);

        Button b = (Button) findViewById(R.id.btnPro);
        b.setTypeface(Constante.getInstance(this).getFont());

        if(Constante.getInstance(this).getTaste().equals(Constante.Flavor.pro.name() ) ){

            b.setText(getResources().getString(R.string.ok));

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }

            });

        }

    }

    public void InitPro(View v) {

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + this.getApplicationContext().getPackageName()
                        + ".pro"));
        startActivity(intent);

    }
}
