package com.example.cinewatch20.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.models.User;
import com.example.cinewatch20.utils.Credentials;

public class Account extends AppCompatActivity {



     ConstraintLayout mConstraintLayout;
     View mView;
     Button mBackButton;
     TextView mTextView;
     Button mLogoutButton;
     CardView search_movies_button, updateSubs, likedMoviesButton;
     ConstraintLayout mCardConstraintLayout;
     TextView firstname_lastname;
     TextView user_name;
     Button mBookmarksButton;
     Button mEditProfileButton;
     Button mProfileButton;
     LinearLayout mLinearLayout;
     String userId;
     User activeUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_page);



        mView = findViewById(R.id.view);
        mBackButton = findViewById(R.id.backB);
        mTextView = findViewById(R.id.textView);
        mLogoutButton = findViewById(R.id.logout);


        //cards in center
        search_movies_button = findViewById(R.id.search_movies_button);
        updateSubs = findViewById(R.id.updatesubs);
        likedMoviesButton = findViewById(R.id.liked_movies_button);

        firstname_lastname = findViewById(R.id.firstname_lastname);
        user_name = findViewById(R.id.user_name);
        mBookmarksButton = findViewById(R.id.bookmarks);
        mEditProfileButton = findViewById(R.id.editProfileB);
        mProfileButton = findViewById(R.id.profileB);
        mLinearLayout = findViewById(R.id.linearLayout);

        userId = this.getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
        activeUser = ((CineWatchApplication)getApplication()).getActiveSessionUser();

        firstname_lastname.setText(activeUser.getName());
        user_name.setText(activeUser.getUsername());



        // add any listeners or custom logic as needed

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform your action here

                Intent intent = new Intent(Account.this, Swipe.class);
                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                startActivity(intent);

            }
        });


        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // log out

                //temporary
                Toast.makeText(Account.this,"sign out",Toast.LENGTH_SHORT).show();

                //use this sign out code for later!!
                ((CineWatchApplication) getApplication()).setActiveSessionUser(null);
                Intent intent = new Intent(Account.this, LoginScreen.class);
                intent.putExtra("logged out", true);
                startActivity(intent);

            }
        });

        mBookmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to bookmarks page

                Intent intent = new Intent(Account.this, Bookmarks.class);
                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                startActivity(intent);

            }
        });

        search_movies_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Search.class);
                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                startActivity(intent);
            }
        });

        updateSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, Subscriptions.class);
                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                startActivity(intent);
            }
        });

        likedMoviesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, LikedMovies.class);
                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                startActivity(intent);
            }
        });



    }


}