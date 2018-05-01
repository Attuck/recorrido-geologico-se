package com.eniacs_team.rutamurcielago;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class DescriptionViewer extends AppCompatActivity {

    private BaseDatos baseDatos;
    private int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_viewer);

        TextView descriptionView = (TextView)findViewById(R.id.description_text);
        baseDatos = BaseDatos.getInstancia();
        baseDatos.cargarBase();

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

        String description = baseDatos.selectDescripcion(id);
        descriptionView.setText(description);


    }
}
