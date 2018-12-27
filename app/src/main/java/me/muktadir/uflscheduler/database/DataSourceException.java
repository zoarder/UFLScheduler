package me.muktadir.uflscheduler.database;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class DataSourceException extends Exception {
    public static final String DATA_SOURCE_EXCEPTION_TAG = "DataSourceException";
    public String message = "Invalid TAG";

    @Override
    public String getMessage() {
        return DATA_SOURCE_EXCEPTION_TAG + " " + message;
    }
}
