package com.example.mobilepaindiary.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    //user email
    private String userEmail;
    //user password
    private String userPassWord;


    public User(Parcel in) {
        userEmail = in.readString();
        userPassWord = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        userPassWord = userPassword;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userEmail);
        dest.writeString(userPassWord);
    }

    @Override
    public String toString() {
        return "User{" +
                "userEmail='" + userEmail + '\'' +
                ", userPassWord='" + userPassWord + '\'' +
                '}';
    }
}
