package com.softup.utkarsh.feedhigh.activity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.database.ValueEventListener;

import com.softup.utkarsh.feedhigh.DepartmentReviewmodel.Note;
import com.softup.utkarsh.feedhigh.Home;
import com.softup.utkarsh.feedhigh.R;
import com.softup.utkarsh.feedhigh.adapter.ItemClickAdapter;
import com.softup.utkarsh.feedhigh.adapter.NoteAdapter;

import java.util.ArrayList;

public class DepartmentReview extends AppCompatActivity {

    private static final String TAG = DepartmentReview.class.getSimpleName();

    public static final String NOTES = "Notes";

    private RecyclerView mRecyclerView;
    private NoteAdapter mNoteAdapter;
    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ArrayList<Note> mDataList;
    private View rootView;

    private int doubleBackToExitPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departmentreview);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        updateUI();
        displayNotes();
    }

    private void isAuthorized() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(DepartmentReview.this, LoginActivity.class);
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
                final Note note = mDataList.get(id);
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
        mNoteAdapter = new NoteAdapter(this, mDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mNoteAdapter);
       // swipeToDelete();
        mRecyclerView.addOnItemTouchListener(new ItemClickAdapter(this, new ItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               // Intent intent = new Intent(DepartmentReview.this, DetailsActivity.class);
               // startActivity(intent);
            }
        }));

        mNoteAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();

        getMenuInflater().inflate(R.menu.menu_main_departmentreview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionAdd) {
            Intent intent = new Intent(this, AddNoteActivity.class);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.actionSignOut) {
            LoginActivity.signout();
            Intent intent = new Intent(this, LoginActivity.class);
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
                    Note tmpNote = child.getValue(Note.class);
                    mDataList.add(tmpNote);
                }
                mRecyclerView.setAdapter(mNoteAdapter);
                mNoteAdapter.notifyDataSetChanged();
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
        if (doubleBackToExitPressed == 2) {
            Intent intent = new Intent(DepartmentReview.this, Home.class);
            startActivity(intent);
          //  finishAffinity();
          //  System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Please press Back again to go to Home", Toast.LENGTH_SHORT).show();
        }

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
            Toast.makeText(DepartmentReview.this, body + "\n" + title, Toast.LENGTH_LONG).show();
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
