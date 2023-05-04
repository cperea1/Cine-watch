package com.example.cinewatch20.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.models.User;
import com.example.cinewatch20.utils.Credentials;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Subscriptions extends AppCompatActivity {
    private final String TAG = "CineWatch - Subscriptions";

    User activeUser;
    String userId;

    CheckBox netflix;
    CheckBox hulu;
    CheckBox disney;
    CheckBox paramount;
    CheckBox primevideo;
    CheckBox crunchyroll;
    CheckBox HBOMax;
    CheckBox peacock;
    CheckBox showtime;
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscriptions);

        userId = getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
        activeUser = ((CineWatchApplication)getApplication()).getActiveSessionUser();

        netflix = findViewById(R.id.netflix);
        hulu = findViewById(R.id.hulu);
        disney = findViewById(R.id.disney);
        paramount = findViewById(R.id.paramount);
        primevideo = findViewById(R.id.primevideo);
        crunchyroll = findViewById(R.id.crunchyroll);
        HBOMax = findViewById(R.id.HBOMax);
        peacock = findViewById(R.id.peacock);
        showtime = findViewById(R.id.showtime);

        submit = findViewById(R.id.buttonUpdate);
        
        
        setChecks();

        netflix.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activeUser.addSubscription("Netflix");
                else
                    activeUser.removeSubscription("Netflix");
            }
        });

        hulu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activeUser.addSubscription("Hulu");
                else
                    activeUser.removeSubscription("Hulu");
            }
        });

        disney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activeUser.addSubscription("Disney Plus");
                else
                    activeUser.removeSubscription("Disney Plus");
            }
        });

        paramount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activeUser.addSubscription("Paramount Plus");
                else
                    activeUser.removeSubscription("Paramount Plus");
            }
        });

        primevideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activeUser.addSubscription("Amazon Prime Video");
                else
                    activeUser.removeSubscription("Amazon Prime Video");
            }
        });

        crunchyroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activeUser.addSubscription("Crunchyroll");
                else
                    activeUser.removeSubscription("Crunchyroll");
            }
        });

        showtime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activeUser.addSubscription("Showtime");
                else
                    activeUser.removeSubscription("Showtime");
            }
        });

        HBOMax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activeUser.addSubscription("HBO Max");
                else
                    activeUser.removeSubscription("HBO Max");
            }
        });

        peacock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    activeUser.addSubscription("Peacock Premium");
                else
                    activeUser.removeSubscription("Peacock Premium");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(userId)
                        .setValue(activeUser).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(Subscriptions.this,"User has been updated successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), Account.class);
                                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                                startActivity(intent);
                            }
                            else{
                                Log.e(TAG, "Unable to update database with user details: "+task1.getException());
                                Toast.makeText(Subscriptions.this,"Failed to update! Try again with different information!",Toast.LENGTH_LONG).show();
                                return;
                            }

                        });

                
            }
        });


    } //end onCreate

    private void setChecks() {
        for (int i = 0; i < activeUser.getSubscriptions().size(); i++) {
            switch (activeUser.getSubscriptions().get(i)) {
                case "Netflix":
                    netflix.setChecked(true);
                    break;
                case "Hulu":
                    hulu.setChecked(true);
                    break;
                case "Disney Plus":
                    disney.setChecked(true);
                    break;
                case "Paramount Plus":
                    paramount.setChecked(true);
                    break;
                case "Amazon Prime Video":
                    primevideo.setChecked(true);
                    break;
                case "Crunchyroll":
                    crunchyroll.setChecked(true);
                    break;
                case "HBO Max":
                    HBOMax.setChecked(true);
                    break;
                case "Peacock Premium":
                    peacock.setChecked(true);
                    break;
                case "Showtime":
                    showtime.setChecked(true);
                    break;
            } //end switch
        } //end for
    } //end setChecks
} //end class
