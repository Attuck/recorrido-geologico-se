package com.eniacs_team.rutamurcielago;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Esta clase es para pedir permisos de android. Contiene distintos metodos para solicitar permisos
 * y permitir su correcto funcionamiento en la aplicacion
 * @author  EniacsTeam
 */

public class Permisos extends AppCompatActivity {

    Activity activity;
    public Permisos(Activity mActivity)
    {
        this.activity = mActivity;
    }

    /**
     * Metodo encargado de pedir que se le conceda a la aplicacion ciertos permisos.
     */
    public void requestPermission(String permiso, int permissionRequestCode) {
        //Preguntar por permiso
        if(askPermissions())
        {
            ActivityCompat.requestPermissions(activity, new String[]{permiso}, permissionRequestCode);
        }
    }

    private boolean askPermissions(){

        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);

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
                //markAsAsked(permissions[0]); //Marco que he preguntado por este permiso anteriormente
                Boolean storage = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (storage) {
                    //se crea bien


                } else {
                    Toast.makeText(this, "Need your storage", Toast.LENGTH_SHORT).show();
                }
                break;

            /*FINE_LOCATION*/
            case 2:
                //markAsAsked(permissions[0]); //Marco que he preguntado por este permiso anteriormente
                Boolean location = activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                if (location) {
                    //se crea bien
                } else {
                    Toast.makeText(this, "Need your location", Toast.LENGTH_SHORT).show();
                }
                break;

            /*CAMERA*/
            case 3:
                // markAsAsked(permissions[0]); //Marco que he preguntado por este permiso anteriormente
                Boolean camera = activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                if (camera) {
                    //se crea bien
                } else {
                    Toast.makeText(this, "Need your camera", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
