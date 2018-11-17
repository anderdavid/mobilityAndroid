package mobilityv1.smartappsolutions.com.mobilityv1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import mobilityv1.smartappsolutions.com.mobilityv1.ConectionManager.ConstanstConnection;
import mobilityv1.smartappsolutions.com.mobilityv1.clases.ShowDialog;
import mobilityv1.smartappsolutions.com.mobilityv1.clases.ShowDialogProgressItem;
import mobilityv1.smartappsolutions.com.mobilityv1.modelo.Usuario;

/**
 * Created by user on 01/12/2017.
 */
public class RegisterActivity extends AppCompatActivity implements ShowDialogProgressItem.ShowDialogProgressItemListener,ShowDialog.ShowDialogListener{

    private final static String TAG="RegisterActivity";

    EditText nombre;
    EditText apellido;
    EditText fechaNacimiento;
    EditText email;
    EditText login;
    EditText password;
    EditText passwordConfirmacion;
    Spinner spinnerCiudad;
    RadioGroup rdgGenero;
    Button register;

    ShowDialogProgressItem mShowDialogProgressItem;
    ShowDialog mShowDialog;

    private int mYear, mMonth, mDay;

    String uNombre;
    String uApellido;
    String uFechaNacimiento;
    String uEdad="0"; //falta calcularla
    String uEmail;
    String uLogin;
    String uPassword;
    String uPasswordConf;
    String uCiudad="";
    String uGenero="";

    Usuario mUsuario;

