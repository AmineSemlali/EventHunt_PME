package de.pme.eventhunt.view.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.pme.eventhunt.R;
import de.pme.eventhunt.view.ui.core.BaseFragment;

public class homeFragment extends BaseFragment {

    private HomeViewModel homeViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}