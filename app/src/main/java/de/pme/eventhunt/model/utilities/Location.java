package de.pme.eventhunt.model.utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationString(Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context);

        List<Address> adressList = geocoder.getFromLocation(latitude, longitude, 1);
        Address address = adressList.get(0);

        return address.getThoroughfare() + ", " + address.getPostalCode()
                + " " + address.getLocality() + ", " + address.getCountryName();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
