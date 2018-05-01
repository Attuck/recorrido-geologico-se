package com.eniacs_team.rutamurcielago;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;

/**
 * Clase ayudante para crear el mundo de realidad aumentada el cual poseera objetos georeferenciados.
 *
 * @author EniacsTeam
 */
@SuppressLint("SdCardPath")
public class CustomWorldHelper {

    public static World sharedWorld;

    private static BaseDatos baseDatos;

    private static final double[] longitudes = DatosGeo.longitudes();
    private static final double[] latitudes = DatosGeo.latitudes();

    private static final int[] resources
            = {R.drawable.vinicial,R.drawable.vdescartes,R.drawable.vvegetacion,R.drawable.vperidotitas,R.drawable.vsanta_elena,
            R.drawable.vrivas,R.drawable.vdescartes2,R.drawable.vpliegues,R.drawable.vmaravillas,
            R.drawable.vvista,R.drawable.verosion,R.drawable.vintrusiones,R.drawable.vperidotitas2, R.drawable.vdunas,
            R.drawable.vsanta_rosa,R.drawable.visla,R.drawable.varcos,R.drawable.vupwelling,R.drawable.vpaleodunas};

    private static final int[] resources2
            = {R.drawable.inicial,R.drawable.descartes,R.drawable.vegetacion,R.drawable.peridotitas,R.drawable.santa_elena,
            R.drawable.rivas,R.drawable.descartes2,R.drawable.pliegues,R.drawable.maravillas,
            R.drawable.vista,R.drawable.erosion,R.drawable.intrusiones,R.drawable.peridotitas2, R.drawable.dunas,
            R.drawable.santa_rosa,R.drawable.isla,R.drawable.arcos,R.drawable.upwelling,R.drawable.paleodunas};

    /**
     * Metodo que agrega los carteles de realidad aumentada a puntos georeferenciados
     */
    private static void agregarCarteles() {
        /*Se crea un cartel para cada punto en el mapa*/
        for (int i = 0; i < DatosGeo.cantidadElementos; i++)
        {
            // Create an object with an image in the app resources.
            GeoObject go = new GeoObject(100+i);
            go.setGeoPosition(latitudes[i], longitudes[i]);
            if(baseDatos.visitadoPreviamente(i+1) != 0)
            {
                go.setImageResource(resources2[i]);
            }
            else
            {
                go.setImageResource(resources[i]);
            }
            String titulo = baseDatos.selectTitulo(i+1);
            go.setName(titulo);
            Log.d("GeoObjectTitle",titulo);
            // Add the GeoObjects to the world
            sharedWorld.addBeyondarObject(go);
        }
    }

    /**
     * Si no existe un mundo lo crea, genera objetos georeferenciados y los introduce dentro de dicho mundo.
     *
     * @param context contexto del estado actual de la aplicacion
     * @return el mundo de realidad aumentada
     */
    public static World generateObjects(Context context) {
        if (sharedWorld != null) {
            return sharedWorld;
        }
        sharedWorld = new World(context);
        baseDatos = BaseDatos.getInstancia();

        agregarCarteles();

        return sharedWorld;
    }

    /**
     * Registra un cambio de cartel al visitar un punto.
     *
     * @param nObject numero del cartel
     */
    public static void changeARObject(int nObject) {
        if (sharedWorld != null) {
            sharedWorld.getBeyondarObjectList(World.LIST_TYPE_DEFAULT).get(nObject).setImageResource(resources2[nObject]);
        }
    }

}
