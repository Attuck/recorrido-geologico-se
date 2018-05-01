package com.eniacs_team.rutamurcielago;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.util.location.BeyondarLocationManager;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.World;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Esta actividad proporciona una camara de realidad aumentada que permite sobreponer elementos digitales sobre el mundo real.
 *
 * @author EniacsTeam
 */
public class CameraOSMaps extends FragmentActivity implements OnClickListener, OnClickBeyondarObjectListener {

    private BeyondarFragmentSupport mBeyondarFragment;
    private World mWorld;
    private ImageButton mShowMap;
    private BaseDatos baseDatos;
    private int idPunto = -1;
    private String nPunto;
    //private int length = 0;
    private FloatingActionsMenu actionButton;
    private FloatingActionButton btn_animacion;
    private FloatingActionButton btn_moreinfo;


    private static boolean anim_bool = true;
    private static MediaPlayer mPlayer;

    private static GifImageView geoImage;
    private GifDrawable gifDrawable;

    private boolean isMenuOpen = false;

    final Handler handler = new Handler();

    /**
     * Inicializa la vista, crea el mundo de realidad aumentada y asocia este mundo al fragmento de la camara.
     *
     * @param savedInstanceState contiene los datos mas recientemente suministrados en {@link #onSaveInstanceState(Bundle) onSaveInstanceState}
     */
    @Override
    @SuppressWarnings("ResourceType")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Esconde el titulo de la ventana.
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        loadViewFromXML();
        actionButton = (FloatingActionsMenu)findViewById(R.id.camera_menu);
        actionButton.setVisibility(View.GONE);

        // We create the world and fill it
        mWorld = CustomWorldHelper.generateObjects(this);

        mBeyondarFragment.setMaxDistanceToRender(3000);
        mBeyondarFragment.setDistanceFactor(20);
        mBeyondarFragment.setPushAwayDistance(15);
        mBeyondarFragment.setPullCloserDistance(50);
        mBeyondarFragment.setWorld(mWorld);

        BeyondarLocationManager.addWorldLocationUpdate(mWorld);

        // We need to set the LocationManager to the BeyondarLocationManager.
        BeyondarLocationManager
                .setLocationManager((LocationManager) getSystemService(Context.LOCATION_SERVICE));

        crearFab();
        setAvailabilityFab(false);

        if (savedInstanceState != null) {
            anim_bool = savedInstanceState.getBoolean("Bool_anim");
            idPunto = savedInstanceState.getInt("ID_Punto");
            nPunto = savedInstanceState.getString("N_Punto");
            geoImage.setVisibility(savedInstanceState.getInt("Gif_visible"));
            if (idPunto != -1) {
                setAvailabilityFab(true);
            }

        }

