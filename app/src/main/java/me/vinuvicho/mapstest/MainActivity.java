package me.vinuvicho.mapstest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import me.vinuvicho.mapstest.directionhelpers.FetchURL;
import me.vinuvicho.mapstest.directionhelpers.TaskLoadedCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    GoogleMap map;
    Button btnGetDirection;
    MarkerOptions place1, place2;
    Polyline currentPolyLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        btnGetDirection = findViewById(R.id.btnGetDirection);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        //27.658143,85.3199503
        //27.667491,85.3208583
        place1 = new MarkerOptions().position(new LatLng(49.834846, 24.014439)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(49.849826, 24.022414)).title("Location 2");

        btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getUrl(place1.getPosition(), place2.getPosition(), "driving");
                new FetchURL(MainActivity.this).execute(url, "driving");
            }
        });


    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(place1);
        map.addMarker(place2);
    }

    @Override
    public void onTaskDone(Object... values) {
        if(currentPolyLine != null) currentPolyLine.remove();
        currentPolyLine = map.addPolyline((PolylineOptions) values[0]);
    }
}
