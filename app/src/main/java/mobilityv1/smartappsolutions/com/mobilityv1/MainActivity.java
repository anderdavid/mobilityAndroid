package mobilityv1.smartappsolutions.com.mobilityv1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


import mobilityv1.smartappsolutions.com.mobilityv1.services.ServiceReporteGps;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ServiceReporteGps.IServiceListener{



    Toolbar toolbar; private final static  String TAG="MainActivity";
    TabLayout tabs;
    ViewPager viewPager;
    Adapter adapter;

    AddPhotoFragment mAddPhotoFragment;
    DashBoardFragment mDashBoardFragment;
    MyImagesFragment mMyImagesFragment;

    public static List<Drawable> mListDrawable;



    private ServiceReporteGps mServiceReporteGps =null;
    public ServiceReporteGps.ServiceReporteGpsBinder binder =null;

    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"hello world "+TAG);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabs = (TabLayout) findViewById(R.id.tabs);

        mListDrawable = new ArrayList<>();

        mAddPhotoFragment =new AddPhotoFragment();
        mMyImagesFragment = new MyImagesFragment();
        mDashBoardFragment = new DashBoardFragment();


        setSupportActionBar(toolbar);

        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setIcon(mListDrawable.get(i));
        }

        intent = new Intent(MainActivity.this,ServiceReporteGps.class);
        // startServiceReporteGps();
        bindServiceReporteGps();
        //stopServiceReporteGps();

    }
    private void setupViewPager(ViewPager viewPager) {

        adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(mAddPhotoFragment,"add",getDrawable(R.drawable.ic_camera_24dp));
        adapter.addFragment(mMyImagesFragment,"images",getDrawable(R.drawable.ic_images_24dp));
        adapter.addFragment(mDashBoardFragment,"dashboard",getDrawable(R.drawable.ic_users_24dp));

        viewPager.setAdapter(adapter);
    }
    static class Adapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title,Drawable mDrawable) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mListDrawable.add(mDrawable);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private void startServiceReporteGps() {
        Log.d(TAG,"start service");
        startService(intent);
    }

    private void stopServiceReporteGps(){
        Log.d(TAG,"stop service");
        stopService(intent);
    }

    private void bindServiceReporteGps(){

        Log.d(TAG,"bindServiceReporteGps()");
        bindService(intent, mConnection, getApplicationContext().BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected");

            binder = (ServiceReporteGps.ServiceReporteGpsBinder) service;
            mServiceReporteGps = binder.getService();
            try {
                binder.testBinder(); //_deb
                mServiceReporteGps.initialize(MainActivity.this);
                //mServiceReporteGps.testBinder2(); //_deb
            }catch (Exception e){
                Log.e(TAG,e.toString());
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconected");

            mServiceReporteGps =null;
        }
    };


    /**
     *  comunicacion del servicio con la actividad
     * */

    @Override
    public void onReceiveMessage(String message) {
        Toast.makeText(getApplicationContext(),message, Toast.LENGTH_LONG).show();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(),"confifurar servidor",Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}