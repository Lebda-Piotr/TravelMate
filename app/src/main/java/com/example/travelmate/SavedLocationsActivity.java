package com.example.travelmate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travelmate.database.LocationDataSource;
import com.example.travelmate.database.LocationModel;

import java.util.List;

public class SavedLocationsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private List<LocationModel> locations;
    private ArrayAdapter<LocationModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savedlocationsactivity);

        // Inicjalizacja widoku dla zapisanych lokalizacji
        ListView locationsListView = findViewById(R.id.locationsListView);
        locationsListView.setOnItemClickListener(this);
        locationsListView.setOnItemLongClickListener(this);

        // Pobranie danych z bazy danych
        loadLocations();

        // Utworzenie adaptera i przypisanie go do listy
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locations);
        locationsListView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LocationModel location = locations.get(position);
        // Tutaj obsłuż kliknięcie rekordu w historii, np. ustaw jako cel na mapie
        // Możesz wykorzystać kod z MainActivity z punktu 5
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        LocationModel location = locations.get(position);
        showOptionsPopup(location);
        return true;
    }

    private void showOptionsPopup(final LocationModel location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opcje");

        // Przytrzymaj około sekundy
        final String[] options = {"Ustaw jako aktualną lokalizację", "Ustaw jako miejsce docelowe", "Usuń", "Anuluj"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        setAsCurrentLocation(location);
                        break;
                    case 1:
                        setAsDestination(location);
                        break;
                    case 2:
                        deleteLocation(location);
                        break;
                    case 3:
                        dialog.dismiss();
                        break;
                }
            }
        });

        builder.show();
    }

    private void setAsCurrentLocation(LocationModel location) {
        // Tutaj ustaw aktualną lokalizację na mapie
    }

    private void setAsDestination(LocationModel location) {
        // Tutaj ustaw lokalizację jako cel docelowy na mapie
    }

    private void deleteLocation(final LocationModel location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Potwierdź usunięcie");
        builder.setMessage("Czy na pewno chcesz usunąć tę lokalizację?");

        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LocationDataSource dataSource = new LocationDataSource(SavedLocationsActivity.this);
                dataSource.open();
                dataSource.deleteLocation(location.getId());
                dataSource.close();

                refreshLocationList();
            }
        });

        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void loadLocations() {
        // Pobranie danych z bazy danych
        LocationDataSource dataSource = new LocationDataSource(this);
        dataSource.open();
        locations = dataSource.getAllLocations();
        dataSource.close();
    }

    private void refreshLocationList() {
        // Odświeżenie listy po usunięciu lokalizacji
        loadLocations();
        adapter.clear();
        adapter.addAll(locations);
        adapter.notifyDataSetChanged();
    }
}