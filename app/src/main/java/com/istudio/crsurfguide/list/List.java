// Author: Edgar Ríos Cardenas.
// Está es la clase representando la lista del Pacifico Norte.

package com.istudio.crsurfguide.list;

/**
 * @author Edgar Ríos
 * @modified Pablo Mora Lunes 18 de Abril del 2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.custom.BaseActivity;
import com.istudio.crsurfguide.custom.CustomMessage;
import com.istudio.crsurfguide.obj.Constante;
import com.istudio.crsurfguide.obj.Entry;

import java.util.ArrayList;

public class List extends BaseActivity {

    public final static String EXTRA_MESSAGE = "com.istudio.crsurfguide.MESSAGE";
    private ListView lista;
    private Constante cons;
    private ArrayList<Entry> datos = new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        this.cons = Constante.getInstance(this);
        String msg = this.cons.getZone();

        this.setTitle(msg);

        this.getList(msg);

        lista = (ListView) findViewById(R.id.ListView_listado);

        lista.setAdapter(new ListAdapter(this, R.layout.entry, datos) {

            @Override
            public void onEntrada(Object entrada, View view) {

                if (entrada != null) {

                    TextView texto_superior_entrada = (TextView) view.findViewById(R.id
                            .textView_superior);
                    texto_superior_entrada.setTypeface(cons.getFont());

                    if (texto_superior_entrada != null) {
                        texto_superior_entrada.setText(((Entry) entrada).getName());
                        texto_superior_entrada.setTypeface(cons.getFont());
                        texto_superior_entrada.setTextColor(cons.getActivity().getResources().getColor(R.color.white));

                        texto_superior_entrada.setTextSize(cons.getActivity().getResources().getDimension(R
                                    .dimen.text_size_title));

                    }

                    TextView texto_inferior_entrada = (TextView) view.findViewById(R.id
                            .textView_inferior);

                    texto_inferior_entrada.setVisibility(View.GONE);

                    if (texto_inferior_entrada != null) {

                        if (((Entry) entrada).get_textoDebajo() != -1) {

                            String text = cons.getRes( ( (Entry) entrada).get_textoDebajo());
                            String[] parts = text.split("-");
                            Log.d("DBG","parts[4]: "+parts[4]);
                            texto_inferior_entrada.setText(((Entry) entrada).get_textoDebajo());

                        }
                    }

                    ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen);

                    if (((Entry) entrada).get_textoDebajo() == -1) {

                        imagen_entrada.setEnabled(false);
                        imagen_entrada.setClickable(false);
                        texto_superior_entrada.setClickable(false);

                    }

                    if (imagen_entrada != null) {

                        {
                            // Los objetos inicializados sin imagen deberian poder ser referenciados
                            // en tiempo de ejecucion.

                            if (((Entry) entrada).get_idImagen() == -1) {
                                imagen_entrada.setImageResource(R.drawable.generic2);
                            }
                            else
                                imagen_entrada.setImageResource(((Entry) entrada).get_idImagen());
                        }
                    }
                }
            }
        });

        lista.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view,
                                    int posicion, long id) {

                Entry elegido = (Entry) pariente.getItemAtPosition(posicion);


                if (cons.getTaste() == Constante.Flavor.pro.name()) {

                    if (cons.getMap()) {

                        cons.setDestination(elegido);
                        sendDescripcion2(elegido);

                    } else {

                        sendDescripcion(elegido);

                    }

                } else if (elegido.getHiddenSpot()) {

                    startIntent2(getApplicationContext().getResources().getString(R.string.getpro));


                } else {

                    if (cons.getMap()) {

                        cons.setDestination(elegido);
                        sendDescripcion2(elegido);

                    } else {

                        sendDescripcion(elegido);

                    }


                }

            }
        });

    }

    // Iniciar intencion 2
    private void startIntent2(String pMsj1) {

        Intent intent = new Intent(this, CustomMessage.class);
        intent.putExtra("key0", pMsj1);
        startActivity(intent);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.actionList1) {
            new MyCustomAsyncTask(this).execute();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (this.cons.getDebug())
            getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    private void getList(String param) {

        if (param.equals(getResources().getString(R.string.northpacific))) {

            datos = this.cons.getNorthPacificList();

        } else if (param.equals(getResources().getString(R.string.centralpacific))) {

            datos = this.cons.getCentralPacificList();

        } else if (param.equals(getResources().getString(R.string.southpacific))) {

            datos = this.cons.getSouthPacificList();

        } else if (param.equals(getResources().getString(R.string.southcaribbean))) {

            datos = this.cons.getSouthCaribbeanList();
        }

        for (int i = 0; i < datos.size(); ++i) {

            if (datos.get(i).get_textoDebajo() == -1)
                datos.remove(i);

        }

    }

    public void sendDescripcion(Entry elegido) {
        // Do something in response to button
        Intent intent = new Intent(this, DescriptionPlace.class);

        String message = "";

        String zone = cons.getZone();

        if (elegido.get_textoDebajo() != -1) {

            message = this.getRes(elegido.get_textoDebajo());

            if ((elegido.get_idImagen() + "").equals("-1")) {

                intent.putExtra(EXTRA_MESSAGE, zone + "-" + R.drawable.generic + "-" + message);

            } else {

                intent.putExtra(EXTRA_MESSAGE, zone + "-" + elegido.get_idImagen() + "-" + message);

            }

            startActivity(intent);
        }

    }

    public void sendDescripcion2(Entry elegido) {

        if (elegido.getBoundsCoordinates() != null) {

            this.cons.setDestination(elegido);

            this.exit(elegido.getBoundsCoordinates().get(0).toString());

        } else {
            cons.IniciateSuperToast(getString(R.string.coordenatesnotavailable),
                    getApplicationContext(), 0, R.drawable.ic_wave, 2000);
        }
    }

    public void exit(String coord1) {

        Intent output = new Intent();
        output.putExtra("coord1", coord1);

        setResult(RESULT_OK, output);
        finish();

        super.onBackPressed();

    }

    private String getRes(int pPar) {

        return this.getResources().getString(pPar);

    }

    @Override
    public void onBackPressed() {

        Intent output = new Intent();
        output.putExtra("coord1", "2015");

        setResult(RESULT_OK, output);
        finish();

        super.onBackPressed();
    }

    public class MyCustomAsyncTask extends AsyncTask<Void, Void, Void> {
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

            for (int i = 0; i < datos.size(); ++i) {

                Entry entrada = datos.get(i);

                if (entrada != null) {

                    TextView texto_superior_entrada = (TextView) ((Activity) context)
                            .findViewById(R.id.textView_superior);

                    if (texto_superior_entrada != null) {
                        texto_superior_entrada.setText(((Entry) entrada).getName());

                    }


                    TextView texto_inferior_entrada = (TextView) ((Activity) context)
                            .findViewById(R.id.textView_inferior);
                    texto_inferior_entrada.setVisibility(View.GONE);


                    if (texto_inferior_entrada != null) {
                        texto_inferior_entrada.setText(((Entry) entrada).get_textoDebajo());
                    }
                    ImageView imagen_entrada = (ImageView) ((Activity) context).findViewById(R.id
                            .imageView_imagen);

                    if (imagen_entrada != null) {

                        {

                            if (((Entry) entrada).get_idImagen() == -1)
                                imagen_entrada.setImageResource(R.drawable.generic);
                            else
                                imagen_entrada.setImageResource(((Entry) entrada).get_idImagen());
                        }
                    }
                }
                Intent intent = new Intent(context, DescriptionPlace.class);

                String message;

                String zone = cons.getZone();

                message = getRes(entrada.get_textoDebajo());


                if ((entrada.get_idImagen() + "").equals("-1")) {

                    intent.putExtra(EXTRA_MESSAGE, zone + "-" + R.drawable.generic + "-" +
                            message);

                } else {

                    intent.putExtra(EXTRA_MESSAGE, zone + "-" + entrada.get_idImagen() + "-" +
                            message);

                }

                context.startActivity(intent);

            }

        }
    }


}
