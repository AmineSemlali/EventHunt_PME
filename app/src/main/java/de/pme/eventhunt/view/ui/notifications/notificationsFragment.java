package de.pme.eventhunt.view.ui.notifications;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        // Create Layout/Views
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Get View Model
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);


        // Get RecyclerView Reference
        RecyclerView notificationView = root.findViewById(R.id.list_view_notifications);

        // Create Adapter
        final notificationAdapter adapter = new notificationAdapter(this.requireActivity());

        // Configure RecyclerView with Adapter and LayoutManager
        notificationView.setAdapter( adapter );
        notificationView.setLayoutManager( new LinearLayoutManager(this.requireActivity() ) );
        //notificationsViewModel.getNotifications().observe(this.requireActivity(), adapter::setNotifications); // requires livedata??
        return root;
    }
}