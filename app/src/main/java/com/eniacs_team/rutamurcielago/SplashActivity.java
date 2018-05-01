package com.eniacs_team.rutamurcielago;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kenca on 21/04/2017.
 */

/**
 * Clase que muestra un splash mientras carga la aplicación y solicita los permisos requeridos por el mapa.
 *
 * @author  EniacsTeam
 */
public class SplashActivity extends AppCompatActivity {
    CustomDialogClass dialogo;
    BaseDatos baseDatos;
    ImageView background;
    int[] backgoundImgs = new int[]{R.drawable.splash_01,R.drawable.splash_02,R.drawable.splash_03};
    int index = 0;
    String Permiso[] = {"android.permission.WRITE_EXTERNAL_STORAGE","android.permission.ACCESS_FINE_LOCATION"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Start home activity

        setContentView(R.layout.splash_background);
        background = (ImageView) findViewById(R.id.background);

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                background.setImageResource(backgoundImgs[index]);
                index++;
            }
        };
        handler.postDelayed(r, 1);
        handler.postDelayed(r, 3000);
        handler.postDelayed(r, 6000);
        r = new Runnable() {
            public void run() {
                requestPermission(Permiso, 1);

            }

        };
        handler.postDelayed(r, 9000);
    }


    /**
     * Metodo encargado de mostrar los dialogos de solicitud de permisos si es necesario.
     *
     * @param permiso hilera de permisos por pedir
     * @param permissionRequestCode resultado de obtencion de permisos
     */
    public void requestPermission(String permiso[], int permissionRequestCode) {
        //Preguntar por permiso
        if(askPermissions()
                && (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
                ActivityCompat.requestPermissions(this, permiso, permissionRequestCode);
        }
        else
        {
            baseDatos = new BaseDatos(this);

            //se crea bien
            int res = baseDatos.selectEstadoDatos(2);
            if (res == 0)
            {
                baseDatos = null;
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            }
            else
            {
                baseDatos = null;
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }

            // close splash activity
            finish();
        }
    }

    /**
     * Metodo encargado de cerciorarse si es o no necesaria la solicitud dinamica de permisos.
     *
     * @return verdadero si android del dispositivo es mayor a Lollipop, en caso contrario falso
     */
    private boolean askPermissions(){

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1)
        {
            return true;
        }
        return false;
    }

    /**
     * Verifica que tenga los permisos apropiados para acceder a la ubicación de usuario.
     *
     * @param  requestCode  codigo del permiso
     * @param  permissions  los permisos que se solicitan
     * @param  grantResults  indica si permiso es concedido o no
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            /*WRITE_EXTERNAL_STORAGE*/
            case 1:
//                permissions[0]; //Marco que he preguntado por este permiso anteriormente
                Boolean storage = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                Boolean location = this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                if (storage && location) {
                    //se crea bien
                    baseDatos = new BaseDatos(this);
                    int res = baseDatos.selectEstadoDatos(2);
                    if (res == 0)
                    {
                        baseDatos = null;
                        startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    }
                    else
                    {
                        baseDatos = null;
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    }
                    // close splash activity

                    if (dialogo!= null) {
                        dialogo = null;
                        dialogo.c = null;

                    }
                    finish();
                } else {
                    //Toast.makeText(this, "Need your storage", Toast.LENGTH_SHORT).show();
                    dialogo = new CustomDialogClass(this);
                    dialogo.show();
                }
                break;

        }
    }

    /**
     * Clase para controlar el dialogo que indica que el usuario no acepto un permiso necesario para el funcionamiento de
     * la apllicacion.
     *
     * @author  EniacsTeam
     */
    public class CustomDialogClass extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Button btnAceptar;
        public TextView txt;

        public CustomDialogClass(Activity a) {
            super(a);
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialogo_ver_mas);
            txt = (TextView) findViewById(R.id.texto_dialogo);
            txt.setText("Se necesitan los permisos de acceso al almacenamiento y tu ubicación GPS para continuar");
            btnAceptar = (Button) findViewById(R.id.aceptar);
            btnAceptar.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.aceptar:
                    dismiss();
                    finish();
                    break;
                default:
                    break;
            }
            dismiss();
        }

    }
}
