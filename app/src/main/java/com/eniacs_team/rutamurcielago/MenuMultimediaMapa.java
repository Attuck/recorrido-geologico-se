package com.eniacs_team.rutamurcielago;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity para generar el menú de datos multimedia disponibles en un sitio.
 */
public class MenuMultimediaMapa extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<listItemMenuMultimedia> listItemns;
    private BaseDatos baseDatos;
    private int id;
    private String nPunto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_multimedia_mapa);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        recyclerView = (RecyclerView) findViewById(R.id.menumultimediamaparv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        listItemns = new ArrayList<>();
        baseDatos = BaseDatos.getInstancia();
        baseDatos.cargarBase();

        Intent intent = getIntent();
        Bundle extras= intent.getExtras();
        if(extras != null){
            id = extras.getInt("id"); // id del punto.
            nPunto = extras.getString("nombre");
        }

        View customBar = getLayoutInflater().inflate(R.layout.titlebar_text, null);
        TextView tv = (TextView) customBar.findViewById(R.id.textTitle);
        tv.setText(nPunto);

        setTitle("");
        actionBar.setCustomView(customBar);
        actionBar.setDisplayShowCustomEnabled(true);
        
        loadRecyclerViewData();
        
        
    }

    /**
     * Método que genera el recycler view con respecto a los datos que se encuentran en la base de datos.
     */
    private void loadRecyclerViewData() {

        if(baseDatos.existenciaDescripcion(id) == 1) {
            Log.d("HayDescripción","true");
            listItemMenuMultimedia item0 = new listItemMenuMultimedia(
                    "Descripción", String.valueOf(id), nPunto
            );
            listItemns.add(item0);
        }
        //if(baseDatos.existenciaPunto(id, "Imagen") == 1){
        listItemMenuMultimedia item3 = new listItemMenuMultimedia(
                "Imagen", String.valueOf(id), nPunto
        );
        listItemns.add(item3);

        //}
        if(baseDatos.existenciaPunto(id, "Audio") == 1){
            listItemMenuMultimedia item2 = new listItemMenuMultimedia(
                    "Audio", String.valueOf(id), nPunto
            );
            listItemns.add(item2);

        }
        if(baseDatos.existenciaPunto(id, "Video") == 1) {
            listItemMenuMultimedia item1 = new listItemMenuMultimedia(
                    "Video", String.valueOf(id), nPunto
            );
            listItemns.add(item1);

        }
        if(baseDatos.existenciaAnimacion(id) == 1){
            listItemMenuMultimedia item4 = new listItemMenuMultimedia(
                    "Animación", String.valueOf(id), nPunto
            );
            listItemns.add(item4);

        }

        if(listItemns.size() == 0){
            Toast.makeText(getApplicationContext(), getString(R.string.No_multimedia), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }else{
            adapter = new AdapterMenuMultimedia(listItemns, this);
            recyclerView.setAdapter(adapter);
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
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
