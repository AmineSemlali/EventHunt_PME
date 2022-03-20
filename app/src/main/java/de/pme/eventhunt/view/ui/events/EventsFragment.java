package de.pme.eventhunt.view.ui.events;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import de.pme.eventhunt.R;
import de.pme.eventhunt.view.ui.core.BaseFragment;
import de.pme.eventhunt.view.ui.create_event.CreateEventViewModel;
import de.pme.eventhunt.view.ui.filter_event.FilterEventViewModel;
import de.pme.eventhunt.view.ui.home.EventListAdapter;
import de.pme.eventhunt.view.ui.home.HomeViewModel;

// this fragment shows a list of all events, or the events kept after applying filters
public class EventsFragment extends BaseFragment  implements MyEventListAdapter.MyEventListViewHolder.OnNoteListener {


    private EventsViewModel eventsViewModel;
    private FilterEventViewModel filterEventViewModel;
    private NavController navController;
    private NavHostFragment navHostFragment;

    RecyclerView eventListView;
    MyEventListAdapter adapter;

    Button joinedEventsButton;
    Button ownEventsButton;

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
        Context context = getContext();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();

        joinedEventsButton = view.findViewById(R.id.button_getJoined);
        ownEventsButton = view.findViewById(R.id.button_getOwn);



        eventsViewModel = this.getViewModel(EventsViewModel.class);
        //filterEventViewModel = this.getViewModel(FilterEventViewModel.class);
        filterEventViewModel = new ViewModelProvider(requireActivity()).get(FilterEventViewModel.class);

        // For Recyclerview
        eventListView = view.findViewById(R.id.list_myEvents);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        //itemDecoration.setDrawable(context.getDrawable(R.drawable.recyclerview_divider));
        //itemDecoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));

        eventListView.addItemDecoration(itemDecoration);

        // Create Adapter
        adapter = new MyEventListAdapter(context, this);
        eventListView.setAdapter(adapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));

        eventsViewModel.setAdapterListJoined(adapter);


        joinedEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eventsViewModel.isJoinedList)
                {
                    eventsViewModel.setAdapterListJoined(adapter);
                    joinedEventsButton.setBackgroundColor(getResources().getColor(R.color.orange));
                    ownEventsButton.setBackgroundColor(getResources().getColor(R.color.yellow));
                }
            }
        });

        ownEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eventsViewModel.isJoinedList)
                {
                    eventsViewModel.setAdapterListOwn(adapter);
                    joinedEventsButton.setBackgroundColor(getResources().getColor(R.color.yellow));
                    ownEventsButton.setBackgroundColor(getResources().getColor(R.color.orange));
                }
            }
        });

        return view;
    }

    @Override
    public void onNoteClick(int position) {
        String eventId = eventsViewModel.events.get(position).getEventId();
        Bundle bundle = new Bundle();
        bundle.putString("eventId", eventId);
        navController.navigate(R.id.action_navigation_events_to_detail_view, bundle);
    }
}