package de.pme.eventhunt.view.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import de.pme.eventhunt.R;
import de.pme.eventhunt.view.MainActivity;
import de.pme.eventhunt.view.StartActivity;
import de.pme.eventhunt.view.ui.core.BaseFragment;

public class LoginFragment extends BaseFragment {

    // environmental Variables
    private FirebaseAuth auth;
    LoginViewModel loginViewModel;
    NavController navController;
    Context context;

    // input fields & buttons
    Button toRegistrationButton;
    Button loginButton;
    TextInputEditText email;
    TextInputEditText password;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // initialize variables
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        context = getContext();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_start);
        navController = navHostFragment.getNavController();



        auth = FirebaseAuth.getInstance();

        email = view.findViewById(R.id.email_login);
        password = view.findViewById(R.id.password_login);
        toRegistrationButton = view.findViewById(R.id.button_to_registration);
        loginButton = view.findViewById(R.id.finish_login);


// onclick listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email, txt_password;
                if(!(email.getText() == null) && !(email.getText().toString().isEmpty()))
                {
                    txt_email = email.getText().toString();
                }
                else
                {
                    Toast.makeText(context, "E-Mail missing!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!(password.getText() == null) && !(password.getText().toString().isEmpty()))
                {
                    txt_password = password.getText().toString();
                }
                else
                {
                    Toast.makeText(context, "Password missing!", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(txt_email, txt_password);
            }


        });

        toRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_registationFragment);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // login after data verification
    private void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getActivity(), "Succesfully logged in", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Login unsuccessful!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}