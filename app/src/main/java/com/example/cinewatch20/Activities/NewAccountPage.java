package com.example.cinewatch20.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.models.User;

import com.example.cinewatch20.utils.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class NewAccountPage extends AppCompatActivity {
    String TAG = "New Account Page";

    User user;
    Button buttonSignup;
    private FirebaseAuth mAuth;
    EditText regName, regUsername, regEmail, regPassword;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Animation genderanim, accountanim;
    LinearLayout genderBday, newAccount;




    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //hooks to xml
        regName = findViewById(R.id.name);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPassword = findViewById(R.id.password);
        buttonSignup = findViewById(R.id.next);


        mAuth = FirebaseAuth.getInstance();



        buttonSignup.setOnClickListener(view -> {
                rootNode = FirebaseDatabase.getInstance();
                reference=rootNode.getReference("users");

                //Get all the values

                String name = regName.getText().toString();
                String username = regUsername.getText().toString();
                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();

            if (TextUtils.isEmpty(name)){
                Toast.makeText(NewAccountPage.this, "Enter name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(username)){
                Toast.makeText(NewAccountPage.this, "Enter username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(email)){
                Toast.makeText(NewAccountPage.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)){
                Toast.makeText(NewAccountPage.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(NewAccountPage.this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }


                //UserHelper helperClass = new UserHelper(name, username, email, password);
                //reference.child("user").setValue(helperClass);


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            user = new User(userId, name, username,email,password);
                            Log.v(TAG, "Just Created User: " + user);

                            ((CineWatchApplication)getApplication()).setActiveSessionUser(user);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(userId)
                                    .setValue(user).addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            Toast.makeText(NewAccountPage.this,"User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Log.e(TAG, "Unable to update database with user details: "+task1.getException());
                                            Toast.makeText(NewAccountPage.this,"Failed to register! Try again with different email!",Toast.LENGTH_LONG).show();
                                        }

                                    });

                            Toast.makeText(NewAccountPage.this, "Account Created.",
                                    Toast.LENGTH_SHORT).show();


                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(NewAccountPage.this, "Email already used",
                                    Toast.LENGTH_SHORT).show();
                            return;

                        }

                        Intent intent =new Intent(getApplicationContext(), GenderAge.class);
                        intent.putExtra(Credentials.ACTIVE_USER_KEY, user.getId());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_account, R.anim.slide_gender);
                    });


            //animation here





        });//Register Button method end









    }//onCreate Method End




    } // end onCreate

