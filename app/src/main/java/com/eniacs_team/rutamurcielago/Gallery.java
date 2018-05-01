package com.eniacs_team.rutamurcielago;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Esta actividad proporciona una galeria de imagenes con descripciones relacionadas a un sitio geologico especifico.
 *
 * @author EniacsTeam
 */
public class Gallery extends AppCompatActivity {

    private BaseDatos baseDatos;
    private int idPunto;
    private String nPunto;
    private boolean isImage;

    /*
     * Inicializa la vista, crea la galeria y habilita el back button del action bar.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        baseDatos = BaseDatos.getInstancia();
        Bundle extras = getIntent().getExtras();
        idPunto = extras.getInt("id");
        nPunto = extras.getString("nombre");
        isImage = extras.getBoolean("image");

        View customBar = getLayoutInflater().inflate(R.layout.titlebar_text, null);
        TextView tv = (TextView) customBar.findViewById(R.id.textTitle);
        tv.setText(nPunto);

        setTitle("");
        getSupportActionBar().setCustomView(customBar);
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        if (isImage)
        {
            ArrayList<CreateList> createLists = prepareData();
            MyAdapter adapter = new MyAdapter(getApplicationContext(), createLists, idPunto);
            recyclerView.setAdapter(adapter);
        }
        else
        {
            ArrayList<CreateListAnim> createListsAnim = prepareDataAnim();
            AnimAdapter animAdapter = new AnimAdapter(getApplicationContext(), createListsAnim, idPunto);
            recyclerView.setAdapter(animAdapter);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
     * Se encarga de conseguir de la base los datos que contiene el sitio geologico actual y preparar cada celda de la galeria.
     */
    private ArrayList<CreateList> prepareData(){
        Map imagenes = baseDatos.selectImagen(idPunto);


        ArrayList<CreateList> theimage = new ArrayList<>();
        Iterator<Map.Entry<Drawable, String>> it = imagenes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Drawable, String> par = it.next();
            CreateList createList = new CreateList();
            createList.setImage_title(par.getValue());
            createList.setImage_drawable(par.getKey());
            theimage.add(createList);
        }

        return theimage;
    }

    /*
     * Se encarga de conseguir de la base los datos que contiene el sitio geologico actual y preparar cada celda de la galeria de animaciones.
     */
    private ArrayList<CreateListAnim> prepareDataAnim(){
        Map animaciones = baseDatos.selectAnimacion(idPunto);

        ArrayList<CreateListAnim> theanim = new ArrayList<>();
        Iterator<Map.Entry<String, String>> it = animaciones.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> par = it.next();
            CreateListAnim createList = new CreateListAnim();
            createList.setAnim_title(par.getValue());
            String gifPath = par.getKey();
            GifDrawable gifFromAssets = null;

            try{
                gifFromAssets = new GifDrawable( getAssets(), gifPath );
            } catch (IOException ex) { ex.printStackTrace(); }

            createList.setAnim_drawable(gifFromAssets);
            theanim.add(createList);
        }

        return theanim;
    }

    /*
     * Maneja el cliqueo del back button.
     */
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }
}
