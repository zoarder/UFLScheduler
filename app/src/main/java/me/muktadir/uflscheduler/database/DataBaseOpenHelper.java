package me.muktadir.uflscheduler.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class DataBaseOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "db_ufl_scheduler.db";
    public static final int DB_VERSION = 1;
    public static final String SCHEDULE_TABLE = "tb_schedule";
    public static final String SCHEDULE_ID_COLUMN = "fld_schedule_id";
    public static final String SCHEDULE_CLIENT_NAME_COLUMN = "fld_schedule_client_name";
    public static final String SCHEDULE_START_DATE_TIME_MILLIS_COLUMN = "fld_schedule_start_date_time_millis";
    public static final String SCHEDULE_END_DATE_TIME_MILLIS_COLUMN = "fld_schedule_end_date_time_millis";
    public static final String SCHEDULE_LATITUDE_COLUMN = "fld_schedule_latitude";
    public static final String SCHEDULE_LONGITUDE_COLUMN = "fld_schedule_longitude";
    public static final String SCHEDULE_ADDRESS_COLUMN = "fld_schedule_address";

    private static final String CREATE = "CREATE";
    private static final String COLUMN = "COLUMN";
    private static final String TABLE = "TABLE";
    private static final String PRIMARY_KEY = "PRIMARY KEY";
    private static final String FOREIGN_KEY = "FOREIGN KEY";
    private static final String REFERENCES = "REFERENCES";
    private static final String DROP = "DROP";
    private static final String IF_NOT_EXISTS = "IF NOT EXISTS";
    private static final String BOOLEAN = "BOOLEAN";
    private static final String INTEGER = "INTEGER";
    private static final String VARCHAR = "VARCHAR";
    private static final String FLOAT = "FLOAT";
    private static final String UNSIGNED_BIG_INT = "UNSIGNED BIG INT";
    private static final String DOUBLE = "DOUBLE";
    private static final String REAL = "REAL";

    private static final String SCHEDULE_TABLE_CREATE = CREATE + " " + TABLE + " " + IF_NOT_EXISTS + " " + SCHEDULE_TABLE + "("
            + SCHEDULE_ID_COLUMN + " " + INTEGER + "," + " "
            + SCHEDULE_CLIENT_NAME_COLUMN + " " + VARCHAR + "," + " "
            + SCHEDULE_START_DATE_TIME_MILLIS_COLUMN + " " + UNSIGNED_BIG_INT + "," + " "
            + SCHEDULE_END_DATE_TIME_MILLIS_COLUMN + " " + UNSIGNED_BIG_INT + "," + " "
            + SCHEDULE_LATITUDE_COLUMN + " " + DOUBLE + "," + " "
            + SCHEDULE_LONGITUDE_COLUMN + " " + DOUBLE + "," + " "
            + SCHEDULE_ADDRESS_COLUMN + " " + VARCHAR + "," + " "
            + PRIMARY_KEY + "(" + SCHEDULE_ID_COLUMN + ")" + ");";
    private static final String SCHEDULE_TABLE_DROP = DROP + " " + TABLE + " " + SCHEDULE_TABLE;

    private Context mContext;

    public DataBaseOpenHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    public DataBaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    public DataBaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SCHEDULE_TABLE_CREATE);
            Log.i("Database Create", "Created city table");
        } catch (Exception e) {
            Log.e("Database Create", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(SCHEDULE_TABLE_DROP);
            Log.i("Database Create", "Dropped city table");
        } catch (Exception e) {
            Log.e("Database Drop", e.getMessage());
        }
        onCreate(db);
    }

    public void dropDatabase(SQLiteDatabase db) {
        try {
            db.execSQL(SCHEDULE_TABLE_DROP);
            Log.i("Database Create", "Dropped city table");
        } catch (Exception e) {
            Log.e("Database Drop", e.getMessage());
        }
    }

    public void createDatabase(SQLiteDatabase db) {
        try {
            db.execSQL(SCHEDULE_TABLE_CREATE);
            Log.i("Database Create", "Created city table");
        } catch (Exception e) {
            Log.e("Database Create", e.getMessage());
        }
    }
}
