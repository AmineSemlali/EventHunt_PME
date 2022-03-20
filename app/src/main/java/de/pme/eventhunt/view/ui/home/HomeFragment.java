package de.pme.eventhunt.view.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.utilities.EventFilter;
import de.pme.eventhunt.view.ui.core.BaseFragment;
import de.pme.eventhunt.view.ui.filter_event.FilterEventViewModel;

public class HomeFragment extends BaseFragment implements EventListAdapter.EventListViewHolder.OnNoteListener {

    private HomeViewModel homeViewModel;
    private FilterEventViewModel filterEventViewModel;
    private NavController navController;
    private NavHostFragment navHostFragment;

    RecyclerView eventListView;

    ImageView addEvent;
    ImageView filterEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Context context = getContext();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();



        homeViewModel = this.getViewModel(HomeViewModel.class);
        homeViewModel.activity = getActivity();
        //filterEventViewModel = this.getViewModel(FilterEventViewModel.class);
        filterEventViewModel = new ViewModelProvider(requireActivity()).get(FilterEventViewModel.class);

        // For Recyclerview
        eventListView = view.findViewById(R.id.list_events);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        //itemDecoration.setDrawable(context.getDrawable(R.drawable.recyclerview_divider));
        //itemDecoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));

        eventListView.addItemDecoration(itemDecoration);

        // Create Adapter
        final EventListAdapter adapter = new EventListAdapter(context, this);
        eventListView.setAdapter(adapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));

        homeViewModel.setAdapterList(adapter, filterEventViewModel);


        addEvent = view.findViewById(R.id.button_addEvent);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_navigation_home_to_createEvent);
            }
        });



        filterEvent = view.findViewById(R.id.button_filterEvent);

        filterEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_filter_events);
            }
        });




        return view;
    }

    // function to be set on list items in the EventListAdapter
    @Override
    public void onNoteClick(int position) {
        String eventId = homeViewModel.events.get(position).getEventId();
        Bundle bundle = new Bundle();
        bundle.putString("eventId", eventId);
        navController.navigate(R.id.action_navigation_home_to_detail_view, bundle);
    }
}