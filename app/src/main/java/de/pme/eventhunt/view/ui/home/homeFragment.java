package de.pme.eventhunt.view.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import de.pme.eventhunt.R;
import de.pme.eventhunt.view.ui.core.BaseFragment;

public class homeFragment extends BaseFragment {

    private HomeViewModel homeViewModel;
    private NavController navController;
    private NavHostFragment navHostFragment;

    ImageView addEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();

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