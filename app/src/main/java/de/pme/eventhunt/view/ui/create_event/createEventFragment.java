package de.pme.eventhunt.view.ui.create_event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.adevinta.leku.LocationPickerActivity;
import com.google.android.material.textfield.TextInputEditText;

import de.pme.eventhunt.R;

public class createEventFragment extends Fragment {

    View view;
    CreateEventViewModel createEventViewModel;

    public createEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        String[] eventCategories = getResources().getStringArray(R.array.eventCategories);
        ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_item, eventCategories);
        AutoCompleteTextView textView = view.findViewById(R.id.createEvent_autoCompleteTextView);
        textView.setAdapter(arrayAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_event, container, false);
        createEventViewModel = new ViewModelProvider(this).get(CreateEventViewModel.class);

        TextInputEditText locationEditText = view.findViewById(R.id.editTextLocation);

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
                        pickLocation();
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    void pickLocation(){
        Intent locationPickerIntent = new LocationPickerActivity.Builder()
                .withLocation(41.4036299, 2.1743558)
                .withSearchZone("es_ES")
                .withDefaultLocaleSearchZone()
                .shouldReturnOkOnBackPressed()
                .withStreetHidden()
                .withCityHidden()
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build(view.getContext());

        // registerForActivityResult(locationPickerIntent, new ActivityResultContract<I, O>() {
        //     @NonNull
        //     @Override
        //     public Intent createIntent(@NonNull Context context, Object input) {
        //         return null;
        //     }

        //     @Override
        //     public Object parseResult(int resultCode, @Nullable Intent intent) {
        //         return null;
        //     }
        // });

    }


}