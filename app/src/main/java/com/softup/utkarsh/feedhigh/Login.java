package com.softup.utkarsh.feedhigh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softup.utkarsh.feedhigh.Common.Common;
import com.softup.utkarsh.feedhigh.Model.EmpMaster;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {
    Button btnSignIn,btnSignUp;
    TextView txtSlogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        txtSlogan = (TextView)findViewById(R.id.txtSlogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);
        Paper.init(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignIn.class);
                startActivity(intent);

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);


            }
        });

        //Check Remember

        String eid = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);

        if(eid != null && pwd !=null)
        {
            if(!eid.isEmpty() && !pwd.isEmpty())
                login(eid,pwd);
        }

    }

    private void login(final String employeeid, final String pwd) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("EmpMaster");

        //copy login code

        final ProgressDialog mDialog = new ProgressDialog(Login.this);

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


        if (dataSnapshot.child(employeeid).exists()) {


            //Get user information
            mDialog.dismiss();
            EmpMaster empMaster = dataSnapshot.child(employeeid).getValue(EmpMaster.class);
            if (empMaster.getPassword().equals(pwd)) {
                //   Toast.makeText(SignIn.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();


                Intent homeintent = new Intent(Login.this, Home.class);
                Common.currentUser = empMaster;
                startActivity(homeintent);
                finish();

            }
        }
        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(Login.this,"HEYYYYYYYY",Toast.LENGTH_SHORT).show();

            }
        });
    }

}
