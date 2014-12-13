package net.cozz.danco.finalproject;

import android.location.Location;

/**
 * Created by onwuneme on 10/29/14.
 */
public class BeerData {
    /*
    The name of the beer
     */
    private String name;

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

    public String getName(){
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
}
