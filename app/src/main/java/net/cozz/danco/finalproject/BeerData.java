package net.cozz.danco.finalproject;

import android.database.Cursor;
import android.location.Location;

import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 */
public class BeerData {
    private final int NAME_COLUMN_INDEX = 0;
    private final int IMAGE_FILE_URI = 1;
    private final int DESCRIPTION_COLUMN_INDEX = 2;
    private final int PUB_COLUMN_INDEX = 3;
    private final int LOCATION_COLUMN_INDEX = 4;

    /*
    The name of the beer
     */
    private String name;

    /*
    The imageUri for the beer
     */
    private String imageFileUri;

    /*
    A description of what the beer is like -- color, flavor, aromatics, etc.
     */
    private String description;

    /*
    Where was I when I had that beer?
     */
    private String pubName;

    /*
    GPS coordinates?
     */
    private Location location;

    public BeerData(String name){
        this.name = name;
    }


    public BeerData(Cursor cursor) {
        this.name = cursor.getString(NAME_COLUMN_INDEX);
        this.imageFileUri = cursor.getString(IMAGE_FILE_URI);
        this.description = cursor.getString(DESCRIPTION_COLUMN_INDEX);
        this.pubName = cursor.getString(PUB_COLUMN_INDEX);
        this.location = buildCoordinates(cursor.getString(LOCATION_COLUMN_INDEX));
    }

    private Location buildCoordinates(String latlong) {
        Location loc = new Location(latlong);

        /*
        I don't really know how to implement location services yet.
        I've added access to GooplePlay Services, but what I want to do is access the location
            from the geo coordinates of the picture. In this class I don't have access to the
            app context, so I can't check:
                    if (GooglePlayServicesUtil.isGooglePlayServicesAvailable()) {
            which is what I should do prior to using the LocationClient it should be fairly
            simple, and there's lots of good examples, but I'm running short on time...

         */
        return loc;
    }

    public String getName(){
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getImageFileUri() {
        return imageFileUri;
    }


    public void setImageFileUri(String imageFileUri) {
        this.imageFileUri = imageFileUri;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getPubName() {
        return pubName;
    }


    public void setPubName(String pubName) {
        this.pubName = pubName;
    }


    public Location getLocation() {
        return location;
    }


    public void setLocation(Location location) {
        this.location = location;
    }


    public void setLocation(String lat_long) {
        location = new Location("");
        String[] coords = lat_long.split(",");
        location.setLatitude(Double.parseDouble(coords[0]));
        location.setLongitude(Double.parseDouble(coords[1]));
    }
}
