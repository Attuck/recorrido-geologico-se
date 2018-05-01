package com.eniacs_team.rutamurcielago;

import android.location.Location;

import org.osmdroid.util.BoundingBox;

import java.util.ArrayList;

/**
 * Clase ayudante que engloba todos los datos de geolocalizacion de interes.
 *
 * @author EniacsTeam
 */
public class DatosGeo {
    static double bsLat=11.052586;
    static double bsLng=-85.594728;
    static double biLat=10.772827;
    static double biLng=-86.078768;
    static Ubicacion ubicacionListener;
    static int cantidadElementos=19;

    public static double[] latitudes(){
        double[] latitude = {10.95124, 10.94081, 10.91958, 10.91338, 10.92506, 10.91753, 10.93645, 10.9502, 10.95016, 10.94015, 10.93171, 10.89343, 10.89428, 10.884, 10.88, 10.85402, 10.85262, 10.85607, 10.85919};
        return latitude;
    }

    public static double[] longitudes(){
        double[] longitud = {-85.70945, -85.77404, -85.79932, -85.80195, -85.81621, -85.78693, -85.81037, -85.875, -85.88396, -85.87438, -85.87749, -85.94944, -85.92604, -85.8998, -85.8791, -85.85996, -85.91207, -85.9103, -85.93745};
        return longitud;
    }

    public static int[] radios(){
        int[] radios = {300, 1000, 400, 250, 500, 400, 500, 450, 450, 400, 450, 1000, 700, 600, 600, 3000, 200, 200, 1000};
        return radios;
    }

    /**
     * Metodo que retorna todas las ubicaciones de los puntos de interes.
     *
     * @return la lista de {@link Location} de todos los sitios geologicos
     */
    public static ArrayList<Location> getLocations(){

		ArrayList<Location>locations = new ArrayList<>();
        double[] latitude = DatosGeo.latitudes();
        double[] longitud =DatosGeo.longitudes();
        for (int i = 0; i < cantidadElementos; i++) {

            Location l=new Location(String.valueOf(i));
            l.setLatitude(latitude[i]);
            l.setLongitude(longitud[i]);
            locations.add(i,l);
        }
        return locations;
    }

    /**
     * Metodo que define el bounding box. Se pueden definir distintos para cada zoom, lat,lon, lat,lon
     *
     * @return
     */
    public static BoundingBox getBoundingBox(int i) {
        if(i==1){//para todos los niveles excepto 16
            return new BoundingBox(11.062255, -85.587361, 10.765595, -86.091224);
        }else{//nivel 16
            return new BoundingBox(10.929974, -85.816022, 10.921589, -85.821822);
        }
    }

    public  static boolean isIntoBoundingBox(Location l){

        if((biLat<l.getLatitude()&&l.getLatitude()<bsLat)&&(biLng<l.getLongitude()&&l.getLongitude()<bsLng)){
            return true;
        }else{
            return false;
        }
    }
}