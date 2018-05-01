package com.eniacs_team.rutamurcielago;

/**
 * Created by kenca on 04/06/2017.
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * Clase para controlar la lista de reproducción de los audios del Glosario.
 */
public class PlayListActivity extends ListActivity {
    // Vocs list
    public ArrayList<HashMap<String, String>> vocList = new ArrayList<HashMap<String, String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);

        ArrayList<HashMap<String, String>> vocListData = new ArrayList<HashMap<String, String>>();

        ManagerVoc plm = new ManagerVoc();
        // get all vocs from sdcard
        this.vocList = plm.getPlayList();

        // looping through playlist
        for (int i = 0; i < vocList.size(); i++) {
            // creating new HashMap
            HashMap<String, String> voc = vocList.get(i);

            // adding HashList to ArrayList
            vocListData.add(voc);
        }

        // Adding menuItems to ListView
        ListAdapter adapter = new SimpleAdapter(this, vocListData,
                R.layout.playlist_item, new String[] { "Title" }, new int[] {
                R.id.songTitle });

        setListAdapter(adapter);

        // selecting single ListView item
        ListView lv = getListView();
        // listening to single listitem click
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting listitem index
                int vocIndex = position;


                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        VocPlayerActivity.class);
                // Sending songIndex to PlayerActivity
                in.putExtra("vocIndex", vocIndex);
                setResult(100, in);
                // Closing PlayListView
                finish();
            }
        });
    }
}