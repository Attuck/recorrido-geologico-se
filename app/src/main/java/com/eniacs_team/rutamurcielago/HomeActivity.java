package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;

public class HomeActivity extends AppCompatActivity {
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Typeface font = Typer.set(getApplicationContext()).getFont(Font.ROBOTO_MEDIUM);
        collapsingToolbar.setExpandedTitleTypeface(font);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));

            }
        };
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.got_to_map);
        mActivity = this;
        BaseDatos baseDatos;
        baseDatos = new BaseDatos(this);
        int res = baseDatos.selectEstadoDatos(2);
        if (res == 0)
        {
            baseDatos.actualizarEstado(2);
            baseDatos = null;
            fab.setVisibility(View.GONE);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(mActivity, "This is my Toast message!",Toast.LENGTH_LONG).show();
//
//                }
//            });
        }
        else
        {
            baseDatos = null;
            fab.setOnClickListener(clickListener);
        }



        Button button = (Button) findViewById(R.id.continue_button);
        button.setOnClickListener(clickListener);

    }
}
