package com.softup.utkarsh.feedhigh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;
import com.softup.utkarsh.feedhigh.Common.Common;
import com.softup.utkarsh.feedhigh.Model.EmpMaster;
import com.softup.utkarsh.feedhigh.Model.User;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
EditText edtEmpId,edtPassword;
Button btnSignIn;
CheckBox ckbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtEmpId = (MaterialEditText)findViewById(R.id.edtEmpId);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        ckbRemember=(CheckBox)findViewById(R.id.ckbRemember);
        Paper.init(this);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("EmpMaster");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtEmpId.getText().toString().trim().equals("") || edtPassword.getText().toString().trim().equals(""))
                {
                    Toast.makeText(SignIn.this, "Kindly enter username and password!", Toast.LENGTH_SHORT).show();
                }
                else{
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            //Check if user not exist in database
                        if (dataSnapshot.child(edtEmpId.getText().toString()).exists()) {


                            //Get user information
                            mDialog.dismiss();
                             EmpMaster empMaster = dataSnapshot.child(edtEmpId.getText().toString()).getValue(EmpMaster.class);
                            if (empMaster.getPassword().equals(edtPassword.getText().toString())) {
                             //   Toast.makeText(SignIn.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();

                                if (ckbRemember.isChecked())
                                {
                                    Paper.book().write(Common.USER_KEY,edtEmpId.getText().toString());
                                    Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());
                                }


                                Intent homeintent=new Intent(SignIn.this,Home.class);
                                homeintent.putExtra("EXTRA_SESSION_ID", edtEmpId.getText().toString());
                                Common.currentUser = empMaster;
                                startActivity(homeintent);
                                finish();

                            } else {
                                Toast.makeText(SignIn.this, "Invalid Credential !", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }}
        });
    }
}
