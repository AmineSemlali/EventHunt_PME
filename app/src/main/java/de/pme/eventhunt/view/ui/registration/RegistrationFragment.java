package de.pme.eventhunt.view.ui.registration;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.User;
import de.pme.eventhunt.model.repositories.UserRepository;
import de.pme.eventhunt.model.utilities.EventLocation;
import de.pme.eventhunt.model.utilities.Image;
import de.pme.eventhunt.model.utilities.PasswordValidator;
import de.pme.eventhunt.model.utilities.UserLocation;
import de.pme.eventhunt.view.ui.utilities.Date;
import de.pme.eventhunt.view.ui.utilities.DatePickerClass;


public class RegistrationFragment extends Fragment {


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

    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private Button registrationButton;
    private ImageView getUserImageView;
    private TextInputEditText dateOfBirth;
    TextInputEditText locationEditText;

    //firebase
    private FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseStorage storage;
    private RegistrationViewModel registrationViewModel;
    NavController navController;
    UserRepository userRepository;





    public RegistrationFragment() {
        // Required empty public constructor
    }

    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

        view = inflater.inflate(R.layout.fragment_registation, container, false);

        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_start);
        navController = navHostFragment.getNavController();


        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        userRepository = new UserRepository();

        email = view.findViewById(R.id.email_registration);
        password = view.findViewById(R.id.password_registration);
        firstName = view.findViewById(R.id.firstName_registration);
        lastName = view.findViewById(R.id.lastName_registration);
        registrationButton = view.findViewById(R.id.finish_registration);
        dateOfBirth = view.findViewById(R.id.editTextDateOfBirth);
        getUserImageView = view.findViewById(R.id.imageViewGetImage);
        locationEditText = view.findViewById(R.id.editTextLocation);

        userImage = new Image();

        DatePickerClass datePickerDOB = new DatePickerClass(context, dateOfBirth);




        getUserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getImage(); }
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
                        return true;
                    }
                }
                return false;
            }
        });



        registrationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (email == null || email.toString().isEmpty() ||
                        password == null || password.toString().isEmpty() ||
                        firstName == null || firstName.toString().isEmpty() ||
                        lastName == null || lastName.toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Empty credentials!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();
                    String txt_firstName = firstName.getText().toString();
                    String txt_lastName = lastName.getText().toString();

                    if(txt_password.length() < 6)
                    {
                        Toast.makeText(getActivity(), "Password too short!", Toast.LENGTH_SHORT).show();
                    }
//                    PasswordValidator checkpass = new PasswordValidator();
//                    checkpass.isValid(txt_password);
                    else registerUser(txt_email, txt_password, txt_firstName, txt_lastName,datePickerDOB,userLocation);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void getImage() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }


        private void registerUser(String email, String password, String firstName, String lastName, DatePickerClass datePickerDob, UserLocation location) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        //Check location
                        if (userLocation == null) {
                            Toast.makeText(context, "No location set!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //Check start & end date
                        Date dob = datePickerDob.getDate();

                        if (dob.day.isEmpty() || dob.month.isEmpty() || dob.year.isEmpty()) {
                            Toast.makeText(context, "Please select a start date and time!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        LocalDate dobLD = dob.toLocalDate();

                        if (dobLD.compareTo(LocalDate.now()) >= 0) {
                            Toast.makeText(context, "Date of Birth has to be  before now!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String dobString = dob.toLocalDateString();

                        // Check image
                        if (imageUri == null) {
                            Toast.makeText(context, "Please select an image!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            String userId = auth.getCurrentUser().getUid();
                            User user = new User(userId, email, firstName, lastName, dobString, userLocation, userImage.getDownloadUrlSmall());

                            userImage.UploadProfileImage(user, activity);


                            navController.navigate(R.id.action_registationFragment_to_loginFragment);

                        }
                    }
            };
        });
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED");
        }
    }




}