package com.example.cinewatch20.database;

import com.google.firebase.database.FirebaseDatabase;

public class DatabaseInstance {

    private DatabaseInstance(){}

    public static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
}