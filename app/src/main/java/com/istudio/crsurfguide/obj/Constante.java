// Author: Pablo Mora Gonzàlez.
// En está clase se almacenán datos de constantes.
// de la aplicación. Usa un patron de dise;o singleton
//
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.

// Nombre de paquete
package com.istudio.crsurfguide.obj;

// Librerias

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.istudio.chart.TimedValue;
import com.istudio.crsurfguide.BuildConfig;
import com.istudio.crsurfguide.R;
import com.istudio.list.ImageDescription;
import com.istudio.supertoast.SuperToast;
import com.istudio.tideengine.BackEndTideComputer;
import com.istudio.tideengine.Coefficient;
import com.istudio.tideengine.TideStation;
import com.istudio.tideengine.TideUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

// Declara la clase, que hereda Activity

public class Constante extends Activity {

    // Variables

    // Esta variable es la clave para usar el patron de diseño singleton
    // Que lo que hace, es cada vez que se llama al objeto en memoria devuelve el mismo objeto.
    // Si el objeto no esta inicializado, lo instancia, si no devuelve el objeto ya instanciado.

    private static Constante instance = null;
    private Activity activity;
    private SuperToast superToast;
    private Entry Country;
    private Entry Origin;
    private Entry Destination;
    private Boolean Map;
    private ArrayList<Entry> ZoneList;
    private ArrayList<Entry> OriginList;
    private ArrayList<Entry> NorthPacificList;
    private ArrayList<Entry> CentralPacificList;
    private ArrayList<Entry> SouthPacificList;
    private ArrayList<Entry> SouthCaribbeanList;
    private ArrayList<ImageDescription> ListImages;
    private ArrayList<ImageDescription> ImageList;
    private float CountryZoom;
    private int MapType;
    private int Zero;
    private int ScreenSize;
    private int waitTime;
    private Typeface Font;
    private Typeface FontBody;
    private Boolean GoogleServices;
    private String UserName;
    private String wsUrl;
    private Metric metricSystem;
    private String tide;
    private String zone;

    // Constructor que se llama de la misma clase.

    protected Constante(Activity pAct) {

        this.activity = pAct;
        this.Country = null;
        this.Destination = null;
        this.tide = null;
        this.ZoneList = new ArrayList<>();
        this.OriginList = new ArrayList<Entry>();
        this.NorthPacificList = new ArrayList<Entry>();
        this.CentralPacificList = new ArrayList<Entry>();
        this.SouthPacificList = new ArrayList<Entry>();
        this.SouthCaribbeanList = new ArrayList<Entry>();
        this.ImageList = new ArrayList<>();
        this.ListImages = new ArrayList<ImageDescription>();
        this.Map = false;

        this.zone = "";
        this.CountryZoom = 7;
        this.MapType = 4;
        this.Zero = 0;
        this.Font = Typeface.createFromAsset(activity.getAssets(), "font/engebrechtreex.ttf");
        //this.Font = Typeface.createFromAsset(activity.getAssets(), "font/longnight.ttf");

//        this.FontBody = Typeface.createFromAsset(activity.getAssets(),  "font/Domine-Regular.ttf");
        this.FontBody = Typeface.createFromAsset(activity.getAssets(),
                "font/librebaskerville_regular.ttf");
        this.waitTime = 7000;

        this.GoogleServices = false;
        this.superToast = SuperToast.getInstance(this.getActivity().getApplicationContext());
        this.InitList();


    }

    public static void setInstance(Constante instance) {
        Constante.instance = instance;
    }

    public static Constante getInstance(Activity pAct) {

        if (instance == null) {

            instance = new Constante(pAct);
            instance.setWsUrl(pAct.getString(R.string.magicseaweedapikey));
        }
        return instance;

    }

    public void TakeScreenShot() {

        String path = Environment.getExternalStorageDirectory().toString() + "/" + "1.jpg";

        View v = activity.getWindow().getDecorView().getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        OutputStream out = null;
        File imageFile = new File(path);

        try {
            out = new FileOutputStream(imageFile);
            // choose JPEG format
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
        } catch (FileNotFoundException e) {
            // manage exception
        } catch (IOException e) {
            // manage exception
        } finally {

            try {
                if (out != null) {
                    out.close();
                }

            } catch (Exception exc) {
            }

        }

    }

    public int getWaitTime() {
        return this.waitTime;

    }

    // Este metodo publico, es el que aplica el patron.

