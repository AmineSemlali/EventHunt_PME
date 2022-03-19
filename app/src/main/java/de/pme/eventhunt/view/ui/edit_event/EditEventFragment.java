package de.pme.eventhunt.view.ui.edit_event;

import static android.provider.CallLog.Locations.LATITUDE;
import static android.provider.CallLog.Locations.LONGITUDE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.repositories.EventRepository;
import de.pme.eventhunt.model.repositories.NotificationRepository;
import de.pme.eventhunt.model.utilities.EventLocation;
import de.pme.eventhunt.model.utilities.Image;
import de.pme.eventhunt.view.MainActivity;
import de.pme.eventhunt.view.ui.core.BaseFragment;
import de.pme.eventhunt.view.ui.create_event.CreateEventViewModel;
import de.pme.eventhunt.view.ui.utilities.DateAndTime;
import de.pme.eventhunt.view.ui.utilities.DateAndTimePicker;

public class EditEventFragment extends BaseFragment {

    // environmental Variables
    Context context;
    Activity activity;
    View view;
    FirebaseAuth auth;
    FirebaseFirestore db;
    EventRepository eventRepository;
    CreateEventViewModel createEventViewModel;
    FirebaseStorage storage;
    NotificationRepository notificationRepository;

    // event to be edited
    Event curEvent;

    // codes for actions
    private static final int PICK_IMAGE = 1;
    private final static int PLACE_PICKER_REQUEST = 2;

    // input fields
    TextInputEditText titleEditText;
    TextInputEditText descriptionEditText;
    AutoCompleteTextView categoryEditText;
    TextInputEditText locationEditText;
    TextInputEditText dateStartEditText;
    TextInputEditText dateEndEditText;
    ImageView getEventImageView;
    ImageView deleteEventImageView;

    // variables for holding image and location data
    Image eventImage;
    Uri imageUri;
    EventLocation eventLocation;

    double lastLongitude = 0.0;
    double lastLatitude = 0.0;

    // hold information on which event data changed
    boolean titleChanged = false,
            categoryChanged = false,
            locationChanged = false,
            startDateChanged = false,
            endDateChanged = false,
            descriptionChanged = false,
            imageChanged = false;

    private Boolean imageAdjusted = false;




    public EditEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        // get and set category names for the dropdown
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

        // initialize variables
        context = getContext();
        activity = getActivity();
        view = inflater.inflate(R.layout.fragment_create_event, container, false);

        createEventViewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        eventRepository = new EventRepository();
        notificationRepository = new NotificationRepository();

        titleEditText = view.findViewById(R.id.editTextTitle);
        descriptionEditText = view.findViewById(R.id.editTextDescription);
        categoryEditText = view.findViewById(R.id.editTextCategory);
        locationEditText = view.findViewById(R.id.editTextLocation);
        dateStartEditText = view.findViewById(R.id.editTextDateStart);
        dateEndEditText = view.findViewById(R.id.editTextDateEnd);
        getEventImageView = view.findViewById(R.id.imageViewGetImage);
        Button updateButton = view.findViewById(R.id.buttonCreateEvent);
        deleteEventImageView = view.findViewById(R.id.imageView_DeleteEvent);

        eventImage = new Image();

        // initialize date pickers
        DateAndTimePicker dateAndTimePickerStart = new DateAndTimePicker(context, dateStartEditText);
        DateAndTimePicker dateAndTimePickerEnd = new DateAndTimePicker(context, dateEndEditText);

        // get event-id given by the previous fragment
        String eventId = getArguments().getString("eventId");

