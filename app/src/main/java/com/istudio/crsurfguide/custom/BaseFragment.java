// author : Pablo
// Esta clase es la base de los fragmentos para customizar.

package com.istudio.crsurfguide.custom;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.istudio.crsurfguide.R;

/**
 * Created by pablomorag on 4/3/15.
 */
public class BaseFragment extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.Custom();
    }

    protected void Custom() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

}