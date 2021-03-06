package de.pme.eventhunt.model.utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import de.pme.eventhunt.model.documents.Event;
// class for managing the event location

public class EventLocation {
    private double latitude;
    private double longitude;

    public EventLocation(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public EventLocation() {};

    //returns the location as String
    public String getLocationString(Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context);

        List<Address> adressList = geocoder.getFromLocation(latitude, longitude, 1);
        Address address = adressList.get(0);

        Log.d("address", address.toString());

        String feature = address.getFeatureName();
        String thoroughfare = address.getThoroughfare();
        String postalCode = address.getPostalCode();
        String locality = address.getLocality();
        String country = address.getCountryName();

        String addressString = "";

        if(thoroughfare != null) addressString += thoroughfare;

        if(feature != null && !feature.equals(thoroughfare)) addressString += " " + feature;

        if(postalCode != null)
        {
            if (addressString.isEmpty()) addressString += postalCode;
            else addressString += ", " + postalCode;
        }

        if(locality != null)
        {
            addressString += " " + locality;
        }

        if(country != null)
        {
            if (addressString.isEmpty()) addressString += country;
            else addressString += ", " + country;
        }

        return addressString;
    }
// returns the street as String
    public String getStreetString(Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context);

        List<Address> adressList = geocoder.getFromLocation(latitude, longitude, 1);
        Address address = adressList.get(0);

        String feature = address.getFeatureName();
        String thoroughfare = address.getThoroughfare();

        String addressString = "";

        if(thoroughfare != null) addressString += thoroughfare;

        if(feature != null && !feature.equals(thoroughfare)) addressString += " " + feature;

        return addressString;
    }
    // returns the city as String
    public String getCityString(Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context);

        List<Address> adressList = geocoder.getFromLocation(latitude, longitude, 1);
        Address address = adressList.get(0);

        Log.d("address", address.toString());

        String postalCode = address.getPostalCode();
        String locality = address.getLocality();

        String addressString = "";

        if(postalCode != null)
        {
            addressString += postalCode;
        }

        if(locality != null)
        {
            addressString += " " + locality;
        }

        return addressString;
    }
    // returns the city and country as String
    public String getCityCountryString(Context context) throws IOException {
        Geocoder geocoder = new Geocoder(context);

        List<Address> adressList = geocoder.getFromLocation(latitude, longitude, 1);
        Address address = adressList.get(0);

        Log.d("address", address.toString());

        String locality = address.getLocality();
        String country = address.getCountryName();

        String addressString = "";

        if(locality != null)
        {
            addressString += locality;
        }

        if(country != null)
        {
            if (addressString.isEmpty()) addressString += country;
            else addressString += ", " + country;
        }

        return addressString;
    }

    public List<Event> GetLocationsInRange(Integer range)
    {
        List<Event> eventsInRage = null;

        return eventsInRage;
    }
    // getters and setters
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
