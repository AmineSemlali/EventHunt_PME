package de.pme.eventhunt.view.ui.notifications;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.pme.eventhunt.R;
import de.pme.eventhunt.view.ui.core.BaseFragment;


public class notificationsFragment extends BaseFragment {

   private NotificationsViewModel notificationsViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }
}