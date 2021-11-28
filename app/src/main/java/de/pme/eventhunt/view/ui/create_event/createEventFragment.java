package de.pme.eventhunt.view.ui.create_event;

import static android.provider.CallLog.Locations.LATITUDE;
import static android.provider.CallLog.Locations.LONGITUDE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog.Locations;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;

import de.pme.eventhunt.MapsActivity;
import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.repositories.EventRepository;
import de.pme.eventhunt.view.ui.utilities.DateAndTime;
import de.pme.eventhunt.view.ui.utilities.DateAndTimePicker;

public class createEventFragment extends Fragment {

    View view;
    FirebaseAuth auth;
    EventRepository eventRepository;
    CreateEventViewModel createEventViewModel;
    FirebaseStorage storage;

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
        view = inflater.inflate(R.layout.fragment_create_event, container, false);
        createEventViewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        eventRepository = new EventRepository();


        TextInputEditText titleEditText = view.findViewById(R.id.editTextTitle);
        TextInputEditText descriptionEditText = view.findViewById(R.id.editTextDescription);
        AutoCompleteTextView categoryEditText = view.findViewById(R.id.editTextCategory);
        TextInputEditText locationEditText = view.findViewById(R.id.editTextLocation);
        TextInputEditText dateStartEditText = view.findViewById(R.id.editTextDateStart);
        TextInputEditText dateEndEditText = view.findViewById(R.id.editTextDateEnd);
        Button createButton = view.findViewById(R.id.buttonCreateEvent);

        DateAndTimePicker dateAndTimePickerStart = new DateAndTimePicker(getContext(), dateStartEditText);
        DateAndTimePicker dateAndTimePickerEnd = new DateAndTimePicker(getContext(), dateEndEditText);



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
                String location_txt = "null";

                createEvent(title_txt, description_txt, category_txt,
                            dateAndTimePickerStart, dateAndTimePickerEnd, location_txt);
            }


        });




        return view;
    }

    private void createEvent(String title, String description, String category,
                             DateAndTimePicker dateAndTimePickerStart, DateAndTimePicker dateAndTimePickerEnd, String location) {

        Context context = getContext();

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

          //////////////////////////////////
         ///////// CHECK LOCATION /////////
        //////////////////////////////////

        Event newEvent = new Event(creatorId, title, description, category,
                location, startDateString, endDateString);

        eventRepository.createEvent(newEvent);
    }


    void pickLocation(View view){
        startActivity(new Intent(getActivity(), MapsActivity.class));

    }



}