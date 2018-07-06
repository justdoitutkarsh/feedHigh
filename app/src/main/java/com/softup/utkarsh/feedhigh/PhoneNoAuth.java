package com.softup.utkarsh.feedhigh;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softup.utkarsh.feedhigh.Model.EmpMaster;

import java.util.Arrays;

/**
 * Created by AnupTechTips on 13/7/18.
 */

public class PhoneNoAuth extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        //Init Firebase
      //  FirebaseDatabase database = FirebaseDatabase.getInstance();
        //final DatabaseReference table_user = database.getReference("EmpMaster");

        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        //DatabaseReference mostafa = ref.child("EmpMaster").child("1505574").child("Mobile");


      /*  mostafa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Log.v("tmz",""+dataSnapshot.getValue());
                 String email = dataSnapshot.getValue().toString();
                //do what you want with the email
                Toast.makeText(PhoneNoAuth.this, email, Toast.LENGTH_SHORT).show();
                // Create object of SharedPreferences.
                SharedPreferences sharedPref = getSharedPreferences("mypref", 0);

                //now get Editor
                SharedPreferences.Editor editor = sharedPref.edit();

                //put your value
                editor.putString("name", email);

                //commits your edits
                editor.commit();



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */



        if (auth.getCurrentUser() != null) {

            // already signed in
            Toast.makeText(this, "OTP Already Verified Before, Sign In now!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PhoneNoAuth.this, SignIn.class));

            finish();

        }

        else {

            Bundle bundle = getIntent().getExtras();
            String phone = bundle.getString("phoneParams");

            Bundle phoneParams = new Bundle();
            phoneParams.putString(AuthUI.EXTRA_DEFAULT_PHONE_NUMBER,"+91"+phone);


            startActivityForResult(

                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setAvailableProviders(
                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER)

                                   .setParams(phoneParams)

                                    .build())
                            )


                            .build(), RC_SIGN_IN);

            // not signed in
            /*
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                            ))
                            .build(),
                    RC_SIGN_IN); */

        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                Toast.makeText(this, "Verified , Sign in now!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PhoneNoAuth.this,SignIn.class));
                finish();
                return;
            }
            // Sign in failed
            else {
                // User pressed back button
                if (response == null) {
                    ShowAlertBox("Login canceled by User");
                    return;
                }
                //No internet connection on the device
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    ShowAlertBox("No Internet Connection");
                    return;
                }
                //other errors
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    ShowAlertBox("Unknown Error");
                    return;
                }
            }
            ShowAlertBox("Unknown sign in response");
        }
    }
    //Creating a custom alert box
    protected void ShowAlertBox(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PhoneNoAuth.this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });



        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
