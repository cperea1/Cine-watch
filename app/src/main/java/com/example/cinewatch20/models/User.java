package com.example.cinewatch20.models;

import androidx.annotation.NonNull;

import com.example.cinewatch20.data.MovieItem;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;




public class User implements Serializable {
    private String id;
    private String name;
    private int age;
//    private String bday;
    private String contact;
    private String email;
    private String username;
    private String password;
    private String gender;
    private List<MovieItem> likedMovies = new ArrayList<>();
    private List<MovieItem> dislikedMovies = new ArrayList<>();
    private List<MovieItem> bookmarkedMovies = new ArrayList<>();
    private List<String> subscriptions = new ArrayList<>();

    public User(){}

    public User(String id, String name, String username, String email, String password){
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<MovieItem> getLikedMovies() {
        return likedMovies;
    }


    public void addLikedMovieItem(MovieItem movieItem) {
        this.likedMovies.add(movieItem);
    }



    public void removeLikedMovieItem(int movieItemId){
        this.likedMovies = this.likedMovies.stream()
                .filter(movie -> movieItemId != movie.getId()).collect(Collectors.toList());
    }



    public void addAllLikedMovieItem(List<MovieItem> likedMovieItems) {

        this.likedMovies.addAll(likedMovieItems);
    }

    public void emptyLikedMovies() {
        this.likedMovies = new ArrayList<>();
    }

    public List<MovieItem> getDislikedMovies() {
        return dislikedMovies;
    }


    public void addDislikedMovieItem(MovieItem movieItems) {
        this.dislikedMovies.add(movieItems);
    }

    public void removeDislikedMovie(int movieItemId){
        this.dislikedMovies = this.dislikedMovies.stream()
                .filter(movie -> movieItemId != movie.getId()).collect(Collectors.toList());
    }

    public void emptyDislikedMovies() {
        this.dislikedMovies = new ArrayList<>();
    }

    public List<MovieItem> getBookmarkedMovies() {
        return bookmarkedMovies;
    }

    public void addBookmarkedMovies(MovieItem movieItem) {
        this.bookmarkedMovies.add(movieItem);

    }

    public void removeBookmarkedMovies(int movieItemId){
        this.bookmarkedMovies = this.bookmarkedMovies.stream()
                .filter(movie -> movieItemId != movie.getId()).collect(Collectors.toList());
    }

    public void emptyBookmarkedMovies() {
        this.bookmarkedMovies = new ArrayList<>();
    }

    public List<String> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<String> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void addSubscription(String sub) {
        this.subscriptions.add(sub);
    }

    public void removeSubscription(String sub){
        this.subscriptions = this.subscriptions.stream()
                .filter(s -> !s.equals(sub)).collect(Collectors.toList());
    }

    public void emptySubscriptions() {
        this.subscriptions = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", likedMovies=" + likedMovies +
                ", dislikedMovies=" + dislikedMovies +
                ", bookmarkedMovies=" + bookmarkedMovies +
                ", subscriptions=" + subscriptions +
                '}';
    }
}
