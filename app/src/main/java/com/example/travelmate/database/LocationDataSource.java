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
    public long addLocation(String locationName, double latitude, double longitude, String address, boolean isManual) {
        // Sprawdź ilość zapisanych lokalizacji
        if (getAllLocations().size() >= 5) {
            // Pobierz id najstarszej lokalizacji
            long oldestLocationId = getOldestLocationId();
            // Nadpisz najstarszą lokalizację
            updateLocation(oldestLocationId, locationName, latitude, longitude, address, isManual);
            return oldestLocationId;
        } else {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_LOCATION_NAME, locationName);
            values.put(DatabaseHelper.COLUMN_LATITUDE, latitude);
            values.put(DatabaseHelper.COLUMN_LONGITUDE, longitude);
            values.put(DatabaseHelper.COLUMN_IS_MANUAL, isManual ? 1 : 0);
            values.put(DatabaseHelper.COLUMN_ADDRESS, address); // Dodaj adres do bazy danych

            return database.insert(DatabaseHelper.TABLE_LOCATIONS, null, values);
        }
    }

    // Pobierz ID najstarszej lokalizacji
    private long getOldestLocationId() {
        String orderBy = DatabaseHelper.COLUMN_ID + " ASC";
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_LOCATIONS,
                new String[]{DatabaseHelper.COLUMN_ID},
                null,
                null,
                null,
                null,
                orderBy,
                "1"
        );

        long oldestId = -1;
        if (cursor.moveToFirst()) {
            oldestId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
        }

        cursor.close();
        return oldestId;
    }


    // Aktualizuj lokalizację
    private void updateLocation(long locationId, String locationName, double latitude, double longitude, String address, boolean isManual) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LOCATION_NAME, locationName);
        values.put(DatabaseHelper.COLUMN_LATITUDE, latitude);
        values.put(DatabaseHelper.COLUMN_LONGITUDE, longitude);
        values.put(DatabaseHelper.COLUMN_ADDRESS, address);  // Dodaj adres do kolumny
        values.put(DatabaseHelper.COLUMN_IS_MANUAL, isManual ? 1 : 0); // 1 for true, 0 for false

        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(locationId)};

        database.update(DatabaseHelper.TABLE_LOCATIONS, values, selection, selectionArgs);
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

    // Pobierz tylko lokalizacje zapisane przez użytkownika
    public List<LocationModel> getUserLocations() {
        List<LocationModel> userLocations = new ArrayList<>();

        String selection = DatabaseHelper.COLUMN_IS_MANUAL + " = ?";
        String[] selectionArgs = {"1"}; // 1 for true

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_LOCATIONS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocationModel location = cursorToLocation(cursor);
            userLocations.add(location);
            cursor.moveToNext();
        }

        cursor.close();
        return userLocations;
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
        location.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS))); // Dodaj pobieranie adresu
        return location;
    }
    // Aktualizuj nazwę lokalizacji
    public void updateLocationName(long locationId, String newLocationName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LOCATION_NAME, newLocationName);

        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(locationId)};

        database.update(DatabaseHelper.TABLE_LOCATIONS, values, selection, selectionArgs);
    }
}
