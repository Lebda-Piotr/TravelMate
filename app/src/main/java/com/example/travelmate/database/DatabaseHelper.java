package com.example.travelmate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nazwa bazy danych i wersja
    private static final String DATABASE_NAME = "TravelMateDatabase";
    private static final int DATABASE_VERSION = 2;  // Zwiększ wersję, aby wywołać onUpgrade

    // Nazwa tabeli
    public static final String TABLE_LOCATIONS = "locations";

    // Kolumny tabeli
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LOCATION_NAME = "location_name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_IS_MANUAL = "is_manual";
    public static final String COLUMN_ADDRESS = "address";  // Nowa kolumna dla adresu

    // Zapytanie do stworzenia tabeli
    private static final String CREATE_LOCATIONS_TABLE =
            "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LOCATION_NAME + " TEXT NOT NULL, " +
                    COLUMN_LATITUDE + " REAL NOT NULL, " +
                    COLUMN_LONGITUDE + " REAL NOT NULL, " +
                    COLUMN_IS_MANUAL + " INTEGER NOT NULL, " +
                    COLUMN_ADDRESS + " TEXT " +  // Nowa kolumna dla adresu
                    ");";

    // Zapytanie do usunięcia tabeli
    private static final String DROP_LOCATIONS_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_LOCATIONS;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tworzenie tabeli podczas pierwszego uruchomienia aplikacji
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aktualizacja tabeli, jeśli zmieni się wersja bazy danych
        db.execSQL(DROP_LOCATIONS_TABLE);
        onCreate(db);
    }
}


