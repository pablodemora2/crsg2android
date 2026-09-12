// author : Pablo Mora G.
// Here is where all the magic is done.
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.

// Todo change legend according to days and months in the charts needed respectivily.

package com.istudio.crsurfguide.chart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.PopupMenu;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;

import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartGestureListener;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.LimitLine;
import com.github.mikephil.charting.utils.LimitLine.LimitLabelPosition;
import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.custom.BaseActivity;
import com.istudio.crsurfguide.custom.BaseFragment;
import com.istudio.crsurfguide.custom.CustomMessage;
import com.istudio.crsurfguide.obj.Constante;
import com.istudio.tideengine.BackEndTideComputer;
import com.istudio.tideengine.Coefficient;
import com.istudio.tideengine.TideStation;
import com.istudio.tideengine.TideUtilities;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class LineChartActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    private Constante cons;
    private LineChart mChart;
    private Boolean meters;
    private int hours = 0;

    private int Year;
    private int Month;
    private int Day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linechart);

        cons = Constante.getInstance(this);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        meters = settings.getBoolean("metric", false);

        Year = Calendar.getInstance().get(Calendar.YEAR);
        Month = Calendar.getInstance().get(Calendar.MONTH);
        Day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        this.hours = 24;
        this.InitChart(hours);
        this.CustomAct();
        this.InitButtons();
        this.InitMenuButton();

    }

    private void InitMenu(View view){

        PopupMenu popupMenu = new PopupMenu(LineChartActivity.this, view);
        popupMenu.setOnMenuItemClickListener(LineChartActivity.this);
        popupMenu.inflate(R.menu.line);
        popupMenu.show();

    }

    private void InitMenuButton(){

        findViewById(R.id.imgView1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitMenu(view);
            }
        });
        findViewById(R.id.imgView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitMenu(view);
            }
        });

    }


    private void InitButtons() {

        Button b1 = (Button) findViewById(R.id.button1);
        b1.setTypeface(cons.getFont());

        Button b2 = (Button) findViewById(R.id.button11);
        b2.setTypeface(cons.getFont());

        Button b3 = (Button) findViewById(R.id.button2);
        b3.setTypeface(cons.getFont());

        Button b4 = (Button) findViewById(R.id.button3);
        b4.setTypeface(cons.getFont());

        Button b5 = (Button) findViewById(R.id.button4);
        b5.setTypeface(cons.getFont());

    }

    private void setData(int count, float range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = LoadTide(count);

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, getString(R.string.dailytide));
        set1.setFillAlpha(110);
        set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10, 5f, 0f);
        set1.setColor(Color.CYAN);
        set1.setCircleColor(Color.CYAN);
        set1.setLineWidth(4f);
        set1.setCircleSize(4f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
        set1.setDrawFilled(true);

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        LimitLine ll1 = null;

        if (meters) {
            ll1 = new LimitLine(3f);

        } else {
            ll1 = new LimitLine(9f);

        }

        ll1.setLineWidth(4f);
        ll1.enableDashedLine(5f, 5f, 0f);
        ll1.setDrawValue(true);
        ll1.setLabelPosition(LimitLabelPosition.RIGHT);

        LimitLine ll2 = new LimitLine(0f);
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(5f, 5f, 0f);
        ll2.setDrawValue(true);
        ll2.setLabelPosition(LimitLabelPosition.RIGHT);

        data.addLimitLine(ll1);
        data.addLimitLine(ll2);

        // set data
        mChart.setData(data);
    }

    private ArrayList<Entry> LoadTide(int pHours) {

        BackEndTideComputer b = BackEndTideComputer.getInstance();

        b.setAct(this);

        ArrayList<Entry> vals1 = null;
        List<Coefficient> constSpeed = null;

        try {

            b.connect();
            b.setVerbose(false);

            TideStation ts = null;

            constSpeed = BackEndTideComputer.buildSiteConstSpeed();

            String location = null;

            location = getString(R.string.tidelocation1);
            ts = b.findTideStation(location, Year);

            Calendar.getInstance().setTimeZone(TimeZone.getTimeZone(ts.getTimeZone()));

            vals1 = new ArrayList<Entry>();

            float wh;
            int h;

            int days = pHours / 24;

            int counter = 0;

            for (int d = 0; d < days; d++) {

                for (h = 0; h < 24; h++) {

                    counter++;

                    Calendar cal = new GregorianCalendar(Year, Month, Day + d,
                            h, 0);

                    wh = (float) TideUtilities.getWaterHeight(ts, constSpeed, cal);

                    if (meters) {

                        wh = (float) TideUtilities.feetToMeters(wh);

                    }

                    vals1.add(new Entry(wh, counter));
                }

            }

            b.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return vals1;
    }

    private void CustomAct() {

        if (cons.getTaste() == com.istudio.crsurfguide.obj.Constante.Flavor.pro.name()) {
            this.findViewById(R.id.button3).setEnabled(true);
            this.findViewById(R.id.button4).setEnabled(true);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    private void setupMetric(String pMetric) {

        mChart.setUnit(getString(R.string.space) + pMetric);
        mChart.setDrawUnitsInChart(true);

    }

    public void InitChart(int pHours) {

        mChart = (LineChart) findViewById(R.id.chart1);

        if (meters) {

            this.setupMetric(getString(R.string.mt));

        } else {

            this.setupMetric(getString(R.string.feet));

        }
        // if enabled, the chart will always start at zero on the y-axis
        mChart.setStartAtZero(false);

        // disable the drawing of values into the chart
        mChart.setDrawYValues(false);

        mChart.setDrawBorder(true);
        mChart.setBorderPositions(new BorderPosition[]{
                BorderPosition.BOTTOM
        });

        mChart.getYLabels().setTextColor(Color.WHITE);
        mChart.getYLabels().setTypeface(cons.getFont());

        if(cons.getActivity().getResources().getBoolean(R.bool.isTablet)) {
            mChart.getYLabels().setTextSize(cons.getActivity().getResources().getDimension(R.dimen
                    .text_size));
            mChart.getXLabels().setTextSize(cons.getActivity().getResources().getDimension(R.dimen
                    .text_size));
            mChart.setValueTextSize(cons.getActivity().getResources().getDimension(R.dimen
                    .text_size));
        }

        mChart.setDescriptionTypeface(cons.getFontBody());

        mChart.setValueTypeface(cons.getFont());
        mChart.getXLabels().setTextColor(Color.WHITE);
        mChart.getXLabels().setTypeface(cons.getFont());

        // no description text
        mChart.setDescription(getString(R.string.empty));
        mChart.setNoDataTextDescription(getString(R.string.youneedtoprovidedataforthechart));

        // // enable / disable grid lines
        mChart.setDrawVerticalGrid(true);
        mChart.setDrawHorizontalGrid(true);
        //
        // // enable / disable grid background
        mChart.setDrawGridBackground(true);

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.BLACK);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

        mChart.setValueTextColor(Color.BLACK);

        // set the marker to the chart
        mChart.setMarkerView(mv);

        mChart.setHighlightIndicatorEnabled(true);
        mChart.setDrawYValues(true);
        mChart.setDrawXLabels(true);
        // add data
        setData(pHours, 24);

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setTypeface(cons.getFont());
        l.setTextColor(Color.WHITE);
        l.setTextSize(cons.getActivity().getResources().getDimension(R.dimen.text_size_title));

        // modify the legend
        l.setForm(LegendForm.LINE);

    }

    private void LoadMetricSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.metric_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.heigth_metric_array,
                R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                if (pos == 0) {

                    setupMetric(getString(R.string.feet));
                    meters = false;

                } else if (pos == 1) {

                    setupMetric(getString(R.string.mt));
                    meters = true;

                }
                // mChart.invalidate();
                InitChart(hours);

                // We need an Editor object to make preference changes.
                // All objects are from android.context.Context
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("metric", meters);

                // Commit the edits!
                editor.commit();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.line, menu);
        getLayoutInflater().setFactory(new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context,
                                     AttributeSet attrs) {

                if (name.equalsIgnoreCase(
                        "com.android.internal.view.menu.IconMenuItemView")) {
                    try {
                        LayoutInflater li = LayoutInflater.from(context);
                        final View view = li.createView(name, null, attrs);
                        new Handler().post(new Runnable() {
                            public void run() {
                                ((TextView) view).setTextSize(getResources().getDimension(R.dimen.text_size_title));
                                // set the text color
                                ((TextView) view).setTypeface(cons.getFontBody());
                                ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                            }
                        });
                        return view;
                    } catch (InflateException e) {
                        //Handle any inflation exception here
                    } catch (ClassNotFoundException e) {
                        //Handle any ClassNotFoundException here
                    }
                }
                return null;
            }
        });
        return super.onCreateOptionsMenu(menu);

       // getMenuInflater().inflate(R.menu.line, menu);
       // return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            int resultYear = data.getIntExtra(getString(R.string.year), 0);
            int resultMonth = data.getIntExtra(getString(R.string.month), 0);
            int resultDay = data.getIntExtra(getString(R.string.day), 0);

            Year = resultYear;
            Month = resultMonth;
            Day = resultDay;

            InitChart(hours);

        }
    }

    // Iniciar intencion 2
    private void startIntent2(String pMsj1) {

        Intent intent = new Intent(this, CustomMessage.class);
        intent.putExtra("key0", pMsj1);
        startActivity(intent);

    }

    public void sendMessage1(View v) {

        hours = 24;
        InitChart(hours);

    }

    public void sendMessage11(View v) {

        hours = 72;
        InitChart(hours);

    }

    public void sendMessage2(View v) {

        hours = 168;
        InitChart(hours);


    }

    public void sendMessage3(View v) {

        if (Constante.getInstance(this).getTaste() == Constante.Flavor.pro.name()) {

            hours = 720;
            InitChart(hours);

        } else {

            startIntent2(getString(R.string.getpro));

        }

    }

    public void sendMessage4(View v) {


        if (Constante.getInstance(this).getTaste() == Constante.Flavor.pro.name()) {

            hours = 8760;
            InitChart(hours);

        } else {

            startIntent2(getString(R.string.getpro));

        }

    }

