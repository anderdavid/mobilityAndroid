package mobilityv1.smartappsolutions.com.mobilityv1.services;

import android.app.Service;

/**
 * Created by user on 12/12/2017.
 */
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import mobilityv1.smartappsolutions.com.mobilityv1.ConectionManager.ConstanstConnection;
import mobilityv1.smartappsolutions.com.mobilityv1.MainActivity;
import mobilityv1.smartappsolutions.com.mobilityv1.modelo.Localizacion;
import mobilityv1.smartappsolutions.com.mobilityv1.modelo.User;


public class ServiceReporteGps extends Service implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private final static String TAG ="ServiceReporteGps";

    private  final IBinder mBinder = new ServiceReporteGpsBinder();
    private IServiceListener mIServiceListener = null;

    private GoogleApiClient mGoogleApiClient = null;
    private LocationManager mLocationManager = null;
    private LocationRequest mLocationRequest = null;
    //private Location mLocation = null;

    User user;



    public void testBinder2(){
        Log.d(TAG,"testBinder2()");
    }

    public void initialize(IServiceListener mIServiceListener) throws Exception {
        Log.d(TAG,"initialize()");

        this.mIServiceListener =mIServiceListener;
              mIServiceListener.onReceiveMessage("El servicio se ha inicializado");

        connectGoogleApiClient();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate()");

        user = new User();

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(7000);
        mLocationRequest.setSmallestDisplacement(1);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, TAG + "onDestroy()");
        disconectGoogleApiClient();
        super.onDestroy();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,  "onBind()");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG,"onRebind()");
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");
        // return super.onStartCommand(intent, Service.START_NOT_STICKY, startId);
        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind()");
        return super.onUnbind(intent);
    }

    public class ServiceReporteGpsBinder extends Binder {

        public ServiceReporteGps getService() {
            Log.d(TAG,"getService()");
            return ServiceReporteGps.this;
        }
        public void testBinder(){
            Log.d(TAG,"testBinder()");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(TAG, "onConnected");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        Log.d("connected", String.valueOf(mGoogleApiClient.isConnected()));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Log.d(TAG, String.valueOf(mGoogleApiClient.isConnected()));

        if (location != null) {

            Log.d(TAG, "latitud: "+location.getLatitude() + " longitud " + location.getLongitude()+" time:"+location.getTime());


            final Localizacion localizacion = new Localizacion(getFormat24Fecha(),String.valueOf(location.getTime()),user.getIdDBUsuario(),String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()),"kra2");
            final RequestQueue requestQueueRegisterGps = Volley.newRequestQueue(ServiceReporteGps.this);
            StringRequest postRequest = new StringRequest(Request.Method.POST, ConstanstConnection.BASE_URL_REGISTER_GPS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Log.d(TAG, "respuesta " + response.toString());
                            //Toast.makeText(getApplicationContext(),"respuesta: "+response.toString(),Toast.LENGTH_LONG).show();
                            requestQueueRegisterGps.stop();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = localizacion.getPostParams();
                    return params;
                }
            };
            requestQueueRegisterGps.add(postRequest);

        } else {
            //Toast.makeText(this, "location is null", Toast.LENGTH_SHORT).show();

        }
    }


    protected void connectGoogleApiClient(){
        mGoogleApiClient.connect();

    }


    public String getFormat24Fecha(){

        Date mDate =null;
        String formattedDate="";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss aa");
        DateFormat writeFormat24 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        DateFormat readFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss aa");
        String date = simpleDateFormat.format(new Date());

        try {
            mDate =readFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (mDate != null) {
            formattedDate= writeFormat24.format(mDate);
        }
        return formattedDate;
    }


    protected void disconectGoogleApiClient() {

        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }


    /**
     * interface para comunicar servicio con Activity
     * */

    public interface IServiceListener {
        void onReceiveMessage(String message);
    }
}