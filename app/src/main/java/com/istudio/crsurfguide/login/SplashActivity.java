// author : Pablo Mora G.
// Ultima edición: Pablo Mora González
// Fecha: Lunes 18 de Abril 2016.

package com.istudio.crsurfguide.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
//import com.facebook.login.widget.ProfilePictureView;;
import com.istudio.crsurfguide.login.ProfilePictureView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.istudio.blureffect.Blur;
import com.istudio.blureffect.ImageUtils;
import com.istudio.blureffect.ScrollableImageView;
import com.istudio.crsurfguide.MainActivity;
import com.istudio.crsurfguide.R;
import com.istudio.crsurfguide.custom.BaseActivity;
import com.istudio.crsurfguide.obj.Constante;
import com.istudio.crsurfguide.ui.debug.LogBuffer;
import com.istudio.crsurfguide.ui.debug.DebugHudKt;
import androidx.compose.ui.platform.ComposeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SplashActivity extends BaseActivity {

    private static final String BLURRED_IMG_PATH = "blurred_image.png";
    private static final int TOP_HEIGHT = 700;

    private View headerView;
    private View headerView2;
    private ScrollableImageView mBlurredImageHeader;
    private ScrollableImageView mBlurredImageBottom;

    private TextView info;
    private CallbackManager callbackManager;
    //private LoginButton loginButton;
    private ProfilePictureView profile;
    private Constante constante;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            LogBuffer.INSTANCE.e("CRITICAL", "Uncaught Exception in SplashActivity", throwable);
            // Dar un momento para que el log se procese (aunque sea en memoria)
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        });

        LogBuffer.INSTANCE.d("SplashActivity", "onCreate: Iniciando Splash...");

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_splash);

        // Inicializar el ComposeView del HUD de depuración de forma segura
        try {
            ComposeView debugHudView = findViewById(R.id.debug_hud_view);
            if (debugHudView != null) {
                androidx.lifecycle.ViewTreeLifecycleOwner.set(debugHudView, this);
                androidx.savedstate.ViewTreeSavedStateRegistryOwner.set(debugHudView, this);
                com.istudio.crsurfguide.ui.debug.DebugHudHelper.attachHud(debugHudView);
            }
        } catch (Exception e) {
            LogBuffer.INSTANCE.e("SplashActivity", "Error al inyectar DebugHud", e);
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                LogBuffer.INSTANCE.d("SplashActivity", "MobileAds inicializado.");
            }
        });

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        InitTracker();

        mBlurredImageHeader = (ScrollableImageView) findViewById(R.id.blurred_image_header);
        mBlurredImageBottom = (ScrollableImageView) findViewById(R.id.blurred_image_bottom);

        initBlurHeader(R.id.blurred_image_header, headerView, mBlurredImageHeader);

        constante = Constante.getInstance(this);

        InitFonts();
    }

    private void InitTracker(){

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                LogBuffer.INSTANCE.d("Facebook", "AccessToken changed: " + (newToken != null ? "Logged In" : "Logged Out"));
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                LogBuffer.INSTANCE.d("Facebook", "Profile changed: " + (newProfile != null ? newProfile.getName() : "None"));
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    private void displayMessage(Profile profile2){
        if(profile2 != null){
            info.setText(this.getApplicationContext().getResources().getString(R.string.hi) + profile2.getName());
            profile.setProfileId(profile2.getId());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    private void InitFrame() {

        ProfilePictureView imagen = (ProfilePictureView) this.findViewById(R.id.picture);
        imagen.setPadding(7, 7, 7, 7);
    }

    private void InitFonts() {

        TextView tx = (TextView) findViewById(R.id.txtTitle);
        tx.setTypeface(this.constante.getFont());

        Button btn = (Button) findViewById(R.id.btnNext2);
        btn.setTypeface(this.constante.getFont());

        TextView tx2 = (TextView) findViewById(R.id.selection_user_name);
        tx2.setTypeface(this.constante.getFont());
    }

    private void InitFB() {


        info = (TextView) findViewById(R.id.selection_user_name);


        //loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setTypeface(constante.getFont());

        //profile = (ProfilePictureView) findViewById(R.id.picture);

/*
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile2 = Profile.getCurrentProfile();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    info.setText(getString(R.string.hi) + object.getString("name"));
                                    profile.setProfileId(object.getString("id"));
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday, picture");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
  */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initBlurHeader(int pImage, View pView, final ScrollableImageView pImg) {

        final int screenWidth = ImageUtils.getScreenWidth(this);

        pImg.setScreenWidth(screenWidth);
        mBlurredImageBottom.setScreenWidth(screenWidth);

        // Try to find the blurred image
        final File blurredImage = new File(getFilesDir() + BLURRED_IMG_PATH);

        if (!blurredImage.exists()) {

            // launch the progressbar in ActionBar
            setProgressBarIndeterminateVisibility(true);

            new Thread(new Runnable() {

                @Override
                public void run() {

                    // No image found => let's generate it!
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable
                            .splash, options);


                    Bitmap newImg = Blur.fastblur(SplashActivity.this, image, 12);

                    ImageUtils.storeImage(newImg, blurredImage);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            updateView(screenWidth, pImg);

                            // And finally stop the progressbar
                            setProgressBarIndeterminateVisibility(false);
                        }
                    });

                }
            }).start();

        } else {

            // The image has been found. Let's update the view
            updateView(screenWidth, pImg);

        }

    }

    private void updateView(final int screenWidth, final ScrollableImageView pImg) {

        //  int barHeight = (int) R.dimen.bar_height;
        int barHeight = (int) getResources().getDimension(R.dimen.bar_height);

        Bitmap bmpBlurred = BitmapFactory.decodeFile(getFilesDir() + BLURRED_IMG_PATH);

        Bitmap bmpBlurred2 = bmpBlurred;


        bmpBlurred = Bitmap.createScaledBitmap(bmpBlurred,
                screenWidth,
                (int) (bmpBlurred.getHeight() * ((float) screenWidth) / (float) bmpBlurred
                        .getWidth()),
                false);

        pImg.setoriginalImage(bmpBlurred);

        bmpBlurred2 = Bitmap.createBitmap(bmpBlurred,
                0,
                bmpBlurred.getHeight() - barHeight,
                screenWidth, barHeight);


        mBlurredImageBottom.setoriginalImage(bmpBlurred2);

    }

    public void sendMessage1(View view) {
        LogBuffer.INSTANCE.d("SplashActivity", "Button NEXT clicked. Attempting to start MainActivity...");
        try {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            LogBuffer.INSTANCE.d("SplashActivity", "MainActivity started successfully.");
        } catch (Exception e) {
            LogBuffer.INSTANCE.e("SplashActivity", "CRASH in sendMessage1 (Next Button)", e);
            Toast.makeText(this, "Error al iniciar: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
