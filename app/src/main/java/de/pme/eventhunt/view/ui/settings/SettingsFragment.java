package de.pme.eventhunt.view.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.List;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.User;
import de.pme.eventhunt.model.documents.UserSettings;
import de.pme.eventhunt.model.repositories.UserSettingsRepository;


public class SettingsFragment extends Fragment {

    View view;

    private Switch locationSwitch;
    private Switch nameSwitch;
    private Switch emailSwitch;
    private Switch ageSwitch;

    private UserSettings userSettings;
    private UserSettingsRepository userSettingsRepository;
    FirebaseFirestore db;
    FirebaseAuth auth;
    Context context;

    private SettingsViewModel settingsViewModel;
    NavController navController;


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userSettingsRepository = new UserSettingsRepository();
        context = getContext();

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();

        locationSwitch = view.findViewById(R.id.switchLocation);
        nameSwitch = view.findViewById(R.id.switchName);
        emailSwitch = view.findViewById(R.id.switchEmail);
        ageSwitch = view.findViewById(R.id.switchAge);

        String userId = auth.getCurrentUser().getUid();

        if(userId != null && !userId.isEmpty())
        {
            db.collection("userSettings").whereEqualTo("userId",userId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                   List<UserSettings> userSettingsList = task.getResult().toObjects(UserSettings.class);
                                                   userSettings = userSettingsList.get(0);

                                                   if (userSettings.getShowAge())
                                                   {
                                                       ageSwitch.setChecked(true);
                                                   }
                                                   else
                                                   {
                                                       ageSwitch.setChecked(false);
                                                   }

                                                   if (userSettings.getShowLocation())
                                                   {
                                                       locationSwitch.setChecked(true);
                                                   }
                                                   else
                                                   {
                                                       locationSwitch.setChecked(false);
                                                   }

                                                   if (userSettings.getShowEmail())
                                                   {
                                                       emailSwitch.setChecked(true);
                                                   }
                                                   else
                                                   {
                                                       emailSwitch.setChecked(false);
                                                   }

                                                   if (userSettings.getShowName())
                                                   {
                                                       nameSwitch.setChecked(true);
                                                   }
                                                   else
                                                   {
                                                       nameSwitch.setChecked(false);
                                                   }
                                               }
                    });


            nameSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked){
                    if (checked) {
                        userSettings.setShowName(true);
                        userSettingsRepository.updateUserSettings(userSettings);
                    }
                    else
                        userSettings.setShowName(false);
                        userSettingsRepository.updateUserSettings(userSettings);
                }
            });

            emailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked){
                    if (checked) {
                        userSettings.setShowEmail(true);
                        userSettingsRepository.updateUserSettings(userSettings);
                    }
                    else
                        userSettings.setShowEmail(false);
                    userSettingsRepository.updateUserSettings(userSettings);
                }
            });

            locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked){
                    if (checked) {
                        userSettings.setShowLocation(true);
                        userSettingsRepository.updateUserSettings(userSettings);
                    }
                    else
                        userSettings.setShowLocation(false);
                    userSettingsRepository.updateUserSettings(userSettings);
                }
            });

            ageSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked){
                    if (checked) {
                        userSettings.setShowAge(true);
                        userSettingsRepository.updateUserSettings(userSettings);
                    }
                    else
                        userSettings.setShowAge(false);
                    userSettingsRepository.updateUserSettings(userSettings);
                }
            });
        }


        // Inflate the layout for this fragment
        return view;
    }
}


