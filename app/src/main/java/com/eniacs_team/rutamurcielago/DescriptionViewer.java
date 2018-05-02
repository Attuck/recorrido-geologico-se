package com.eniacs_team.rutamurcielago;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class DescriptionViewer extends AppCompatActivity implements View.OnTouchListener {

    private BaseDatos baseDatos;
    private int id;
    private TouchableWebView webView;
    private static final String HTML_IMG_TEMPLATE = "\n<figure>\n  <img src=\"$RUTA\"  width=\"$width\"/>\n <figcaption>$DESC</figcaption>\n  </figure>\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_viewer);

//        TextView descriptionView = (TextView)findViewById(R.id.description_text);
        webView = (TouchableWebView) findViewById(R.id.description_webview);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        long width = size.x;
//        int height = size.y;
//        width = Math.round(0.4width);
        Log.d("WindowWidth",String.valueOf(width));
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
        Log.d("description",description);
        String[] paragraphs = description.split("\n");
        StringBuilder data = new StringBuilder();
        data.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
        data.append("<style>img{display: inline;height: auto;max-width: 100%;}</style>");
        data.append("<body>");
        boolean hasImg = false;
        for(String paragraph : paragraphs){
            if(paragraph.startsWith("img_id=")){
                Log.d("Image","this text has an img");
                try{
                    int imgId = Integer.parseInt(paragraph.substring(7));
                    BaseDatos.ImageObject imagen = baseDatos.selectImagen(imgId);
                    String htmlImg = HTML_IMG_TEMPLATE.replace("$RUTA",imagen.ruta).replace("$DESC",imagen.descripcion).replace("$width",String.valueOf(width));
                    data.append(htmlImg);
                }catch(Exception e){
                    Log.d("ErrorFindingImage","unable to find Img with id:" + paragraph.substring(7));
                }
            }else {
                data.append("<p>");
                data.append(paragraph);
                data.append("</p>");
            }
        }
        data.append("</body>");
        Log.d("descriptionHtml",data.toString());
        webView.setPadding(0, 0, 0, 0);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(false);
        webView.setOnTouchListener(this);
        webView.loadDataWithBaseURL("file:///android_asset/",data.toString(), "text/html", "UTF-8", null);
//        descriptionView.setText(description);


    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        WebView.HitTestResult hr = ((WebView)v).getHitTestResult();
        String path = hr.getExtra();
        Log.i("HITtouch", "getExtra = "+ path + "\t\t Type=" + hr.getType());
        if(hr.getExtra() != null && path.contains("Media")) {
            Intent imagen = new Intent(this, FullscreenImage.class);
            String ruta = hr.getExtra().replace("file:///android_asset/", "");
//        BaseDatos.ImageObject img = baseDatos.selectImagenByRuta(ruta);
            imagen.putExtra("ruta", ruta);
            this.startActivity(imagen);
        }

        return false;
    }




}
