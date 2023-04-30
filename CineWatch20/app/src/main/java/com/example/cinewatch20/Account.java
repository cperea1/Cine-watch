package com.example.cinewatch20;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
public class Account extends AppCompatActivity {



        private ConstraintLayout mConstraintLayout;
        private View mView;
        private Button mBackButton;
        private TextView mTextView;
        private Button mLogoutButton;
        private CardView mCardView1,mCardView2,mCardView3,mCardView4 ;
        private ConstraintLayout mCardConstraintLayout;
        private TextView mCardTextView1;
        private TextView mCardTextView2;
        private Button mBookmarksButton;
        private Button mEditProfileButton;
        private Button mProfileButton;
        private LinearLayout mLinearLayout;
 @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.account_page);



            mView = findViewById(R.id.view);
            mBackButton = findViewById(R.id.backB);
            mTextView = findViewById(R.id.textView);
            mLogoutButton = findViewById(R.id.logout);

            //cards in center
            mCardView1 = findViewById(R.id.contributeCard);
            mCardView2 = findViewById(R.id.updatesubs);
            mCardView3 = findViewById(R.id.helpCard);
            mCardView4 = findViewById(R.id.settingsCard);

            mCardTextView1 = findViewById(R.id.textView2);
            mCardTextView2 = findViewById(R.id.textView3);
            mBookmarksButton = findViewById(R.id.bookmarks);
            mEditProfileButton = findViewById(R.id.editProfileB);
            mProfileButton = findViewById(R.id.profileB);
            mLinearLayout = findViewById(R.id.linearLayout);

            // add any listeners or custom logic as needed

     mBackButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             // Perform your action here

             Intent intent = new Intent(Account.this, Swipe.class);
             startActivity(intent);

         }
     });


     mLogoutButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             // log out

             Intent intent = new Intent(Account.this, Swipe.class);
             startActivity(intent);

         }
     });

     mBookmarksButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             // go to bookmarks page

             Intent intent = new Intent(Account.this, LikedMoviesList.class);
             startActivity(intent);

         }
     });



    }


}
