package com.eniacs_team.rutamurcielago;

/**
 * Created by kenca on 04/06/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class ManagerVoc {

    private BaseDatos baseDatos;
    private ArrayList<HashMap<String, String>> vocList = new ArrayList<HashMap<String, String>>();
    Map<String,String[]> audios = new LinkedHashMap<String, String[]>();

    // Constructor
    public ManagerVoc(){
        baseDatos = BaseDatos.getInstancia();
        baseDatos.cargarBase();
    }

    /**
     * Pide los datos a la base de datos para las actividades VocPlayerActivity y PlayListActivity
     *
     * */
    public ArrayList<HashMap<String, String>> getPlayList(){

        audios = baseDatos.audiosExtraDisponibles();


        if(audios.size()!=0){
            for( Map.Entry<String,String[]> entry : audios.entrySet()){
                HashMap<String, String> voc = new HashMap<String, String>();
                voc.put("id", entry.getKey());
                String[] valores = entry.getValue();
                voc.put("Title", valores[0]);
                voc.put("Texto", valores[1]);
                vocList.add(voc);
            }
        }
        return vocList;
    }


}
