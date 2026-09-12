// author : Pablo Mora G.
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.

package com.istudio.crsurfguide.map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.custom.BaseActivity;
import com.istudio.crsurfguide.custom.CustomMessage;
import com.istudio.crsurfguide.obj.Constante;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class SwellActivity extends BaseActivity {

    // Variables
    final Handler handler = new Handler();
    private final int steps = 10;
    private Constante Constante;
    private String[] swellList = {" 0", "5", "11", "16", "21", "27", "32", "37", "43", "48 ft"};
    private String chart;
    private String chartNew;
    private TextView txtLegend;
    private ImageView img;
    private ArrayList<Bitmap> swellBitmapList = new ArrayList<Bitmap>();
    private ArrayList<String> timestampList = new ArrayList<String>();
    private Boolean swellLockLoop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swell);
        img = (ImageView) findViewById(R.id.imgswell);
        Log.d("DBG", "swell on create");
        this.Constante = com.istudio.crsurfguide.obj.Constante.getInstance(this);
        this.setLabelsTest(swellList);
        this.InitButtons();
        this.InitGradient();

        this.chart = "swell";
        this.chartNew = "";
        this.swellLockLoop = true;

        new ReadSweelJSONFeedTask().execute(this.Constante.getWsUrl() + this.chart);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.swell, menu);
        return true;
    }

    private void InitGradient() {

        final ImageView i = (ImageView) this.findViewById(R.id.imgSymbology);

        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient linearGradient = new LinearGradient(0, 0,
                        i.getWidth(), i.getHeight(), new int[]{0xFF02209a,
                        0xFF01f74c, 0xFFfdf800, 0xFF940700}, null,
                        Shader.TileMode.CLAMP);
                return linearGradient;
            }
        };
        PaintDrawable paint = new PaintDrawable();
        paint.setShape(new RectShape());
        paint.setShaderFactory(shaderFactory);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            i.setBackground(paint);

        } else {
        }

    }

    private void InitButtons() {

        Button btnSwell = (Button) this.findViewById(R.id.btnSwell);
        btnSwell.setTypeface(this.Constante.getFont());

        Button btnPeriod = (Button) this.findViewById(R.id.btnPeriod);
        btnPeriod.setTypeface(this.Constante.getFont());

        Button btnWind = (Button) this.findViewById(R.id.btnWind);
        btnWind.setTypeface(this.Constante.getFont());

        Button btnPressure = (Button) this.findViewById(R.id.btnPressure);
        btnPressure.setTypeface(this.Constante.getFont());

        Button btnSst = (Button) this.findViewById(R.id.btnSst);
        btnSst.setTypeface(this.Constante.getFont());


        if (this.Constante.getTaste() == com.istudio.crsurfguide.obj.Constante.Flavor.pro.name()) {

            btnWind.setEnabled(true);
            btnPressure.setEnabled(true);
            //   btnSst.setEnabled(true);
        }


    }

    private void Iteration() {

        // final Handler handler = new Handler();
        handler.post(new Runnable() {

            private int k = 0;

            public void run() {

                txtLegend = (TextView) findViewById(R.id.txtUnits);
                img = (ImageView) findViewById(R.id.imgswell);

                txtLegend.setText(" " + timestampList.get(k) + "");
                txtLegend.setTypeface(Constante.getFontBody());

                if (chart == "swell" || chart == "period" || chart == "wind" | chart ==
                        "pressure") {
                    img.setImageBitmap(swellBitmapList.get(k));
                }

                k++;
                if (k < steps) {
                    // Here `this` refers to the anonymous `Runnable`
                    handler.postDelayed(this, 500);
                } else {

                    if (chart == "swell" || chart == "period" || chart == "wind" || chart ==
                            "pressure") {
                        swellLockLoop = false;
                    }

                }
            }
        });

    }


    private void initLabelsSwell(){

        this.setLabelsTest(new String[]{" 0", "5", "11", "16", "21", "27", "32", "37", "43", "48 ft"});
    }

    private void initLabelsPeriod(){

        this.setLabelsTest(new String[]{" 6", "10", "14", "17", "21",
                "25", "29", "32", "36", "40 sec."});
    }
    private void SetGradientVisibility(Boolean pVar){

        ImageView i = (ImageView) this.findViewById(R.id.imgSymbology);

        if(pVar){
            i.setVisibility(View.VISIBLE);
        }else{
            i.setVisibility(View.INVISIBLE);
        }

    }

    // Buttons.

    public void btnSwell(View view) {

            initLabelsSwell();

        if (this.swellLockLoop == false){

            this.SetGradientVisibility(true);
            this.chart = "swell";
            this.swellLockLoop = true;

            if(this.chart.equals(this.chartNew)){
                this.Iteration();
            } else {
                this.swellBitmapList.clear();
                new ReadSweelJSONFeedTask().execute(this.Constante.getWsUrl()
                        + this.chart);
            }

            this.chartNew=this.chart;
        } else {

            Constante.IniciateSuperToast(getString(R.string.alreadyrunning),
                    getApplicationContext(), 0, R.drawable.ic_wave, 1000);

        }

    }

    public void btnPeriod(View view) {

        initLabelsPeriod();

        if (this.swellLockLoop == false){

            this.SetGradientVisibility(true);
            this.setLabelsTest(new String[]{" 6", "10", "14", "17", "21",
                    "25", "29", "32", "36", "40 sec."});
            this.chart = "period";
            this.swellLockLoop = true;

          if(this.chart.equals(this.chartNew)){
              this.Iteration();
            } else {
              this.swellBitmapList.clear();
              new ReadSweelJSONFeedTask().execute(this.Constante.getWsUrl()
                      + this.chart);
            }

            this.chartNew=this.chart;

        } else {
            Constante.IniciateSuperToast(getString(R.string.alreadyrunning),
                    getApplicationContext(), 0, R.drawable.ic_wave, 1000);
        }

    }

    public void btnWind(View view) {

        if (this.Constante.getTaste() == com.istudio.crsurfguide.obj.Constante.Flavor.pro.name()) {

            if (this.swellLockLoop == false){

                this.SetGradientVisibility(false);
                this.setLabelsTest(new String[]{"1", "2", "4", "5", "6", "7",
                        "8", "10", "11", "12"});

                this.chart = "wind";
                this.swellLockLoop = true;

                if(this.chart.equals(this.chartNew)){
                    this.Iteration();
                } else {
                    this.swellBitmapList.clear();
                    new ReadSweelJSONFeedTask().execute(this.Constante.getWsUrl()
                            + this.chart);
                }

                this.chartNew=this.chart;


            } else {
                Constante.IniciateSuperToast(getString(R.string.alreadyrunning), getApplicationContext
                        (), 0, R.drawable.ic_wave, 1000);
            }
        } else {

            startIntent2(getApplicationContext().getResources().getString(R.string.getpro));

        }
    }

    public void btnPressure(View view) {

        if (this.Constante.getTaste() == com.istudio.crsurfguide.obj.Constante.Flavor.pro.name()) {

            if (this.swellLockLoop == false){

                this.SetGradientVisibility(false);
                this.setLabelsTest(new String[]{
                        getApplicationContext().getResources().getString(R.string.mainsealevel),
                        "", "", "", "", "", "", "",
                        "", ""});

                this.chart = "pressure";
                if(this.chart.equals(this.chartNew)){
                    this.Iteration();
                } else {
                    this.swellBitmapList.clear();
                    new ReadSweelJSONFeedTask().execute(this.Constante.getWsUrl()
                            + this.chart);
                }

                this.chartNew=this.chart;


            } else {
                Constante.IniciateSuperToast(getString(R.string.alreadyrunning),
                        getApplicationContext(), 0, R.drawable.ic_wave, 1000);
            }

        } else {

            startIntent2(getApplicationContext().getResources().getString(R.string.getpro));

        }

    }

    private void setLabelsTest(String[] listText) {

        TextView t1 = (TextView) this.findViewById(R.id.tvSymbology1);
        t1.setText(listText[0]);
        t1.setTypeface(Constante.getFontBody());
        TextView t2 = (TextView) this.findViewById(R.id.tvSymbology2);
        t2.setText(listText[1]);
        t2.setTypeface(Constante.getFontBody());
        TextView t3 = (TextView) this.findViewById(R.id.tvSymbology3);
        t3.setText(listText[2]);
        t3.setTypeface(Constante.getFontBody());
        TextView t4 = (TextView) this.findViewById(R.id.tvSymbology4);
        t4.setText(listText[3]);
        t4.setTypeface(Constante.getFontBody());
        TextView t5 = (TextView) this.findViewById(R.id.tvSymbology5);
        t5.setText(listText[4]);
        t5.setTypeface(Constante.getFontBody());
        TextView t6 = (TextView) this.findViewById(R.id.tvSymbology6);
        t6.setText(listText[5]);
        t6.setTypeface(Constante.getFontBody());
        TextView t7 = (TextView) this.findViewById(R.id.tvSymbology7);
        t7.setText(listText[6]);
        t7.setTypeface(Constante.getFontBody());
        TextView t8 = (TextView) this.findViewById(R.id.tvSymbology8);
        t8.setText(listText[7]);
        t8.setTypeface(Constante.getFontBody());
        TextView t9 = (TextView) this.findViewById(R.id.tvSymbology9);
        t9.setText(listText[8]);
        t9.setTypeface(Constante.getFontBody());
        TextView t10 = (TextView) this.findViewById(R.id.tvSymbology10);
        t10.setText(listText[9]);
        t10.setTypeface(Constante.getFontBody());
    }

    // Async classes.

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    private String readJSONFeed(String URL2) {

        URL url = null;
        String result="";
        HttpURLConnection urlConnection=null;
        try {
            url = new URL(URL2);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            result=readStream(in);

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            urlConnection.disconnect();
        }
        return result;
    }

    // Iniciar intencion 2
    private void startIntent2(String pMsj1) {

        Intent intent = new Intent(this, CustomMessage.class);
        intent.putExtra("key0", pMsj1);
        startActivity(intent);

    }

    private class ReadSweelJSONFeedTask extends AsyncTask<String, Void, String> {

        JSONArray jsonArray;
        ArrayList<JSONObject> jsonObjectList = new ArrayList<JSONObject>();
        ArrayList<JSONObject> jsonObjectSwellList = new ArrayList<JSONObject>();
        ArrayList<String> imageUrlList = new ArrayList<String>();

        Long l;
        Time time;
        Date date;

        protected String doInBackground(String... urls) {

            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            try {

                // 1
                jsonArray = new JSONArray(result);

                //
                for (int i = 0; i < steps; ++i) {

                    // Check mem
                    //if (Constante.getDebug())
                        //     getMem();

                        // 2

                        jsonObjectList.add(new JSONObject(jsonArray.get(i)
                                .toString()));

                    // 3
                    jsonObjectSwellList.add(new JSONObject(jsonObjectList
                            .get(i).getString("charts").toString()));

                    // 4
                    imageUrlList.add(jsonObjectSwellList.get(i)
                            .getString(chart).toString());

                    // 5
                    new DownloadImageTask(
                            (ImageView) findViewById(R.id.imgswell))
                            .execute(imageUrlList.get(i));

                    // 6
                    l = Long.valueOf(jsonObjectList.get(i).getString(
                            "timestamp")) * 1000;

                    time = new Time(l);
                    date = new Date(l);

                    timestampList
                            .add(date.toString() + " - " + time.toString());
                    new UpdateTexViewTask(
                            (TextView) findViewById(R.id.txtUnits))
                            .execute(timestampList.get(i));

                }
            } catch (Exception e) {

            }
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageTask(ImageView bmImage) {
            img = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];

            InputStream in = null;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            try {
                in = new java.net.URL(urldisplay).openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap pic = BitmapFactory.decodeStream(in, null, options);

            options.inSampleSize = calculateInSampleSize(options, img.getWidth(), img.getHeight());
            try {
                in = new java.net.URL(urldisplay).openStream();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            options.inJustDecodeBounds = false;
            pic = BitmapFactory.decodeStream(in, null, options);
            Log.i("DBG", "img.getWidth() "+img.getWidth());
            Log.i("DBG", "img.getHeight() "+img.getHeight());
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(pic, img.getWidth(), img.getHeight(), true);
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 200, 100, img.getWidth()-200, img.getHeight()-200);
            return scaledBitmap;
        }

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int
                reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        protected void onPostExecute(Bitmap result) {

            img.setImageBitmap(result);

            if (chart == "swell" || chart == "period" || chart == "wind" || chart == "pressure")
                swellBitmapList.add(result);

            if (swellBitmapList.size() >= steps - 1 && (chart == "swell" || chart == "period" || chart == "wind" || chart == "pressure")) {
                swellLockLoop = false;
            }

        }
    }

    private class UpdateTexViewTask extends AsyncTask<String, Void, String> {

        public UpdateTexViewTask(TextView pTxt) {
            txtLegend = pTxt;
        }

        protected void onPostExecute(String result) {
            txtLegend.setText(" " + result);
            txtLegend.setTypeface(Constante.getFontBody());
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            return params[0];
        }
    }
}
