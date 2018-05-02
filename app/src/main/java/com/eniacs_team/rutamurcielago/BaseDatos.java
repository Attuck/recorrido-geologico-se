package com.eniacs_team.rutamurcielago;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base de datos --- Clase que gestiona las consultas a la base de datos
 * @author    EniacsTeam
 */
public class BaseDatos extends SQLiteOpenHelper {
    private static Context context;
    private static BaseDatos baseUnica;

    BaseDatos(Context context) {
        super(context, "IslaMurcielagoDB", null,1);
        this.context=context.getApplicationContext();
        // Al quitar el comentario de la siguiente linea, se borran los datos que hayan sido actualizados en la aplicacion
//        context.deleteDatabase("IslaMurcielagoDB");
        cargarBase();
    }

    public static BaseDatos getInstancia() {
        return baseUnica;
    }

    public static BaseDatos getInstanciaInicial(Context contextoAplicacion) {
        baseUnica = new BaseDatos(contextoAplicacion);
        return baseUnica;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * Carga una nueva base de datos en el caso de que no exista
     */
    public void cargarBase(){
        boolean existeBase = existenciaBase();

        if(!existeBase){
            Log.i("Base de datos", "Creando base");
            this.getReadableDatabase();
            this.close();
            copiarBase();
        }
    }
    /**
     * Verifica si la base de datos existe
     * @return checkDB como booleano
     */
    private boolean existenciaBase(){
        String DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        String DB_NAME = "IslaMurcielagoDB";
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){
            Log.i("Base de datos", "No existe la base ");
        }

        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Carga la base de datos inicial
     * Solo se requiere ejecutar la primera vez que se abre el programa
     */
    public void copiarBase()
    {
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        InputStream myInput = null;
        try
        {
            String package_name = context.getPackageName();
            String DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            String DB_NAME = "IslaMurcielagoDB";
            myInput = context.getAssets().open(DB_NAME);
            myOutput =new FileOutputStream(DB_PATH+ DB_NAME);
            while((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Base de datos", "Se terminó de copiar la base de datos");
        }
        catch(IOException e)
        {
            Log.i("Base de datos", "Error en la copia de la base de datos");
            e.printStackTrace();
        }
    }

    /**
     * Devuelve las imagenes ordenadas disponibles para un punto dado
     * @param idPunto El identificador del lugar de consulta
     * @return map de Drawable y String
     */
    public Map selectImagenes(int idPunto) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Fotos";
        String[] columns = {"Descripcion", "Ruta"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(idPunto)};
        String groupBy = null;
        String having = null;
        String orderBy = "IDFoto";
        String limit = null;

        Map<Drawable,String> imagenes = new LinkedHashMap<Drawable, String>();

        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            if (cursor.moveToFirst()) {
                do {
                    String descripcion = cursor.getString(0);
                    String ruta = cursor.getString(1);

                    try
                    {
                        InputStream ims = context.getAssets().open(ruta);
                        imagenes.put(Drawable.createFromStream(ims, null),descripcion);
                        ims.close();
                    }
                    catch(IOException ex)
                    {
                        Log.i("Base de datos", "Archivo no encontrado.");
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }

        return imagenes;
    }

    /**
     * Devuelve una imagen dado su id
     * @param idImagen El identificador de la imagen de consulta
     * @return ImageObject
     */
    public ImageObject selectImagen(int idImagen) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Fotos";
        String[] columns = {"Descripcion", "Ruta"};
        String selection = "IDFoto =?";
        String[] selectionArgs = {Integer.toString(idImagen)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        ImageObject imagen = new ImageObject();

        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            if (cursor.moveToFirst()) {
                do {
                    String descripcion = cursor.getString(0);
                    String ruta = cursor.getString(1);

                    try
                    {
                        InputStream ims = context.getAssets().open(ruta);
                        Drawable.createFromStream(ims, null);
                        imagen.ruta = ruta;
                        imagen.descripcion = descripcion;
                        ims.close();
                    }
                    catch(IOException ex)
                    {
                        Log.i("Base de datos", "Archivo no encontrado.");
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }

        return imagen;
    }

    public class ImageObject{
        String ruta;
        String descripcion;
        Drawable imagen;
    }

    /**
     * Devuelve las imagenes ordenadas disponibles para un punto dado
     * @param id El identificador del lugar de consulta
     * @return map de Drawable y String
     */
    public Map selectAnimacion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Animaciones";
        String[] columns = {"Ruta", "TextoAnimacion"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(id)};
        String groupBy = null;
        String having = null;
        String orderBy = "IDLugar";
        String limit = null;

        Map<String,String> animaciones = new LinkedHashMap<String, String>();

        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            if (cursor.moveToFirst()) {
                do {
                    String ruta = cursor.getString(0);
                    String texto = cursor.getString(1);
                    animaciones.put(ruta, texto);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }

        return animaciones;
    }

    /**
     * Devuelve la descripcion de un punto dado
     * @param id El identificador del lugar de consulta
     * @return descripcion como String
     */
    public String selectDescripcion(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Lugares";
        String[] columns = {"Descripcion"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        String descripcion = null;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor != null) {
                cursor.moveToFirst();
                descripcion = cursor.getString(0);
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }

        return descripcion;
    }

    /**
     * Devuelve el título de un punto dado
     * @param id El identificador del lugar de consulta
     * @return descripcion como String
     */
    public String selectTitulo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Lugares";
        String[] columns = {"Titulo"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        String titulo = null;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor != null) {
                cursor.moveToFirst();
                titulo = cursor.getString(0);
            }
            cursor.close();
            db.close();
            Log.i("Base de datos", "Título:"+titulo);
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }

        return titulo;
    }

    /**
     * Devuelve el audio para un punto dado
     * @param id El identificador del lugar de consulta
     * @return ruta como descriptor
     */
    public AssetFileDescriptor selectAudio(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Lugares";
        String[] columns = {"Audio"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        String rutaAudio = null;
        AssetFileDescriptor descriptor = null;
        AssetManager assetManager = context.getAssets();

        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor != null) {
                cursor.moveToFirst();
                rutaAudio = cursor.getString(0);
                descriptor = assetManager.openFd(rutaAudio);
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        return descriptor;
    }

    /**
     * Devuelve el video para un punto dado
     * @param id El identificador del lugar de consulta
     * @return ruta como String
     */
    public String selectVideo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Lugares";
        String[] columns = {"Video"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        String rutaVideo = null;

        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor != null) {
                cursor.moveToFirst();
                rutaVideo = cursor.getString(0);
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        return rutaVideo;
    }

    /**
     * Devuelve la informacion disponible sobre los audios adicionales
     * @return audios como un map <String, String[]>
     */
    public Map audiosExtraDisponibles() {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "AudiosAdicionales";
        String[] columns = {"IDAudio","Descripcion", "Texto"};
        String selection = "IDAudio <>?";
        String[] selectionArgs = {"1"};
        String groupBy = null;
        String having = null;
        String orderBy = "IDAudio";
        String limit = null;

        Map<String,String[]> audios = new LinkedHashMap<String, String[]>();

        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(0);
                    String[] textos = new String [2];
                    textos[0] = cursor.getString(1);
                    textos[1] = cursor.getString(2);
                    audios.put(id,textos);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }

        return audios;
    }


    /**
     * Devuelve el audio adicional indicado
     * @param id El identificador del audio
     * @return ruta como descriptor
     */
    public AssetFileDescriptor selectAudioExtra(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "AudiosAdicionales";
        String[] columns = {"Ruta"};
        String selection = "IDAudio =?";
        String[] selectionArgs = {Integer.toString(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;

        String rutaAudio = null;
        AssetFileDescriptor descriptor = null;
        AssetManager assetManager = context.getAssets();

        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor != null) {
                cursor.moveToFirst();
                rutaAudio = cursor.getString(0);
                descriptor = assetManager.openFd(rutaAudio);
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        return descriptor;
    }

    /**
     * Devuelve la transcripcion del audio para un punto dado
     * @param id El identificador del lugar de consulta
     * @return textoAudio como String
     */
    public String selectTextoAudio(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = "Lugares";
        String[] columns = {"TextoAudio"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        String textoAudio = null;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor != null) {
                cursor.moveToFirst();
                textoAudio = cursor.getString(0);
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }

        return textoAudio;
    }

    /**
     * Verifica si hay datos para un punto especifico
     * @param punto El lugar de consulta
     * @param tipoMedia El tipo de archivo que se requiere
     * @return existe como entero 1->Existe, 0->No existe
     */
    public int existenciaPunto(int punto, String tipoMedia) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "Lugares";
        String[] columns = {tipoMedia};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(punto)};
        int estado = 0;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getString(0).isEmpty()){
                    estado = 0;
                } else {
                    estado = 1;
                }
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        Log.i("Base de datos", Integer.toString(estado));
        return estado;
    }

    /**
     * Verifica si hay animaciones para un punto especifico
     * @param punto El lugar de consulta
     * @return existe como entero 1->Existe, 0->No existe
     */
    public int existenciaAnimacion(int punto) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "Animaciones";
        String[] columns = {"IDLugar"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(punto)};
        int estado = 0;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getString(0).isEmpty()){
                    estado = 0;
                } else {
                    estado = 1;
                }
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        Log.i("Base de datos", Integer.toString(estado));
        return estado;
    }

    /**
     * Verifica si hay descripción para un punto especifico
     * @param punto El lugar de consulta
     * @return existe como entero 1->Existe, 0->No existe
     */
    public int existenciaDescripcion(int punto) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "Lugares";
        String[] columns = {"Descripcion"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(punto)};
        int estado = 0;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getString(0).isEmpty()){
                    estado = 0;
                } else {
                    estado = 1;
                }
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        Log.i("Base de datos", Integer.toString(estado));
        return estado;
    }

    /**
     * Verifica si un punto en el mapa ya ha sido visitado
     * @param punto El lugar de consulta
     * @return existe como entero 1->Existe, 0->No existe
     */
    public int visitadoPreviamente(int punto) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "Lugares";
        String[] columns = {"Visitado"};
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(punto)};
        int estado = 0;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                estado = Integer.parseInt(cursor.getString(0));
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        return estado;
    }

    /**
     * Actualiza un punto en el mapa si ha sido visitado
     * @param punto El lugar visitado
     */
    public void agregarVisita(int punto) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "Lugares";
        String selection = "IDLugar =?";
        String[] selectionArgs = {Integer.toString(punto)};
        ContentValues estado = new ContentValues();
        estado.put("Visitado","1");
        try {
            db.update(table, estado, selection, selectionArgs);
            Log.i("Base de datos", "Se actualizo un valor en la base de datos");
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "Error al insertar en la base");
        }
    }

    /**
     * Verifica si un dato ya ha sido cargado
     * * @param datoConsulta El dato que se requiere
     * 1 = Mapa, 2 = Mensaje inicial
     * @return estado como entero
     */
    public int selectEstadoDatos(int datoConsulta) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "DatosCargados";
        String[] columns = {"Estado"};
        String selection = "Condicion =?";
        String[] selectionArgs = {Integer.toString(datoConsulta)};
        int estado = 0;
        try {
            Cursor cursor = db.query(table, columns, selection, selectionArgs, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                estado = Integer.parseInt(cursor.getString(0));
            }
            cursor.close();
            db.close();
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "No hay datos en la base");
        }
        return estado;
    }

    /**
     * Actualiza el estado de datos ya cargados
     * @param datoActualizado El dato ya cargado
     * 1 = Mapa, 2 = Mensaje inicial
     */
    public void actualizarEstado(int datoActualizado) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = "DatosCargados";
        String selection = "Condicion =?";
        String[] selectionArgs = {Integer.toString(datoActualizado)};
        ContentValues estado = new ContentValues();
        estado.put("Estado","1");
        try {
            db.update(table, estado, selection, selectionArgs);
            Log.i("Base de datos", "Se actualizo un valor en la base de datos");
        }
        catch(Exception e)
        {
            Log.i("Base de datos", "Error al insertar en la base");
        }
    }
}