package de.pme.eventhunt.view.ui.registration;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.User;
import de.pme.eventhunt.model.repositories.UserRepository;


public class RegistrationFragment extends Fragment {

    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private Button registrationButton;

    //firebase
    private FirebaseAuth auth;
    FirebaseFirestore db;
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
        userRepository = new UserRepository();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registation, container, false);

        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_start);
        navController = navHostFragment.getNavController();


        auth = FirebaseAuth.getInstance();

        email = view.findViewById(R.id.email_registration);
        password = view.findViewById(R.id.password_registration);
        firstName = view.findViewById(R.id.firstName_registration);
        lastName = view.findViewById(R.id.lastName_registration);
        registrationButton = view.findViewById(R.id.finish_registration);


        registrationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_firstName = firstName.getText().toString();
                String txt_lastName = lastName.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)
                        || TextUtils.isEmpty(txt_firstName) || TextUtils.isEmpty(txt_lastName))
                {
                    Toast.makeText(getActivity(), "Empty credentials!", Toast.LENGTH_SHORT).show();
                }
                else if(txt_password.length() < 6)
                {
                    Toast.makeText(getActivity(), "Password too short!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    registerUser(txt_email, txt_password, txt_firstName, txt_lastName);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

        private void registerUser(String email, String password, String firstName, String lastName) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User user = new User();
                        user.setId(auth.getCurrentUser().getUid());
                        user.setEmail(email);
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        userRepository.createUser(user);
                        Toast.makeText(getActivity(), "Registering user succesful!", Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_registationFragment_to_loginFragment);
                    } else {
                        Toast.makeText(getActivity(), "Registering user failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
}