        // set listener for the geoObjects
        mBeyondarFragment.setOnClickBeyondarObjectListener(this);
    }

    /**
     * Carga y establece la vista de la actividad. Ademas, resuelve referencias a elementos visuales de dicha vista,
     * asocia un escuchador de clicks a algunos de ellos y fija el fondo para una de las vistas.
     */
    private void loadViewFromXML() {
        setContentView(R.layout.camera_with_osmaps);

        mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager().findFragmentById(
                R.id.beyondarFragment);

        mShowMap = (ImageButton) findViewById(R.id.imageButton1);
        mShowMap.setOnClickListener(this);

        baseDatos = BaseDatos.getInstancia();

        geoImage = (GifImageView) findViewById(R.id.gifImageView);
    }

    /**
     * Metodo encargado de devolverse al mapa o iniciar la animación dependiendo de cual ha sido el boton apretado por el
     * usuario.
     *
     * @param v la vista que fue seleccionada
     */
    @Override
    public void onClick(View v) {
        if (v == mShowMap) {
            onBackPressed();
//            stopAudio();
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        // Make sure to call the super method so that the states of our views are saved
        savedInstanceState.putBoolean("Bool_anim", anim_bool);
        savedInstanceState.putInt("ID_Punto", idPunto);
        savedInstanceState.putString("N_Punto", nPunto);
        savedInstanceState.putInt("Gif_visible", geoImage.getVisibility());
        super.onSaveInstanceState(savedInstanceState);
        // Save our own state now
    }

    @Override
    public void onBackPressed() {
        stopAudio();
        anim_bool = true;
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // When the activity is resumed it is time to enable the
        // BeyondarLocationManager
        BeyondarLocationManager.enable();
        if(mPlayer != null)
        {
            mPlayer.seekTo(0);
            mPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // To avoid unnecessary battery usage disable BeyondarLocationManager
        // when the activity goes on pause.
        BeyondarLocationManager.disable();
        if(mPlayer != null)
        {
            mPlayer.pause();
            //length = mPlayer.getCurrentPosition();
        }

    }

    /**
     * Metodo encargado de crear fab.
     */
    private void crearFab() {

        btn_animacion = (FloatingActionButton)findViewById(R.id.btn_animacion);
        btn_moreinfo = (FloatingActionButton)findViewById(R.id.btn_moreinfo);

        if (!anim_bool) {
            btn_animacion.setImageResource(R.mipmap.percy);
        } else {
            btn_animacion.setImageResource(R.mipmap.mute_percy);
        }

        //Creo listener para los botones
        OnClickListener btn_Listener = new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v == btn_moreinfo) {
                    Intent intent = new Intent(getApplicationContext(), MenuMultimediaMapa.class);
                    intent.putExtra("id", idPunto);
                    intent.putExtra("nombre", nPunto);
                    startActivity(intent);
                }

                if (v == btn_animacion) {

                    if (anim_bool) {
                        //reproduzco
                        geoImage.setVisibility(View.VISIBLE);
                        playAudio();
                        gifDrawable = (GifDrawable) geoImage.getDrawable();
                        gifDrawable.start();
                        btn_animacion.setImageResource(R.mipmap.mute_percy);
                        anim_bool = false;
                    } else {
                        //cambio icono y stop animacion
                        gifDrawable.stop();
                        geoImage.setVisibility(View.INVISIBLE);
                        stopAudio();
                        btn_animacion.setImageResource(R.mipmap.percy);
                        anim_bool = true;
                    }

                }

            }
        };

        btn_moreinfo.setOnClickListener(btn_Listener);
        btn_animacion.setOnClickListener(btn_Listener);

    }


    /*
     * Metodo que maneja los cliqueos a los objetos de realidad aumentada y crea un boton de recursos asociado.
     */
    @Override
    public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
        if (beyondarObjects.size() > 0) {
            int tempId = (int) beyondarObjects.get(0).getId() - 99;
            int wait = 300;
            Log.d("BeyondARObjectTouched", "tempId " + String.valueOf(tempId));

            if (isMenuOpen)
            {
                wait = 1000;
                actionButton.performClick();
            }

            if (baseDatos.visitadoPreviamente(tempId) != 0)
            {

                idPunto = tempId;
                nPunto = beyondarObjects.get(0).getName();
                Log.d("BeyondARObjectTouched", "name " + nPunto);

                setAvailabilityFab(false);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after wait ms
                        if (idPunto != 1)
                        {
                            setAvailabilityFab(true);
                        }
                    }
                }, wait);
            }
            else
            {
                stopAudio();
                if (gifDrawable != null)
                {
                    gifDrawable.stop();
                }
                ImageView intermedio = new ImageView(CameraOSMaps.this);
                geoImage.setVisibility(View.INVISIBLE);
                btn_animacion.setImageResource(R.mipmap.percy);
                anim_bool = true;
                setAvailabilityFab(false);
                Toast msj = Toast.makeText(this,"Debe visitar el punto antes de poder ver más información", Toast.LENGTH_SHORT);
                msj.show();
            }
        }
    }

    public void setAvailabilityFab(boolean available)
    {
        if (actionButton != null)
        {
            if (available)
            {
                actionButton.setEnabled(true);
                actionButton.setVisibility(View.VISIBLE);
            }
            else {
                actionButton.setEnabled(false);
                actionButton.setVisibility(View.GONE);
            }
        }
    }


    private void playAudio() {
        try {
            AssetFileDescriptor descriptor = baseDatos.selectAudio(idPunto);
            mPlayerBuilder();
            mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(),descriptor.getLength());
            descriptor.close();
            mPlayer.prepare();
            mPlayer.start();
        } catch (Exception e) {
            Log.i("Audio", "Error " + e);
        }
    }

    private void stopAudio() {
        if(mPlayer != null)
        {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void mPlayerBuilder() {
        //Creo reproductor de audio
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            //Listener para que se borre cuando termine de reproducirse audio
            MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopAudio();
                    if (gifDrawable != null)
                    {
                        gifDrawable.stop();
                    }
                    geoImage.setVisibility(View.INVISIBLE);
                    btn_animacion.setImageResource(R.mipmap.percy);
                    anim_bool = true;
                }
            };
            mPlayer.setOnCompletionListener(onCompletionListener);
        }
    }
}
