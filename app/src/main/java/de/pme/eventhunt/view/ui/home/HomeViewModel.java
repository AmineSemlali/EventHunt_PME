package de.pme.eventhunt.view.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.repositories.EventRepository;
import de.pme.eventhunt.model.utilities.EventFilter;
import de.pme.eventhunt.model.utilities.EventLocation;
import de.pme.eventhunt.view.ui.filter_event.FilterEventViewModel;

public class HomeViewModel extends AndroidViewModel {

    private final EventRepository eventRepository;

    FirebaseFirestore db;
    List<Event> events;
    EventListAdapter adapter;
    Location lastLocation;
    Activity activity;

    Boolean toDelete = false;

    public HomeViewModel(@NonNull Application application) {

        super(application);
        eventRepository = new EventRepository();
        db = FirebaseFirestore.getInstance();
    }

    public List<Event> getAllEvents() {
        db.collection("event").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                events = task.getResult().toObjects(Event.class);
                adapter.setEventList(events);
            }
        });

        return events;
    }

    public void setAdapterList(EventListAdapter adapter, FilterEventViewModel filterEventViewModel) {
        this.adapter = adapter;
        if (filterEventViewModel.isDefault()) getAllEvents();
        else getLocationAndFilter(filterEventViewModel.eventFilter);
    }

    public void getLocationAndFilter(EventFilter eventFilter)
    {
        if(lastLocation == null)
        {
            FusedLocationProviderClient fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(activity);

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                lastLocation = location;
                                getFilteredEvents(eventFilter);
                            }
                        }
                    });
        }
        else getFilteredEvents(eventFilter);
    }

    private List<Event> getFilteredEvents(EventFilter eventFilter) {

        db.collection("event").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                events = task.getResult().toObjects(Event.class);

                Map<String, Object> map = new HashMap<>();


                // check if title filter set
                String filterTitle = eventFilter.filterTitle;
                if (!filterTitle.equals("")) {
                    //get regex
                    map.put("title", filterTitle);
                }

                // check if category filter set
                String filterCategory = eventFilter.filterCategory;
                if (!filterCategory.equals("")) {
                    map.put("category", filterCategory);
                }

                // check if distance filter set
                int filterDistance = eventFilter.filterDistance;
                if (filterDistance != 0) {
                    map.put("distance", filterDistance);
                }

                // check if date filter set
                LocalDateTime firstDate;
                firstDate = LocalDateTime.parse(eventFilter.filterFirstDate);

                if(firstDate.isAfter(LocalDateTime.now())) map.put("firstDate", firstDate);

                String filterLastDate = eventFilter.filterLastDate;
                if(!filterLastDate.equals("2100-01-01T12:00:00"))
                    map.put("lastDate", LocalDateTime.parse(filterLastDate));


                events.removeIf(event -> {
                    toDelete = false;
                    if(map.size() != 0) {


                        for (String key : map.keySet()) {
                            if(key.equals("title"))
                            {
                                //regex
                                String titleRegex = (String) map.get(key);
                                if(!event.getTitle().matches(".*"+ Pattern.quote(titleRegex)+".*")
                                        && !event.getTitle().matches(".*"+Pattern.quote(titleRegex.toLowerCase(Locale.ROOT))+".*")) {
                                    toDelete = true;
                                }
                            }
                            else if(key.equals("category"))
                            {
                                String category = (String) map.get(key);
                                if(!event.getCategory().equals(category)) toDelete = true;

                            }
                            else if(key.equals("distance"))
                            {
                                EventLocation eventLocation = event.getLocation();
                                Location eventLocationLoc = new Location("");
                                eventLocationLoc.setLatitude(eventLocation.getLatitude());
                                eventLocationLoc.setLongitude(eventLocation.getLongitude());

                                Integer distanceInt = (Integer) map.get(key);
                                double distanceDouble = (double)distanceInt * 1000;

                                if(lastLocation.distanceTo(eventLocationLoc) > distanceDouble) toDelete=true;

                            }
                            else if(key.equals("firstDate"))
                            {
                                LocalDateTime firstDateLD = (LocalDateTime) map.get(key);
                                LocalDateTime eventStartLD = LocalDateTime.parse(event.getStartTime());

                                if(eventStartLD.isBefore(firstDateLD)) toDelete=true;

                            }
                            else if(key.equals("lastDate"))
                            {
                                LocalDateTime lastDateLD = (LocalDateTime) map.get(key);
                                LocalDateTime eventStartLD = LocalDateTime.parse(event.getStartTime());

                                if(eventStartLD.isAfter(lastDateLD)) toDelete=true;
                            }
                        }
                    }
                    return toDelete;
                });


                adapter.setEventList(events);
            }
        });

        return events;




    }
}
