package de.pme.eventhunt.view.ui.notifications;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.repositories.EventRepository;
import de.pme.eventhunt.model.repositories.NotificationRepository;
import de.pme.eventhunt.view.ui.core.BaseFragment;
import de.pme.eventhunt.view.ui.create_event.CreateEventViewModel;
import de.pme.eventhunt.view.ui.login.LoginViewModel;


public class notificationsFragment extends Fragment {

    NotificationsViewModel notificationsViewModel;
    private NavController navController;
    private NavHostFragment navHostFragment;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        // Create Layout/Views
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Context context = getContext();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();

        // Get View Model
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);


        // Get RecyclerView Reference
        RecyclerView notificationView = root.findViewById(R.id.list_view_notifications);

        // Create Adapter
        final notificationAdapter adapter = new notificationAdapter(context);

        // Configure RecyclerView with Adapter and LayoutManager
        notificationView.setAdapter( adapter );
        notificationView.setLayoutManager( new LinearLayoutManager(context ));
        //notificationsViewModel.getNotifications().observe(this.requireActivity(), adapter::setNotifications); // requires livedata??
        notificationsViewModel.setAdapterList(adapter);
        return root;
    }
}