package com.softup.utkarsh.feedhigh.headactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softup.utkarsh.feedhigh.Common.Common;
import com.softup.utkarsh.feedhigh.Home;
import com.softup.utkarsh.feedhigh.OthersForm;
import com.softup.utkarsh.feedhigh.R;
import com.softup.utkarsh.feedhigh.headDepartmentReviewmodel.HeadNote;
import com.softup.utkarsh.feedhigh.headservice.HeadMessageSenderService;

import static com.softup.utkarsh.feedhigh.headactivity.HeadDepartmentReview.NOTES;


public class HeadAddNoteActivity extends AppCompatActivity {

    public static final String REQUIRED = "Required";
    private int mPriority;

    EditText addTtl;
    EditText addBody;
    EditText addBody2;
    EditText addBody3;
    EditText addBody4;
    EditText addBody5;
    Button saveBtn;

    private BroadcastReceiver mMessageReceiver;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_add_note);

        FirebaseAuth.getInstance();

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getExtras().getString("title");
                String body = intent.getExtras().getString("body");
                String body2 = intent.getExtras().getString("body2");
                String body3 = intent.getExtras().getString("body3");
                String body4 = intent.getExtras().getString("body4");
                String body5 = intent.getExtras().getString("body5");
                String body6 = intent.getExtras().getString("body6");
                String body7 = intent.getExtras().getString("body7");
                Toast.makeText(HeadAddNoteActivity.this, title+"\n"+body+"\n"+body2+"\n"+body3+"\n"+body4+"\n"+body5+"\n"+body6+"\n"+body7, Toast.LENGTH_LONG).show();
            }
        };

        initUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
    };

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    };

    private void initUI() {
        addTtl = (EditText)findViewById(R.id.addTitle);
        addBody = (EditText)findViewById(R.id.addBody);
        addBody2 = (EditText)findViewById(R.id.addBody2);
        addBody3 = (EditText)findViewById(R.id.addBody3);
        addBody4 = (EditText)findViewById(R.id.addBody4);
        addBody5 = (EditText)findViewById(R.id.addBody5);
        saveBtn = (Button)findViewById(R.id.btnSave);
        ((RadioButton) findViewById(R.id.radButton1)).setChecked(true);
        mPriority = 1;

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(addTtl.getText())) {
                    addTtl.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(addBody.getText())) {
                    addBody.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(addBody.getText())) {
                    addBody2.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(addBody.getText())) {
                    addBody3.setError(REQUIRED);
                    return;
                }
                if (TextUtils.isEmpty(addBody.getText())) {
                    addBody4.setError(REQUIRED);
                    return;
                }


                View view = getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(view != null ? view.getWindowToken() : null, 0);

                setEditingEnabled(false);
                saveBtn.setEnabled(false);
                SharedPreferences sharedPref = getSharedPreferences("Department", 0);
                String name = sharedPref.getString("name", "");

                postNote(addTtl.getText().toString(), addBody.getText().toString(),addBody2.getText().toString(),addBody3.getText().toString(),addBody4.getText().toString(),addBody5.getText().toString(), Common.currentUser.getDesignation().toString(),name, mPriority);
               // Toast.makeText(HeadAddNoteActivity.this,"Posting...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postNote(String title, String body, String body2, String body3, String body4, String body5,String body6,String body7, int priority) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child(NOTES).push().getKey();

        HeadNote noteToSave = new HeadNote(key, title, body,body2,body3,body4,body5,body6,body7, priority);
        mDatabase.child(NOTES).child(key)
                .setValue(noteToSave, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                saveBtn.setEnabled(true);

                if (databaseError == null) {
                  //  Toast.makeText(HeadAddNoteActivity.this, "Successfully Posted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_SYNC, null, HeadAddNoteActivity.this, HeadMessageSenderService.class);
                    startService(intent);
                    startActivity(new Intent(HeadAddNoteActivity.this, OthersForm.class));
                } else {
                    Toast.makeText(HeadAddNoteActivity.this, "Error Posting Review", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onPrioritySelected(View view) {
        if (((RadioButton) findViewById(R.id.radButton1)).isChecked()) {
            mPriority = 1;
        } else if (((RadioButton) findViewById(R.id.radButton2)).isChecked()) {
            mPriority = 2;
        } else if (((RadioButton) findViewById(R.id.radButton3)).isChecked()) {
            mPriority = 3;
        }
    }

   private void setEditingEnabled(boolean state) {
        addTtl.setEnabled(state);
        addBody.setEnabled(state);
    }
}
