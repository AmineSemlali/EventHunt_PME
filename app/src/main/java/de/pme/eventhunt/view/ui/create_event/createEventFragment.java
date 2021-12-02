package de.pme.eventhunt.view.ui.create_event;

import static android.provider.CallLog.Locations.LATITUDE;
import static android.provider.CallLog.Locations.LONGITUDE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog.Locations;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.adevinta.leku.LocationPickerActivity;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import de.pme.eventhunt.MapsActivity;
import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.repositories.EventRepository;
import de.pme.eventhunt.model.utilities.Image;
import de.pme.eventhunt.model.utilities.Location;
import de.pme.eventhunt.view.MainActivity;
import de.pme.eventhunt.view.StartActivity;
import de.pme.eventhunt.view.ui.utilities.DateAndTime;
import de.pme.eventhunt.view.ui.utilities.DateAndTimePicker;

public class createEventFragment extends Fragment {


    Context context;
    Activity activity;
    
    private static final int PICK_IMAGE = 1;
    private final static int PLACE_PICKER_REQUEST = 2;

    View view;
    FirebaseAuth auth;
    EventRepository eventRepository;
    CreateEventViewModel createEventViewModel;
    FirebaseStorage storage;

    TextInputEditText titleEditText;
    TextInputEditText descriptionEditText;
    AutoCompleteTextView categoryEditText;
    TextInputEditText locationEditText;
    TextInputEditText dateStartEditText;
    TextInputEditText dateEndEditText;
    ImageView getEventImageView;

    Image eventImage;
    Location eventLocation;


    private Boolean imageAdjusted = false;
    Uri imageUri;

    public createEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();


