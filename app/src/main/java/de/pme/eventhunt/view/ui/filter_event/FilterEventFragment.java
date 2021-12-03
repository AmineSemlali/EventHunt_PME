package de.pme.eventhunt.view.ui.filter_event;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.utilities.EventFilter;
import de.pme.eventhunt.view.ui.core.BaseFragment;
import de.pme.eventhunt.view.ui.utilities.DateAndTime;
import de.pme.eventhunt.view.ui.utilities.DateAndTimePicker;

public class FilterEventFragment extends BaseFragment {

    View view;

    private NavController navController;
    private NavHostFragment navHostFragment;

    TextInputEditText firstDateEditText;
    TextInputEditText lastDateEditText;

    public FilterEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        String[] distances = getResources().getStringArray(R.array.distances);
        ArrayAdapter arrayAdapterDistance = new ArrayAdapter(requireContext(), R.layout.dropdown_item, distances);
        AutoCompleteTextView textViewDistance = view.findViewById(R.id.editTextDistance);
        textViewDistance.setAdapter(arrayAdapterDistance);

        String[] categories = getResources().getStringArray(R.array.eventCategories);
        ArrayAdapter arrayAdapterCategory = new ArrayAdapter(requireContext(), R.layout.dropdown_item, categories);
        AutoCompleteTextView textViewCategory = view.findViewById(R.id.editTextCategoryFilter);
        textViewCategory.setAdapter(arrayAdapterCategory);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filter_events, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();

        Context context = getContext();

        firstDateEditText = view.findViewById(R.id.editTextDateFirstDateFilter);
        lastDateEditText = view.findViewById(R.id.editTextLastDateFilter);

        DateAndTimePicker dateAndTimePickerFirst = new DateAndTimePicker(context, firstDateEditText);
        DateAndTimePicker dateAndTimePickerLast = new DateAndTimePicker(context, lastDateEditText);

        Button applyFilterButton = view.findViewById(R.id.buttonFilterEvent);
        ImageView closeFilterButton = view.findViewById(R.id.closeFilter);

        EventFilter newFilter = new EventFilter();


        closeFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigate back up and make filter empty
                Object object = (Object) new EventFilter();
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("filter", object);
            }
        });


        firstDateEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (firstDateEditText.getRight() - firstDateEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    dateAndTimePickerFirst.pickDate();
                    return true;
                }
            }
            return false;
        });

        lastDateEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_LEFT = 0;
            final int DRAWABLE_TOP = 1;
            final int DRAWABLE_RIGHT = 2;
            final int DRAWABLE_BOTTOM = 3;

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (lastDateEditText.getRight() - lastDateEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    // your action here
                    dateAndTimePickerLast.pickDate();
                    return true;
                }
            }
            return false;
        });

        applyFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateAndTime firstDate = dateAndTimePickerFirst.getDateAndTime();
                DateAndTime lastDate = dateAndTimePickerLast.getDateAndTime();



                navController.getPreviousBackStackEntry().getSavedStateHandle().set("filter", newFilter);

            }
        });





        return view;
    }
}