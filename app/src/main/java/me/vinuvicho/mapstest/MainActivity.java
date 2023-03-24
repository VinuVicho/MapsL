package me.vinuvicho.mapstest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.time.LocalDateTime;

import me.vinuvicho.mapstest.api.Direction;
import me.vinuvicho.mapstest.api.Place;
import me.vinuvicho.mapstest.helpers.FetchURL;
import me.vinuvicho.mapstest.directionhelpers.TaskLoadedCallback;
import me.vinuvicho.mapstest.helpers.UkrainianToLatin;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, LocationListener {

    GoogleMap map;
    Button btnGetDirection;
    Polyline currentPolyLine;
    EditText txtToGo;
    EditText txtFrom;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    private LocalDateTime time;
    LatLng currentLocation;
    public Place place;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        place = new Place();
        time = LocalDateTime.now();
        setContentView(R.layout.main_activity);
        btnGetDirection = findViewById(R.id.btnGetDirection);
        txtToGo = findViewById(R.id.txtToGo);
        txtFrom = findViewById(R.id.txtFrom);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("No location access");
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);


        findViewById(R.id.toSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(switchActivityIntent);
            }
        });
        btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String direction = txtToGo.getText().toString().trim();
                String directionFrom = txtFrom.getText().toString().trim();
                if (direction.isEmpty()) {
                    removePolyline();
                    return;
                }
                String url;
                if (directionFrom.isEmpty()) {
                    url = Direction.getUrl(currentLocation.latitude + "," + currentLocation.longitude,
                            UkrainianToLatin.generateLat(direction).replace(" ", "+"),
                            "driving", getString(R.string.google_maps_key));
                } else {
                    url = Direction.getUrl(UkrainianToLatin.generateLat(directionFrom).replace(" ", "+"),
                            UkrainianToLatin.generateLat(direction).replace(" ", "+"),
                            "driving", getString(R.string.google_maps_key));
                }
                new FetchURL(MainActivity.this).execute(url, "driving");
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.83972479758758, 24.029615048431022), 13));
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyLine != null) removePolyline();
        currentPolyLine = map.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        if (!map.isMyLocationEnabled()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Give Perm");
                return;
            }
            map.setMyLocationEnabled(true);
        }
        if (time.isBefore(LocalDateTime.now().minusSeconds(15))) {
            time = LocalDateTime.now();
            new FetchURL(this).sendRequest(place.getURL());
        }
    }

    public void removePolyline() {
        currentPolyLine.remove();
    }
}
