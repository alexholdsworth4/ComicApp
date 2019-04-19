/*
Class: SignUpActivity
Author: B5017070
Purpose: Controls functionality for Sign in and sign up page.
         Users can be created or logged into the system,
         with all user data being uploaded to Firebase.
*/

package com.example.androidfirebasecomicreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.androidfirebasecomicreader.Common.Common;
import com.example.androidfirebasecomicreader.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUpActivity extends AppCompatActivity {

    //SignUp Activity fields
    MaterialEditText edtNewUser, edtNewPassword, edtNewEmail; //for Sign Up
    MaterialEditText edtUser, edtPassword; //for Sign In
    Button btnSignUp, btnSignIn;

    //The  users created are uploaded to Firebase
    // Users can only log in by creating an account
    FirebaseDatabase database;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //The list of users are added to the 'Users' list in Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        //There are two text input boxes where the user can login
        edtUser = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPassword);

        //Under the input boxes will be a sign in and sign up button
        btnSignIn = findViewById(R.id.btn_sign_in);
        btnSignUp = findViewById(R.id.btn_sign_up);

        //The sign up dialog popup will be shown when sign up button is pressed
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(edtUser.getText().toString(), edtPassword.getText().toString());
            }
        });
    }

    //Sign in functionality
    private void signIn(final String user, final String pwd) {
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //The data snapshot will be compared with the users list in Firebase
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user).exists()) {
                    if (!user.isEmpty()) {
                        User login = dataSnapshot.child(user).getValue(User.class);
                        if (login.getPassword().equals(pwd)) {
                            //If the user exists in Firebase then take the user to the home page
                            Intent MainActivity = new Intent(SignUpActivity.this, MainActivity.class);
                            Common.currentUser = login;
                            startActivity(MainActivity);
                            finish();
                        } else
                            //If the user exists, but the password is not correct
                            Toast.makeText(SignUpActivity.this, "The password input is incorrect!", Toast.LENGTH_SHORT).show();
                    } else {
                        //If the user name and password box are empty
                        Toast.makeText(SignUpActivity.this, "Please enter your user name and password!", Toast.LENGTH_SHORT).show();
                    }
                } else
                    //If the user does not exist in the Firebase list.
                    Toast.makeText(SignUpActivity.this, "The user does not exist!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //When sign up button is pressed- show the sign up dialog popup controlled by this method
    private void showSignUpDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignUpActivity.this);
        //Title so user immediately knows this is to sign up (and not sign in)
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Please enter your account details.");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout, null);

        //Each user requires a user name , email and password
        edtNewUser = sign_up_layout.findViewById(R.id.edtNewUserName);
        edtNewEmail = sign_up_layout.findViewById(R.id.edtNewEmail);
        edtNewPassword = sign_up_layout.findViewById(R.id.edtNewPassword);

        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);

        //If the user accidentally pressed this popup, it can be dismissed with cancel button
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        //The new user creation can be confirmed by pressing this button
        alertDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final User user = new User(edtNewUser.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    //Apply the following popups when user is created
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user.getUserName()).exists())
                            //If the user already exists, do not create a duplicate user
                            Toast.makeText(SignUpActivity.this, "User name already exists in system!", Toast.LENGTH_SHORT).show();
                        else {
                            //Add the user to Firebase user list if they do not exist
                            users.child(user.getUserName()).setValue(user);
                            Toast.makeText(SignUpActivity.this, "User registration successful!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }
}
