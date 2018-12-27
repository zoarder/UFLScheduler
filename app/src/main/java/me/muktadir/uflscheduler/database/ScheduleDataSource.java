package me.muktadir.uflscheduler.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import me.muktadir.uflscheduler.R;
import me.muktadir.uflscheduler.models.ScheduleModelItem;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class ScheduleDataSource {

    protected Context mContext;
    protected SQLiteDatabase mSQLiteDatabase;
    protected DataBaseOpenHelper mDataBaseOpenHelper;
    private String[] mAllColumns = {
            DataBaseOpenHelper.SCHEDULE_ID_COLUMN,
            DataBaseOpenHelper.SCHEDULE_CLIENT_NAME_COLUMN,
            DataBaseOpenHelper.SCHEDULE_START_DATE_TIME_MILLIS_COLUMN,
            DataBaseOpenHelper.SCHEDULE_END_DATE_TIME_MILLIS_COLUMN,
            DataBaseOpenHelper.SCHEDULE_LATITUDE_COLUMN,
            DataBaseOpenHelper.SCHEDULE_LONGITUDE_COLUMN};

    public ScheduleDataSource(Context context) {
        this.mContext = context;
        mDataBaseOpenHelper = new DataBaseOpenHelper(this.mContext);
    }

    public void insertScheduleModelItem(String mClientName, long mStartDateTimeMillis, long mEndDateTimeMillis, Double mLatitude, Double mLongitude)
            throws DataSourceException {
        ContentValues values = new ContentValues();
        values.put(DataBaseOpenHelper.SCHEDULE_CLIENT_NAME_COLUMN, mClientName);
        values.put(DataBaseOpenHelper.SCHEDULE_START_DATE_TIME_MILLIS_COLUMN, mStartDateTimeMillis);
        values.put(DataBaseOpenHelper.SCHEDULE_END_DATE_TIME_MILLIS_COLUMN, mEndDateTimeMillis);
        values.put(DataBaseOpenHelper.SCHEDULE_LATITUDE_COLUMN, mLatitude);
        values.put(DataBaseOpenHelper.SCHEDULE_LONGITUDE_COLUMN, mLongitude);
        mSQLiteDatabase.insert(DataBaseOpenHelper.SCHEDULE_TABLE, null, values);

    }

    public boolean isNotConflictWithPreviousSchedule(ScheduleModelItem scheduleModelItem)
            throws DataSourceException {
        return isNotConflictWithPreviousSchedule(scheduleModelItem.getScheduleID(), scheduleModelItem.getStartDateTimeMillis(), scheduleModelItem.getEndDateTimeMillis());
    }

    public boolean isNotConflictWithPreviousSchedule(int mScheduleID, long mStartDateTimeMillis, long mEndDateTimeMillis)
            throws DataSourceException {
        String queryString = String.format(mContext.getResources().getString(R.string.db_select_conflict_with_previous_schedule_query_template), DataBaseOpenHelper.SCHEDULE_TABLE, DataBaseOpenHelper.SCHEDULE_START_DATE_TIME_MILLIS_COLUMN, mStartDateTimeMillis, mEndDateTimeMillis);
//        Toast.makeText(mContext, queryString, Toast.LENGTH_LONG).show();
        Cursor cursor = mSQLiteDatabase.rawQuery(queryString, null);
        if (cursor.getCount() > 0) {
            if (mScheduleID < 0) {
                return false;
            } else if (cursor.getCount() > 1) {
                return false;
            } else {
                cursor.moveToFirst();
                if (cursor.getInt(cursor
                        .getColumnIndex(DataBaseOpenHelper.SCHEDULE_ID_COLUMN)) != mScheduleID) {
                    return false;
                }
            }
        } else {
            queryString = String.format(mContext.getResources().getString(R.string.db_select_conflict_with_previous_schedule_query_template), DataBaseOpenHelper.SCHEDULE_TABLE, DataBaseOpenHelper.SCHEDULE_END_DATE_TIME_MILLIS_COLUMN, mStartDateTimeMillis, mEndDateTimeMillis);
//            Toast.makeText(mContext, queryString, Toast.LENGTH_LONG).show();
            cursor = mSQLiteDatabase.rawQuery(queryString, null);
            if (cursor.getCount() > 0) {
                if (mScheduleID < 0) {
                    return false;
                } else if (cursor.getCount() > 1) {
                    return false;
                } else {
                    cursor.moveToFirst();
                    if (cursor.getInt(cursor
                            .getColumnIndex(DataBaseOpenHelper.SCHEDULE_ID_COLUMN)) != mScheduleID) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void insertScheduleModelItem(ScheduleModelItem scheduleModelItem)
            throws DataSourceException {
        insertScheduleModelItem(scheduleModelItem.getClientName(), scheduleModelItem.getStartDateTimeMillis(), scheduleModelItem.getEndDateTimeMillis(), scheduleModelItem.getLatitude(), scheduleModelItem.getLongitude());
    }

    public ScheduleModelItem getScheduleModelItem(int scheduleID) {
        Cursor cursor = mSQLiteDatabase.query(DataBaseOpenHelper.SCHEDULE_TABLE,
                mAllColumns, DataBaseOpenHelper.SCHEDULE_ID_COLUMN + " LIKE "
                        + "'" + scheduleID + "'", null, null, null, null);
        cursor.moveToFirst();
        ScheduleModelItem scheduleModelItem = cursorToScheduleModelItem(cursor);
        cursor.close();
        return scheduleModelItem;
    }

    public void updateScheduleModelItem(int mScheduleID, String mClientName, long mStartDateTimeMillis, long mEndDateTimeMillis)
            throws DataSourceException {
        ContentValues values = new ContentValues();
        values.put(DataBaseOpenHelper.SCHEDULE_CLIENT_NAME_COLUMN, mClientName);
        values.put(DataBaseOpenHelper.SCHEDULE_START_DATE_TIME_MILLIS_COLUMN, mStartDateTimeMillis);
        values.put(DataBaseOpenHelper.SCHEDULE_END_DATE_TIME_MILLIS_COLUMN, mEndDateTimeMillis);
        mSQLiteDatabase.update(DataBaseOpenHelper.SCHEDULE_TABLE, values, DataBaseOpenHelper.SCHEDULE_ID_COLUMN + " = " + mScheduleID, null);
    }

    public void updateScheduleModelItem(ScheduleModelItem scheduleModelItem)
            throws DataSourceException {
        updateScheduleModelItem(scheduleModelItem.getScheduleID(), scheduleModelItem.getClientName(), scheduleModelItem.getStartDateTimeMillis(), scheduleModelItem.getEndDateTimeMillis());
    }

    public void open() throws SQLException {
        mSQLiteDatabase = mDataBaseOpenHelper.getWritableDatabase();
    }

    public void close() {
        mDataBaseOpenHelper.close();
    }

    public void deleteScheduleItem(ScheduleModelItem scheduleModelItem) {
        mSQLiteDatabase.delete(DataBaseOpenHelper.SCHEDULE_TABLE,
                DataBaseOpenHelper.SCHEDULE_ID_COLUMN + " LIKE "
                        + "'" + scheduleModelItem.getScheduleID() + "'", null);
    }

    public ArrayList<ScheduleModelItem> getAllScheduleItem() {
        ArrayList<ScheduleModelItem> items = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query(DataBaseOpenHelper.SCHEDULE_TABLE,
                mAllColumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ScheduleModelItem scheduleModelItem = cursorToScheduleModelItem(cursor);
                items.add(scheduleModelItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }

    public int getSize() {
        Cursor cursor = mSQLiteDatabase.query(DataBaseOpenHelper.SCHEDULE_TABLE,
                mAllColumns, null, null, null, null, null);
        cursor.moveToFirst();
        cursor.close();
        return cursor.getCount();
    }

    private ScheduleModelItem cursorToScheduleModelItem(Cursor cursor) {
        ScheduleModelItem scheduleModelItem = new ScheduleModelItem();
        scheduleModelItem.setScheduleID(cursor.getInt(cursor
                .getColumnIndex(DataBaseOpenHelper.SCHEDULE_ID_COLUMN)));
        scheduleModelItem.setClientName(cursor.getString(cursor
                .getColumnIndex(DataBaseOpenHelper.SCHEDULE_CLIENT_NAME_COLUMN)));
        scheduleModelItem.setStartDateTimeMillis(cursor.getLong(cursor
                .getColumnIndex(DataBaseOpenHelper.SCHEDULE_START_DATE_TIME_MILLIS_COLUMN)));
        scheduleModelItem.setEndDateTimeMillis(cursor.getLong(cursor
                .getColumnIndex(DataBaseOpenHelper.SCHEDULE_END_DATE_TIME_MILLIS_COLUMN)));
        scheduleModelItem.setLatitude(cursor.getDouble(cursor
                .getColumnIndex(DataBaseOpenHelper.SCHEDULE_LATITUDE_COLUMN)));
        scheduleModelItem.setLongitude(cursor.getDouble(cursor
                .getColumnIndex(DataBaseOpenHelper.SCHEDULE_LONGITUDE_COLUMN)));
        return scheduleModelItem;
    }
}
