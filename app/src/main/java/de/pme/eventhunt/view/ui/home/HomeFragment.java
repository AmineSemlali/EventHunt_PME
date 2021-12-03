package de.pme.eventhunt.view.ui.home;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

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
import de.pme.eventhunt.model.repositories.EventRepository;
import de.pme.eventhunt.view.ui.core.BaseFragment;

public class HomeFragment extends BaseFragment {

    private HomeViewModel homeViewModel;
    private NavController navController;
    private NavHostFragment navHostFragment;

    ImageView addEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Context context = getContext();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();

        homeViewModel = this.getViewModel(HomeViewModel.class);

        // For Recyclerview
        RecyclerView eventListView = view.findViewById(R.id.list_events);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        //itemDecoration.setDrawable(context.getDrawable(R.drawable.recyclerview_divider));
        //itemDecoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.yellow)));

        eventListView.addItemDecoration(itemDecoration);

        // Create Adapter
        final EventListAdapter adapter = new EventListAdapter(context);
        eventListView.setAdapter(adapter);
        eventListView.setLayoutManager(new LinearLayoutManager(context));

        homeViewModel.setAdapterList(adapter);


        addEvent = view.findViewById(R.id.button_addEvent);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_navigation_home_to_createEvent);
            }
        });




        return view;
    }
}