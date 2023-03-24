package me.vinuvicho.mapstest.api;

import me.vinuvicho.mapstest.R;

public class Direction {
    public static String getUrl(String origin, String dest, String directionMode, String key) {
        // Origin of route
        String str_origin = "origin=" + origin;
        // Destination of route
        String str_dest = "destination=" + dest;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode + "&language=ua";
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key;
        System.out.println(url);
        return url;
    }
}
