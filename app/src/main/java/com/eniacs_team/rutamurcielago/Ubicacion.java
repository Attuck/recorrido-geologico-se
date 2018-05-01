package com.eniacs_team.rutamurcielago;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase se encarga de obtener la ubicación del usuario y controlar lo que sucede en el onLocationChanged.
 *
 * @author EniacsTeam
 */
public class Ubicacion implements LocationListener {

    private MainActivity mainActivity;
    MyLocationNewOverlay mLocationOverlay;
    private Snackbar snackbar;
    MapView map;
    private  List<Marker> marcadores;
    private ArrayList<Location> locations;
    private int[] distancias;

    private int marcadorActual = -1;
    private Location currentLocation;
//    private boolean isFollowing = false;
    View v;
//    FloatingActionButton btFollowMe;
    private Marker currentPosition;
    GeoPoint routeCenter = new GeoPoint(10.904823, -85.867302);

    /**
     * Constructor de clase, Se inicializan variables globales.
     * @param map
     * @param main: Activity main
     * @param v : View contiene:( layout, drawing, focus change, scrolling, etc..)
     */

    public Ubicacion(final MapView map, final MainActivity main, View v, List<Marker> markers, Marker currentMarker, View center, Activity activity) {
        FloatingActionButton btCenterMap;
        CustomDialogClass dialog;
        this.locations = DatosGeo.getLocations();
        this.distancias = DatosGeo.radios();
        this.v = v;
        //this.currentLocation= location;
        //currentPosition.setPosition(new GeoPoint(location.getLatitude() + 0.0001, location.getLongitude()));
        this.marcadores=markers;
        this.map = map;
        this.currentPosition=currentMarker;
        this.mainActivity = main;
        dialog = new CustomDialogClass(mainActivity);
        gpsActivo(v);
        btCenterMap = (FloatingActionButton) center;
//        btCenterMap.setBackgroundTintList(ColorStateList.valueOf(mainActivity.getResources().getColor(R.color.blanco)));
        btCenterMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View center) {
                if (currentLocation != null) {
                    if(DatosGeo.isIntoBoundingBox(currentLocation)){
                    //map.getController().setZoom(13);
                    map.getController().animateTo(new GeoPoint(currentLocation.getLatitude()+0.0001,currentLocation.getLongitude()));
                    }else{
                        Toast.makeText(mainActivity, "Esta fuera del recorrido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//        this.btFollowMe = (FloatingActionButton) this.mainActivity.findViewById(R.id.ic_follow_me);
//
//
//        btFollowMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isFollowing) {
//                    if(currentLocation!=null){
//                        if(DatosGeo.isIntoBoundingBox(currentLocation)){
//                        map.getController().animateTo(new GeoPoint(currentLocation.getLatitude() + 0.0001, currentLocation.getLongitude()));
//                        }else{
//                            Toast.makeText(mainActivity, "Esta fuera del recorrido", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    isFollowing= true;
//
//                    btFollowMe.setBackgroundTintList(
//                            ColorStateList.valueOf(mainActivity.getResources().getColor(R.color.rojo)));
//                } else {
//                    isFollowing= false;
//                    btFollowMe.setBackgroundTintList(
//                            ColorStateList.valueOf(mainActivity.getResources().getColor(R.color.blanco)));
//                }
//            }
//        });
    }

    /**
     *  Este método lo que hace es crear el Snackbar que se muestra si el gps esta desactivado
     * @param v : contiene la información ( layout, drawing, focus change, scrolling, etc..)
     *          para poder imprimir en pantalla el snackbar
     */
    public void gpsActivo(View v){
        snackbar = Snackbar
                .make(v, "¡Se necesita activar el GPS!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Activar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainActivity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(this.mainActivity.getResources().getColor(R.color.celeste));
        snackbar.setActionTextColor(this.mainActivity.getResources().getColor(R.color.naranja_dark));

    }

    /**
     * Hace visible el snackbar de gps desactivado
     */
    public void mostrarMsjGpsDesactivado(){
        snackbar.show();
    }

    public void desMsjGpsDesactivado(){
        snackbar.dismiss();
    }

    /**
     * Permite actualizar la vista del mapa, cambio de colores y funcionalidad del "ver más" en el marcador
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLocation= new Location(location);
        Mapa.MyInfoWindow ma;
        //Se cambia la ubicación del marcador de mí ubicación
        currentPosition.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));

        int marcador = distanciaEntrePuntos(location);
        //map.getController().animateTo(new GeoPoint(map.getMapCenter().getLatitude()+0.0001,map.getMapCenter().getLongitude()));

//        if(isFollowing) {
//            map.getController().animateTo(new GeoPoint(location.getLatitude() + 0.0001, location.getLongitude()));
//        }

        Marker marker;
        if (marcador == -1){
            if (marcadorActual!= -1){
                marker= marcadores.get(marcadorActual);
                marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_verde));
                marcadores.set(marcadorActual, marker);
            }
        }else{
            if (marcadorActual!= marcador) {
                BaseDatos base = BaseDatos.getInstancia();
                if(marcador==0){
                    Snackbar s = Snackbar
                            .make(v, "Esta cerca de " + base.selectTitulo(marcador + 1) + ".", Snackbar.LENGTH_LONG);


                    s.show();

                }else {
                    Snackbar s = Snackbar
                            .make(v, "Esta cerca de " + base.selectTitulo(marcador + 1) + "", Snackbar.LENGTH_LONG);
                    s.show();
                    Handler handler=new Handler();
                    Runnable r=new Runnable() {
                        public void run() {
                            Toast.makeText(mainActivity, "¡Vaya a la cámara para ver más!",Toast.LENGTH_LONG).show();

                        }
                    };
                    handler.postDelayed(r, 3000);
                }
                if (marcadorActual!= -1) {
                    marker = marcadores.get(marcadorActual);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_verde));

                    marcadores.set(marcadorActual, marker);
                    marker = marcadores.get(marcador);
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_rosa));
                    ma = (Mapa.MyInfoWindow)marker.getInfoWindow();
                    ma.setTipo();
                    if(base.visitadoPreviamente(marcador+1)!=0){
                        CustomWorldHelper.changeARObject(marcador);
                    }
                    marcadores.set(marcador, marker);
                    marcadorActual= marcador;

                }else {
                    marker = marcadores.get(marcador);
                    ma = (Mapa.MyInfoWindow)marker.getInfoWindow();
                    ma.setTipo();
                    if(base.visitadoPreviamente(marcador+1)!=0){
                        CustomWorldHelper.changeARObject(marcador);
                    }
                    marker.setIcon(this.mainActivity.getResources().getDrawable(R.drawable.ic_marker_rosa));
                    marcadores.set(marcador, marker);

                }
            }
        }
        marcadorActual = marcador;
    }

    /**
     * Se ejecuta cuando se desactiva el GPS
     * @param provider: proveedor de GPS
     */
    @Override
    public void onProviderDisabled(String provider) {
       // Toast.makeText(mainActivity, "GPS desactivado", Toast.LENGTH_SHORT).show();
        mostrarMsjGpsDesactivado();

    }

    /**
     * Se ejecuta cuando se Activa el GPS
     * @param provider : proveedor de GPS
     */
    @Override
    public void onProviderEnabled(String provider) {
       // Toast.makeText(mainActivity, "Gps activado", Toast.LENGTH_SHORT).show();
        desMsjGpsDesactivado();
    }

    /**
     * Se ejecuta cuando el status del GPS cambia, puede estar disponible, fuera de servicio o temporalmente no disponible
     * @param provider: proveedor de GPS
     * @param status : AVAILABLE, OUT_OF_SERVICE or TEMPORARILY_UNAVAILABLEa
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                //Toast.makeText(mainActivity, "LocationProvider.AVAILABLE", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.OUT_OF_SERVICE:
                //Toast.makeText(mainActivity, "LocationProvider.OUT_OF_SERVICE", Toast.LENGTH_SHORT).show();
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
               // Toast.makeText(mainActivity, "LocationProvider.TEMPORARILY_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Ahorita devuelve el punto más cercano dentro de los rangos
     * @param current
     * @return
     */
    public int distanciaEntrePuntos(Location current){
        float menorDist= Float.MAX_VALUE;
        int ret= -1;
        for(int i=0;i<locations.size();i++){
            if(current.distanceTo(locations.get(i))<distancias[i]){
                if (current.distanceTo(locations.get(i))<menorDist) {
                    menorDist = current.distanceTo(locations.get(i));
                    ret = i;
                }
            }
        }
        return ret;
    }
}

