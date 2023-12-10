package com.example.travelmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;



    public LocationDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Otwórz bazę danych do zapisu/odczytu
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Zamknij bazę danych
    public void close() {
        dbHelper.close();
    }

    // Dodaj nową lokalizację do bazy danych
    public long addLocation(String locationName, double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LOCATION_NAME, locationName);
        values.put(DatabaseHelper.COLUMN_LATITUDE, latitude);
        values.put(DatabaseHelper.COLUMN_LONGITUDE, longitude);

        return database.insert(DatabaseHelper.TABLE_LOCATIONS, null, values);
    }

    // Pobierz wszystkie zapisane lokalizacje
    public List<LocationModel> getAllLocations() {
        List<LocationModel> locations = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_LOCATIONS,
                null,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocationModel location = cursorToLocation(cursor);
            locations.add(location);
            cursor.moveToNext();
        }

        cursor.close();
        return locations;
    }

    // Usuń lokalizację z bazy danych
    public void deleteLocation(long locationId) {
        database.delete(
                DatabaseHelper.TABLE_LOCATIONS,
                DatabaseHelper.COLUMN_ID + " = " + locationId,
                null
        );
    }

    // Konwertuj dane z kursora do obiektu LocationModel

    private LocationModel cursorToLocation(Cursor cursor) {
        String[] columnNames = cursor.getColumnNames();
        Log.d("ColumnNames", Arrays.toString(columnNames));

        LocationModel location = new LocationModel();
        location.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
        location.setLocationName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOCATION_NAME)));
        location.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LATITUDE)));
        location.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LONGITUDE)));
        return location;
    }
}
