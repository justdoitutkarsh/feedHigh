package com.softup.utkarsh.feedhigh.headactivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.softup.utkarsh.feedhigh.Common.Common;
import com.softup.utkarsh.feedhigh.Home;
import com.softup.utkarsh.feedhigh.R;
import com.softup.utkarsh.feedhigh.headDepartmentReviewmodel.HeadNote;
import com.softup.utkarsh.feedhigh.headadapter.HeadItemClickAdapter;
import com.softup.utkarsh.feedhigh.headadapter.HeadNoteAdapter;


import java.util.ArrayList;
import java.util.List;

public class HeadDepartmentReview extends AppCompatActivity {

    private static final String TAG = HeadDepartmentReview.class.getSimpleName();

    public static final String NOTES = "HeadNotes";

    private RecyclerView mRecyclerView;
    private HeadNoteAdapter mHeadNoteAdapter;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ArrayList<HeadNote> mDataList;
    private View rootView;

    private int doubleBackToExitPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headdepartmentreview);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        updateUI();
        displayNotes();
    }

    private void isAuthorized() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(HeadDepartmentReview.this, HeadLoginActivity.class);
            startActivity(intent);
        }
    }

    private void swipeToDelete() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int id = viewHolder.getAdapterPosition();
                final HeadNote note = mDataList.get(id);
                final String keyToRemove = note.getFirebaseKey();

                mDatabase.child(NOTES).child(keyToRemove).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.e(TAG, "Some error has occured while deleting the note");
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(rootView, "Record is deleted", Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                          mDatabase.child(NOTES).child(keyToRemove)
                                                  .setValue(note, new DatabaseReference.CompletionListener() {
                                                      @Override
                                                      public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                          Snackbar snackbar1 = Snackbar
                                                                  .make(rootView, "Record is restored", Snackbar.LENGTH_LONG);
                                                          snackbar1.show();
                                                      }
                                                  });
                                        }
                                    });
                            snackbar.show();
                        }
                    }
                });
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    private void updateUI() {
        isAuthorized();

        rootView = getWindow().getDecorView().getRootView();

        doubleBackToExitPressed = 1;
        mDataList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mHeadNoteAdapter = new HeadNoteAdapter(this, mDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mHeadNoteAdapter);
        swipeToDelete();
        mRecyclerView.addOnItemTouchListener(new HeadItemClickAdapter(this, new HeadItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               // Intent intent = new Intent(HeadDepartmentReview.this, HeadDetailsActivity.class);
               // startActivity(intent);
            }
        }));

        mHeadNoteAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_headdepartmentreview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionAdd) {
            Intent intent = new Intent(this, HeadAddNoteActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.actionSignOut) {
            HeadLoginActivity.signout();
            Intent intent = new Intent(this, HeadLoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayNotes() {
        showProgressBar(true);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDataList.clear();
                DataSnapshot tableNote = dataSnapshot.child(NOTES);

                for (DataSnapshot child : tableNote.getChildren()) {
                    String id = child.child("body6").getValue(String.class);
                    String id1 = child.child("body7").getValue(String.class);
                    if (Common.currentUser.getDesignation().equals("Manager")) {
                        if (id.equalsIgnoreCase("employee") && id1.equalsIgnoreCase(Common.currentUser.getDepartment())) {

                            HeadNote tmpNote = child.getValue(HeadNote.class);
                            mDataList.add(tmpNote);
                          //  Toast.makeText(HeadDepartmentReview.this, id, Toast.LENGTH_SHORT).show();

                            //  HeadNote tmpNote = child.getValue(HeadNote.class);
                            //mDataList.add(tmpNote);

                        }
                    }
                }



                mRecyclerView.setAdapter(mHeadNoteAdapter);
                mHeadNoteAdapter.notifyDataSetChanged();
                showProgressBar(false);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                showProgressBar(false);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {

            Intent intent = new Intent(HeadDepartmentReview.this, Home.class);
            startActivity(intent);
           // finishAffinity();
            //System.exit(0);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("MyData")
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getExtras().getString("title");
            String body = intent.getExtras().getString("body");
            Toast.makeText(HeadDepartmentReview.this, body + "\n" + title, Toast.LENGTH_LONG).show();
        }
    };

    private void showProgressBar(boolean isProgrssBarVisible) {
        if (isProgrssBarVisible) {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