        // check if eventId is set and retrieve associated data
        if (eventId != null && !eventId.isEmpty()) {
            db.collection("event").whereEqualTo("eventId", eventId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Event> events = task.getResult().toObjects(Event.class);
                            curEvent = events.get(0);

                            EventLocation eventLocation = curEvent.getLocation();

                            Picasso.get()
                                    .load(curEvent.getImageLargeRef())
                                    .into(getEventImageView);

                            titleEditText.setText(curEvent.getTitle());

                            try {
                                locationEditText.setText(eventLocation.getLocationString(context));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            DateAndTime startDate = new DateAndTime();
                            DateAndTime endDate = new DateAndTime();
                            startDate.setDateAndTime(LocalDateTime.parse(curEvent.getStartTime()));
                            endDate.setDateAndTime(LocalDateTime.parse(curEvent.getEndTime()));

                            dateStartEditText.setText(dateStartEditText.getText() + startDate.formatString());
                            dateEndEditText.setText(dateEndEditText.getText() + endDate.formatString());

                            descriptionEditText.setText(curEvent.getDescription());

                        }

                    });


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

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (locationEditText.getRight() - locationEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (dateStartEditText.getRight() - dateStartEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            dateAndTimePickerStart.pickDate();
                            startDateChanged = true;
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

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (dateEndEditText.getRight() - dateEndEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            dateAndTimePickerEnd.pickDate();
                            endDateChanged = true;
                            return true;
                        }
                    }
                    return false;
                }
            });

            // update event, data is checked inside updateEvent function
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title_txt = "";
                    Editable title_editable = titleEditText.getText();
                    if (title_editable != null) title_txt = title_editable.toString();

                    String description_txt = "";
                    Editable description_editable = descriptionEditText.getText();
                    if (description_editable != null)
                        description_txt = descriptionEditText.getText().toString();

                    String category_txt = "";
                    Editable category_editable = categoryEditText.getText();
                    if (category_editable != null) category_txt = category_editable.toString();

                    updateEvent(title_txt, description_txt, category_txt,
                            dateAndTimePickerStart, dateAndTimePickerEnd, eventLocation);

                }


            });

            // delete image (would be nice to have a popup instead of instant deletion)
