package mobilityv1.smartappsolutions.com.mobilityv1;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mobilityv1.smartappsolutions.com.mobilityv1.ConectionManager.ConstanstConnection;
import mobilityv1.smartappsolutions.com.mobilityv1.clases.ShowDialog;
import mobilityv1.smartappsolutions.com.mobilityv1.clases.ShowDialogProgressItem;
import mobilityv1.smartappsolutions.com.mobilityv1.database.AdapterDB;
import mobilityv1.smartappsolutions.com.mobilityv1.modelo.Usuario;

/**
 * Created by user on 30/11/2017.
 */

public class LoginActivity extends Activity implements ShowDialogProgressItem.ShowDialogProgressItemListener,ShowDialog.ShowDialogListener {

    public static final String TAG = "LoginActivity";


    EditText edtLogin;
    EditText edtPassword;
    TextView txvRegister;
    Button btnButtonEntry;

    ShowDialogProgressItem mShowDialogProgressItem;
    ShowDialog mShowDialog;

    String uLogin;
    String uPassword;

    String url;
    AdapterDB db;
    Usuario mUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, TAG + " onCreate()");

        setContentView(R.layout.login);
        edtLogin = (EditText) findViewById(R.id.login_edt_login);
        edtPassword = (EditText) findViewById(R.id.login_edt_password);
        txvRegister = (TextView)findViewById(R.id.login_txv_register);
        btnButtonEntry = (Button) findViewById(R.id.login_edt_entry);

        db = new AdapterDB(getApplicationContext());
        initDB();

        mShowDialogProgressItem = new ShowDialogProgressItem("Espere...", LoginActivity.this, getApplicationContext());
        mShowDialogProgressItem.setmShowDialogProgressItemListener(LoginActivity.this);

        mShowDialog = new ShowDialog("Error", "El servidor no responde", LoginActivity.this, getApplicationContext());
        mShowDialog.setmShowDialogListener(LoginActivity.this);

        txvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,TAG+ "txvRegister onClick()");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnButtonEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, TAG + "btnEntry onClick()");

                uLogin =edtLogin.getText().toString();
                uPassword =edtPassword.getText().toString();

                if(uLogin.equals("")){
                    Toast.makeText(getApplicationContext(),"El campo Login esta vacio",Toast.LENGTH_LONG).show();
                }else if(uPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "El campo password esta vacio", Toast.LENGTH_LONG).show();
                }else{

                    mUsuario = new Usuario(edtLogin.getText().toString(), edtPassword.getText().toString());
                    Log.d(TAG, mUsuario.toString());

                    //url = mUsuario.getUrlLogin(ConstanstConnection.BASE_URL_LOGIN);
                    url = ConstanstConnection.BASE_URL_API_REST+ConstanstConnection.URL_LOGIN;
                    Log.d(TAG, url);

                    final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            requestQueue.stop();
                            Log.d(TAG, response.toString());
                            mShowDialogProgressItem.dimissDialog();

                            try {

                                JSONObject jsonResponse = new JSONObject(response.toString());
                                String status = jsonResponse.getString("status");
                                Log.d(TAG, "status: " + status);

                                if (status.equals("true")) {



                                    JSONObject jsonUser = jsonResponse.getJSONObject("usuario");

                                    String id = jsonUser.getString(Usuario.KEY_ID_USUARIO);
                                    String nombre = jsonUser.getString(Usuario.KEY_NOMBRE);
                                    String apellido = jsonUser.getString(Usuario.KEY_APELLIDO);
                                    String fechaNacimiento = jsonUser.getString(Usuario.KEY_CIUDAD);
                                    String edad = jsonUser.getString(Usuario.KEY_EDAD);
                                    String genero = jsonUser.getString(Usuario.KEY_GENERO);
                                    String ciudad = jsonUser.getString(Usuario.KEY_CIUDAD);
                                    String email = jsonUser.getString(Usuario.KEY_EMAIL);
                                    String login = jsonUser.getString(Usuario.KEY_LOGIN);
                                    String password = jsonUser.getString(Usuario.KEY_PASSWORD);

                                    Log.d(TAG, "se  obtiene objeto usuario a partir de respuesta JSON " +
                                            "Usuario{" +
                                            " id='" + id + '\'' +
                                            " nombre='" + nombre + '\'' +
                                            ", apellido='" + apellido + '\'' +
                                            ", fechaNacimiento='" + fechaNacimiento + '\'' +
                                            ", edad='" + edad + '\'' +
                                            ", genero='" + genero + '\'' +
                                            ", ciudad='" + ciudad + '\'' +
                                            ", email='" + email + '\'' +
                                            ", login='" + login + '\'' +
                                            ", password='" + password + '\'' +
                                            '}'
                                    );

                                    Usuario usuario = new Usuario(id, nombre, apellido, fechaNacimiento, edad, genero, ciudad, email, login, password);
                                    Log.d(TAG, usuario.toString());

                                   Cursor mCursor = usuario.fetchAllUsuarios("1");
                                    if (mCursor.getCount() == 1) {

                                        Log.d(TAG, "ya existe un usuario logeado " + mCursor.getCount());
                                        usuario.updateDBUsuario(1);

                                    } else {

                                        Log.d(TAG, "primer usuario registrado " + mCursor.getCount());
                                        usuario.createDBUsuario();
                                    }

                                    testSQLite3();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                   // finish();



                                } else if (status.equals("false")) {
                                    Log.d(TAG, "login respuesta: " + response.toString());

                                    mShowDialogProgressItem.dimissDialog();
                                    mShowDialog.setMessage("Usuario no encontrado");
                                    mShowDialog.showDialog();


                                }

                            } catch (Exception e) {
                                Log.d(TAG, e.toString());
                            }

                        }

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            mShowDialog.setMessage("El servidor no responde");

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = mUsuario.getPostParams();
                            return params;
                        }

                    };

                    requestQueue.add(stringRequest);
                    mShowDialogProgressItem.showDialog();
                }
            }
        });

    }

    private void initDB() {

        db = new AdapterDB(this);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onTimeOut() {
        Log.d(TAG,"onTimeOut()");
       // Toast.makeText(LoginActivity.this,"Error en la conexion",Toast.LENGTH_LONG).show(); //_deb
        //btnButtonEntry.setText("probando interface"); //_deb
        mShowDialog.showDialog();
    }

    @Override
    public void onSetPositiveButton() {

        Log.d(TAG,"ShowDialog | onSetPositiveButton()");
        mShowDialog.dimissDialog();

    }


    /////////////funciones de test ///////////////////////////////////////

   /* public void testSQLite(){
      Localizacion localizacion;

        db = new AdapterDB(getApplicationContext());
        initDB();

        localizacion=new Localizacion("2017-12-11 13:34:42","62","23423","30485", "kra9");
        localizacion.createDBReporteLocalizacion();
        Cursor cursor = localizacion.fetchAllReporteLocalizacion("1");

        String latitud= cursor.getString(cursor.getColumnIndex(Localizacion.KEY_LATITUD));
        String longitud= cursor.getString(cursor.getColumnIndex(Localizacion.KEY_LONGITUD));
        String direccion= cursor.getString(cursor.getColumnIndex(Localizacion.KEY_DIRECCION));
        String fechaUltimoReporte= cursor.getString(cursor.getColumnIndex(Localizacion.KEY_FECHA_ULTIMO_REPORTE));
        String idUsuario= cursor.getString(cursor.getColumnIndex(Localizacion.KEY_ID_USUARIO));

        Toast.makeText(getApplicationContext(),latitud +" "+longitud+" "+direccion+" "+fechaUltimoReporte+" "+idUsuario,Toast.LENGTH_LONG).show();
    }

    public void testSQLite2(){

        db = new AdapterDB(getApplicationContext());
        initDB();

        Usuario user = new Usuario("David", "Rodriguez","1986-05-28", "31", "M", "Ipiales", "Anderdavid86@hotmail.com", "adrn","1234");

        Cursor mCursor=user.fetchAllUsuarios("1");

        if(mCursor.getCount()==1){
            Log.d(TAG,"ya existe un usuario logeado "+mCursor.getCount());
            Toast.makeText(getApplicationContext(),"ya existen usuarios logeados "+mCursor.getCount(),Toast.LENGTH_LONG).show(); //_deb

            user.updateDBUsuario(1);

        }else{
            Toast.makeText(getApplicationContext(),"no se ha registrado ningun usuario "+mCursor.getCount(),Toast.LENGTH_LONG).show(); //_deb
            Log.d(TAG,"primer usuario registrado "+mCursor.getCount());
            user.createDBUsuario();
        }


        Cursor cursor = user.fetchAllUsuarios("1");



        String nombre=cursor.getString(cursor.getColumnIndex(Usuario.KEY_NOMBRE));
        String apellido=cursor.getString(cursor.getColumnIndex(Usuario.KEY_APELLIDO));
        String fechaNacimiento=cursor.getString(cursor.getColumnIndex(Usuario.KEY_FECHA_NACIMIENTO));
        String edad=cursor.getString(cursor.getColumnIndex(Usuario.KEY_EDAD));
        String genero=cursor.getString(cursor.getColumnIndex(Usuario.KEY_GENERO));
        String ciudad=cursor.getString(cursor.getColumnIndex(Usuario.KEY_CIUDAD));
        String email=cursor.getString(cursor.getColumnIndex(Usuario.KEY_EMAIL));
        String login=cursor.getString(cursor.getColumnIndex(Usuario.KEY_LOGIN));
        String password=cursor.getString(cursor.getColumnIndex(Usuario.KEY_PASSWORD));

        Toast.makeText(getApplicationContext(),
                "Usuario{" +
                        "nombre='" + nombre + '\'' +
                        ", apellido='" + apellido + '\'' +
                        ", fechaNacimiento='" + fechaNacimiento + '\'' +
                        ", edad='" + edad + '\'' +
                        ", genero='" + genero + '\'' +
                        ", ciudad='" + ciudad + '\'' +
                        ", email='" + email + '\'' +
                        ", login='" + login + '\'' +
                        ", password='" + password + '\'' +
                        '}'
                ,Toast.LENGTH_LONG).show();
    }




    */// no delete

    public void testSQLite3() {
        Log.d(TAG, "testSQLite3()");
        Usuario user = new Usuario();
        Cursor cursor = user.fetchAllUsuarios("1");

        String mIdUsuario = cursor.getString(cursor.getColumnIndex(Usuario.KEY_ID_USUARIO));
        String mNombre = cursor.getString(cursor.getColumnIndex(Usuario.KEY_NOMBRE));
        String mApellido = cursor.getString(cursor.getColumnIndex(Usuario.KEY_APELLIDO));
        String mFechaNacimiento = cursor.getString(cursor.getColumnIndex(Usuario.KEY_FECHA_NACIMIENTO));
        String mEdad = cursor.getString(cursor.getColumnIndex(Usuario.KEY_EDAD));
        String mGenero = cursor.getString(cursor.getColumnIndex(Usuario.KEY_GENERO));
        String mCiudad = cursor.getString(cursor.getColumnIndex(Usuario.KEY_CIUDAD));
        String mEmail = cursor.getString(cursor.getColumnIndex(Usuario.KEY_EMAIL));
        String mLogin = cursor.getString(cursor.getColumnIndex(Usuario.KEY_LOGIN));
        String mPassword = cursor.getString(cursor.getColumnIndex(Usuario.KEY_PASSWORD));

        Log.d(TAG, "se obtinene usuario desde bd" +
                "Usuario{" +
                "nombre='" + mNombre + '\'' +
                ", idUsuario='" + mIdUsuario + '\'' +
                ", apellido='" + mApellido + '\'' +
                ", fechaNacimiento='" + mFechaNacimiento + '\'' +
                ", edad='" + mEdad + '\'' +
                ", genero='" + mGenero + '\'' +
                ", ciudad='" + mCiudad + '\'' +
                ", email='" + mEmail + '\'' +
                ", login='" + mLogin + '\'' +
                ", password='" + mPassword + '\'' +
                '}'
        );
    }

}
