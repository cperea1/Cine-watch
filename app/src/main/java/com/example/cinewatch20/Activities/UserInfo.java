package com.example.cinewatch20.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.models.User;

import com.example.cinewatch20.utils.Credentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class UserInfo extends AppCompatActivity {
    String TAG = "New Account Page";
    User activeUser;
    Button buttonSignup;
    private FirebaseAuth mAuth;
    EditText regName, regUsername, regEmail, regPassword;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    TextView update_info;
    boolean fromAccount;
    private String userId;


    @SuppressLint({"MissingInflatedId", "ResourceType", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        fromAccount = getIntent().getBooleanExtra("from account", false);

        //hooks to xml
        regName = findViewById(R.id.name);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPassword = findViewById(R.id.password);
        buttonSignup = findViewById(R.id.next);
        update_info = findViewById(R.id.update_info);


        mAuth = FirebaseAuth.getInstance();

        if (fromAccount) {
            userId = this.getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
            activeUser = ((CineWatchApplication)getApplication()).getActiveSessionUser();
            update_info.setText("Update User Info");
            regName.setText(activeUser.getName());
            regEmail.setText(activeUser.getEmail());
            regPassword.setText(activeUser.getPassword());
            regUsername.setText(activeUser.getUsername());

        }



        buttonSignup.setOnClickListener(view -> {
                rootNode = FirebaseDatabase.getInstance();
                reference=rootNode.getReference("users");

                //Get all the values

                String name = regName.getText().toString();
                String username = regUsername.getText().toString();
                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();

            if (TextUtils.isEmpty(name)){
                Toast.makeText(UserInfo.this, "Enter name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(username)){
                Toast.makeText(UserInfo.this, "Enter username", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(email)){
                Toast.makeText(UserInfo.this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)){
                Toast.makeText(UserInfo.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(UserInfo.this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!fromAccount) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                activeUser = new User(userId, name, username, email, password);
                                Log.v(TAG, "Just Created User: " + activeUser);

                                ((CineWatchApplication) getApplication()).setActiveSessionUser(activeUser);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(userId)
                                        .setValue(activeUser).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(UserInfo.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Log.e(TAG, "Unable to update database with user details: " + task1.getException());
                                                Toast.makeText(UserInfo.this, "Failed to register! Try again with different email!", Toast.LENGTH_LONG).show();
                                            }

                                        });

                                Toast.makeText(UserInfo.this, "Account Created.",
                                        Toast.LENGTH_SHORT).show();


                            }  //end if
                            else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(UserInfo.this, "Email already used",
                                        Toast.LENGTH_SHORT).show();
                                return;

                            }

                            Intent intent = new Intent(getApplicationContext(), GenderAge.class);
                            intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_account, R.anim.slide_gender);
                        });
            } //end if
            else {
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(userId)
                        .setValue(activeUser).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(UserInfo.this,"User has been updated successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), GenderAge.class);
                                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                                intent.putExtra("from account", true);
                                startActivity(intent);
                            } //end if
                            else{
                                Log.e(TAG, "Unable to update database with user details: " + task1.getException());
                                Toast.makeText(UserInfo.this,"Failed to update! Try again with different information!",Toast.LENGTH_LONG).show();
                            } //end else

                        });
            } //end else
        });//Register Button method end









    }//onCreate Method End




    } // end onCreate

