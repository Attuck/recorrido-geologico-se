package com.eniacs_team.rutamurcielago;


/**
 * Created by kenca on 04/06/2017.
 */

import android.content.res.AssetFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.SeekBar;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class VocPlayerActivity extends AppCompatActivity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnPlaylist;
    private SeekBar vocProgressBar;
    private TextView vocTitleLabel;
    private TextView vocCurrentDurationLabel;
    private TextView vocTotalDurationLabel;
    private TextView texto_voc;
    // Media Player
    private  MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private ManagerVoc vocManager;
    private Utilities utils;
    private int seekForwardTime = 2000; // 2000 milisegundos
    private int seekBackwardTime = 2000; // 2000 milisegundos
    private int currentVocIndex = 0;
    private ArrayList<HashMap<String, String>> vocsList = new ArrayList<HashMap<String, String>>();
    private BaseDatos baseDatos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        // All player buttons
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
        vocProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        vocTitleLabel = (TextView) findViewById(R.id.songTitle);
        vocCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        vocTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        texto_voc = (TextView) findViewById(R.id.texto_voc);

        //Bases de datos
        baseDatos = BaseDatos.getInstancia();
        baseDatos.cargarBase();

        // Mediaplayer
        mp = new MediaPlayer();
        vocManager = new ManagerVoc();
        utils = new Utilities();

        // Listeners
        vocProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important

        // Getting all vocs list
        vocsList = vocManager.getPlayList();

        if(vocsList.size() > 0) {
            //playVoc(0);
            mp.pause();
            btnPlay.setImageResource(R.drawable.btn_play);
            Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
            startActivityForResult(i, 100);
        }
        // By default play first voc


        /**
         * Evento click play
         * reproduce un audio y cambia al boton pausa
         * pausa un audio y cambia a reproducir.
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                }else{
                    // Resume voc
                    if(mp!=null){
                        mp.start();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }

            }
        });

        /**
         * Adelanta el audio una cantidad de segundos predeterminada
         * */
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current voc position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than voc duration
                if(currentPosition + seekForwardTime <= mp.getDuration()){
                    // forward voc
                    mp.seekTo(currentPosition + seekForwardTime);
                }else{
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
            }
        });

        /**
         * Retrocede el audio una cantidad de segundos predeterminada
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current voc position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward voc
                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mp.seekTo(0);
                }

            }
        });

        /**
         * Reproduce el siguiente audio
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next voc is there or not
                if(currentVocIndex < (vocsList.size() - 1)){
                    playVoc(currentVocIndex + 1);
                    currentVocIndex = currentVocIndex + 1;
                }else{
                    // play first voc
                    playVoc(0);
                    currentVocIndex = 0;
                }

            }
        });

        /**
         * Reproduce el audio anterior.
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(currentVocIndex > 0){
                    playVoc(currentVocIndex - 1);
                    currentVocIndex = currentVocIndex - 1;
                }else{
                    // play last voc
                    playVoc(vocsList.size() - 1);
                    currentVocIndex = vocsList.size() - 1;
                }

            }
        });





        /**
         * Abre la lista de reproducción.
         * */
        btnPlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
                startActivityForResult(i, 100);
            }
        });

    }

    /**
     * Recibe un indice desde la lista de reproducción y reproduce ese audio.
     *
     * */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
            currentVocIndex = data.getExtras().getInt("vocIndex");
            playVoc(currentVocIndex);
        }

    }

    /**
     * Método para iniciar un audio
     * @param indice - indice del audio
     * */
    public void  playVoc(int indice){
        // Play voc
        try {
            mPlayerBuilder();
            mp.reset();
            AssetFileDescriptor descriptor = baseDatos.selectAudioExtra( Integer.parseInt(vocsList.get(indice).get("id")));
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(),descriptor.getLength());
            descriptor.close();
            mp.prepare();
            mp.start();
            // Displaying Voc title
            texto_voc.setText(vocsList.get(indice).get("Texto"));
            String vocTitle = vocsList.get(indice).get("Title");
            vocTitleLabel.setText(vocTitle);

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.btn_pause);

            // set Progress bar values
            vocProgressBar.setProgress(0);
            vocProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para actualizar el seekbar
     * */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Hilo Runnable para controlar los tiempos del audio
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if(mp!= null){long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            vocTotalDurationLabel.setText(utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            vocCurrentDurationLabel.setText(utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            vocProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
            }
        }
    };

    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * Método para controlar cuando el usuario empieza de arrastrar el seekbar
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * Método para controlar cuando el usuario termina de arrastrar el seekbar
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * Método para controlar si el audio terminó.
     * Reproduce el siguiente audio
     *
     * */
    @Override
    public void onCompletion(MediaPlayer arg0) {


        if(currentVocIndex < (vocsList.size() - 1)){
            playVoc(currentVocIndex + 1);
            currentVocIndex = currentVocIndex + 1;
        }else{
            // play first voc
            playVoc(0);
            currentVocIndex = 0;
        }

    }

    @Override
    public void onDestroy(){
        stopAudio();
        super.onDestroy();
    }


    /**
     * Método llamado cuando se da "click" en el boton de atrás.
     * Mata al media player.
     */
    @Override
    public void onBackPressed() {
        stopAudio();
        super.onBackPressed();
    }

    private void stopAudio() {
        if(mp != null)
        {
            mp.release();
            vocProgressBar = null;
            mp = null;
        }
    }


    /**
     * Método llamado cuando la aplicación vuelve a ejecución.
     * Vuelve a poner el audio en play y llama a playCycle para que se actualize el seekBar.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(mp != null)
        {
            mp.start();
            btnPlay.setImageResource(R.drawable.btn_pause);
            // Updating progress bar
            updateProgressBar();
        }

    }

    /**
     * Metodo llamado cuando la aplicación se pone en pausa.
     * se pone en pasusa el reproductor.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(mp != null)
        {
            mp.pause();
        }

    }

    /**
     * Método para tomar la accion de un item, en este caso para devolverse a la activity anterior.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                //this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * Método que construye el media player.
     */
    private void mPlayerBuilder() {
        //Creo reproductor de audio
        if (mp == null) {
            mp = new MediaPlayer();
        }
    }

}
