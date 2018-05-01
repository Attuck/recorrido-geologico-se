package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.beyondar.android.util.location.BeyondarLocationManager;

import static com.eniacs_team.rutamurcielago.R.mipmap.video;

/**
 * Created by Francisco on 6/8/2017.
 */

public class VideoPlayerController extends Activity {

    private VideoView videoView;
    private MediaController mediaController;
    private static int idPunto;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_view);

        videoView = (VideoView) findViewById(R.id.VideoView);
        mediaController = new MediaController(this);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            idPunto = extras.getInt("id");
        }
        if (savedInstanceState != null) {
            int buffer = savedInstanceState.getInt("Buffer");
            idPunto = savedInstanceState.getInt("IDpunto");
            playvideo();
            videoView.seekTo(buffer);
            videoView.start();
        }
        else
        {
            playvideo();
        }

    }

    /**
     * Metodo para reproducir el video
     */
    private void playvideo(){
        try{
            String videopath = "android.resource://" + this.getPackageName() + "/raw/video"+Integer.toString(idPunto);
            Uri uri = Uri.parse(videopath);
            videoView.setVideoURI(uri);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.start();

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("Buffer",videoView.getCurrentPosition());
        savedInstanceState.putInt("IDpunto",idPunto);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView != null)
        {
            videoView = null;
        }
        if (mediaController != null)
        {
            mediaController = null;
        }
    }

}