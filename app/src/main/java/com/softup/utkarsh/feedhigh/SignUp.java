package com.softup.utkarsh.feedhigh;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.softup.utkarsh.feedhigh.Model.EmpMaster;

public class SignUp extends AppCompatActivity {
    MaterialEditText edtEmpId,edtName,edtEmail,edtDepartment,edtPassword,edtPhone;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtEmpId = (MaterialEditText)findViewById(R.id.edtEmpId);
        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtEmail = (MaterialEditText)findViewById(R.id.edtEmail);
        edtDepartment = (MaterialEditText)findViewById(R.id.edtDepartment);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("EmpMaster");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check if already Emp Id
                        if (dataSnapshot.child(edtEmpId.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "You are already registered", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            mDialog.dismiss();
                            EmpMaster empMaster = new EmpMaster(edtDepartment.getText().toString(),edtEmail.getText().toString(),
                                    edtPhone.getText().toString(),edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtEmpId.getText().toString()).setValue(empMaster);
                            Toast.makeText(SignUp.this, "Sign up successfully !", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
