package mobilityv1.smartappsolutions.com.mobilityv1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import mobilityv1.smartappsolutions.com.mobilityv1.modelo.Localizacion;
import mobilityv1.smartappsolutions.com.mobilityv1.modelo.User;

/**
 * Created by user on 12/12/2017.
 */

public class AdapterDB {

    private static final String TAG = "AdapterDB";

    private static final String DATABASE_NAME ="mobilityApp_testDB";
    private static final int VERSION =2;

    public static  DatabaseHelper mDatabaseHelper;
    public static SQLiteDatabase mSqLiteDatabase;
    private Context context;

    public AdapterDB(Context context) {
        this.context = context;
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
            Log.d(TAG, DATABASE_NAME + " onCreate()");

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(User.DATABASE_CREATE_TABLE_USUARIO);
            db.execSQL(Localizacion.DATABASE_CREATE_TABLE_REPORTE_LOCALIZACION);
            Log.d(TAG, "onCreate table products " + Localizacion.DATABASE_TABLE_REPORTE_LOCALIZACION);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.d(TAG,"onUpgrade");
            db.execSQL("DROP TABLE IF EXISTS " + Localizacion.DATABASE_TABLE_REPORTE_LOCALIZACION);
            db.execSQL("DROP TABLE IF EXISTS " + User.DATABASE_TABLE_USUARIO);
            onCreate(db);
        }
    }

    public AdapterDB open() throws SQLException {
        mSqLiteDatabase =mDatabaseHelper.getWritableDatabase();

        return  this;
    }

    public void close() throws SQLException {

        mDatabaseHelper.close();
    }

    public void dropDatabase(){
        mSqLiteDatabase.execSQL("DROP DATABASE " + DATABASE_NAME);
    }

    public void dropTable(String table){
        mSqLiteDatabase.execSQL("DROP TABLE " + table);
    }

    public void truncateTable(String table){
        mSqLiteDatabase.execSQL("DELETE FROM "+ table);
    }

}
