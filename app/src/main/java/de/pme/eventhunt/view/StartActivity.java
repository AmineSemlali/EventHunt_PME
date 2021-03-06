package de.pme.eventhunt.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import de.pme.eventhunt.R;

public class StartActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null)
        {
            startActivity(new Intent(this, MainActivity.class));
        }

    }
}