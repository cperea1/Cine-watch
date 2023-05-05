package com.example.cinewatch20.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.models.User;
import com.example.cinewatch20.utils.Credentials;
import com.google.firebase.database.FirebaseDatabase;

public class GenderAge extends AppCompatActivity {
    User activeUser;
    Button nextButton;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    String gender;
    EditText age;
    private String userId;
    private final String TAG = "CineWatch - GenderBday";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.age_gender_page);

        userId = getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
        spinner = findViewById(R.id.spinner);
        nextButton = findViewById(R.id.next2);
        age = findViewById(R.id.age);
        activeUser = ((CineWatchApplication)getApplication()).getActiveSessionUser();


        adapter = ArrayAdapter.createFromResource(this,
                R.array.genders, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gender = parent.getItemAtPosition(position).toString();
                Toast.makeText(GenderAge.this, "You selected: " + gender, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gender.equals("Choose Your Gender")) {
                    Toast.makeText(GenderAge.this, "No Gender Selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(age.getText().toString())){
                    Toast.makeText(GenderAge.this, "Enter age ", Toast.LENGTH_SHORT).show();
                    return;
                }

                int Age = Integer.parseInt(age.getText().toString());

                activeUser.setGender(gender);
                activeUser.setAge(Age);

                FirebaseDatabase.getInstance().getReference("Users")
                        .child(userId)
                        .setValue(activeUser).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(GenderAge.this,"User has been updated successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), Subscriptions.class);
                                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                                startActivity(intent);
                            }
                            else{
                                Log.e(TAG, "Unable to update database with user details: "+task1.getException());
                                Toast.makeText(GenderAge.this,"Failed to update! Try again with different information!",Toast.LENGTH_LONG).show();
                                return;
                            }

                        });





            }
        });


    } //end onCreate
} //end class