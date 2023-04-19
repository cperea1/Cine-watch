package com.example.cinewatch20.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinewatch20.R;
import com.example.cinewatch20.utils.Credentials;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity {

    ImageView image;
    TextView title, slogan, name;
    //TextInputLayout username,pass;
    EditText editTextEmail, editTextPassword;
    Button buttonLogin, buttonSignup;

    private FirebaseAuth mAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();
        //Login button
        buttonLogin = findViewById(R.id.login_button);
        //my new account page the signup.
        buttonSignup = findViewById(R.id.sign_up);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        //Hooks for animations


        //New account Button clicker
        buttonSignup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginScreen.this, NewAccountPage.class);
            startActivity(intent);
            finish();

        });//end setOnClickListener


        //Login Button Clicker
        buttonLogin.setOnClickListener(view -> {
            String email, password;
            email = editTextEmail.getText().toString();
            password = editTextPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(LoginScreen.this, "Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginScreen.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            //firebase authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(), Swipe.class);
                            i.putExtra(Credentials.ACTIVE_USER_KEY, mAuth.getCurrentUser().getUid());
                            startActivity(i);

                        } else {
                            Toast.makeText(LoginScreen.this, "Authentication failed. Please Sign up ",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), NewAccountPage.class);
                            startActivity(i);

                        }
                    });
        });
    }

    @Override
    public void onStart() {
        super.onStart();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            Intent intent = new Intent(getApplicationContext(), Swipe.class);
//            startActivity(intent);
        }//end On Start
    //}
}


