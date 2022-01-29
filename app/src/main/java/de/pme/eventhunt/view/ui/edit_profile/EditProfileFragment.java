package de.pme.eventhunt.view.ui.edit_profile;

import static android.provider.CallLog.Locations.LATITUDE;
import static android.provider.CallLog.Locations.LONGITUDE;

import android.Manifest;
import android.app.Activity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.adevinta.leku.LocationPickerActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.User;
import de.pme.eventhunt.model.repositories.UserRepository;
import de.pme.eventhunt.model.utilities.EmailAndPasswordValidator;
import de.pme.eventhunt.model.utilities.EventLocation;
import de.pme.eventhunt.model.utilities.Image;
import de.pme.eventhunt.model.utilities.UserLocation;
import de.pme.eventhunt.view.ui.registration.RegistrationViewModel;
import de.pme.eventhunt.view.ui.utilities.Date;
import de.pme.eventhunt.view.ui.utilities.DateAndTime;
import de.pme.eventhunt.view.ui.utilities.DatePickerClass;



public class EditProfileFragment extends Fragment {

    Context context;
    Activity activity;

    private static final int PICK_IMAGE = 1;
    private final static int PLACE_PICKER_REQUEST = 2;

    View view;
    UserLocation userLocation;
    Image userImage;
    private Boolean imageAdjusted = false;
    Uri imageUri;
    double lastLongitude = 0.0;
    double lastLatitude = 0.0;
    String dobString;
    User userRepo;
    boolean firstNameChanged = false,
            lastNameChanged = false,
            locationChanged = false,locationTouched = false,
            emailChanged = false,
            dateOfBirthChanged = false, dateOfBirthTouched = false ,
            imageTouched = false, imageChanged = false;


    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText oldPassword;
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private Button editButton;
    private ImageView getUserImageView;
    private TextInputEditText dateOfBirth;
    private TextInputEditText locationEditText;

    //firebase

    private FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    private EditProfileViewModel editProfileViewModel;
    NavController navController;
    UserRepository userRepository;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        activity = getActivity();

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        userRepository = new UserRepository();


        email = view.findViewById(R.id.email_registration);
        password = view.findViewById(R.id.password_registration);
        oldPassword = view.findViewById(R.id.old_password);
        firstName = view.findViewById(R.id.firstName_registration);
        lastName = view.findViewById(R.id.lastName_registration);
        editButton = view.findViewById(R.id.finish_edit);
        dateOfBirth = view.findViewById(R.id.editTextDateOfBirth);
        getUserImageView = view.findViewById(R.id.imageViewGetImage);
        locationEditText = view.findViewById(R.id.editTextLocation);

        userImage = new Image();

        DatePickerClass datePickerDOB = new DatePickerClass(context, dateOfBirth);
        String currentUserId = auth.getUid();


        if (currentUserId != null && !currentUserId.isEmpty()) {
            db.collection("user").whereEqualTo("id", currentUserId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<User> users = task.getResult().toObjects(User.class);
                            userRepo = users.get(0);

                            Picasso.get()
                                    .load(userRepo.getImageSmallRef())
                                    .into(getUserImageView);

                            firstName.setText(userRepo.getFirstName());
                            lastName.setText(userRepo.getLastName());
                            email.setText(userRepo.getEmail());
                            UserLocation userLocation = userRepo.getLocation();
                            try {
                                locationEditText.setText(userLocation.getLocationString(context));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Date dateBirth = new Date();
                            dateBirth.setDate(LocalDate.parse(userRepo.getDateOfBirth()));
                            dateOfBirth.setText(dateOfBirth.getText() + dateBirth.formatString());
                        }

                    });


        getUserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
                imageTouched = true;
            }
        });

        locationEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent user) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (user.getAction() == MotionEvent.ACTION_UP) {
                    if (user.getRawX() >= (locationEditText.getRight() - locationEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        pickLocation(view);
                        locationTouched = true;
                        return true;
                    }
                }
                return false;
            }
        });

        dateOfBirth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent user) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (user.getAction() == MotionEvent.ACTION_UP) {
                    if (user.getRawX() >= (dateOfBirth.getRight() - dateOfBirth.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        datePickerDOB.pickDate();
                        dateOfBirthTouched = true;
                        return true;
                    }
                }
                return false;
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (email == null || email.toString().isEmpty() ||
                        password == null || password.toString().isEmpty() || oldPassword == null || oldPassword.toString().isEmpty() ||
                        firstName == null || firstName.toString().isEmpty() ||
                        lastName == null || lastName.toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Empty credentials!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();
                    String txt_oldPassword = oldPassword.getText().toString();
                    String txt_firstName = firstName.getText().toString();
                    String txt_lastName = lastName.getText().toString();

                    EmailAndPasswordValidator checkPass = new EmailAndPasswordValidator();

                    if(!checkPass.isPasswordValid(txt_password))
                    {
                        Toast.makeText(getActivity(), "Password conditions not fulfilled!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(!checkPass.isEmailValid(txt_email))
                    {
                        Toast.makeText(getActivity(), "Email conditions not fulfilled!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else updateUser(txt_email, txt_password,txt_oldPassword, txt_firstName, txt_lastName,datePickerDOB,userLocation);
                }
            }
        });
        }
        // Inflate the layout for this fragment
        return view;
    }

    private void getImage() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }
    private void updateUser(String email, String password, String oldPassword, String firstName, String lastName, DatePickerClass datePickerDob, UserLocation location) {


                    //Check location
                    if (userLocation == null && locationTouched) {
                        Toast.makeText(context, "No location set!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(dateOfBirthTouched) {
                        //Check date of birth
                        Date dob = datePickerDob.getDate();

                        if (dob.day.isEmpty() || dob.month.isEmpty() || dob.year.isEmpty()) {
                            Toast.makeText(context, "Please select a date of birth!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        LocalDate dobLD = dob.toLocalDate();

                        if (dobLD.compareTo(LocalDate.now()) >= 0) {
                            Toast.makeText(context, "Date of Birth has to be  before now!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dobString = dob.toLocalDateString();
                        dateOfBirthChanged = true;
                    }

                    // Check image
                    if (imageUri == null && imageTouched ) {
                        Toast.makeText(context, "Please select an image!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        String userId = auth.getCurrentUser().getUid();
                        Task<QuerySnapshot> userQuery;
                        userQuery = db.collection("user").whereEqualTo("id", userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<User> users = queryDocumentSnapshots.toObjects(User.class);
                                userRepo = users.get(0);
                                String oldEmail = userRepo.getEmail();

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(oldEmail, oldPassword);

                                user.reauthenticate(credential)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d("Re-authentication", "User re-authenticated.");

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                user.updateEmail(email)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d("Email updated", "User email address updated.");

                                                                }
                                                            }
                                                        });
                                                user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("Email updated", "User email address updated.");

                                                        }
                                                    }
                                                });
                                            }
                                        });


                                userRepository.updateUser(userRepo, email, firstName, lastName);

                                if(imageChanged) {
                                    userImage.editProfileImage(userRepo, activity);
                                }
                                if(locationChanged)
                                {
                                    userRepository.updateLocation(userRepo,userLocation);
                                }
                                if(dateOfBirthChanged)
                                {
                                    userRepository.updateDateOfBirth(userRepo,dobString);
                                }
                                Toast.makeText(activity, "updating user successful!", Toast.LENGTH_SHORT).show();
                            }
                        });



                        navController.navigate(R.id.action_edit_profile_to_navigation_home);

                    }
                }


    void pickLocation(View view) {



        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        lastLongitude = location.getLongitude();
                        lastLatitude = location.getLatitude();
                    }

                    Intent locationPickerIntent = new LocationPickerActivity.Builder()
                            .withLocation(lastLatitude, lastLongitude)
                            .withGeolocApiKey("AIzaSyBOYMpJcVgIx7_kx-Oi7jHICLUuAdG9g8s")
                            .withGooglePlacesApiKey("AIzaSyBOYMpJcVgIx7_kx-Oi7jHICLUuAdG9g8s")
                            .shouldReturnOkOnBackPressed()
                            .withCityHidden()
                            .withZipCodeHidden()
                            .withSatelliteViewHidden()
                            .withGoogleTimeZoneEnabled()
                            .withVoiceSearchHidden()
                            .withUnnamedRoadHidden()
                            .build(context);

                    startActivityForResult(locationPickerIntent, PLACE_PICKER_REQUEST);
                });


    }


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== PICK_IMAGE && resultCode==Activity.RESULT_OK)
        {
            assert data != null;
            imageUri = data.getData();
            userImage.CreateBitmapSmall(imageUri);
            userImage.CreateBitmapLarge(imageUri, getUserImageView);

            if(!imageAdjusted)
            {
                imageAdjusted = true;
                DisplayMetrics metrics = new DisplayMetrics();
                float logicalDensity = metrics.density;
                getUserImageView.getLayoutParams().width = (int) ((int) 250 * context.getResources().getDisplayMetrics().density);
                getUserImageView.getLayoutParams().height = (int) ((int) 175 * context.getResources().getDisplayMetrics().density);
                getUserImageView.requestLayout();
                imageChanged = true;
            }
        }
        else if (resultCode == Activity.RESULT_OK && data != null && requestCode == PLACE_PICKER_REQUEST) {
            Log.d("RESULT****", "OK");

            double latitude = data.getDoubleExtra(LATITUDE, 0.0);
            Log.d("LATITUDE****", latitude+"");
            double longitude = data.getDoubleExtra(LONGITUDE, 0.0);
            Log.d("LONGITUDE****", longitude+"");

            userLocation = new UserLocation(latitude, longitude);

            try {
                locationEditText.setText(userLocation.getLocationString(context));
                locationChanged = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED");
        }
    }




}