    private void InitList() {

        //1
        ImageList.add(new ImageDescription("im_agujasweb", getRes(R.string.im_agujasweb)));
        //2
        ImageList.add(new ImageDescription("im_anaweb2", getRes(R.string.im_anaweb2)));
        //3
        ImageList.add(new ImageDescription("im_anaweb", getRes(R.string.im_anaweb)));
        //4
        ImageList.add(new ImageDescription("im_ario", getRes(R.string.im_aRío)));
        //5
        ImageList.add(new ImageDescription("im_avesec", getRes(R.string.im_avesec)));
        //6
        ImageList.add(new ImageDescription("im_bahia1", getRes(R.string.im_bahia1)));
        //7
        ImageList.add(new ImageDescription("im_bahia2", getRes(R.string.im_bahia2)));
        //8
        ImageList.add(new ImageDescription("im_baldir", getRes(R.string.im_baldir)));
        //9
        ImageList.add(new ImageDescription("im_barrancaweb", getRes(R.string.im_barrancaweb)));
        //10
        ImageList.add(new ImageDescription("im_bejuco", getRes(R.string.im_bejuco)));
        //11
        ImageList.add(new ImageDescription("im_blancaweb", getRes(R.string.im_blancaweb)));
        //12
        ImageList.add(new ImageDescription("im_bocadamas", getRes(R.string.im_bocadamas)));
        //13
        ImageList.add(new ImageDescription("im_brasilito", getRes(R.string.im_brasilito)));
        //14
        ImageList.add(new ImageDescription("im_cahuita", getRes(R.string.im_cahuita)));
        //15
        ImageList.add(new ImageDescription("im_calderapointbreaks", getRes(R.string
                .im_calderapointbreaks)));
        //16
        ImageList.add(new ImageDescription("im_calderaweb", getRes(R.string.im_calderaweb)));
        //17
        ImageList.add(new ImageDescription("im_camaronal", getRes(R.string.im_camaronal)));
        //18
        ImageList.add(new ImageDescription("im_carbonal2web", getRes(R.string.im_carbonal2web)));
        //19
        ImageList.add(new ImageDescription("im_carbonalweb", getRes(R.string.im_carbonalweb)));
        //20
        ImageList.add(new ImageDescription("im_carrillo", getRes(R.string.im_carrillo)));
        //21
        ImageList.add(new ImageDescription("im_casonaf", getRes(R.string.im_casonaf)));
        //22
        ImageList.add(new ImageDescription("im_cocles", getRes(R.string.im_cocles)));
        //23
        ImageList.add(new ImageDescription("im_coclesweb", getRes(R.string.im_coclesweb)));
        //24
        ImageList.add(new ImageDescription("im_condovacweb", getRes(R.string.im_condovacweb)));
        //25
        ImageList.add(new ImageDescription("im_corralweb", getRes(R.string.im_corralweb)));
        //26
        ImageList.add(new ImageDescription("im_coyote", getRes(R.string.im_coyote)));
        //27
        ImageList.add(new ImageDescription("im_crbeautis", getRes(R.string.im_crbeautis)));
        //28
        ImageList.add(new ImageDescription("im_dominical", getRes(R.string.im_dominical)));
        //29
        ImageList.add(new ImageDescription("im_drakebay", getRes(R.string.im_drakebay)));
        //31
        ImageList.add(new ImageDescription("im_elbajoweb", getRes(R.string.im_elbajoweb)));
        //32
        ImageList.add(new ImageDescription("im_elcano", getRes(R.string.im_elcano)));
        //33
        ImageList.add(new ImageDescription("im_elgato", getRes(R.string.im_elgato)));
        //34
        ImageList.add(new ImageDescription("im_elrey", getRes(R.string.im_elrey)));
        //35
        ImageList.add(new ImageDescription("im_escondidaweb1", getRes(R.string.im_escondidaweb1)));
        //36
        ImageList.add(new ImageDescription("im_escondidaweb2", getRes(R.string.im_escondidaweb2)));
        //37
        ImageList.add(new ImageDescription("im_espadilla", getRes(R.string.im_espadilla)));
        //38
        ImageList.add(new ImageDescription("im_esterillos", getRes(R.string.im_esterillos)));
        //39
        ImageList.add(new ImageDescription("im_esterillosoeste", getRes(R.string
                .im_esterillosoeste)));
        //40
        ImageList.add(new ImageDescription("im_flamweb", getRes(R.string.im_flamweb)));
        //41
        ImageList.add(new ImageDescription("im_flweb", getRes(R.string.im_flweb)));
        //43
        ImageList.add(new ImageDescription("im_guacalweb", getRes(R.string.im_guacalweb)));
        //44
        ImageList.add(new ImageDescription("im_hermosa2", getRes(R.string.im_hermosa2)));
        //45
        ImageList.add(new ImageDescription("im_hermosasunday", getRes(R.string.im_hermosasunday)));
        //46
        ImageList.add(new ImageDescription("im_islauvita", getRes(R.string.im_islauvita)));
        //47
        ImageList.add(new ImageDescription("im_jaco", getRes(R.string.im_jaco)));
        //48
        ImageList.add(new ImageDescription("im_jaco1", getRes(R.string.im_jaco1)));
        //49
        ImageList.add(new ImageDescription("im_jaco4", getRes(R.string.im_jaco4)));
        //50
        ImageList.add(new ImageDescription("im_junquillal", getRes(R.string.im_junquillal)));
        //51
        ImageList.add(new ImageDescription("im_laisla", getRes(R.string.im_laisla)));

        ImageList.add(new ImageDescription("im_laisla2", getRes(R.string.laisla)));
        //52
        ImageList.add(new ImageDescription("im_langosta", getRes(R.string.im_langosta)));
        //53
        ImageList.add(new ImageDescription("im_lavweb", getRes(R.string.im_lavweb)));
        //54
        ImageList.add(new ImageDescription("im_littlefiji", getRes(R.string.im_littlefiji)));
        //55
        ImageList.add(new ImageDescription("im_littlefiji2", getRes(R.string.im_littlefiji2)));
        //56
        ImageList.add(new ImageDescription("im_lolapig", getRes(R.string.im_lolapig)));
        //57
        ImageList.add(new ImageDescription("im_manuelantonio", getRes(R.string.im_manuelantonio)));
        //58
        ImageList.add(new ImageDescription("im_manzanillo", getRes(R.string.im_manzanillo)));
        //59
        ImageList.add(new ImageDescription("im_manzanilloguana", getRes(R.string
                .im_manzanilloguana)));
        //60
        ImageList.add(new ImageDescription("im_marazul", getRes(R.string.im_marazul)));
        //61
        ImageList.add(new ImageDescription("im_marbellaset", getRes(R.string.im_marbellaset)));
        //62
        ImageList.add(new ImageDescription("im_marbellatub", getRes(R.string.im_marbellatub)));
        //63
        ImageList.add(new ImageDescription("im_matapalo", getRes(R.string.im_matapalo)));
        //64
        ImageList.add(new ImageDescription("im_negraentru", getRes(R.string.im_negraentru)));
        //65
        ImageList.add(new ImageDescription("im_nosaraweb", getRes(R.string.im_nosaraweb)));
        //66
        ImageList.add(new ImageDescription("im_olliesweb", getRes(R.string.im_olliesweb)));
        //67
        ImageList.add(new ImageDescription("im_olliesweb2", getRes(R.string.im_olliesweb2)));
        //68
        ImageList.add(new ImageDescription("im_olloweb", getRes(R.string.im_olloweb)));
        //69
        ImageList.add(new ImageDescription("im_pandulce", getRes(R.string.im_pandulce)));
        //70
        ImageList.add(new ImageDescription("im_pavair", getRes(R.string.im_pavair)));
        //71
        ImageList.add(new ImageDescription("im_pavones", getRes(R.string.im_pavones)));
        //72
        ImageList.add(new ImageDescription("im_pg", getRes(R.string.im_pg)));
        //73
        ImageList.add(new ImageDescription("im_pgweb", getRes(R.string.im_pgweb)));
        //74
        ImageList.add(new ImageDescription("im_pirataswallyweb", getRes(R.string
                .im_pirataswallyweb)));
        //74.1
        ImageList.add(new ImageDescription("im_playablanca", getRes(R.string.im_playablanca)));
        //75
        ImageList.add(new ImageDescription("im_playaelcoco1", getRes(R.string.im_playaelcoco1)));
        //76
        ImageList.add(new ImageDescription("im_playaelcoco2", getRes(R.string.im_playaelcoco2)));
        //77
        ImageList.add(new ImageDescription("im_playagrande", getRes(R.string.im_playagrande)));
        //78
        ImageList.add(new ImageDescription("im_playitas", getRes(R.string.im_playitas)));
        //79
        ImageList.add(new ImageDescription("im_playitasnude", getRes(R.string.im_playitasnude)));
        //80
        ImageList.add(new ImageDescription("im_puntaislita", getRes(R.string.im_puntaislita)));
        //81
        ImageList.add(new ImageDescription("im_puntauva", getRes(R.string.im_puntauva)));
        //82
        ImageList.add(new ImageDescription("im_queporrivermouth", getRes(R.string
                .im_queporrivermouth)));
        //83
        ImageList.add(new ImageDescription("im_riooclaro", getRes(R.string.im_Ríoclaro)));
        //85
        ImageList.add(new ImageDescription("im_robertama", getRes(R.string.im_robertama)));
        //86
        ImageList.add(new ImageDescription("im_roca1", getRes(R.string.im_roca1)));
        //87
        ImageList.add(new ImageDescription("im_rocaboom", getRes(R.string.im_rocaboom)));
        //88
        ImageList.add(new ImageDescription("im_rocaloca", getRes(R.string.im_rocaloca)));
        //89
        ImageList.add(new ImageDescription("im_salsaweb", getRes(R.string.im_salsaweb)));
        //90
        ImageList.add(new ImageDescription("im_samara", getRes(R.string.im_samara)));
        //91
        ImageList.add(new ImageDescription("im_santat", getRes(R.string.im_santat)));
        //92
        ImageList.add(new ImageDescription("im_sierperivermouth", getRes(R.string
                .im_sierperivermouth)));
        //93
        ImageList.add(new ImageDescription("im_splash", getRes(R.string.im_splash)));
        //94
        ImageList.add(new ImageDescription("im_stbarrel", getRes(R.string.im_stbarrel)));
        //95
        ImageList.add(new ImageDescription("im_stmap", getRes(R.string.im_stmap)));
        //96
        ImageList.add(new ImageDescription("im_stroad", getRes(R.string.im_stroad)));
        //97
        ImageList.add(new ImageDescription("im_stsunset", getRes(R.string.im_stsunset)));
        //98
        ImageList.add(new ImageDescription("im_tivives", getRes(R.string.im_tivives)));
        //99
        ImageList.add(new ImageDescription("im_tortuga", getRes(R.string.im_tortuga)));
        //100
        ImageList.add(new ImageDescription("im_tortugagrande", getRes(R.string.im_tortugagrande)));
        //101
        ImageList.add(new ImageDescription("im_tumbos", getRes(R.string.im_tumbos)));
        //102
        ImageList.add(new ImageDescription("im_tuti11", getRes(R.string.im_tuti11)));
        //103
        ImageList.add(new ImageDescription("im_valorweb", getRes(R.string.im_valorweb)));
        //104
        ImageList.add(new ImageDescription("im_zancudo", getRes(R.string.im_zancudo)));
        //105
    }
    // En este metodo se carga la informacion.

