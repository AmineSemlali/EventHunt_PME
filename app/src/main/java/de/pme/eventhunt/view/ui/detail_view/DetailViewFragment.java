package de.pme.eventhunt.view.ui.detail_view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.model.utilities.EventLocation;
import de.pme.eventhunt.model.utilities.Image;
import de.pme.eventhunt.view.ui.core.BaseFragment;
import de.pme.eventhunt.view.ui.utilities.DateAndTime;


public class DetailViewFragment extends BaseFragment {

    View view;
    Context context;

    FirebaseFirestore db;
    FirebaseAuth auth;
    NavController navController;

    Boolean isJoined = false;


    DetailViewViewModel detailViewViewModel;

    ImageView eventImage;
    TextView captionTitleTextView;
    TextView captionLocationTextView;

    Button joinButton;
    ImageView locationButton;
    ImageView shareButton;
    ImageView chatButton;
    ImageView editButton;

    TextView startDateTextView;
    TextView endDateTextView;
    TextView locationStreetTextView;
    TextView locationCityTextView;
    TextView descriptionTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_detail_view, container, false);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        context = getContext();
        detailViewViewModel = this.getViewModel(DetailViewViewModel.class);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)fragmentManager.findFragmentById(R.id.nav_host_fragment_main);
        navController = navHostFragment.getNavController();

        eventImage = view.findViewById(R.id.imageViewEventImage);
        captionTitleTextView = view.findViewById(R.id.textViewTitleDetail);
        captionLocationTextView = view.findViewById(R.id.textViewCityDetail);

        joinButton = view.findViewById(R.id.buttonJoinEvent);
        locationButton = view.findViewById(R.id.buttonOpenMapsLocation);
        shareButton = view.findViewById(R.id.buttonShareEvent);
        chatButton = view.findViewById(R.id.buttonOpenChat);
        editButton = view.findViewById(R.id.buttonEditEvent);

        startDateTextView = view.findViewById(R.id.textViewDateStartDetail);
        endDateTextView = view.findViewById(R.id.textViewDateEndDetail);
        locationStreetTextView = view.findViewById(R.id.textViewLocationStreetDetail);
        locationCityTextView = view.findViewById(R.id.textViewLocationCityDetail);
        descriptionTextView = view.findViewById(R.id.textViewDescriptionDetail);

        /* assert savedInstanceState != null;
        String eventId = savedInstanceState.getString("eventId");*/
        String eventId = getArguments().getString("eventId");

        if(eventId != null && !eventId.isEmpty())
        {
            db.collection("event").whereEqualTo("eventId", eventId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            List<Event> events = task.getResult().toObjects(Event.class);
                            Event event = events.get(0);

                            EventLocation eventLocation = event.getLocation();

                            Picasso.get()
                                    .load(event.getImageLargeRef())
                                    .into(eventImage);

                            captionTitleTextView.setText(event.getTitle());

                            try {
                                captionLocationTextView.setText(eventLocation.getCityCountryString(context));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            DateAndTime startDate = new DateAndTime();
                            DateAndTime endDate = new DateAndTime();
                            startDate.setDateAndTime(LocalDateTime.parse(event.getStartTime()));
                            endDate.setDateAndTime(LocalDateTime.parse(event.getEndTime()));

                            startDateTextView.setText(startDateTextView.getText() + startDate.formatString());
                            endDateTextView.setText(endDateTextView.getText() + endDate.formatString());


                            try {
                                locationStreetTextView.setText(eventLocation.getStreetString(context));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            try {
                                locationCityTextView.setText(eventLocation.getCityString(context));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            descriptionTextView.setText(event.getDescription());

                            db.collection("eventUser").whereEqualTo("eventId", eventId)
                                    .whereEqualTo("userId", auth.getCurrentUser().getUid())
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    List<EventUser> eventUser = task.getResult().toObjects(EventUser.class);
                                    if(!eventUser.isEmpty())
                                    {
                                        makeButtonJoined();
                                        isJoined = true;
                                    }
                                    else joinButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(!isJoined)
                                            {
                                                EventUser newEventUser = new EventUser();
                                                newEventUser.setUserId(auth.getCurrentUser().getUid());
                                                newEventUser.setEventId(eventId);
                                                newEventUser.setJoinedAt(LocalDateTime.now().toString());

                                                db.collection("eventUser").document().set(newEventUser);
                                                isJoined = true;
                                                makeButtonJoined();
                                            }
                                        }
                                    });
                                }
                            });

                            if(auth.getCurrentUser().getUid().equals(event.getCreatorId()))
                            {
                                editButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //go to edit fragment
                                    }
                                });
                            }
                            else
                            {
                                editButton.setVisibility(View.INVISIBLE);
                            }

                            locationButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EventLocation eventLocation = event.getLocation();

                                    Uri navigationIntentUri =
                                            Uri.parse("google.navigation:q=" + eventLocation.getLatitude()
                                                                    + "," + eventLocation.getLongitude());

                                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                                    mapIntent.setPackage("com.google.android.apps.maps");
                                    try
                                    {
                                        startActivity(mapIntent);
                                    }
                                    catch(ActivityNotFoundException ex)
                                    {
                                        try
                                        {
                                            Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                                            startActivity(unrestrictedIntent);
                                        }
                                        catch(ActivityNotFoundException innerEx)
                                        {
                                            Toast.makeText(context, "Please install a maps application", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });


                            chatButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // navigate to chat fragment
                                }
                            });
                        }
                    });



        }

        return view;
    }

    void makeButtonJoined()
    {
        joinButton.setBackgroundColor(getResources().getColor(R.color.tertiaryColor_red));
        joinButton.setText("Joined");
    }
}