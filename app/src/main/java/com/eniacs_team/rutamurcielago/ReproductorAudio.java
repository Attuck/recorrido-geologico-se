package com.eniacs_team.rutamurcielago;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Clase encargada de la reproducción de audios.
 */
public class ReproductorAudio extends AppCompatActivity {
    SeekBar seekBar;
    ImageButton reproductor;
    TextView texto;
    Context mContext;

    private MediaPlayer mediaPlayer;
    private BaseDatos baseDatos;

    Handler handler;
    Runnable runnable;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_audio);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle extras= intent.getExtras();
        if(intent.hasExtra("id")){
            id = extras.getInt("id");// Obtiene el id de la vista anterior para consultar en la base de datos.
        }else{
            onBackPressed();
        }

        View customBar = getLayoutInflater().inflate(R.layout.titlebar_text, null);
        TextView tv = (TextView) customBar.findViewById(R.id.textTitle);
        tv.setText(extras.getString("nombre"));

        setTitle("");
        getSupportActionBar().setCustomView(customBar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        handler = new Handler();

        baseDatos = BaseDatos.getInstancia();
        baseDatos.cargarBase();

        seekBar = (SeekBar)findViewById(R.id.sb_reproductor);
        reproductor = (ImageButton) findViewById(R.id.btn_reproductor);
        texto = (TextView) findViewById(R.id.texto_audio);

        String texto_del_audio = baseDatos.selectTextoAudio(id); //Obtiene el texto relacionado al audio para ser mostrado en el textView
        if(texto_del_audio != null){
            texto.setText(texto_del_audio);
            texto.setMovementMethod(new ScrollingMovementMethod());
        }

        playAudio();
        seekBar.setMax(mediaPlayer.getDuration()); //Define el tamaño maximo del seekBar = duracion del audio

        reproductor.setOnClickListener(new View.OnClickListener() { // click listener del boton de reproducir.
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){ // si se esta reproduciendo se pausa y se cambia el icono al de play
                    mediaPlayer.pause();
                    reproductor.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_play_arrow_white_24px));
                }else{// si no, se reproduce el audio y se cambia el icono al de pausa.
                    mediaPlayer.start();
                    playCycle();
                    reproductor.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_pause_white_24px));
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress); //cambia al segundo de reproducción indicado por el seekbar cuando este cambia por interacción humana.
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }


    /**
     * Método que actualiza el seekBar según lo que lleva de audio.
     */
    public void playCycle(){
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if(mediaPlayer.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }

    }


    /**
     * Método llamado cuando la aplicación vuelve a ejecución.
     * Vuelve a poner el audio en play y llama a playCycle para que se actualize el seekBar.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(mediaPlayer != null)
        {
            mediaPlayer.start();
            mContext = this;
            reproductor.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_pause_white_24px));
            playCycle();
        }

    }

    /**
     * Metodo llamado cuando la aplicación se pone en pausa.
     * se pone en pasusa el reproductor.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer != null)
        {
            mediaPlayer.pause();
        }

    }

    /**
     * Método llamado cuando la actividad muere. Destruye el media player para que no quede basura.
     */
    @Override
    protected void onDestroy() {
        stopAudio();
        super.onDestroy();
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
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    /**
     * método que carga el audio al media player para su ejecución.
     */
    private void playAudio() {
        try {
            AssetFileDescriptor descriptor = baseDatos.selectAudio(id);
            mPlayerBuilder();
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(),descriptor.getLength());
            descriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.i("Audio", "Error " + e);
        }
    }

    /**
     * Método llamado para destruir los datos del media player.
     */
    private void stopAudio() {
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            handler.removeCallbacks(runnable);
            seekBar = null;
            mediaPlayer = null;
        }
    }

    /**
     * Método que construye el media player.
     */
    private void mPlayerBuilder() {
        //Creo reproductor de audio
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            //Listener para que se borre cuando termine de reproducirse audio
            MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.seekTo(0);
                    reproductor.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_play_arrow_white_24px));
                    seekBar.setProgress(0);
                }
            };
            mediaPlayer.setOnCompletionListener(onCompletionListener);
        }
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


}