    String urlRegisterPost= ConstanstConnection.BASE_URL_API_REST+ConstanstConnection.URL_USUARIO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);
        Log.d(TAG,TAG + " onCreate()");

        nombre = (EditText)findViewById(R.id.register_edt_nombre);
        apellido = (EditText)findViewById(R.id.register_edt_apellido);
        fechaNacimiento = (EditText)findViewById(R.id.register_edt_fecha_nacimiento);
        email = (EditText)findViewById(R.id.register_edt_email);
        login = (EditText)findViewById(R.id.register_edt_login);
        password = (EditText)findViewById(R.id.register_edt_password);
        passwordConfirmacion=(EditText)findViewById(R.id.register_edt_password_conf);
        spinnerCiudad =(Spinner)findViewById(R.id.spinner_ciudad);
        rdgGenero =(RadioGroup)findViewById(R.id.register_rdg_genero);
        register = (Button)findViewById(R.id.register_btn_register);

        mYear= 1990;
        mMonth =12;
        mDay=1;
        uFechaNacimiento="1990-01-01";
        fechaNacimiento.setText(uFechaNacimiento);


        ArrayAdapter adapter = ArrayAdapter.createFromResource( this, R.array.ciudades , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCiudad.setAdapter(adapter);


        fechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(RegisterActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                mYear=year;
                                mMonth =monthOfYear+1;
                                mDay=dayOfMonth;

                                String montFormat="";
                                String dayFormat="";

                                if(mMonth<10){
                                    montFormat ="0"+mMonth;
                                }else{
                                    montFormat=String.valueOf(mMonth);
                                }
                                if(mDay<10){
                                    dayFormat ="0"+mDay;
                                }else{
                                    dayFormat=String.valueOf(mDay);
                                }

                                fechaNacimiento.setText(mYear+"-"+montFormat+"-"+dayFormat);

                            }
                        },mYear,mMonth,mDay).show();
            }
        });

        mShowDialogProgressItem = new ShowDialogProgressItem("Espere...", RegisterActivity.this, getApplicationContext());
        mShowDialogProgressItem.setmShowDialogProgressItemListener(RegisterActivity.this);

        mShowDialog = new ShowDialog("Error", "El servidor no responde", RegisterActivity.this, getApplicationContext());
        mShowDialog.setmShowDialogListener(RegisterActivity.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"onClick() registrar");

                uNombre =nombre.getText().toString().trim();
                uApellido = apellido.getText().toString();
                uFechaNacimiento =fechaNacimiento.getText().toString().trim();
                uEmail =email.getText().toString();
                uLogin =login.getText().toString();
                uPassword =password.getText().toString();
                uPasswordConf =passwordConfirmacion.getText().toString();

                int selectedRadioButtonID = rdgGenero.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                uGenero = selectedRadioButton.getText().toString();
                Log.d(TAG,  "selected: "+uGenero);

                uFechaNacimiento=fechaNacimiento.getText().toString();
                Log.d(TAG,"fecha de nacimiento: "+uFechaNacimiento);

                uCiudad = spinnerCiudad.getSelectedItem().toString();
                Log.d(TAG,"ciudad: "+uCiudad);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateActual = simpleDateFormat.format(new Date());
                int diference = getDateDiference(dateActual,uFechaNacimiento.toString());
                uEdad = String.valueOf(diference);
                Log.d(TAG,"edad: "+uEdad);


                if (uNombre.equals("")) {
                    Toast.makeText(getApplicationContext(),"El campo nombre esta vacio",Toast.LENGTH_LONG).show();
                } else if (uApellido.equals("")) {
                    Toast.makeText(getApplicationContext(),"El campo apellido esta vacio",Toast.LENGTH_LONG).show();
                }else if(uFechaNacimiento.equals("")){
                    Toast.makeText(getApplicationContext(),"El campo fecha de nacimiento esta vacio",Toast.LENGTH_LONG).show();
                }else if(uEmail.equals("")){
                    Toast.makeText(getApplicationContext(),"El campo email esta vacio",Toast.LENGTH_LONG).show();
                }else if(uLogin.equals("")){
                    Toast.makeText(getApplicationContext(),"El campo Login esta vacio",Toast.LENGTH_LONG).show();
                }else if(uPassword.equals("")){
                    Toast.makeText(getApplicationContext(),"El campo password esta vacio",Toast.LENGTH_LONG).show();
                }else if(uPasswordConf.equals("")){
                    Toast.makeText(getApplicationContext(),"Se debe confirmar el password",Toast.LENGTH_LONG).show();
                }else if(!uPassword.equals(uPasswordConf)){
                    Toast.makeText(getApplicationContext(),"Los passwords no coinciden",Toast.LENGTH_LONG).show();
                }else{

                    mUsuario = new Usuario(uNombre, uApellido, uFechaNacimiento, uEdad, uGenero, uCiudad, uEmail, uLogin, uPassword);
                    Log.d(TAG, mUsuario.toString());

                    final RequestQueue requestQueuePost = Volley.newRequestQueue(RegisterActivity.this);
                    StringRequest postRequest = new StringRequest(Request.Method.POST, urlRegisterPost,

                            new Response.Listener<String>()
                            {
                                @Override
                                public void onResponse(String response) {
                                    // response
                                    Log.d(TAG,response.toString());
                                    requestQueuePost.stop();
                                    mShowDialogProgressItem.dimissDialog();
                                    //Toast.makeText(getApplicationContext(),"respuesta: "+response.toString(),Toast.LENGTH_LONG).show();

                                    try{

                                        JSONObject jsonResponse = new JSONObject(response.toString());

                                        String status = jsonResponse.getString("status");
                                        String mensaje= jsonResponse.getString("msg");

                                        Log.d(TAG, "status: " + status+ " mensaje "+mensaje);

                                        if (status.equals("true")) {
                                            mShowDialog.setTitle("Mensaje");

                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            mShowDialog.setTitle("Error");
                                        }

                                        mShowDialog.setMessage(mensaje);
                                        mShowDialog.showDialog();

                                    }catch (Exception e){
                                        Log.d(TAG,e.toString());
                                    }


                                }
                            },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error

                                    Log.d(TAG, "El servidor no responde "+error.toString());


                                    //Toast.makeText(getApplicationContext(),"error "+error.toString(),Toast.LENGTH_LONG).show();
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams()
                        {
                            Map<String, String>  params = mUsuario.getPostParams();
                            return params;
                        }
                    };
                    requestQueuePost.add(postRequest);
                    mShowDialogProgressItem.showDialog();

                }

            }
        });

    }

    public int getDateDiference(String stDate1,String stDate2){

        final String TAG1="getDateDiference";

        int years =0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try{
            Date date1 = dateFormat.parse(stDate1);
            Date date2 = dateFormat.parse(stDate2);

            int dias = (int)((date1.getTime()-date2.getTime())/86400000);
            Log.d(TAG1,"La diferencia en dias es: "+dias);
            years =(int)(dias/365);
            Log.d(TAG1,"La diferencia en años es: "+years);

        }catch (Exception e){
            Log.d(TAG1,e.toString());
        }


        return  years;
    }

    @Override
    public void onTimeOut() {

        Log.d(TAG,"onTimeOut()");
        mShowDialog.setMessage("El servidor no responde");
        mShowDialog.showDialog();
    }

    @Override
    public void onSetPositiveButton() {
        Log.d(TAG,"ShowDialog | onSetPositiveButton()");
        mShowDialog.dimissDialog();
    }
}
