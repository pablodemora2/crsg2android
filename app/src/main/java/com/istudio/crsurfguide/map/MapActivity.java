// author : Pablo Mora G.
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.

package com.istudio.crsurfguide.map;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.istudio.crsurfguide.BuildConfig;
import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.R.drawable;
import com.istudio.crsurfguide.custom.BaseFragment;
import com.istudio.crsurfguide.custom.ZoneActivity;
import com.istudio.crsurfguide.custom.SourceActivity;
import com.istudio.crsurfguide.obj.Constante;
import com.istudio.crsurfguide.obj.Entry;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends BaseFragment implements OnMapReadyCallback {

    private static final int MODE_DRIVING = 0;
    private static final int MODE_BICYCLING = 1;
    private static final int MODE_WALKING = 2;
    // Variables
    private List<LatLng> Coordenadas = new ArrayList<LatLng>();
    private ArrayList<LatLng> MarkerPoints = new ArrayList<LatLng>();
    private ArrayList<Marker> MarkerList = new ArrayList<>();
    private List<Polyline> Polylines = new ArrayList<>();
    private ArrayList<Entry> TourList;
    private String[] ParentItems;
    private String[] ChildItems;
    private String[] OriginItems;
    private String[] NorthPacificdItems;
    private String[] CentralPacificItems;
    private String[] SouthPacificChildItems;
    private String[] SouthCaribbeanItems;
    private Activity Act = null;
    private GoogleMap Map;
    private Constante Constante;
    private Entry ActualPlace;
    private LatLng Origin;
    private LatLng Destination;
    private String DistanceKm = "";
    private String DistanceMiles = "";
    private String Duration = "";
    private Boolean AlternativeRoute;
    private boolean CalculateRoute;
    private Boolean FlagShowMarkers;
    private TextView DistanceDuration;
    private LinearLayout DistanceDurationLayout;
    private RadioButton rbDriving;
    private RadioButton rbBiCycling;
    private RadioButton rbWalking;
    private RadioGroup rgModes;
    private RadioGroup rgRoutes;
    private RadioButton rbNormal;
    private RadioButton rbAlternative;
    private double FixedZoom;
    private int Mode = 0;
    private int counter = 0;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);

            Log.d("DBG", "onCreate");
            this.Act = this;

            this.InicializeVariables();
            this.InitAd();
            this.GetScreenSize();
            //this.IniciateMap();
            this.LoadArrays();

            this.InicializeRoute();
            this.InicializeButtons();
            this.InicializeRadioButtonsModes();
            this.InicializeRadioButtonsRoutes();
            this.LoadMetricSpinner();
            this.InitCameraChangeListener();
            this.LoadAllBeachMarkets();

            this.ValidateGooglePlayServices();
            this.CalculateRoute = false;

        } catch (Exception ex) {

            Log.d("DBG", "Exception: "+ex.toString());

        }

    }

    private void InitAd() {
        requestNewInterstitial();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        String adUnitId = getString(R.string.interstitial_ad_unit_id);

        InterstitialAd.load(this, adUnitId, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@androidx.annotation.NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                mInterstitialAd = null;
                                requestNewInterstitial();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(@androidx.annotation.NonNull com.google.android.gms.ads.AdError adError) {
                                mInterstitialAd = null;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@androidx.annotation.NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    private void ShowAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this);
        }
    }

    private void beginSecondActivity() {
        // requestNewInterstitial(); // Ya se llama al cerrar el anuncio
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            if (this.Constante.getDestination() != null) {


                Coordenadas.clear();
                this.Coordenadas.add(this.Constante.getDestination().getBoundsCoordinates().get(2));
                this.Coordenadas.add(this.Constante.getDestination().getBoundsCoordinates().get(3));

                if (this.Constante.getDestination() != null) {

                    ((Button) this.findViewById(R.id.btnRoute)).setEnabled(true);

                    if (this.Constante.getDestination().getBoundsCoordinates().size() > 1) {

                        AddMarket(Constante.getDestination().getBoundsCoordinates().get(0));

                        CenterScreen(Coordenadas, false);

                    }

                }

            }
        }

        if (requestCode == 2) {

            // globos
            //
            Log.d("LOG","antes de poner globos");
            AddMarket(Constante.getOrigin(), R.drawable.ic_bubble);
            AddMarket(Constante.getDestination(), R.drawable.ic_bubble);
            Log.d("LOG","despues de poner globos");

            Origin = Constante.getOrigin().getBoundsCoordinates().get(0);
            Destination = Constante.getDestination().getBoundsCoordinates().get(1);
            //Map.setTrafficEnabled(false);
            CalculateRoute();

            //ShowAd();
        }


        if (requestCode == 3){

            Button btnBeaches = (Button) this.findViewById(R.id.btnBeaches);
            btnBeaches.setVisibility(View.VISIBLE);

            if(this.Constante.getZone().equals(this.getResources().getString(R.string.northpacific))){

                ClearMarkets();
                AddMarkersToMapNorthPacific();
                CenterScreenEntries(Constante.getNorthPacificList(), true);

            }

            if(this.Constante.getZone().equals(this.getResources().getString(R.string.centralpacific))) {

                ClearMarkets();
                AddMarkersToMapCentralPacific();
                CenterScreenEntries(this.Constante.getCentralPacificList(), true);
            }

            if(this.Constante.getZone().equals(this.getResources().getString(R.string.southpacific))) {

                ClearMarkets();
                AddMarkersToMapSouthPacific();
                CenterScreenEntries(this.Constante.getSouthPacificList(), true);
            }

            if(this.Constante.getZone().equals(this.getResources().getString(R.string.southcaribbean))) {

                ClearMarkets();
                AddMarkersToMapSouthCaribbean();
                CenterScreenEntries(this.Constante.getSouthCaribbeanList(), true);
            }

            if(this.Constante.getZone().equals(this.getResources().getString(R.string.all_zones))) {

                ClearMarkets();
                AddMarkersToMapNorthPacific();
                AddMarkersToMapCentralPacific();
                AddMarkersToMapSouthPacific();
                AddMarkersToMapSouthCaribbean();
                CenterScreenEntries(this.Constante.getSouthCaribbeanList(), true);
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        counter = 0;
        this.ClearMarkets();
        int id = item.getItemId();
        if (id == R.id.tournp) {
            TourList = this.Constante.getNorthPacificList();
            this.CalculateRoute = false;
        } else if (id == R.id.tourpc) {
            TourList = this.Constante.getCentralPacificList();
            this.CalculateRoute = false;
        } else if (id == R.id.tourps) {
            TourList = this.Constante.getSouthPacificList();
            this.CalculateRoute = false;
        } else if (id == R.id.toursc) {
            TourList = this.Constante.getSouthCaribbeanList();
            this.CalculateRoute = false;
        } else if (id == R.id.rutasnp) {
            TourList = this.Constante.getNorthPacificList();
            this.CalculateRoute = true;
        } else if (id == R.id.rutaspc) {
            TourList = this.Constante.getCentralPacificList();
            this.CalculateRoute = true;
        } else if (id == R.id.rutasps) {
            TourList = this.Constante.getSouthPacificList();
            this.CalculateRoute = true;
        } else if (id == R.id.rutassc) {
            TourList = this.Constante.getSouthCaribbeanList();
            this.CalculateRoute = true;
        }
        new MyCustomAsyncTask(this).execute();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (Constante.getTaste() == com.istudio.crsurfguide.obj.Constante.Flavor.pro.name())
            getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if (BuildConfig.FLAVOR.equals("free")) {
            //if (!mInterstitialAd.isLoaded()) {
             //   requestNewInterstitial();
            //}
        //}

        setUpMapIfNeeded();
    }

    public void InitSwell(View view) {
        Intent intent = new Intent(this, SwellActivity.class);
        startActivity(intent);
    }

    private double getFixedZoom() {
        return FixedZoom;
    }

    private void setFixedZoom(double fixedZoom) {
        FixedZoom = fixedZoom;
    }

    private void CalculateRoute() {

        if (this.Origin != null && this.Destination != null) {

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(this.Origin, this.Destination);

            DownloadTask downloadTask = new DownloadTask(this);
            downloadTask.execute(url);

            this.CenterScreen(new ArrayList<LatLng>() {{
                add(Origin);
                add(Destination);
            }}, true);


        } else {
            Constante.IniciateSuperToast(getString(R.string.chooserighcoordenates),
                    getApplicationContext(), 0, R.drawable.ic_wave, 1000);
        }
    }

    private void InicializeVariables() {

        this.DistanceDuration = (TextView) findViewById(R.id.distance);
        this.DistanceDurationLayout = (LinearLayout) findViewById(R.id.llDistance);

        this.ActualPlace = null;
        this.FixedZoom = 0.0;

        this.Constante = com.istudio.crsurfguide.obj.Constante.getInstance(this);
        this.Constante.setActivity(this);

        this.ParentItems = new String[this.Constante.getZoneList().size()];
        this.ChildItems = new String[this.Constante.getNorthPacificList().size()];
        this.NorthPacificdItems = new String[this.Constante.getNorthPacificList().size()];
        this.CentralPacificItems = new String[this.Constante.getCentralPacificList().size()];
        this.SouthPacificChildItems = new String[this.Constante.getSouthPacificList().size()];
        this.SouthCaribbeanItems = new String[this.Constante.getSouthCaribbeanList().size()];

        this.OriginItems = new String[this.Constante.getOriginList().size()];

        this.Origin = null;
        this.Destination = null;
        this.FlagShowMarkers = true;
        this.AlternativeRoute = false;

    }

    private void LoadAllBeachMarkets() {

        this.AddMarkersToMapCentralPacific();
        this.AddMarkersToMapNorthPacific();
        this.AddMarkersToMapSouthCaribbean();
        this.AddMarkersToMapSouthPacific();

    }

    private void GetScreenSize() {

        // Determine screen size
        if (this.Constante.getScreenSize() == Configuration.SCREENLAYOUT_SIZE_LARGE) {

        } else if (this.Constante.getScreenSize() == Configuration.SCREENLAYOUT_SIZE_NORMAL) {

        } else if (this.Constante.getScreenSize() == Configuration.SCREENLAYOUT_SIZE_SMALL) {

            this.setFixedZoom(-1);

        } else {
            // "Screen size is neither large, normal or small"
        }
    }

    private void IniciateMap() {

        this.Map.setMapType(4);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo((float) (7 + this.getFixedZoom()));

        this.Map.animateCamera(zoom);

        this.Map.setTrafficEnabled(true);
        this.Map.getUiSettings().setIndoorLevelPickerEnabled(true);
        this.Map.getUiSettings().setCompassEnabled(true);
        this.Map.getUiSettings().setZoomControlsEnabled(true);
        this.Map.setPadding(0, 70, 0, 0);

    }

    private void LoadArrays() {

        for (int i = 0; i < this.Constante.getZoneList().size(); ++i) {

            ParentItems[i] = this.getRes(this.Constante.getZoneList().get(i).getName());

        }


        for (int i = 0; i < this.Constante.getNorthPacificList().size(); ++i) {

            ChildItems[i] = this.getRes(this.Constante.getNorthPacificList().get(i)
                    .getName());

        }

        for (int i = 0; i < this.Constante.getNorthPacificList().size(); ++i) {

            this.NorthPacificdItems[i] = this.getRes(this.Constante.getNorthPacificList()
                    .get(i).getName());

        }

        for (int i = 0; i < this.Constante.getCentralPacificList().size(); ++i) {

            this.CentralPacificItems[i] = this.getRes(this.Constante
                    .getCentralPacificList().get(i).getName());

        }

        for (int i = 0; i < this.Constante.getSouthPacificList().size(); ++i) {

            this.SouthPacificChildItems[i] = this.getRes(this.Constante
                    .getSouthPacificList().get(i).getName());

        }

        for (int i = 0; i < this.Constante.getSouthCaribbeanList().size(); ++i) {

            this.SouthCaribbeanItems[i] = this.getRes(this.Constante
                    .getSouthCaribbeanList().get(i).getName());

        }

        for (int i = 0; i < this.Constante.getOriginList().size(); ++i) {

            OriginItems[i] = this.getRes(this.Constante.getOriginList().get(i).getName());

        }

    }

    private void InicializeButtons() {

        // Begin buttons
        Log.d("DBG", "InicializeButtons");
        Button btnRoute = (Button) this.findViewById(R.id.btnRoute);

        btnRoute = (Button) this.findViewById(R.id.btnRoute);
        btnRoute.setTypeface(this.Constante.getFont());
        btnRoute.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                if (Constante.getDestination() != null) {

                    ClearMarkets();

                    Destination = Constante.getDestination().getBoundsCoordinates().get(3);

                    Intent intent = new Intent(Act, SourceActivity.class);
                    startActivityForResult(intent, 2);
                } else {
                    Constante.IniciateSuperToast(getString(R.string
                            .youneedtoselectadestinationfirst), getApplicationContext(), 0,
                            R.drawable.ic_palmtree, 2000);
                }


            }

        });

        Button btnZones = (Button) this.findViewById(R.id.btnZones);
        btnZones.setTypeface(this.Constante.getFont());
        btnZones.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.d("DBG", "btnZones.setOnClickListener");
                Intent intent = new Intent(Act, ZoneActivity.class);
                startActivityForResult(intent, 3);


            }

        });

        Button btnBeaches = (Button) this.findViewById(R.id.btnBeaches);
        btnBeaches.setTypeface(this.Constante.getFont());
        btnBeaches.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Perform action on click

                Intent intent = new Intent(Constante.getActivity(), com.istudio.crsurfguide.list.List.class);
                startActivityForResult(intent, 1);

            }
        });

        Button btnSpots = (Button) this.findViewById(R.id.btnGPS);
        btnSpots.setTypeface(this.Constante.getFont());
        btnSpots.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String message = "GPS: "
                        + String.format("%.6f",
                        Map.getCameraPosition().target.latitude)
                        + ", "
                        + String.format("%.6f",
                        Map.getCameraPosition().target.longitude)
                        + ", " + Map.getCameraPosition().zoom;
                // Constante.IniciateSuperToast(message, getApplicationContext(), Act, SuperToast
                // .ANIMATION_SCALE, drawable.ic_palmtree, 7000);

            }
        });

        Button btnSwell = (Button) this.findViewById(R.id.btnSwell);
        btnSwell.setTypeface(this.Constante.getFont());


        if (this.Constante.getScreenSize() == Configuration.SCREENLAYOUT_SIZE_LARGE) {

            final Button btnOrientation = (Button) this.findViewById(R.id.btnOrientation);
            btnOrientation.setVisibility(View.VISIBLE);
            btnOrientation.setTypeface(this.Constante.getFont());
            btnOrientation.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // Perform action on click

                    if (getResources().getConfiguration().orientation == 2) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        btnOrientation.setText("Landscape");
                    } else {
                        // defaults to landscape
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        btnOrientation.setText("Portrait");
                    }
                    if (ActualPlace != null)
                        CenterScreen(ActualPlace.getBoundsCoordinates(), false);
                }
            });

        }

        // End buttons

    }

    private void InicializeRoute() {

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
       // Map = fm.getMap();

        if (Map != null) {

            // Enable MyLocation Button in the Map
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission
                    .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                    .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {

                Map.setMyLocationEnabled(true);
            }

            // Setting onclick event listener for the map
            Map.setOnMapClickListener(new OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {

                    // Already two locations
                    if (MarkerPoints.size() > 1) {
                        MarkerPoints.clear();
                        Map.clear();
                    }

                    // Adding new item to the ArrayList
                    MarkerPoints.add(point);

                    // Creating MarkerOptions
                    MarkerOptions options = new MarkerOptions();

                    // Setting the position of the marker
                    options.position(point);

                    /**
                     * For the start location, the color of marker is GREEN and
                     * for the end location, the color of marker is RED.
                     */
                    if (MarkerPoints.size() == 1) {
                        options.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    } else if (MarkerPoints.size() == 2) {
                        options.icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }

                    // Add new marker to the Google Map Android API V2
                    Map.addMarker(options);
                    Log.d("LOG","CalculateRoute");
                    // Checks, whether start and end locations are captured
                    if (MarkerPoints.size() >= 2) {


                        Origin = MarkerPoints.get(0);
                        Destination = MarkerPoints.get(1);
                        CalculateRoute();

                    }

                }
            });
        }

        // End routes

    }

    private void InicializeRadioButtonsModes() {

        // Getting reference to rb_driving
        rbDriving = (RadioButton) findViewById(R.id.rb_driving);

        // Getting reference to rb_bicylcing
        rbBiCycling = (RadioButton) findViewById(R.id.rb_bicycling);

        // Getting reference to rb_walking
        rbWalking = (RadioButton) findViewById(R.id.rb_walking);

        // Getting Reference to rg_modes
        rgModes = (RadioGroup) findViewById(R.id.rg_modes);

        rgModes.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // Checks, whether start and end locations are captured
                CalculateRoute();
            }
        });
    }

    private void InicializeRadioButtonsRoutes() {

        rbNormal = (RadioButton) findViewById(R.id.rb_normal);
        rbAlternative = (RadioButton) findViewById(R.id.rb_alternative);

        // Getting Reference to rg_routes
        rgRoutes = (RadioGroup) findViewById(R.id.rg_routes);

        rgRoutes.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // Checks, whether start and end locations are captured
                CalculateRoute();

            }
        });
    }

    private void LoadMetricSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.metric_spinner);

        String [] items = new String[2];
        items[0]="km";
        items[1]="ml";

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.spinner_item, items) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTypeface(Constante.getFontBody());
                return v;
            }


            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                ((TextView) v).setTypeface(Constante.getFontBody());
                ((TextView) v).setBackgroundColor(getApplicationContext().getResources().getColor
                        (R.color.white));
                ((TextView) v).setTextColor(getApplicationContext().getResources().getColor(R.color.black));

                return v;
            }
        };

        spinner.setAdapter(adapter2);

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                if (pos == 0) {

                    DistanceDuration.setText("" + DistanceKm + ", " + Duration);
                    DistanceDuration.setTypeface(Constante.getFontBody());

                } else if (pos == 1) {

                    try {

                        if (DistanceMiles == "") {

                            Double dDistanceMiles = Double.parseDouble(DistanceKm.replace(" km",
                                    "")) * 0.62137;
                            DistanceMiles = String.format("%.1f", dDistanceMiles) + " ml";

                        }
                        DistanceDuration.setText("" + DistanceMiles.replace(",", ".") + ", " +
                                Duration);
                        DistanceDuration.setTypeface(Constante.getFontBody());

                    } catch (Exception ex) {

                    }

                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });

    }

    private void InitCameraChangeListener() {

        this.Map.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {
                if (FlagShowMarkers) {

                    CenterScreen(Constante.getCountry().getBoundsCoordinates(),
                            true);
                    FlagShowMarkers = false;
                }
            }
        });

    }

    private void CenterScreen(List<LatLng> pListCoord, Boolean pPadding) {

        DisplayMetrics metrics = getApplicationContext().getResources()
                .getDisplayMetrics();
        int width = metrics.widthPixels;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < pListCoord.size(); ++i)
            builder.include(pListCoord.get(i));
        LatLngBounds bounds = builder.build();

        int padding = 0;

        if (pPadding) {

            padding = ((width * 5) / 100); // offset from edges of the map
        }

        // in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        Map.animateCamera(cu);

    }

    private void CenterScreenEntries(List<Entry> pListCoord, Boolean pPadding) {

        DisplayMetrics metrics = getApplicationContext().getResources()
                .getDisplayMetrics();
        int width = metrics.widthPixels;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < pListCoord.size(); ++i) {

            if(pListCoord.get(i).getBoundsCoordinates()!=null) {
                builder.include(pListCoord.get(i).getBoundsCoordinates().get(0));
            }

        }
        LatLngBounds bounds = builder.build();

        int padding = 0;

        if (pPadding) {

            padding = ((width * 5) / 100); // offset from edges of the map
        }

        // in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        Map.animateCamera(cu);

    }

    private void CaptureMapScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                bitmap = snapshot;
                try {
                    FileOutputStream out = new FileOutputStream("/mnt/sdcard/crsg-route-" +
                            System.currentTimeMillis() + ".png");

                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Map.snapshot(callback);
    }

    private void AddMarket(Entry place, int pResourceId) {

        if (place.getBoundsCoordinates() != null) {

            MarkerOptions o = new MarkerOptions();
            o.position(place.getBoundsCoordinates().get(0));
            o.title(this.getRes(place.getName()));

            if (this.getRes(place.get_textoDebajo()) != null) {

                o.snippet(this.getRes(place.get_textoDebajo()));
            } else {
                o.snippet(this.getRes(place.getName()));
            }
            o.icon(BitmapDescriptorFactory.fromResource(pResourceId));

            Marker m = Map.addMarker(o);

            MarkerList.add(m);

        }


    }

    private void AddMarket(LatLng place) {

        this.AddMarket(place, R.drawable.ic_wave);


    }

    private void AddMarket(LatLng place, int pImage) {


        MarkerOptions o = new MarkerOptions();
        o.position(place);

        o.icon(BitmapDescriptorFactory.fromResource(pImage));

        Marker m = Map.addMarker(o);

        MarkerList.add(m);


    }

    private void ClearMarkets() {

        for (int i = 0; i < this.MarkerList.size(); ++i) {

            this.MarkerList.remove(i);

        }
        this.MarkerList.clear();
        this.Map.clear();

    }

    private void AddMarkersToMapNorthPacific() {

        for (int i = 0; i < this.Constante.getNorthPacificList().size(); ++i) {

            this.AddMarket(this.Constante.getNorthPacificList().get(i),
                    R.drawable.ic_wave);

        }

    }

    private void AddMarkersToMapCentralPacific() {

        for (int i = 0; i < this.Constante.getCentralPacificList().size(); ++i) {

            this.AddMarket(this.Constante.getCentralPacificList().get(i),
                    R.drawable.ic_wave);
        }

    }

    private void AddMarkersToMapSouthPacific() {

        for (int i = 0; i < this.Constante.getSouthPacificList().size(); ++i) {

            this.AddMarket(this.Constante.getSouthPacificList().get(i),
                    R.drawable.ic_wave);
        }

    }

    private void AddMarkersToMapSouthCaribbean() {

        for (int i = 0; i < this.Constante.getSouthCaribbeanList().size(); ++i) {

            this.AddMarket(this.Constante.getSouthCaribbeanList().get(i),
                    R.drawable.ic_wave);
        }

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (Map == null) {
            // Try to obtain the map from the SupportMapFragment.


            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


            // Check if we were successful in obtaining the map.
            if (Map != null) {
                setUpMap(getResources().getString(R.string.costarica));
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Map = googleMap;


        this.Map.setMapType(4);
        this.Map.moveCamera(CameraUpdateFactory.newLatLng(this.Constante.getCountry()
                .getBoundsCoordinates().get(0)));

        // Add a marker in Costa Rica and move the camera
        LatLng cr = new LatLng(9.935674, -84.109647);
        //Map.addMarker(new MarkerOptions().position(cr).title("Costa Rica"));
        Map.moveCamera(CameraUpdateFactory.newLatLng(cr));

        CenterScreen(Constante.getCountry().getBoundsCoordinates(),
                true);

        this.Map.setMinZoomPreference((float) (7 + this.getFixedZoom()));
        this.IniciateMap();
    }

    private void setUpMap(String pMarker) {
       // Map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title(
       //         pMarker));
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        if (rbNormal.isChecked()) {
            AlternativeRoute = false;
        } else if (rbAlternative.isChecked()) {
            AlternativeRoute = true;
        }

        // provideRouteAlternatives
        String alternatives = "provideRouteAlternatives=" + AlternativeRoute;

        // Traveling Mode
        String mode = "mode=driving";

        if (rbDriving.isChecked()) {
            mode = "mode=driving";
            Mode = 0;
        } else if (rbBiCycling.isChecked()) {
            mode = "mode=bicycling";
            Mode = 1;
        } else if (rbWalking.isChecked()) {
            mode = "mode=walking";
            Mode = 2;
        }

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
                + mode + "&" + alternatives;

        // Output format
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&key=" +"AIzaSyCBhZgdJKOrw0FtcVvbK_tYrGEZh-rQ-p0";


        // Building the url to the web service
        //String url = "https://maps.googleapis.com/maps/api/directions/"
        //        + output + "?" + parameters;
        Log.d("LOG",url);
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private String ValidateGooglePlayServices() {

        String result = "";
        // Getting status
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status == 0) {
            // result = "Google Play Services are available";
            result = this.getResources().getString(
                    R.string.googleplayservicesavailable);
            this.Constante.setGoogleServices(true);
        } else {
            // result = "Google Play Services are not available";
            result = this.getResources().getString(
                    R.string.googleplayservicesarenotavailable);
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);

            dialog.show();
        }
        return result;
    }

    private String getRes(int pPar) {

        if (pPar == -1) {
            return "";
        } else {
            return this.getResources().getString(pPar);
        }
    }


    // Route methods

    private class MyCustomAsyncTask extends AsyncTask<Void, Void, Void> {

        Entry entrada = null;
        private Context context;

        public MyCustomAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            // write show progress Dialog code here

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // write service code here
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (counter < TourList.size()) {

                entrada = TourList.get(counter);

                if (entrada != null && entrada.getBoundsCoordinates() != null) {

                    if (CalculateRoute) {

                        Constante.setOrigin(Constante.getOriginList().get(0));
                        Constante.setDestination(entrada);
                        Origin = Constante.getOrigin().getBoundsCoordinates().get(0);
                        Destination = Constante.getDestination().getBoundsCoordinates().get(2);
                        CalculateRoute();

                    } else {

                        CenterScreen(new ArrayList<LatLng>() {{
                            add(entrada.getBoundsCoordinates().get(2));
                            add(entrada.getBoundsCoordinates().get(3));
                        }}, false);

                    }
                    try {
                        Thread.sleep(3500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {


                }
                if (counter < TourList.size()) {

                    new MyCustomAsyncTask(context).execute();

                    ++counter;

                }
            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        private Context context;

        public DownloadTask(Context context) {
            this.context = context;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(context);

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,
            String>>>> {

        private Context context;

        public ParserTask(Context context) {
            this.context = context;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            if (result.size() < 1) {

                Constante.IniciateSuperToast(getApplicationContext().getResources().getString(R.string.coordenatesnotavailable), getApplicationContext(), 0, R.drawable.ic_wave, 2000);

                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {

                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                double lat;
                double lng;

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {

                    HashMap<String, String> point = path.get(j);

                    if (j == 0) { // Get distance from the list

                        DistanceKm = (String) point.get("distance");
                        continue;

                    } else if (j == 1) { // Get Duration from the list

                        Duration = (String) point.get("duration");
                        continue;

                    }

                    lat = Double.parseDouble(point.get("lat"));
                    lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(4);

                // Changing the color polyline according to the mode
                if (Mode == MODE_DRIVING) {
                    lineOptions.color(Color.MAGENTA);
                } else if (Mode == MODE_BICYCLING) {
                    lineOptions.color(Color.GREEN);
                } else if (Mode == MODE_WALKING) {
                    lineOptions.color(Color.BLUE);
                }

                if (AlternativeRoute) {
                    lineOptions.color(Color.CYAN);
                }
            }

            DistanceDuration.setText("" + DistanceKm + ", " + Duration);
            DistanceDuration.setTypeface(Constante.getFontBody());
            DistanceDurationLayout.setVisibility(View.VISIBLE);
            Map.setPadding(0, 130, 0, 0);

            for (Polyline line : Polylines) {
                line.remove();
            }
            Polylines.clear();

            Polylines.add(Map.addPolyline(lineOptions));

        }
    }

}