    public void LoadNorthPacificList() {

        // 1
        NorthPacificList.add(new Entry(R.string.olliespointn, R.string.olliespoint, R.drawable
                .im_olliesweb2, new ArrayList<LatLng>() {{
            add(new LatLng(10.8459030, -85.7934910));
            add(new LatLng(10.7799749, -85.6640804));
            add(new LatLng(10.8550140, -85.8008070));
            add(new LatLng(10.8431562, -85.7884474));
        }}));

        // 2
        NorthPacificList.add(new Entry(R.string.laberintosn, R.string.laberintos, R.drawable
                .im_lavweb, new ArrayList<LatLng>() {{
            add(new LatLng(10.8101658, -85.7216460));
            add(new LatLng(10.7947100, -85.6812600));
            add(new LatLng(10.8454016, -85.8566197));
            add(new LatLng(10.8543420, -85.8484417));
        }}));

        // 3
        NorthPacificList.add(new Entry(R.string.lasvaritasn, R.string.lasvaritas));

        // 4
        NorthPacificList.add(new Entry(R.string.rocabrujan, R.string.rocabruja, R.drawable
                .im_tortugagrande, new ArrayList<LatLng>() {{
            add(new LatLng(10.7947100, -85.6812600));
            add(new LatLng(10.7799749, -85.6640804));
            add(new LatLng(10.7916031, -85.6837359));
            add(new LatLng(10.7990212, -85.6774874));
        }}));

        // 5
        NorthPacificList.add(new Entry(R.string.elbajon, R.string.elbajo, R.drawable
                .im_elbajoweb, new ArrayList<LatLng>() {{
            add(new LatLng(10.7415770, -85.6635580));
            add(new LatLng(10.7371180, -85.6334570));
            add(new LatLng(10.7460570, -85.6627740));
            add(new LatLng(10.7375710, -85.6628210));
        }}));

        // 6
        NorthPacificList.add(new Entry(R.string.playaiguanita, R.string.iguanita, new
                ArrayList<LatLng>() {{
            add(new LatLng(10.6304150, -85.6313730));
            add(new LatLng(10.6375300, -85.6375540));
            add(new LatLng(10.6364430, -85.6364220));
            add(new LatLng(10.6225510, -85.6273660));
        }}));

        // 7
        NorthPacificList.add(new Entry(R.string.carbonaln, R.string.carbonal, R.drawable
                .im_carbonalweb, new ArrayList<LatLng>() {{
            add(new LatLng(10.6577150, -85.6737110));
            add(new LatLng(10.6589900, -85.6658280));
            add(new LatLng(10.6421450, -85.6828470));
            add(new LatLng(10.6627290, -85.6684360));
        }}, true));

        // 8
        NorthPacificList.add(new Entry(R.string.playahermosa, R.string.playahermosaguana, R
                .drawable.im_condovacweb, new ArrayList<LatLng>() {{
            add(new LatLng(10.5762410, -85.6794400));
            add(new LatLng(10.5719700, -85.6805050));
            add(new LatLng(10.5697760, -85.6840970));
            add(new LatLng(10.5820540, -85.6762670));
        }}));

        // 9
        NorthPacificList.add(new Entry(R.string.playasdelcocon, R.string.playasdelcoco, new
                ArrayList<LatLng>() {{
            add(new LatLng(10.5566245, -85.6958371));
            add(new LatLng(10.5515681, -85.6978967));
            add(new LatLng(10.5515601, -85.6980620));
            add(new LatLng(10.5657191, -85.6902316));
        }}));

        // 10
        NorthPacificList.add(new Entry(R.string.flamingon, R.string.flamingospoint, R.drawable
                .im_flamweb, new ArrayList<LatLng>() {{
            add(new LatLng(10.4370530, -85.7929240));
            add(new LatLng(10.4389466, -85.7930008));
            add(new LatLng(10.4420147, -85.7948173));
            add(new LatLng(10.4290698, -85.7944981));
        }}));

        // 11
        NorthPacificList.add(new Entry(R.string.playabrasilito, R.string.brasilito, R.drawable
                .im_brasilito, new ArrayList<LatLng>() {{
            add(new LatLng(10.4117340, -85.7974470));
            add(new LatLng(10.4092170, -85.7962080));
            add(new LatLng(10.4039120, -85.8020290));
            add(new LatLng(10.4201020, -85.7946840));
        }}));

        // 12
        NorthPacificList.add(new Entry(R.string.bahiadelospiratasn, R.string.bahiadelospiratas, R
                .drawable.im_bahia2, new ArrayList<LatLng>() {{
            add(new LatLng(10.3884910, -85.8407340));
            add(new LatLng(10.3855140, -85.8394600));
            add(new LatLng(10.3857680, -85.8409010));
            add(new LatLng(10.3898380, -85.8380280));
        }}));

        // 13
        NorthPacificList.add(new Entry(R.string.playagranden, R.string.playagrande, R.drawable
                .im_pg, new ArrayList<LatLng>() {{
            add(new LatLng(10.3281920, -85.8479440));
            add(new LatLng(10.3343453, -85.8482003));
            add(new LatLng(10.3294584, -85.8459217));
            add(new LatLng(10.3117998, -85.8401788));
        }}));

        // 14
        NorthPacificList.add(new Entry(R.string.playatamarindon, R.string.tamarindo, R.drawable
                .im_robertama, new ArrayList<LatLng>() {{
            add(new LatLng(10.3027590, -85.8423730));
            add(new LatLng(10.2986308, -85.8426491));
            add(new LatLng(10.3064540, -85.8407783));
            add(new LatLng(10.2971167, -85.8455389));
        }}));

        // 15
        NorthPacificList.add(new Entry(R.string.playalangostan, R.string.langosta, R.drawable
                .im_langosta, new ArrayList<LatLng>() {{
            add(new LatLng(10.2927990, -85.8499260));
            add(new LatLng(10.2922051, -85.8536536));
            add(new LatLng(10.2841318, -85.8504520));
            add(new LatLng(10.2922051, -85.8536536));
        }}));

        // 16
        NorthPacificList.add(new Entry(R.string.playaavellanas, R.string.avellanas, R.drawable
                .im_lolapig, new ArrayList<LatLng>() {{
            add(new LatLng(10.2320720, -85.8375620));
            add(new LatLng(10.2274722, -85.8370876));
            add(new LatLng(10.2268492, -85.8376364));
            add(new LatLng(10.2355654, -85.8411997));
        }}));

        // 17
        NorthPacificList.add(new Entry(R.string.playanegran, R.string.playanegraguana, R.drawable
                .im_negraentru, new ArrayList<LatLng>() {{
            add(new LatLng(10.1974068, -85.8321084));
            add(new LatLng(10.1974068, -85.8321084));
            add(new LatLng(10.1997143, -85.8357351));
            add(new LatLng(10.1956431, -85.8312715));
        }}));

        // 18
        NorthPacificList.add(new Entry(R.string.junquillaln, R.string.junquillal, R.drawable
                .im_junquillal, new ArrayList<LatLng>() {{
            add(new LatLng(10.1591880, -85.8078220));
            add(new LatLng(10.1619597, -85.8089291));
            add(new LatLng(10.1577391, -85.8079822));
            add(new LatLng(10.1626933, -85.8080506));
        }}));

        // 19
        NorthPacificList.add(new Entry(R.string.playamarbella, R.string.marbella, R.drawable
                .im_marbellatub, new ArrayList<LatLng>() {{
            add(new LatLng(10.0737980, -85.7748360));
            add(new LatLng(10.0685970, -85.7682020));
            add(new LatLng(10.0807790, -85.7775710));
            add(new LatLng(10.0684620, -85.7667490));
        }}));

        // 20
        NorthPacificList.add(new Entry(R.string.callejonesn, R.string.callejones, new
                ArrayList<LatLng>() {{
            add(new LatLng(10.1976250, -85.8357870));
            add(new LatLng(10.2001390, -85.8343020));
            add(new LatLng(10.1912240, -85.8265490));
            add(new LatLng(10.2041470, -85.8406440));
        }}));

        // 21
        NorthPacificList.add(new Entry(R.string.ostinal, R.string.ostional, new ArrayList<LatLng>
                () {{
            add(new LatLng(10.0071790, -85.7155780));
            add(new LatLng(10.0105850, -85.7180180));
            add(new LatLng(10.0109150, -85.7202070));
            add(new LatLng(10.0024880, -85.7092310));
        }}));

        // 22
        NorthPacificList.add(new Entry(R.string.playaguionesn, R.string.guiones, R.drawable
                .im_nosaraweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.9381630, -85.6667590));
            add(new LatLng(9.9408449, -85.6669980));
            add(new LatLng(9.9465548, -85.6734916));
            add(new LatLng(9.9352678, -85.6655134));
        }}));

        // 23
        NorthPacificList.add(new Entry(R.string.camaronaln, R.string.camaronal, R.drawable
                .im_camaronal, new ArrayList<LatLng>() {{
            add(new LatLng(9.8605840, -85.4396250));
            add(new LatLng(9.8637464, -85.4444816));
            add(new LatLng(9.8676038, -85.4463259));
            add(new LatLng(9.8554272, -85.4330510));
        }}));

        // 24
        NorthPacificList.add(new Entry(R.string.playasamaran, R.string.samara, R.drawable
                .im_samara, new ArrayList<LatLng>() {{
            add(new LatLng(9.8766720, -85.5238880));
            add(new LatLng(9.8808050, -85.5275550));
            add(new LatLng(9.8746350, -85.5338390));
            add(new LatLng(9.8755550, -85.5125780));
        }}));

        // 25
        NorthPacificList.add(new Entry(R.string.playacarrillon, R.string.playacarrillo, R
                .drawable.im_carrillo, new ArrayList<LatLng>() {{
            add(new LatLng(9.8681810, -85.4922630));
            add(new LatLng(9.8716710, -85.4913470));
            add(new LatLng(9.8662460, -85.5006990));
            add(new LatLng(9.8655870, -85.4828200));
        }}));

        // 26
        NorthPacificList.add(new Entry(R.string.puntaislitan, R.string.puntaislita, R.drawable
                .im_puntaislita, new ArrayList<LatLng>() {{
            add(new LatLng(9.8504760, -85.4016640));
            add(new LatLng(9.8530710, -85.4020070));
            add(new LatLng(9.8468890, -85.4043640));
            add(new LatLng(9.8542530, -85.3967700));
        }}));

        // 27
        NorthPacificList.add(new Entry(R.string.playabejucon, R.string.bejuco, new
                ArrayList<LatLng>() {{
            add(new LatLng(9.8355550, -85.3505500));
            add(new LatLng(9.8395850, -85.3576320));
            add(new LatLng(9.8395330, -85.3610280));
            add(new LatLng(9.8312210, -85.3437240));
        }}));

        // 28
        NorthPacificList.add(new Entry(R.string.playasanmigueln, R.string.playasanmiguel, new
                ArrayList<LatLng>() {{
            add(new LatLng(9.8107120, -85.3105260));
            add(new LatLng(9.8126650, -85.3098240));
            add(new LatLng(9.8156640, -85.3189860));
            add(new LatLng(9.8067430, -85.3016290));
        }}));

        // 29
        NorthPacificList.add(new Entry(R.string.playacoyoten, R.string.coyote, R.drawable
                .im_coyote, new ArrayList<LatLng>() {{
            add(new LatLng(9.7922940, -85.2798770));
            add(new LatLng(9.7962303, -85.2829529));
            add(new LatLng(9.7903398, -85.2783358));
            add(new LatLng(9.7980184, -85.2888933));
        }}));

        // 30
        NorthPacificList.add(new Entry(R.string.playacaletasn, R.string.caletas, new
                ArrayList<LatLng>() {{
            add(new LatLng(9.6837980, -84.6784350));
            add(new LatLng(9.6832540, -84.6609840));
            add(new LatLng(9.6893070, -84.6807180));
            add(new LatLng(9.6764890, -84.6677140));
        }}));

        // 31
        NorthPacificList.add(new Entry(R.string.playamanzanillo, R.string.playamanzanilloguana, R
                .drawable.im_manzanilloguana, new ArrayList<LatLng>() {{
            add(new LatLng(9.6912820, -85.2041220));
            add(new LatLng(9.6901740, -85.2024220));
            add(new LatLng(9.6964590, -85.2078320));
            add(new LatLng(9.6843100, -85.2014560));
        }}));

        // 32
        NorthPacificList.add(new Entry(R.string.playahermosa, R.string.playahermosamalpais, new
                ArrayList<LatLng>() {{
            add(new LatLng(9.6607430, -85.1860830));
            add(new LatLng(9.6593570, -85.1848640));
            add(new LatLng(9.6636400, -85.1896210));
            add(new LatLng(9.6586790, -85.1841560));
        }}));

        // 33
        NorthPacificList.add(new Entry(R.string.santateresamalpaisn, R.string.santateresamalpais,
                R.drawable.im_stbarrel));

        // 46
        NorthPacificList.add(new Entry(R.string.playasantateresan, new ArrayList<LatLng>() {{
            add(new LatLng(9.6440360, -85.1736090));
            add(new LatLng(9.6436280, -85.1692940));
            add(new LatLng(9.6403600, -85.1679270));
            add(new LatLng(9.6537800, -85.1784950));
        }}));

        // 47
        NorthPacificList.add(new Entry(R.string.playaelcarmenn, new ArrayList<LatLng>() {{
            add(new LatLng(9.6297810, -85.1592580));
            add(new LatLng(9.6326060, -85.1586120));
            add(new LatLng(9.6274080, -85.1546650));
            add(new LatLng(9.6376110, -85.1631170));
        }}));

        // 48
        NorthPacificList.add(new Entry(R.string.playamalpaisn, new ArrayList<LatLng>() {{
            add(new LatLng(9.6152300, -85.1475100));
            add(new LatLng(9.6091820, -85.1452360));
            add(new LatLng(9.6102490, -85.1421370));
            add(new LatLng(9.6224920, -85.1513660));
        }}));

        // 34
        NorthPacificList.add(new Entry(R.string.barrigonan, R.string.barrigona, R.drawable
                .im_marazul, new ArrayList<LatLng>() {{
            add(new LatLng(9.6059910, -85.1448280));
            add(new LatLng(9.6090880, -85.1451610));
            add(new LatLng(9.6086070, -85.1456220));
            add(new LatLng(9.6032110, -85.1438920));
        }}));

        // 35
        NorthPacificList.add(new Entry(R.string.lossuecosn, R.string.lossuecos, R.drawable
                .im_sunsetreef));

        // 40
        NorthPacificList.add(new Entry(R.string.golfon, new ArrayList<LatLng>() {{
            add(new LatLng(10.2206650, -85.8392310));
            add(new LatLng(10.2234990, -85.8365890));
            add(new LatLng(10.2228720, -85.8423320));
            add(new LatLng(10.2149640, -85.8358990));
        }}));

    }

    public void LoadCentralPacificList() {

        // 50
        CentralPacificList.add(new Entry(R.string.elpuerton, R.string.elpuerto));

        // 51
        CentralPacificList.add(new Entry(R.string.bocabarrancan, R.string.bocabarranca, R
                .drawable.im_barrancaweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.9622130, -84.7396610));
            add(new LatLng(9.9585709, -84.7367613));
            add(new LatLng(9.9588694, -84.7368320));
            add(new LatLng(9.9729703, -84.7485181));
        }}));

        // 52
        CentralPacificList.add(new Entry(R.string.playasdedonaana, R.string.donaana, R.drawable
                .im_anaweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.9376560, -84.7276600));
            add(new LatLng(9.9383239, -84.7267519));
            add(new LatLng(9.9406775, -84.7341156));
            add(new LatLng(9.9330260, -84.7227856));
        }}));

        // 53
        CentralPacificList.add(new Entry(R.string.puertocalderan, R.string.puertocaldera, R
                .drawable.im_calderaweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.9215470, -84.7170890));
            add(new LatLng(9.9240140, -84.7141820));
            add(new LatLng(9.9250560, -84.7159050));
            add(new LatLng(9.9207780, -84.7138570));
        }}));

        // 54
        CentralPacificList.add(new Entry(R.string.corralillon, R.string.corralillo, R.drawable
                .im_corralweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.9036010, -84.7281680));
            add(new LatLng(9.9106550, -84.7219610));
            add(new LatLng(9.9000230, -84.7304990));
            add(new LatLng(9.9045290, -84.7245180));
        }}));

        // 55
        CentralPacificList.add(new Entry(R.string.elhoyon, R.string.elhoyo, R.drawable
                .im_olloweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.9117610, -84.7266430));
            add(new LatLng(9.9120920, -84.7207910));
            add(new LatLng(9.9153280, -84.7283130));
            add(new LatLng(9.9087690, -84.7230220));
        }}));

        // 56
        CentralPacificList.add(new Entry(R.string.bancoquebrado, R.string.calderapoints, R
                .drawable.im_calderapointbreaks, new ArrayList<LatLng>() {{
            add(new LatLng(9.9053130, -84.7237190));
            add(new LatLng(9.9083430, -84.7214850));
            add(new LatLng(9.9031510, -84.7249300));
            add(new LatLng(9.9073290, -84.7237920));
        }}));

        // 57
        CentralPacificList.add(new Entry(R.string.elsilencion, R.string.elsilencio, new
                ArrayList<LatLng>() {{
            add(new LatLng(9.8948660, -84.7303700));
            add(new LatLng(9.9082950, -84.7216220));
            add(new LatLng(9.8928070, -84.7318310));
            add(new LatLng(9.8988470, -84.7307130));
        }}));

        // 58
        CentralPacificList.add(new Entry(R.string.guacalillon, R.string.guacalillo, R.drawable
                .im_guacalweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.8270500, -84.6684860));
            add(new LatLng(9.8291905, -84.6695736));
            add(new LatLng(9.8165251, -84.6558370));
            add(new LatLng(9.8331022, -84.6743027));
        }}));

        // 59
        CentralPacificList.add(new Entry(R.string.tivivesrivermouthn, R.string.tivivesrivermouth,
                R.drawable.im_tivives, new ArrayList<LatLng>() {{
            add(new LatLng(9.9111930, -84.7190230));
            add(new LatLng(9.8782309, -84.7092763));
            add(new LatLng(9.8817956, -84.7160932));
            add(new LatLng(9.8621060, -84.6965171));
        }}));

        // 60
        CentralPacificList.add(new Entry(R.string.valorpointn, R.string.valorpoint, R.drawable
                .im_valorweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.8608820, -84.7033030));
            add(new LatLng(9.8666700, -84.6982910));
            add(new LatLng(9.8582280, -84.7020770));
            add(new LatLng(9.8637730, -84.7056940));
        }}));

        // 61
        CentralPacificList.add(new Entry(R.string.playaagujas, R.string.agujas, R.drawable
                .im_agujasweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.7228890, -84.6535380));
            add(new LatLng(9.7221710, -84.6523240));
            add(new LatLng(9.7217690, -84.6551950));
            add(new LatLng(9.7273100, -84.6513380));
        }}));

        // 62
        CentralPacificList.add(new Entry(R.string.playablancan, R.string.playablanca, R.drawable
                .im_blancaweb, new ArrayList<LatLng>() {{
            add(new LatLng(9.7075030, -84.6623580));
            add(new LatLng(9.7056590, -84.6611920));
            add(new LatLng(9.7055960, -84.6636850));
            add(new LatLng(9.7084820, -84.6599420));
        }}));

        // 63
        CentralPacificList.add(new Entry(R.string.eltragaderon, R.string.eltragadero, new
                ArrayList<LatLng>() {{
            add(new LatLng(9.7080400, -84.6665720));
            add(new LatLng(9.7037980, -84.6626070));
            add(new LatLng(9.7030550, -84.6648190));
            add(new LatLng(9.7129010, -84.6716110));
        }}));

        // 64
        CentralPacificList.add(new Entry(R.string.elculon, R.string.elculo, new ArrayList<LatLng>
                () {{
            add(new LatLng(9.7078810, -84.6709920));
            add(new LatLng(9.7038090, -84.6624760));
            add(new LatLng(9.7133010, -84.6750920));
            add(new LatLng(9.6977630, -84.6660340));
        }}));

        // 65
        CentralPacificList.add(new Entry(R.string.playaescondidan, R.string.playaescondida, R
                .drawable.im_escondidaweb1, new ArrayList<LatLng>() {{
            add(new LatLng(9.6654960, -84.6746070));
            add(new LatLng(9.6635940, -84.6635360));
            add(new LatLng(9.6732460, -84.6761730));
            add(new LatLng(9.6532710, -84.6649230));
        }}));

        // 66
        CentralPacificList.add(new Entry(R.string.littlefijin, R.string.littlefiji, R.drawable
                .im_littlefiji, new ArrayList<LatLng>() {{
            add(new LatLng(9.6753330, -84.6853130));
            add(new LatLng(9.6636280, -84.6632610));
            add(new LatLng(9.6860180, -84.6926240));
            add(new LatLng(9.6656880, -84.6780970));
        }}));

        // 67
        CentralPacificList.add(new Entry(R.string.laislan, R.string.laisla, R.drawable
                .im_laisla2, new ArrayList<LatLng>() {{
            add(new LatLng(9.6325280, -84.6719330));
            add(new LatLng(9.6455660, -84.6554220));
            add(new LatLng(9.6265120, -84.6789250));
            add(new LatLng(9.6318690, -84.6642400));
        }}));

        // 68
        CentralPacificList.add(new Entry(R.string.playajacon, R.string.jaco, R.drawable.im_jaco,
                new ArrayList<LatLng>() {{
            add(new LatLng(9.6146290, -84.6324220));
            add(new LatLng(9.6151147, -84.6307406));
            add(new LatLng(9.6212218, -84.6424139));
            add(new LatLng(9.5994805, -84.6218440));
        }}));

        // 69
        CentralPacificList.add(new Entry(R.string.rocalocan, R.string.rocaloca, R.drawable
                .im_rocaloca, new ArrayList<LatLng>() {{
            add(new LatLng(9.5836050, -84.6224310));
            add(new LatLng(9.5864340, -84.6201770));
            add(new LatLng(9.5912140, -84.6263290));
            add(new LatLng(9.5794530, -84.6171570));
        }}));

        // 70
        CentralPacificList.add(new Entry(R.string.elgaton, R.string.elgato, R.drawable.im_elgato,
                new ArrayList<LatLng>() {{
            add(new LatLng(9.5816140, -84.6192920));
            add(new LatLng(9.5837680, -84.6168830));
            add(new LatLng(9.5876070, -84.6226600));
            add(new LatLng(9.5766140, -84.6147080));
        }}));

        // 71
        CentralPacificList.add(new Entry(R.string.playahermosa, R.string.playahermosajaco, R
                .drawable.im_hermosa2, new ArrayList<LatLng>() {{
            add(new LatLng(9.5700210, -84.6021760));
            add(new LatLng(9.5752702, -84.6038390));
            add(new LatLng(9.5787441, -84.6118897));
            add(new LatLng(9.5415673, -84.5586783));
        }}));

        // 87
        CentralPacificList.add(new Entry(R.string.elarboln, new ArrayList<LatLng>() {{
            add(new LatLng(9.5625790, -84.5930490));
            add(new LatLng(9.5746400, -84.6014920));
            add(new LatLng(9.5569900, -84.5972240));
            add(new LatLng(9.5759160, -84.5838200));
        }}));

        // 72
        CentralPacificList.add(new Entry(R.string.esterillosn, R.string.esterillos, R.drawable
                .im_esterillos));


        // 88
        CentralPacificList.add(new Entry(R.string.esterillosoesten, new ArrayList<LatLng>() {{
            add(new LatLng(9.5237430, -84.5016020));
            add(new LatLng(9.5268740, -84.5022700));
            add(new LatLng(9.5193830, -84.5094650));
            add(new LatLng(9.5330480, -84.4978640));
        }}));

        // 89
        CentralPacificList.add(new Entry(R.string.esterilloscentraln, new ArrayList<LatLng>() {{
            add(new LatLng(9.5252310, -84.4832330));
            add(new LatLng(9.5292820, -84.4830180));
            add(new LatLng(9.5233850, -84.4874310));
            add(new LatLng(9.5328370, -84.4781450));
        }}));

        // 90
        CentralPacificList.add(new Entry(R.string.esterillosesten, new ArrayList<LatLng>() {{
            add(new LatLng(9.5256600, -84.4572450));
            add(new LatLng(9.5265256, -84.4553707));
            add(new LatLng(9.5264413, -84.4659879));
            add(new LatLng(9.5208826, -84.4446251));
        }}));

        // 73
        CentralPacificList.add(new Entry(R.string.playabejucon, R.string.playabejuco, R.drawable
                .im_bejuco, new ArrayList<LatLng>() {{
            add(new LatLng(9.5154040, -84.4300840));
            add(new LatLng(9.5186844, -84.4359488));
            add(new LatLng(9.5211035, -84.4384819));
            add(new LatLng(9.5117358, -84.4229609));
        }}));

        // 74
        CentralPacificList.add(new Entry(R.string.bocaqueposn, R.string.bocaquepos, R.drawable
                .im_queposrivermouth));

        // 75
        CentralPacificList.add(new Entry(R.string.bocadamasn, R.string.bocadamas, R.drawable
                .im_bocadamas, new ArrayList<LatLng>() {{
            add(new LatLng(9.4586020, -84.2195580));
            add(new LatLng(9.5186844, -84.4359488));
            add(new LatLng(9.4632474, -84.2274866));
            add(new LatLng(9.4507740, -84.1973687));
        }}));

        // 76
        CentralPacificList.add(new Entry(R.string.manuelantonio, R.string.playasmanuelantonio, R
                .drawable.im_manuelantonio));

        // 77
        CentralPacificList.add(new Entry(R.string.cutters, R.string.cuttersright));

        // 78
        CentralPacificList.add(new Entry(R.string.playitasn, R.string.playitas, R.drawable
                .im_playitas));

        // 79
        CentralPacificList.add(new Entry(R.string.playitasnude, R.string.northplayitas, R
                .drawable.im_playitasnude));

        // 80
        CentralPacificList.add(new Entry(R.string.playaelreyn, R.string.playaelrey, R.drawable
                .im_elrey));

    }

    public void LoadSouthPacificList() {

        // 92
        SouthPacificList.add(new Entry(R.string.dominical, R.string.playadominical, R.drawable
                .im_dominical, new ArrayList<LatLng>() {{
            add(new LatLng(9.2474220, -83.8576200));
            add(new LatLng(9.2494672, -83.8600715));
            add(new LatLng(9.2555103, -83.8712999));
            add(new LatLng(9.2423520, -83.8533489));
        }}));

        // 93
        SouthPacificList.add(new Entry(R.string.dominicalito, R.string.playadominicalito, new
                ArrayList<LatLng>() {{
            add(new LatLng(9.2352800, -83.8468660));
            add(new LatLng(9.2377580, -83.8450250));
            add(new LatLng(9.2424770, -83.8541100));
            add(new LatLng(9.2274160, -83.8402450));
        }}));

        // 94
        SouthPacificList.add(new Entry(R.string.lapuntan, R.string.lapunta, new ArrayList<LatLng>
                () {{
            add(new LatLng(9.2214200, -83.8479150));
            add(new LatLng(9.2256660, -83.8376670));
            add(new LatLng(9.2306090, -83.8518060));
            add(new LatLng(9.2127270, -83.8383340));
        }}));

        // 95
        SouthPacificList.add(new Entry(R.string.puntahermosan, R.string
                .playahermosaypuntahermosa, new ArrayList<LatLng>() {{
            add(new LatLng(9.1897130, -83.7810580));
            add(new LatLng(9.1930880, -83.7777340));
            add(new LatLng(9.1985870, -83.7877760));
            add(new LatLng(9.1821140, -83.7677890));
        }}));

        // 96
        SouthPacificList.add(new Entry(R.string.elshamann, R.string.elshaman));

        // 97
        SouthPacificList.add(new Entry(R.string.matapalon, R.string.matapalohermosa, R.drawable
                .im_matapalo, new ArrayList<LatLng>() {{
            add(new LatLng(8.3814620, -83.2787070));
            add(new LatLng(8.3854360, -83.2812290));
            add(new LatLng(8.3772040, -83.2849430));
            add(new LatLng(8.3889460, -83.2763360));
        }}));

        // 98
        SouthPacificList.add(new Entry(R.string.backwash, R.string.backwashway, new
                ArrayList<LatLng>() {{
            add(new LatLng(8.3986280, -83.2761490));
            add(new LatLng(8.3975950, -83.2820050));
            add(new LatLng(8.3880200, -83.2718600));
            add(new LatLng(8.4041070, -83.2815200));
        }}));

        // 99
        SouthPacificList.add(new Entry(R.string.caraten, R.string.carate, new ArrayList<LatLng>() {{
            add(new LatLng(8.4474770, -83.4552410));
            add(new LatLng(8.4430910, -83.4625966));
            add(new LatLng(8.4430910, -83.4625966));
            add(new LatLng(8.4382235, -83.4362590));
        }}));

        // 100
        SouthPacificList.add(new Entry(R.string.bahiadraken, R.string.bahiadrake, R.drawable
                .im_drakebay, new ArrayList<LatLng>() {{
            add(new LatLng(8.7181570, -83.6478940));
            add(new LatLng(8.6902336, -83.6668948));
            add(new LatLng(8.7187267, -83.6565331));
            add(new LatLng(8.7192914, -83.6417209));
        }}));

        // 101
        SouthPacificList.add(new Entry(R.string.pavonesn, R.string.pavones, R.drawable
                .im_pavones, new ArrayList<LatLng>() {{
            add(new LatLng(8.3967050, -83.1413620));
            add(new LatLng(8.3955320, -83.1358640));
            add(new LatLng(8.3853400, -83.1443630));
            add(new LatLng(8.4047940, -83.1289920));
        }}));

        // 102
        SouthPacificList.add(new Entry(R.string.rioclaron, R.string.rioclaro, R.drawable
                .im_rioclaro, new ArrayList<LatLng>() {{
            add(new LatLng(8.3932030, -83.1418870));
            add(new LatLng(8.3929840, -83.1374650));
            add(new LatLng(8.3892450, -83.1427390));
            add(new LatLng(8.3976050, -83.1358370));
        }}));

        // 103
        SouthPacificList.add(new Entry(R.string.riosierpen, R.string.riosierpe, R.drawable
                .im_sierperivermouth, new ArrayList<LatLng>() {{
            add(new LatLng(8.8731790, -83.6167970));
            add(new LatLng(9.0710440, -83.6547970));
            add(new LatLng(8.9037380, -83.6235930));
            add(new LatLng(8.8431030, -83.5966280));
        }}));

        // 104
        SouthPacificList.add(new Entry(R.string.playazancudon, R.string.playazancudo, R.drawable
                .im_zancudo, new ArrayList<LatLng>() {{
            add(new LatLng(8.4849880, -83.1241020));
            add(new LatLng(8.5062170, -83.1253550));
            add(new LatLng(8.5246100, -83.1455440));
            add(new LatLng(8.4396120, -83.0752330));
        }}));

        // 105
        SouthPacificList.add(new Entry(R.string.pandulcen, R.string.pandulce, R.drawable
                .im_pandulce, new ArrayList<LatLng>() {{
            add(new LatLng(8.4251830, -83.2689740));
            add(new LatLng(8.4268840, -83.2764770));
            add(new LatLng(8.4317490, -83.2726090));
            add(new LatLng(8.4215510, -83.2688200));
        }}));

        // 106
        SouthPacificList.add(new Entry(R.string.puntabancon, R.string.puntabanco, new
                ArrayList<LatLng>() {{
            add(new LatLng(8.3599140, -83.1393880));
            add(new LatLng(8.3625526, -83.1422333));
            add(new LatLng(8.3628143, -83.1361108));
            add(new LatLng(8.3628707, -83.1483557));
        }}));

        // 107
        SouthPacificList.add(new Entry(R.string.puntaburican, R.string.puntaburica, new
                ArrayList<LatLng>() {{
            add(new LatLng(8.1691090, -82.9298000));
            add(new LatLng(8.3625526, -83.1422333));
            add(new LatLng(8.1586196, -82.9253649));
            add(new LatLng(8.2077817, -82.9517512));
        }}));

        // 108
        SouthPacificList.add(new Entry(R.string.queposn, new ArrayList<LatLng>() {{
            add(new LatLng(9.4361480, -84.1719000));
            add(new LatLng(9.4362922, -84.1672934));
            add(new LatLng(9.4369286, -84.1763761));
            add(new LatLng(9.4380193, -84.1653652));
        }}));

        // 109
        SouthPacificList.add(new Entry(R.string.manuelantonion, new ArrayList<LatLng>() {{
            add(new LatLng(9.3901220, -84.1533670));
            add(new LatLng(9.3917503, -84.1499804));
            add(new LatLng(9.3976200, -84.1648814));
            add(new LatLng(9.3879427, -84.1476680));
        }}));

        // 110
        SouthPacificList.add(new Entry(R.string.cutters, new ArrayList<LatLng>() {{
            add(new LatLng(9.3767020, -84.1421100));
            add(new LatLng(9.3894600, -84.1470720));
            add(new LatLng(9.3737820, -84.1416740));
            add(new LatLng(9.3811270, -84.1471680));
        }}));

        // 111
        SouthPacificList.add(new Entry(R.string.playitasn, new ArrayList<LatLng>() {{
            add(new LatLng(9.3917370, -84.1599870));
            add(new LatLng(9.3967500, -84.1592810));
            add(new LatLng(9.3966730, -84.1660530));
            add(new LatLng(9.3906820, -84.1501540));
        }}));

        // 112
        SouthPacificList.add(new Entry(R.string.northplayitasn, new ArrayList<LatLng>() {{
            add(new LatLng(9.3926170, -84.1705470));
            add(new LatLng(9.3978130, -84.1678500));
            add(new LatLng(9.3898980, -84.1746650));
            add(new LatLng(9.3981010, -84.1669920));
        }}));

        // 113
        SouthPacificList.add(new Entry(R.string.elreyn, new ArrayList<LatLng>() {{
            add(new LatLng(9.3495870, -84.0286990));
            add(new LatLng(9.3322379, -83.9583601));
            add(new LatLng(9.3237170, -83.9688918));
            add(new LatLng(9.3184029, -83.9544668));
        }}));

    }

    public void LoadSouthCaribbeanList() {

        // 117
        SouthCaribbeanList.add(new Entry(R.string.elinsiden, R.string.elinside));

        // 118
        SouthCaribbeanList.add(new Entry(R.string.lostumbosn, R.string.lostumbos, R.drawable
                .im_tumbos));

        // 119
        SouthCaribbeanList.add(new Entry(R.string.cieneguitan, R.string.cieneguita));

        // 120
        SouthCaribbeanList.add(new Entry(R.string.islauvitan, R.string.islauvita, R.drawable
                .im_islauvita, new ArrayList<LatLng>() {{
            add(new LatLng(9.9933960, -83.0110340));
            add(new LatLng(10.0000712, -83.0069413));
            add(new LatLng(9.9885192, -83.0156957));
            add(new LatLng(10.0000712, -83.0069413));
        }}));

        // 121
        SouthCaribbeanList.add(new Entry(R.string.playanegran, R.string.playanegra));

        // 122
        SouthCaribbeanList.add(new Entry(R.string.laesculitan, R.string.laescuelita, R.drawable
                .im_laescuelita));

        // 123
        SouthCaribbeanList.add(new Entry(R.string.salsabravan, R.string.salsabrava, new
                ArrayList<LatLng>() {{
            add(new LatLng(9.6592640, -82.7585730));
            add(new LatLng(9.7478974, -82.8552339));
            add(new LatLng(9.6608497, -82.7753822));
            add(new LatLng(9.6561715, -82.7549887));
        }}));

        // 124
        SouthCaribbeanList.add(new Entry(R.string.coclesn, R.string.cocles, R.drawable.im_cocles,
                new ArrayList<LatLng>() {{
            add(new LatLng(9.6475330, -82.7251730));
            add(new LatLng(9.6596519, -82.7402825));
            add(new LatLng(9.6469744, -82.7289075));
            add(new LatLng(9.6558307, -82.7421825));
        }}));

        // 125
        SouthCaribbeanList.add(new Entry(R.string.puntauvan, R.string.puntauva, R.drawable
                .im_puntauva));

        // 126
        SouthCaribbeanList.add(new Entry(R.string.playagranden, R.string.playagrandelimon, R
                .drawable.im_playagrande));

        // 127
        SouthCaribbeanList.add(new Entry(R.string.manzanillodelimonn, R.string.manzanillo, R
                .drawable.im_manzanillo, new ArrayList<LatLng>() {{
            add(new LatLng(9.6371810, -82.6566040));
            add(new LatLng(9.6422240, -82.7151394));
            add(new LatLng(9.6327992, -82.6695080));
            add(new LatLng(9.6355070, -82.6769468));
        }}));

        // 128
        SouthCaribbeanList.add(new Entry(R.string.jahhandersn, R.string.jahhanders));

        // 129
        SouthCaribbeanList.add(new Entry(R.string.norestedecostarica, new ArrayList<LatLng>() {{
            add(new LatLng(10.6364870, -83.5435560));
            add(new LatLng(10.2865522, -83.3576991));
            add(new LatLng(10.8298816, -83.5966565));
            add(new LatLng(10.4717800, -83.4629896));
        }}));

        // 130
        SouthCaribbeanList.add(new Entry(R.string.porteten, new ArrayList<LatLng>() {{
            add(new LatLng(10.0127040, -83.0684110));
            add(new LatLng(10.0104619, -83.0657715));
            add(new LatLng(10.0092871, -83.0709807));
            add(new LatLng(10.0335166, -83.0715530));
        }}));

        // 131
        SouthCaribbeanList.add(new Entry(R.string.playabonitan, new ArrayList<LatLng>() {{
            add(new LatLng(10.0151350, -83.0574480));
            add(new LatLng(10.0091557, -83.0640677));
            add(new LatLng(10.0050701, -83.0452422));
            add(new LatLng(10.0189323, -83.0719071));
        }}));

        // 132
        SouthCaribbeanList.add(new Entry(R.string.rocaaltan, new ArrayList<LatLng>() {{
            add(new LatLng(10.0046450, -83.0315750));
            add(new LatLng(10.0189323, -83.0719071));
            add(new LatLng(9.9966511, -83.0231395));
            add(new LatLng(10.0084597, -83.0377371));
        }}));

        // 133
        SouthCaribbeanList.add(new Entry(R.string.barcoquebradodelimon, new ArrayList<LatLng>() {{
            add(new LatLng(9.8528620, -82.9258900));
            add(new LatLng(9.8561856, -82.9442081));
            add(new LatLng(9.8097862, -82.9047569));
            add(new LatLng(9.9081050, -82.9844074));
        }}));

        // 134
        SouthCaribbeanList.add(new Entry(R.string.westfalian, new ArrayList<LatLng>() {{
            add(new LatLng(9.9362610, -83.0053390));
            add(new LatLng(9.9371066, -83.0070851));
            add(new LatLng(9.9289454, -82.9976320));
            add(new LatLng(9.9460802, -83.0124127));
        }}));

        // 135
        SouthCaribbeanList.add(new Entry(R.string.cahuitan, new ArrayList<LatLng>() {{
            add(new LatLng(9.7427790, -82.8466470));
            add(new LatLng(9.7429468, -82.8520853));
            add(new LatLng(9.7429468, -82.8520853));
            add(new LatLng(9.7478974, -82.8552339));
        }}));

        // 136
        SouthCaribbeanList.add(new Entry(R.string.longshoaln, new ArrayList<LatLng>() {{
            add(new LatLng(9.6525400, -82.7341530));
            add(new LatLng(9.6598244, -82.7413205));
            add(new LatLng(9.6605605, -82.7531373));
            add(new LatLng(9.6596519, -82.7402825));
        }}));

        // 137
        SouthCaribbeanList.add(new Entry(R.string.littleshoaln, new ArrayList<LatLng>() {{
            add(new LatLng(9.6432190, -82.7168410));
            add(new LatLng(9.6422240, -82.7151394));
            add(new LatLng(9.6438317, -82.7183725));
            add(new LatLng(9.6422240, -82.7151394));
        }}));

    }

    public void CloseToast() {

        superToast.setDuration(0);

    }

    private void LoadContry() {

        this.Country = new Entry();
        this.Country = new Entry(R.string.costarica, new LatLng(9.5512, -84.0600), this
                .CountryZoom);

        this.Country.setBoundsCoordinates(new ArrayList<LatLng>());
        this.Country.getBoundsCoordinates().add(new LatLng(8.007511, -82.559488));
        this.Country.getBoundsCoordinates().add(new LatLng(11.199783, -85.908880));

    }

    private void LoadZoneCoordenates() {

        this.ZoneList.add(new Entry(R.string.northpacific, new LatLng(10.366880, -85.483846), 9));
        this.ZoneList.add(new Entry(R.string.centralpacific, new LatLng(9.784130, -84.647882), 9));
        this.ZoneList.add(new Entry(R.string.southpacific, new LatLng(8.999653, -83.535235), 9));
        this.ZoneList.add(new Entry(R.string.southcaribbean, new LatLng(10.251413, -83.039049), 9));

    }

    private void LoadOriginCoordenates() {

        this.OriginList.add(new Entry(R.string.sanjose, new LatLng(9.9993236, -84.2030650), 9));
        this.OriginList.add(new Entry(R.string.alajuelaairport, new LatLng(9.999963, -84.205795),
                0));
        this.OriginList.add(new Entry(R.string.liberiaairport, new LatLng(10.596230, -85.528095),
                0));
        this.OriginList.add(new Entry(R.string.mylocation, new LatLng(0.0, 0.0), 0));

    }

    // Metodos setter y getter.

    public ArrayList<Entry> getNorthPacificList() {

        if (NorthPacificList.size() < 1) {

            this.LoadNorthPacificList();
        }
        return NorthPacificList;

    }

    public void setNorthPacificList(ArrayList<Entry> northPacificList) {
        NorthPacificList = northPacificList;
    }

    public ArrayList<Entry> getCentralPacificList() {

        if (CentralPacificList.size() < 1) {

            this.LoadCentralPacificList();
        }

        return CentralPacificList;
    }

    public void setCentralPacificList(ArrayList<Entry> centralPacificList) {
        CentralPacificList = centralPacificList;
    }

    public ArrayList<Entry> getSouthPacificList() {

        if (SouthPacificList.size() < 1) {

            this.LoadSouthPacificList();
        }

        return SouthPacificList;
    }

    public void setSouthPacificList(ArrayList<Entry> southPacificList) {
        SouthPacificList = southPacificList;
    }

    public ArrayList<Entry> getSouthCaribbeanList() {

        if (SouthCaribbeanList.size() < 1) {

            this.LoadSouthCaribbeanList();
        }

        return SouthCaribbeanList;
    }

    public void setSouthCaribbeanList(ArrayList<Entry> southCaribbeanList) {
        SouthCaribbeanList = southCaribbeanList;
    }

    public ArrayList<ImageDescription> getListImages() {

        return this.ListImages;
    }

    // Este metodo inicia un supertoast, que a la vez es un singleton.

    public void IniciateSuperToast(String message, Context c, int animation, int
            image, int duration) {

        if (!superToast.isShowing()) {

            superToast.setDuration(duration);
            //   superToast.setAnimations(animation);
            superToast.setBackground(com.istudio.R.drawable.shape_black);
            superToast.setTextColor(Color.WHITE);
            superToast.setTextSize((int) c.getResources().getDimension(R.dimen.text_size_title));
            superToast.setTypefaceStyle(this.getFont());
            superToast.setIcon(image, SuperToast.IconPosition.LEFT);
            superToast.setText(message);
            superToast.show();

        }else{

            superToast.setText(message);

        }
    }

    public Entry getCountry() {
        if (Country == null) {
            LoadContry();
        }
        return Country;
    }

    public void setCountry(Entry country) {
        Country = country;
    }

    // Aqui se calcula la marea, basado en el javatideengine, abstraido en la clase
    // BackEndTideComputer

    public String calculateTide() {

        if (this.tide == null) {

            BackEndTideComputer b = BackEndTideComputer.getInstance();
            b.setAct(this.getActivity());

            String resul = "";

            try {

                BackEndTideComputer.connect();
                BackEndTideComputer.setVerbose(false);

                TideStation ts = null;

                long before = 0;
                long after = 0;
                final int RISING = 1;
                final int FALLING = -1;

                double low1 = Double.NaN;
                double low2 = Double.NaN;
                double high1 = Double.NaN;
                double high2 = Double.NaN;
                Calendar low1Cal = null;
                Calendar low2Cal = null;
                Calendar high1Cal = null;
                Calendar high2Cal = null;
                int trend = 0;

                double previousWH = Double.NaN;

                List<Coefficient> constSpeed = BackEndTideComputer.buildSiteConstSpeed();

                Calendar now = GregorianCalendar.getInstance();
                String location = null;

                location = this.getActivity().getResources().getString(R.string.tideplace);
                ts = BackEndTideComputer.findTideStation(location, now.get(Calendar.YEAR));
                now.setTimeZone(TimeZone.getTimeZone(ts.getTimeZone()));

                if (ts != null) {

                    before = System.currentTimeMillis();

                    for (int h = 0; h < 24; h++) {

                        double wh = 0.0;
                        Calendar cal = null;

                        for (int m = 0; m < 60; m++) {

                            cal = new GregorianCalendar(
                                    now.get(Calendar.YEAR),
                                    now.get(Calendar.MONTH),
                                    now.get(Calendar.DAY_OF_MONTH), h, m);

                            wh = TideUtilities.getWaterHeight(ts,
                                    constSpeed, cal);


                            if (Double.isNaN(previousWH))

                                previousWH = wh;

                            else {

                                if (trend == 0) {

                                    if (previousWH > wh)

                                        trend = -1;

                                    else if (previousWH < wh)

                                        trend = 1;

                                } else {

                                    switch (trend) {

                                        case RISING:

                                            if (previousWH > wh) // Now
                                            // going
                                            // down
                                            {
                                                if (Double.isNaN(high1)) {
                                                    high1 = previousWH;
                                                    cal.add(Calendar.MINUTE, -1);
                                                    high1Cal = cal;
                                                } else {
                                                    high2 = previousWH;
                                                    cal.add(Calendar.MINUTE, -1);
                                                    high2Cal = cal;
                                                }
                                                trend = FALLING; // Now
                                                // falling
                                            }
                                            break;
                                        case FALLING:
                                            if (previousWH < wh) // Now
                                            // going
                                            // up
                                            {
                                                if (Double.isNaN(low1)) {
                                                    low1 = previousWH;
                                                    cal.add(Calendar.MINUTE, -1);
                                                    low1Cal = cal;
                                                } else {
                                                    low2 = previousWH;
                                                    cal.add(Calendar.MINUTE, -1);
                                                    low2Cal = cal;
                                                }
                                                trend = RISING; // Now
                                                // rising
                                            }
                                            break;
                                    }
                                }
                                previousWH = wh;
                            }
                        }

                    }
                    after = System.currentTimeMillis();

                    List<TimedValue> timeAL = new ArrayList<TimedValue>(4);
                    if (low1Cal != null)
                        timeAL.add(new TimedValue(this.getActivity().getResources().getString(R
                                .string.low), low1Cal, low1));
                    if (low2Cal != null)
                        timeAL.add(new TimedValue(this.getActivity().getResources().getString(R
                                .string.low), low2Cal, low2));
                    if (high1Cal != null)
                        timeAL.add(new TimedValue(this.getActivity().getResources().getString(R
                                .string.high), high1Cal, high1));
                    if (high2Cal != null)
                        timeAL.add(new TimedValue(this.getActivity().getResources().getString(R.string.high), high2Cal, high2));

                    Collections.sort(timeAL);

                    for (TimedValue tv : timeAL) {

                        resul += tv.getType() + " ";

                        if (tv.getCalendar().get(Calendar.HOUR_OF_DAY) < 10) {

                            resul += "0";
                        }

                        resul += tv.getCalendar().get(Calendar.HOUR_OF_DAY) + ":";

                        if ((int) tv.getCalendar().get(Calendar.MINUTE) < 10) {
                            resul += "0";
                        }
                        resul += tv.getCalendar().get(Calendar.MINUTE) + " ";
                        resul += TideUtilities.DF22PLUS.format(tv.getValue()) + " ft\n";
                    }

                    resul = resul.substring(0, resul.length() - 1);

                    BackEndTideComputer.disconnect();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            tide = resul;

        }


        return tide;
    }

    // Costa Rica Surf Guide: Begin

    public ArrayList<Entry> getZoneList() {
        if (ZoneList.size() < 1) {
            LoadZoneCoordenates();
        }
        return ZoneList;
    }

    public void setZoneList(ArrayList<Entry> zoneList) {
        ZoneList = zoneList;
    }

    public ArrayList<Entry> getOriginList() {
        if (OriginList.size() < 1) {
            LoadOriginCoordenates();
        }
        return OriginList;
    }

    public void setOriginList(ArrayList<Entry> originList) {
        OriginList = originList;
    }

    public String getWsUrl() {
        return wsUrl;
    }

    private void setWsUrl(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    // End

    public int getMapType() {
        return MapType;
    }

    public void setMapType(int mapType) {
        MapType = mapType;
    }

    public int getZero() {
        return Zero;
    }

    public void setZero(int zero) {
        Zero = zero;
    }

    public int getScreenSize() {
        return ScreenSize;
    }

    public void setScreenSize(int screenSize) {
        ScreenSize = screenSize;
    }

    public Typeface getFontBody() {
        return FontBody;
    }

    public void setFontBody(Typeface font) {
        FontBody = font;
    }

    public Typeface getFont() {
        return Font;
    }

    public void setFont(Typeface font) {
        Font = font;
    }

    public Boolean getGoogleServices() {
        return GoogleServices;
    }

    public void setGoogleServices(Boolean googleServices) {
        GoogleServices = googleServices;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Metric getMetricSystem() {
        return metricSystem;
    }

    public void setMetricSystem(Metric metricSystem) {
        this.metricSystem = metricSystem;
    }

    public Entry getDestination() {
        return Destination;
    }

    public void setDestination(Entry destination) {
        Destination = destination;
    }

    public Boolean getMap() {
        return Map;
    }

    public void setMap(Boolean map) {
        Map = map;
    }

    public Entry getOrigin() {
        return Origin;
    }

    public void setOrigin(Entry origin) {
        Origin = origin;
    }

    public SuperToast getSuperToast() {
        return superToast;
    }

    public void setSuperToast(SuperToast superToast) {
        this.superToast = superToast;
    }

    // Metodo para obtener recursos por referencia
    public String getRes(int pPar) {

        return activity.getResources().getString(pPar);

    }

    public ArrayList<ImageDescription> getImageList() {
        if (ImageList.size() < 1) {
            InitList();
        }
        return ImageList;
    }

    public void setImageList(ArrayList<ImageDescription> imageList) {
        ImageList = imageList;
    }

    // Enum del flavor y el tipo de coordenada

    public Boolean getDebug() {

        if (BuildConfig.DEBUG) {
            return true;
        } else
            return false;

    }

    public String getTaste() {

        return Flavor.pro.name();

    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public enum Flavor {
        free,
        pro
    }

    public enum Metric {

        SI
    }

    public enum Coordenate {
        Location,
        Route,
        Bound1,
        Bound2
    }

    public enum Zone {
        North,
        Central,
        South,
        Caribbean,
        All
    }

}

