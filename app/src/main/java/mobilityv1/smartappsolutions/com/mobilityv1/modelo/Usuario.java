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

public class Usuario {

    public static String TAG ="Usuario";


    public static final String DATABASE_TABLE_USUARIO="usuario";
    public static final String ID ="_id";

    public static final String KEY_ID_USUARIO ="id";
    public final static String KEY_NOMBRE ="nombre";
    public final static String KEY_APELLIDO ="apellido";
    public final static String KEY_FECHA_NACIMIENTO="fechacNacimiento";
    public final static String KEY_EDAD ="edad";
    public final static String KEY_GENERO ="genero";
    public final static String KEY_CIUDAD ="ciudad";
    public final static String KEY_EMAIL ="email";
    public final static String KEY_LOGIN ="login";
    public final static String KEY_PASSWORD ="password";

           /* "id": "1933",
            "nombre": "dfsf",
            "apellido": "sdf",
            "fechacNacimiento": "1901-01-01",
            "edad": "117",
            "genero": "M",
            "ciudad": "Bogota",
            "email": "dsfs",
            "login": "sdfs",
            "password": "sdfsdf"*/

    private String idUsuario="";
    private String nombre="";
    private String apellido="";
    private String fechaNacimiento="";
    private String edad="";
    private String genero="";
    private String ciudad="";
    private String email="";
    private String login="";
    private String password="";

    public static final String DATABASE_CREATE_TABLE_USUARIO =
            "create table "+DATABASE_TABLE_USUARIO+"("+ ID +" integer primary key autoincrement," +
                    " "+KEY_ID_USUARIO+" text," +
                    " "+KEY_NOMBRE+" text," +
                    " "+KEY_APELLIDO+" text," +
                    " "+KEY_FECHA_NACIMIENTO+" text," +
                    " "+KEY_EDAD+" text," +
                    " "+KEY_GENERO+" text," +
                    " "+KEY_CIUDAD+" text," +
                    " "+KEY_EMAIL+" text," +
                    " "+KEY_LOGIN+" text," +
                    " "+KEY_PASSWORD+" text);";


    public Usuario(){

    }

    public Usuario(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Usuario(String nombre, String apellido, String fechaNacimiento, String edad, String genero, String ciudad, String email, String login, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.genero = genero;
        this.ciudad = ciudad;
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public Usuario(String idUsuario, String nombre, String apellido, String fechaNacimiento, String edad, String genero, String ciudad, String email, String login, String password) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.genero = genero;
        this.ciudad = ciudad;
        this.email = email;
        this.login = login;
        this.password = password;
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", edad='" + edad + '\'' +
                ", genero='" + genero + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUrlLogin(String url){

        StringBuilder urlRegister = new StringBuilder(url);
        urlRegister.append("?");
        urlRegister.append("uLogin="+this.login+"&");
        urlRegister.append("uPassword="+this.password);

        Log.d(TAG,"url registro de usuario");
        return urlRegister.toString();
    }

    public Map<String, String> getPostParamsUrlLogin(){

        Map<String, String>  params = new HashMap<String, String>();

        params.put(KEY_LOGIN, this.login);
        params.put(KEY_PASSWORD, this.password);

        return params;
    }

    public Map<String, String> getPostParams(){

        Map<String, String>  params = new HashMap<String, String>();

        params.put(KEY_NOMBRE, this.nombre);
        params.put(KEY_APELLIDO, this.apellido);
        params.put(KEY_FECHA_NACIMIENTO, this.fechaNacimiento);
        params.put(KEY_EDAD, this.edad);
        params.put(KEY_GENERO, this.genero);
        params.put(KEY_CIUDAD, this.ciudad);
        params.put(KEY_EMAIL, this.email);
        params.put(KEY_LOGIN, this.login);
        params.put(KEY_PASSWORD, this.password);


        return params;

    }

    public long createDBUsuario(){
        Log.d(TAG,"createDBUsuario()");

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_ID_USUARIO,idUsuario);
        initialValues.put(KEY_NOMBRE,nombre);
        initialValues.put(KEY_APELLIDO,apellido);
        initialValues.put(KEY_FECHA_NACIMIENTO,fechaNacimiento);
        initialValues.put(KEY_EDAD,edad);
        initialValues.put(KEY_GENERO,genero);
        initialValues.put(KEY_CIUDAD,ciudad);
        initialValues.put(KEY_EMAIL,email);
        initialValues.put(KEY_LOGIN,login);
        initialValues.put(KEY_PASSWORD,password);

        return AdapterDB.mSqLiteDatabase.insert(DATABASE_TABLE_USUARIO, null, initialValues);

    }

    public long updateDBUsuario(long id){
        Log.d(TAG,"createDBUsuario()");

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_ID_USUARIO,idUsuario);
        initialValues.put(KEY_NOMBRE,nombre);
        initialValues.put(KEY_APELLIDO,apellido);
        initialValues.put(KEY_FECHA_NACIMIENTO,fechaNacimiento);
        initialValues.put(KEY_EDAD,edad);
        initialValues.put(KEY_GENERO,genero);
        initialValues.put(KEY_CIUDAD,ciudad);
        initialValues.put(KEY_EMAIL,email);
        initialValues.put(KEY_LOGIN,login);
        initialValues.put(KEY_PASSWORD,password);


        return AdapterDB.mSqLiteDatabase.update(DATABASE_TABLE_USUARIO,initialValues,""+ ID +"="+id,null);

    }

    public boolean deleteUsuario(long id) {
        Log.d(TAG, "delete Usuario");
        return AdapterDB.mSqLiteDatabase.delete(DATABASE_TABLE_USUARIO, ID + "=" + id, null) > 0;
    }
    public Cursor fetchAllUsuarios(String id){
        Log.d(TAG,"fetchAllReporteLocalizacion()");
        Cursor mCursor = AdapterDB.mSqLiteDatabase.rawQuery("SELECT * FROM "+DATABASE_TABLE_USUARIO+" WHERE "+ ID +"='"+id+"'", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public String getIdDBUsuario() {
        Cursor mCursor=fetchAllUsuarios("1");
        String idUsuario=mCursor.getString(mCursor.getColumnIndex(Usuario.KEY_ID_USUARIO));
        Log.d(TAG,"idUsuario "+idUsuario);
        return idUsuario;
    }

}