//           deleteEventImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    eventRepository = new EventRepository();
//                    eventRepository.deleteEvent(curEvent);
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                }
//            });



        }
        return view;
    };

        private void getImage () {
            Intent gallery = new Intent();
            gallery.setType("image/*");
            gallery.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
        }


        private void updateEvent(String title, String description, String category,
                                 DateAndTimePicker dateAndTimePickerStart, DateAndTimePicker
        dateAndTimePickerEnd, EventLocation location){


            String creatorId = auth.getUid();


            //Check title
            if(!Objects.requireNonNull(titleEditText.getText()).toString().equals(curEvent.getTitle()))
            {
                titleChanged = true;

                if (title.length() < 3) {
                    Toast.makeText(context, "Title too short!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (title.length() > 25) {
                    Toast.makeText(context, "Title too long!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //Check description
            if(!Objects.requireNonNull(descriptionEditText.getText()).toString().equals(curEvent.getDescription())) {
                descriptionChanged = true;

                if (description.length() < 10) {
                    Toast.makeText(context, "Description too short!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (description.length() > 250) {
                    Toast.makeText(context, "Description too long!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //Check category
            String[] eventCategories = view.getResources().getStringArray(R.array.eventCategories);
            for (int i = 0; i < eventCategories.length; i++) {
                if (category.equals(eventCategories[i]))
                {
                    if(!eventCategories[i].equals(curEvent.getCategory())) categoryChanged = true;
                    break;
                }
                if (i == eventCategories.length - 1) {
                    Toast.makeText(context, "No category selected!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //Check location
            if (eventLocation == null && locationChanged) {
                Toast.makeText(context, "No location set!", Toast.LENGTH_SHORT).show();
                return;
            }

            //Check start & end date
            DateAndTime startDate = dateAndTimePickerStart.getDateAndTime();
            DateAndTime endDate = dateAndTimePickerEnd.getDateAndTime();

            if ((startDate.day.isEmpty() || startDate.month.isEmpty() || startDate.year.isEmpty()
                    || startDate.hour.isEmpty() || startDate.minute.isEmpty()) && startDateChanged) {
                Toast.makeText(context, "Please select a start date and time!", Toast.LENGTH_SHORT).show();
                return;
            }

            if ((endDate.day.isEmpty() || endDate.month.isEmpty() || endDate.year.isEmpty()
                    || endDate.hour.isEmpty() || endDate.minute.isEmpty()) && endDateChanged) {
                Toast.makeText(context, "Please select a end date and time!", Toast.LENGTH_SHORT).show();
                return;
            }

            String endDateString = "";
            String startDateString = "";
            if(startDateChanged || endDateChanged)
            {
                LocalDateTime startDateLDT;
                if(startDateChanged) startDateLDT = startDate.toLocalDateTime();
                else startDateLDT = LocalDateTime.parse(curEvent.getStartTime());

                LocalDateTime endDateLDT;
                if(endDateChanged) endDateLDT = endDate.toLocalDateTime();
                else endDateLDT = LocalDateTime.parse(curEvent.getEndTime());

                if (startDateLDT.compareTo(endDateLDT) >= 0) {
                    Toast.makeText(context, "End date has to be after start date!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (startDateLDT.compareTo(LocalDateTime.now()) <= 0) {
                    Toast.makeText(context, "Start date has to be in the future!", Toast.LENGTH_SHORT).show();
                    return;
                }

                startDateString = startDate.toLocalDateTimeString();
                endDateString =endDate.toLocalDateTimeString();
            }




            // Check image

            if(imageChanged)
            {
                if (imageUri == null) {
                    Toast.makeText(context, "Please select an image!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!eventImage.isUploadStarted()) {
                    eventImage.StartUpload();
                    Toast.makeText(context, "Image processing isn't finished yet!", Toast.LENGTH_SHORT).show();
                }
            }




            if (eventImage.isUploadStarted() && !eventImage.IsFinished() && imageChanged) {
                Toast.makeText(context, "Image processing isn't finished yet!", Toast.LENGTH_SHORT).show();
                return;
            } else {

                if(titleChanged) curEvent.setTitle(titleEditText.getText().toString());

                if(categoryChanged) curEvent.setCategory(categoryEditText.getText().toString());

                if(descriptionChanged) curEvent.setDescription(descriptionEditText.getText().toString());

                if(startDateChanged) curEvent.setStartTime(startDateString);
                if(endDateChanged) curEvent.setEndTime(endDateString);

                if(locationChanged) curEvent.setLocation(eventLocation);

                // delete old images and change to new ones
                if(imageChanged)
                {
                    storage.getReferenceFromUrl(curEvent.getImageSmallRef()).delete();
                    storage.getReferenceFromUrl(curEvent.getImageLargeRef()).delete();

                    curEvent.setImageSmallRef(eventImage.getDownloadUrlSmall());
                    curEvent.setImageLargeRef(eventImage.getDownloadUrlLarge());
                }

                db.collection(Event.collection).document(curEvent.getEventId()).set(curEvent).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        notificationRepository.addNotificationsForEvent(0, curEvent);
                    }
                });


                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        }


    void pickLocation(View view) {



        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        lastLongitude = location.getLongitude();
                        lastLatitude = location.getLatitude();
                    }

                    Intent locationPickerIntent = new LocationPickerActivity.Builder()
                            .withLocation(lastLatitude, lastLongitude)
                            .withGeolocApiKey("@string/api_key")
                            .withGooglePlacesApiKey("@string/api_key" )
                            .shouldReturnOkOnBackPressed()
                            .withCityHidden()
                            .withZipCodeHidden()
                            .withSatelliteViewHidden()
                            .withGoogleTimeZoneEnabled()
                            .withVoiceSearchHidden()
                            .withUnnamedRoadHidden()
                            .build(context);

                    startActivityForResult(locationPickerIntent, PLACE_PICKER_REQUEST);
                });


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

            imageChanged = true;


            if(!imageAdjusted)
            {
                imageAdjusted = true;
                getEventImageView.getLayoutParams().width = (int) ((int) 250 * context.getResources().getDisplayMetrics().density);
                getEventImageView.getLayoutParams().height = (int) ((int) 175 * context.getResources().getDisplayMetrics().density);
                getEventImageView.requestLayout();
            }
        }
        else if (resultCode == Activity.RESULT_OK && data != null && requestCode == PLACE_PICKER_REQUEST) {
            Log.d("RESULT****", "OK");

            double latitude = data.getDoubleExtra(LATITUDE, 0.0);
            Log.d("LATITUDE****", latitude+"");
            double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
            Log.d("LONGITUDE****", longitude+"");

            eventLocation = new EventLocation(latitude, longitude);
            try {
                locationEditText.setText(eventLocation.getLocationString(context));
                locationChanged = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED");
        }
    }




}