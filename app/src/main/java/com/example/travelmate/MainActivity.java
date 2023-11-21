package com.example.travelmate;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.ImageButton;
import org.osmdroid.util.GeoPoint;
import android.widget.LinearLayout;
import org.osmdroid.api.IMapController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import org.osmdroid.views.MapView;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import android.content.Context;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private MapView mapView = null;
    private IMapController mapController;
    private Button locateMeButton;
    private ImageButton compassButton;
    private ImageButton menuButton;
    private MyLocationNewOverlay myLocationOverlay;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private LinearLayout compassLayout;
    private ImageView compassImage;
    private TextView compassDirection;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private boolean compassActive = false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);

        // Inicjalizacja mapy OpenStreetMap
        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        mapController = mapView.getController();
        mapController.setZoom(15);

        // Przyciski zoom i zoom palcami
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Inicjalizacja przycisków
        locateMeButton = findViewById(R.id.locateMeButton);
        compassButton = findViewById(R.id.compassButton);
        menuButton = findViewById(R.id.menuButton);

        Button setDestinationButton = findViewById(R.id.setDestinationButton);

        // Dodane elementy związane z kompasem
        compassLayout = findViewById(R.id.compassLayout);
        compassImage = findViewById(R.id.compassImage);
        compassDirection = findViewById(R.id.compassDirection);

        // Inicjalizacja sensorów do obsługi kompasu
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);








        setDestinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Włącz tryb ustawiania miejsca docelowego po kliknięciu przycisku
                //enableSetDestinationMode();
            }
        });

        // Skalowanie ikon do rozmiaru przycisków
        compassButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
        menuButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);

        // Pobranie aktualnej lokalizacji
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
            myLocationOverlay.enableMyLocation();
            mapView.getOverlays().add(myLocationOverlay);
        }

        locateMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myLocationOverlay != null && myLocationOverlay.getMyLocation() != null) {
                    double latitude = myLocationOverlay.getMyLocation().getLatitude();
                    double longitude = myLocationOverlay.getMyLocation().getLongitude();
                    org.osmdroid.util.GeoPoint userLocation = new org.osmdroid.util.GeoPoint(latitude, longitude);
                    mapController.setCenter(userLocation);
                } else {
                    Toast.makeText(MainActivity.this, "Brak dostępu do lokalizacji.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        compassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCompass();
                toggleCompassLayout();
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout;
                NavigationView navigationView;

                drawerLayout = findViewById(R.id.drawer_layout);
                navigationView = findViewById(R.id.nav_view);

                if (drawerLayout != null && navigationView != null) {
                    drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.open, R.string.close);
                    drawerLayout.addDrawerListener(drawerToggle);
                    drawerToggle.syncState();

                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    }

                    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            int itemId = item.getItemId();
                            if (itemId == R.id.home) {
                                Toast.makeText(MainActivity.this, "Strona główna", Toast.LENGTH_SHORT).show();
                            } else if (itemId == R.id.compass) {
                                Toast.makeText(MainActivity.this, "Kompas", Toast.LENGTH_SHORT).show();
                            } else if (itemId == R.id.map) {
                                Toast.makeText(MainActivity.this, "Mapa", Toast.LENGTH_SHORT).show();
                            } else if (itemId == R.id.rate) {
                                Toast.makeText(MainActivity.this, "Oceń nas", Toast.LENGTH_SHORT).show();
                            } else if (itemId == R.id.action_settings) {
                                Toast.makeText(MainActivity.this, "Ustawienia", Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    });
                }
            }
        });
    }

    private void toggleCompass() {
        if (compassActive) {
            compassActive = false;
            compassLayout.setVisibility(View.GONE);
            sensorManager.unregisterListener(sensorEventListener);
        } else {
            compassActive = true;
            compassLayout.setVisibility(View.VISIBLE);
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private void toggleCompassLayout() {
        if (compassLayout.getVisibility() == View.VISIBLE) {
            compassLayout.setVisibility(View.GONE);
        } else {
            compassLayout.setVisibility(View.VISIBLE);
            // Tutaj możesz dodać kod do obsługi kompasu (np. uzyskanie kierunku i aktualizacja widoku).
        }
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        float[] mGravity;
        float[] mGeomagnetic;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = sensorEvent.values;
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    float azimuth = (float) Math.toDegrees(orientation[0]);
                    compassImage.setRotation(-azimuth);
                    updateCompassDirection(azimuth);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            // Puste - nie potrzebujemy tego
        }
    };
    private void updateCompassDirection(float azimuth) {
        String direction;
        if (azimuth >= -22.5 && azimuth < 22.5) {
            direction = "Północ";
        } else if (azimuth >= 22.5 && azimuth < 67.5) {
            direction = "Północny-wschód";
        } else if (azimuth >= 67.5 && azimuth < 112.5) {
            direction = "Wschód";
        } else if (azimuth >= 112.5 && azimuth < 157.5) {
            direction = "Południowy-wschód";
        } else if (azimuth >= 157.5 || azimuth < -157.5) {
            direction = "Południe";
        } else if (azimuth >= -157.5 && azimuth < -112.5) {
            direction = "Południowy-zachód";
        } else if (azimuth >= -112.5 && azimuth < -67.5) {
            direction = "Zachód";
        } else {
            direction = "Północny-zachód";
        }

        compassDirection.setText(direction);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), mapView);
                myLocationOverlay.enableMyLocation();
                mapView.getOverlays().add(myLocationOverlay);
            } else {
                Toast.makeText(this, "Brak uprawnień do lokalizacji.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

