// author : Edgar Rios C.
// author : Pablo Mora G.
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.

package com.istudio.crsurfguide.gallery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.R.drawable;
import com.istudio.crsurfguide.custom.BaseActivity;
import com.istudio.crsurfguide.obj.Constante;
import com.istudio.supertoast.SuperToast;

public class ImageGallery extends BaseActivity {

    int[] pics;

    private Constante constante;
    private SuperToast toast;

    public ImageGallery() {

        this.constante = Constante.getInstance(this);
        this.toast = SuperToast.getInstance(constante.getActivity().getApplicationContext());

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        ResourceExplorer explorer = new ResourceExplorer();

        pics = explorer.getAllResourceIDs(R.drawable.class);

        EcoGallery ecoGallery = (EcoGallery) findViewById(R.id.gallery);
        ecoGallery.setAdapter(new ImageAdapter(this));

        ecoGallery.setOnItemClickListener(new EcoGalleryAdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(EcoGalleryAdapterView<?> parent, View view,
                                    int position, long id) {

                for (int j = 0; j < constante.getImageList().size(); ++j) {

                    if (constante.getImageList().get(j).getImageName().equals(constante
                            .getListImages().get(position).getImageName())) {

                        if (!toast.isShowing()) {

                            toast.setDuration(2100);
//                            toast.setAnimation(SuperToast.ANIMATION_FADE);
                            toast.setBackground(com.istudio.R.drawable.shape_black);
                            toast.setTextColor(Color.WHITE);
                            toast.setTextSize((int) constante.getActivity().getResources()
                                    .getDimension(R.dimen.text_size_title));
                            toast.setTypefaceStyle(constante.getFont());
                            toast.setIcon(R.drawable.ic_palmtree, SuperToast.IconPosition.LEFT);
                            toast.setText(constante.getImageList().get(j).getDescription());
                            toast.show();

                        } else {

                            toast.setText(constante.getImageList().get(j).getDescription());

                        }
                    }

                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        toast.setDuration(0);
        super.onBackPressed();
    }

    public class ImageAdapter extends BaseAdapter {

        int imageBackground;
        private Context ctx;

        public ImageAdapter(Context c) {
            ctx = c;
            TypedArray ta = obtainStyledAttributes(R.styleable.ImageGallery);
            imageBackground = com.istudio.R.drawable.shape_photo_black;
            ta.recycle();
        }

        @Override
        public int getCount() {

            return pics.length;
        }

        @Override
        public Object getItem(int arg0) {

            return arg0;
        }

        @Override
        public long getItemId(int arg0) {

            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {

            ImageView iv = new ImageView(ctx);
            iv.setImageResource(pics[arg0]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setPadding(7, 7, 7, 7);
            iv.setBackgroundResource(imageBackground);
            return iv;

        }
    }


}
