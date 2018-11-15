package mobilityv1.smartappsolutions.com.mobilityv1.modelo;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import mobilityv1.smartappsolutions.com.mobilityv1.database.AdapterDB;

/**
 * Created by user on 30/11/2017.
 */

public class Localizacion {

    private final static String TAG ="Localizacion";

    private String fechaReporte;
    private String fechaUltimaMedicion;
    private String idUsuario;
    private String latitud;
    private String longitud;
    private String direccion;

    private final static String FECHA_REPORTE ="uFechaReporte";
    private final static String FECHA_ULTIMA_MEDICION="uFechaUltimaMedicion";
    private final static String ID_USUARIO= "uIdUsuario";
    private final static String LATITUD = "uLatitud";
    private final static String LONGITUD = "uLongitud";
    private final static String DIRECCION = "uDireccion";

    public static final String DATABASE_TABLE_REPORTE_LOCALIZACION="localizacion";
    public static final String ID_REPORTE_LOCALIZACION ="_id";
    public final static String KEY_FECHA_REPORTE =FECHA_REPORTE;
    public final static String KEY_FECHA_ULTIMA_MEDICION =FECHA_ULTIMA_MEDICION;
    public final static String KEY_ID_USUARIO= ID_USUARIO;
    public final static String KEY_LATITUD = LATITUD;
    public final static String KEY_LONGITUD = LONGITUD;
    public final static String KEY_DIRECCION =DIRECCION;

    public static final String DATABASE_CREATE_TABLE_REPORTE_LOCALIZACION =
            "create table "+DATABASE_TABLE_REPORTE_LOCALIZACION+"("+ID_REPORTE_LOCALIZACION+" integer primary key autoincrement," +
                    " "+KEY_FECHA_REPORTE+" text," +
                    " "+KEY_FECHA_ULTIMA_MEDICION+" text," +
                    " "+KEY_ID_USUARIO+" text," +
                    " "+KEY_LATITUD+" text," +
                    " "+KEY_LONGITUD+" text," +
                    " "+KEY_DIRECCION+" text);";

    public Localizacion(String fechaReporte, String fechaUltimaMedicion, String idUsuario, String latitud, String longitud, String direccion) {
        this.fechaReporte = fechaReporte;
        this.fechaUltimaMedicion = fechaUltimaMedicion;
        this.idUsuario = idUsuario;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
    }

    public String getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(String fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getFechaUltimaMedicion() {
        return fechaUltimaMedicion;
    }

    public void setFechaUltimaMedicion(String fechaUltimaMedicion) {
        this.fechaUltimaMedicion = fechaUltimaMedicion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Map<String, String> getPostParams(){

        Map<String, String>  params = new HashMap<String, String>();

        params.put(FECHA_REPORTE, this.fechaReporte);
        params.put(FECHA_ULTIMA_MEDICION,this.fechaUltimaMedicion);
        params.put(ID_USUARIO, this.idUsuario);
        params.put(LATITUD, this.latitud);
        params.put(LONGITUD, this.longitud);
        params.put(DIRECCION, this.direccion);

        return params;

    }

    public long createDBReporteLocalizacion(){
        Log.d(TAG,"createDBReporteLocalizacion()");

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_FECHA_REPORTE,fechaReporte);
        initialValues.put(KEY_FECHA_ULTIMA_MEDICION,fechaUltimaMedicion);
        initialValues.put(KEY_ID_USUARIO,idUsuario);
        initialValues.put(KEY_LATITUD,latitud);
        initialValues.put(KEY_LONGITUD,longitud);
        initialValues.put(KEY_DIRECCION,direccion);

        return AdapterDB.mSqLiteDatabase.insert(DATABASE_TABLE_REPORTE_LOCALIZACION, null, initialValues);

    }

    public boolean deleteReporteLocalizacion(long id) {
        Log.d(TAG, "delete Product");
        return AdapterDB.mSqLiteDatabase.delete(DATABASE_TABLE_REPORTE_LOCALIZACION, ID_REPORTE_LOCALIZACION + "=" + id, null) > 0;
    }

    public Cursor fetchAllReporteLocalizacion(String id){
        Log.d(TAG,"fetchAllReporteLocalizacion()");
        Cursor mCursor = AdapterDB.mSqLiteDatabase.rawQuery("SELECT * FROM "+DATABASE_TABLE_REPORTE_LOCALIZACION+" WHERE "+ID_REPORTE_LOCALIZACION+"='"+id+"'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


}
