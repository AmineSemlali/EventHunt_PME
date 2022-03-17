package de.pme.eventhunt.view.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;
import java.util.List;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.User;
import de.pme.eventhunt.model.documents.UserSettings;
import de.pme.eventhunt.model.repositories.UserSettingsRepository;
import de.pme.eventhunt.model.utilities.UserLocation;
import de.pme.eventhunt.view.MainActivity;
import de.pme.eventhunt.view.StartActivity;
import de.pme.eventhunt.view.ui.core.BaseFragment;
import de.pme.eventhunt.view.ui.detail_view.DetailViewViewModel;
import de.pme.eventhunt.view.ui.utilities.Date;
import de.pme.eventhunt.view.ui.utilities.DateAndTime;

public class profileFragment extends BaseFragment {

    private ProfileViewModel profileViewModel;
    View view;
    Context context;

    FirebaseFirestore db;
    FirebaseAuth auth;

    UserSettings userSettings;
    UserSettingsRepository userSettingsRepository;
    NavController navController;

    ImageView profileImage;
    TextView nameTextView;
    TextView ageTextView;
    ImageView settingsButton;
    ImageView editProfileButton;
    ImageView buttonLogOut;
    TextView locationTextView;
    TextView emailTextView;
    TextView dateOfBirthTextView;
    private Period period;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        view =  inflater.inflate(R.layout.fragment_profile, container, false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userSettingsRepository = new UserSettingsRepository();
        context = getContext();
        profileViewModel = this.getViewModel(ProfileViewModel.class);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();

        profileImage = view.findViewById(R.id.profileImage);
        nameTextView = view.findViewById(R.id.Name);
        ageTextView = view.findViewById(R.id.Age);
        settingsButton = view.findViewById(R.id.buttonSettingsImage);
        editProfileButton = view.findViewById(R.id.buttonEditProfileImage);
        buttonLogOut = view.findViewById(R.id.buttonLogOutImage);

        locationTextView = view.findViewById(R.id.LocationInput);
        emailTextView = view.findViewById(R.id.EmailInput);
        dateOfBirthTextView = view.findViewById(R.id.DateOfBirthInput);

        String userId = auth.getCurrentUser().getUid();

        if(userId != null && !userId.isEmpty())
        {

            Task<QuerySnapshot> userSettingsQuery;
            userSettingsQuery = db.collection("userSettings").whereEqualTo("userId",userId).get();
            userSettingsQuery.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<UserSettings> userSettingsList = userSettingsQuery.getResult().toObjects(UserSettings.class);
                           userSettings = userSettingsList.get(0);
                           fetch();
                        }

                private void fetch() {
                    db.collection("user").whereEqualTo("id", userId).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    List<User> users = task.getResult().toObjects(User.class);
                                    User user = users.get(0);

                                    UserLocation userLocation = user.getLocation();
                                    if (userSettings.getShowLocation()) {
                                        try {
                                            locationTextView.setText(userLocation.getCityCountryString(context));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        locationTextView.setText(" HIDDEN ! ");
                                    }

                                    Picasso.get()
                                            .load(user.getImageSmallRef())
                                            .into(profileImage);

                                    if (userSettings.getShowAge()) {
                                        Date dateOfBirth = new Date();

                                        dateOfBirth.setDate(LocalDate.parse(user.getDateOfBirth()));
                                        dateOfBirthTextView.setText(dateOfBirthTextView.getText() + dateOfBirth.formatString());

                                        String dobString = user.getDateOfBirth();
                                        LocalDate dobLDT = LocalDate.parse(dobString);
                                        Period period = Period.between(dobLDT, LocalDate.now());
                                        int periodInYears = period.getYears();
                                        String age = String.valueOf(periodInYears);
                                        ;
                                        //  String age = getAge(dobLDT.getYear(), dobLDT.getMonthValue(), dobLDT.getDayOfMonth());
                                        ageTextView.setText(age);
                                    } else {
                                        dateOfBirthTextView.setText(" HIDDEN ! ");
                                        ageTextView.setText(" AGE HIDDEN ! ");
                                    }

                                    if (userSettings.getShowEmail()) {
                                        emailTextView.setText(user.getEmail());
                                    } else {
                                        emailTextView.setText(" HIDDEN ! ");
                                    }
                                    if (userSettings.getShowName()) {
                                        nameTextView.setText(user.getFirstName() + ' ' + user.getLastName());
                                    } else {
                                        nameTextView.setText(" NAME HIDDEN ! ");
                                    }
                }
            });



                                settingsButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        navController.navigate(R.id.action_navigation_profile_to_settings);
                                    }
                                });

                                editProfileButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        navController.navigate(R.id.action_navigation_profile_to_editprofile);
                                    }
                                });

                                buttonLogOut.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        logoutUser();
                                    }
                                });
                            }
                        });

        }

        return view;
    }
    private void logoutUser() {

        auth.signOut();
        startActivity(new Intent(getActivity(), StartActivity.class));
        Toast.makeText(getActivity(), "you are logged out!", Toast.LENGTH_SHORT).show();

            }


    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

}