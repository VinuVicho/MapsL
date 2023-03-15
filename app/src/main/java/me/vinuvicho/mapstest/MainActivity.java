package me.vinuvicho.mapstest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import me.vinuvicho.mapstest.directionhelpers.FetchURL;
import me.vinuvicho.mapstest.directionhelpers.TaskLoadedCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, LocationListener {

    GoogleMap map;
    Button btnGetDirection;
    Polyline currentPolyLine;
    EditText txtToGo;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    LatLng currentLocation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        btnGetDirection = findViewById(R.id.btnGetDirection);
        txtToGo = findViewById(R.id.txtToGo);
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

        btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String direction = txtToGo.getText().toString().trim();
                String url = getUrl(currentLocation, direction, "driving");
                new FetchURL(MainActivity.this).execute(url, "driving");
            }
        });

    }

    private String getUrl(LatLng origin, String dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + UkrainianToLatin.generateLat(dest).replace(" ", "+");
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&language=ua";
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        System.out.println(url);
        return url;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(49.83972479758758, 24.029615048431022), 13));
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyLine != null) currentPolyLine.remove();
        currentPolyLine = map.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }
}
