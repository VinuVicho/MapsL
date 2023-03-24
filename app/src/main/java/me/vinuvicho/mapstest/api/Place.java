package me.vinuvicho.mapstest.api;

import android.content.Context;
import android.widget.CheckBox;

import com.google.android.gms.maps.model.LatLng;

import me.vinuvicho.mapstest.R;

public class Place {
    private LatLng userLocation;
    private LatLng placeLocation;
    private String name;
    private String description;
    private String language;
    private final String key;

    public Place() {
        key = "AIzaSyB7HYPKPTTeAkXFC6RXiOgZftgUIKekBM";
        language = (R.id.language > 0) ? "ua" : "en";
    }

    public String getPlaces() {
        String places = (R.id.monuments > 0 ? "Monuments" : "")
                + (R.id.historicalPlaces > 0 ? "&or&Historical_places" : "")
                + (R.id.museums > 0 ? "&or&Museums" : "")
                + (R.id.theaters > 0 ? "&or&Theaters" : "")
                + (R.id.restaurants > 0 ? "&or&Restaurants" : "");
        return places;
    }

    public LatLng getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(LatLng userLocation) {
        this.userLocation = userLocation;
    }

    public LatLng getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(LatLng placeLocation) {
        this.placeLocation = placeLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getURL() {
        String places = getPlaces();

        return "https://maps.googleapis.com/maps/api/place/findplacefromtext/json" +
                "?fields=formatted_address%2Cname%2Crating%2Cgeometry" +
                "&input=" + places +
                "&language=" + language +
                "&inputtype=textquery&key=" + key;
    }

}