        String[] eventCategories = getResources().getStringArray(R.array.eventCategories);
        ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_item, eventCategories);
        AutoCompleteTextView textView = view.findViewById(R.id.editTextCategory);
        textView.setAdapter(arrayAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        activity = getActivity();
        
        view = inflater.inflate(R.layout.fragment_create_event, container, false);
        createEventViewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        eventRepository = new EventRepository();
        



        titleEditText = view.findViewById(R.id.editTextTitle);
        descriptionEditText = view.findViewById(R.id.editTextDescription);
        categoryEditText = view.findViewById(R.id.editTextCategory);
        locationEditText = view.findViewById(R.id.editTextLocation);
        dateStartEditText = view.findViewById(R.id.editTextDateStart);
        dateEndEditText = view.findViewById(R.id.editTextDateEnd);

        getEventImageView = view.findViewById(R.id.imageViewGetImage);

        eventImage = new Image();


        Button createButton = view.findViewById(R.id.buttonCreateEvent);

        DateAndTimePicker dateAndTimePickerStart = new DateAndTimePicker(context, dateStartEditText);
        DateAndTimePicker dateAndTimePickerEnd = new DateAndTimePicker(context, dateEndEditText);



        getEventImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });

        locationEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (locationEditText.getRight() - locationEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        pickLocation(view);
                        return true;
                    }
                }
                return false;
            }
        });

        dateStartEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (dateStartEditText.getRight() - dateStartEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        dateAndTimePickerStart.pickDate();
                        return true;
                    }
                }
                return false;
            }
        });

        dateEndEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (dateEndEditText.getRight() - dateEndEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        dateAndTimePickerEnd.pickDate();
                        return true;
                    }
                }
                return false;
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title_txt = titleEditText.getText().toString();
                String description_txt = descriptionEditText.getText().toString();
                String category_txt = categoryEditText.getText().toString();

                createEvent(title_txt, description_txt, category_txt,
                            dateAndTimePickerStart, dateAndTimePickerEnd, eventLocation);
            }


        });




        return view;
    }

    private void getImage() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }



    private void createEvent(String title, String description, String category,
                             DateAndTimePicker dateAndTimePickerStart, DateAndTimePicker dateAndTimePickerEnd, Location location) {


        String creatorId = auth.getUid();



        //Check title
        if(title.length() < 3) {
            Toast.makeText(context, "Title too short!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(title.length() > 25) {
            Toast.makeText(context, "Title too long!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check description
        if(description.length() < 20) {
            Toast.makeText(context, "Description too short!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(description.length() > 250) {
            Toast.makeText(context, "Description too long!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check category
        String[] eventCategories = view.getResources().getStringArray(R.array.eventCategories);
        for (int i = 0; i<eventCategories.length; i++)
        {
            if(category.equals(eventCategories[i])) break;
            if(i == eventCategories.length-1) {
                Toast.makeText(context, "No category selected!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Check location
        if(eventLocation == null)
        {
            Toast.makeText(context, "No location set!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Check start & end date
        DateAndTime startDate = dateAndTimePickerStart.getDateAndTime();
        DateAndTime endDate = dateAndTimePickerEnd.getDateAndTime();

        if(startDate.day.isEmpty() || startDate.month.isEmpty() || startDate.year.isEmpty()
                || startDate.hour.isEmpty() || startDate.minute.isEmpty())
        {
            Toast.makeText(context, "Please select a start date and time!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(endDate.day.isEmpty() || endDate.month.isEmpty() || endDate.year.isEmpty()
                || endDate.hour.isEmpty() || endDate.minute.isEmpty())
        {
            Toast.makeText(context, "Please select a end date and time!", Toast.LENGTH_SHORT).show();
            return;
        }

        LocalDateTime startDateLDT = startDate.toLocalDateTime();
        LocalDateTime endDateLDT = endDate.toLocalDateTime();

        if(startDateLDT.compareTo(endDateLDT) >= 0)
        {
            Toast.makeText(context, "End date has to be after start date!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(startDateLDT.compareTo(LocalDateTime.now()) <= 0)
        {
            Toast.makeText(context, "Start date has to be in the future!", Toast.LENGTH_SHORT).show();
            return;
        }

        String startDateString = startDate.toLocalDateTimeString();
        String endDateString = endDate.toLocalDateTimeString();

        if(!eventImage.isUploadStarted())
        {
            eventImage.StartUpload();
            Toast.makeText(context, "Image processing isn't finished yet!", Toast.LENGTH_SHORT).show();
        }


        if(eventImage.isUploadStarted() && !eventImage.IsFinished())
        {
            Toast.makeText(context, "Image processing isn't finished yet!", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            Event newEvent = new Event(creatorId, title, description,
                    category, eventLocation, startDateString, endDateString,
                    eventImage.getDownloadUrlSmall(), eventImage.getDownloadUrlLarge());
            eventRepository.createEvent(newEvent);
        }
    }


    void pickLocation(View view){

        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withLocation(41.4036299, 2.1743558)
                .withGeolocApiKey("AIzaSyCjgZOgBTM-zW6FMBDNJGvXcK6-D7O6sYY")
                .withGooglePlacesApiKey("AIzaSyCjgZOgBTM-zW6FMBDNJGvXcK6-D7O6sYY")
                .withSearchZone("es_ES")
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withCityHidden()
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(context);

        startActivityForResult(locationPickerIntent, PLACE_PICKER_REQUEST);

    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== PICK_IMAGE && resultCode==Activity.RESULT_OK)
        {
            assert data != null;
            imageUri = data.getData();
            eventImage.CreateBitmapSmall(imageUri);
            eventImage.CreateBitmapLarge(imageUri, getEventImageView);

            if(!imageAdjusted)
            {
                imageAdjusted = true;
                DisplayMetrics metrics = new DisplayMetrics();
                float logicalDensity = metrics.density;
                getEventImageView.getLayoutParams().width = (int) ((int) 250 * context.getResources().getDisplayMetrics().density);
                getEventImageView.getLayoutParams().height = (int) ((int) 175 * context.getResources().getDisplayMetrics().density);
                getEventImageView.requestLayout();
            }
        }
        else if (resultCode == Activity.RESULT_OK && data != null || requestCode == PLACE_PICKER_REQUEST) {
            Log.d("RESULT****", "OK");

            assert data != null;
            double latitude = data.getDoubleExtra(LATITUDE, 0.0);
                Log.d("LATITUDE****", latitude+"");
                double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                Log.d("LONGITUDE****", longitude+"");

            eventLocation = new Location(latitude, longitude);
            try {
                locationEditText.setText(eventLocation.getLocationString(context));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED");
        }
    }
    



}