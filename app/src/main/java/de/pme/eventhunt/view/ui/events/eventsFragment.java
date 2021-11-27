package de.pme.eventhunt.view.ui.events;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.pme.eventhunt.R;
import de.pme.eventhunt.view.ui.core.BaseFragment;
import de.pme.eventhunt.view.ui.create_event.CreateEventViewModel;

public class eventsFragment extends BaseFragment {

    private EventsViewModel eventsViewModel;

    Button addEvent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventsViewModel = new ViewModelProvider(this).get(EventsViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        return view;
    }
}