private void MenuGeneric(MenuItem item){

    int id = item.getItemId();
    if (id == R.id.chooseDate) {
        Intent intent = new Intent(this, DatePickerActivity.class);
        startActivityForResult(intent, 1);
    } else if (id == R.id.actionFeet) {
        setupMetric(getString(R.string.feet));
        meters = false;
        InitChart(hours);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("metric", meters);

        // Commit the edits!
        editor.commit();
    } else if (id == R.id.actionMeter) {
        setupMetric(getString(R.string.mt));
        meters = true;
        InitChart(hours);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("metric", meters);

        // Commit the edits!
        editor.commit();
    } else if (id == R.id.animateXY) {
        mChart.animateXY(3000, 3000);
    } else if (id == R.id.actionMonth) {
        if (Constante.getInstance(this).getTaste() == Constante.Flavor.pro.name()) {
            hours = 720;
            InitChart(hours);
        } else {
            startIntent2(getString(R.string.getpro));
        }
    } else if (id == R.id.actionYear) {
        if (Constante.getInstance(this).getTaste() == Constante.Flavor.pro.name()) {
            hours = 8760;
            InitChart(hours);
        } else {
            startIntent2(getString(R.string.getpro));
        }
    } else if (id == R.id.actionSave) {
        if (mChart.saveToPath(getString(R.string.tide) + System.currentTimeMillis(), "")) {
            cons.IniciateSuperToast(getString(R.string.savesuccesful), this.getApplicationContext(), 0, R.drawable.ic_wave, 1000);
        }
    }
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        this.MenuGeneric(item);
        return true;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        this.MenuGeneric(item);
        return false;
    }
}