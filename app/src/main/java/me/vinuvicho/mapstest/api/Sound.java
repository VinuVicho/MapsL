package me.vinuvicho.mapstest.api;

public class Sound {
    public static String getURL(String language, String text) {
        return "https://maps.googleapis.com/maps/api/place/findplacefromtext/json" +
                "?fields=formatted_address%2Cname%2Crating%2Cgeometry" +
                "&text=" + text +
                "&language=" + language +
                "&inputtype=textquery&key=" + "AIzaSyB7HYPKPTTeAkXFC6RXiOgZftgUIKekBM";
    }
}
