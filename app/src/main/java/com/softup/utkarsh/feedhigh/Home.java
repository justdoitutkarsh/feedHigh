package com.softup.utkarsh.feedhigh;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.softup.utkarsh.feedhigh.Common.Common;
import com.softup.utkarsh.feedhigh.Interface.ItemClickListener;
import com.softup.utkarsh.feedhigh.Model.Department;
import com.softup.utkarsh.feedhigh.ViewHolder.MenuViewHolder;
import com.softup.utkarsh.feedhigh.activity.AddNoteActivity;
import com.softup.utkarsh.feedhigh.activity.DepartmentReview;
import com.softup.utkarsh.feedhigh.headactivity.HeadAddNoteActivity;
import com.softup.utkarsh.feedhigh.headactivity.HeadDepartmentReview;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference review;
    TextView textFullname,txtDepartment,txtDesignation,txtPassUpdate;
    ImageView profileimg;
    private int doubleBackToExitPressed=1;
    //Firebase Storage
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome");
        setSupportActionBar(toolbar);

        //Init Firebase Storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        Paper.init(this);

        database=FirebaseDatabase.getInstance();
        review=database.getReference("Department");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView=navigationView.getHeaderView(0);
        textFullname=(TextView)headerView.findViewById(R.id.getfullname);
        txtDepartment = (TextView)headerView.findViewById(R.id.getdepartment);
        txtDesignation = (TextView)headerView.findViewById(R.id.getdesignation);
        CircleImageView imageAvatar = (CircleImageView) headerView.findViewById(R.id.profileimg);
       // String s = getIntent().getStringExtra("EXTRA_SESSION_ID");

        textFullname.setText(Common.currentUser.getName());/*review-database reference*/
        //But with avatar , we just check it with null or empty
        if (Common.currentUser.getAvatarUrl() != null && !TextUtils.isEmpty(Common.currentUser.getAvatarUrl())) {
            Picasso.with(this)
                    .load(Common.currentUser.getAvatarUrl())
                    .into(imageAvatar);
        }
        txtDepartment.setText(Common.currentUser.getDepartment());
        txtDesignation.setText(Common.currentUser.getDesignation());
      //  Toast.makeText(this, "Designation"+Common.currentUser.getDesignation().toString(), Toast.LENGTH_SHORT).show();
       // Toast.makeText(this, "Department"+Common.currentUser.getDepartment().toString(), Toast.LENGTH_SHORT).show();

        // Create object of SharedPreferences.
        SharedPreferences sharedPref = getSharedPreferences("Department", 0);

        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();

        //put your value
        editor.putString("name", Common.currentUser.getDepartment());

        //commits your edits
        editor.commit();




        //Load Menu

        recyclerView =(RecyclerView)findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        
        loadMenu();


    }

    private void loadMenu() {

        FirebaseRecyclerAdapter<Department,MenuViewHolder> adapter = new FirebaseRecyclerAdapter<Department, MenuViewHolder>(Department.class,R.layout.menu_item,MenuViewHolder.class,review) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, final Department model, int position) {

                viewHolder.DepartmentName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);
                final Department clickitem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                     //   Toast.makeText(Home.this,""+clickitem.getName(),Toast.LENGTH_LONG).show();
                        if(clickitem.getName().toString().equals("Public Feedback to Departments")) {
                            Intent intent = new Intent(Home.this, AddNoteActivity.class);
                            startActivity(intent);

                        }
                        else if (clickitem.getName().toString().equals("Company's Chat Session Subscription"))
                        {
                            Intent intent = new Intent(Home.this, GroupDiscussion.class);
                            startActivity(intent);
                        }
                        else if (clickitem.getName().toString().equals("Feedback Your Senior"))
                        {
                            Intent intent = new Intent(Home.this, HeadAddNoteActivity.class);
                            startActivity(intent);
                        }
                        else if (clickitem.getName().toString().equals("Check Public Feedbacks"))
                        {
                            Intent intent = new Intent(Home.this, DepartmentReview.class);
                            startActivity(intent);
                        }
                        else if (clickitem.getName().toString().equals("My Feedback"))
                        {
                            Intent intent = new Intent(Home.this, HeadDepartmentReview.class);
                            startActivity(intent);
                        }
                        else if (clickitem.getName().toString().equals("Department Chat"))
                        {
                            Toast.makeText(Home.this,""+clickitem.getName(),Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Home.this, DepartmentGroupDiscussion.class);
                            startActivity(intent);
                        }


                    }
                });
            }


        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
      /*  if (doubleBackToExitPressed == 2) {
            //Intent intent = new Intent(DepartmentReview.this, Home.class);
            //startActivity(intent);
            finishAffinity();
            System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Please press Back exit", Toast.LENGTH_SHORT).show();
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

            Paper.book().destroy();
            Intent intent=new Intent(Home.this,Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_signout) {

        }
        else if (id == R.id.nav_update_info) {
            showDialogUpdateInfo();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogUpdateInfo() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Update Your Profile Picture");
        alertDialog.setMessage("Select your profile picture");

        LayoutInflater inflater = this.getLayoutInflater();
        View layout_pwd = inflater.inflate(R.layout.layout_update_information,null);

        final MaterialEditText edtMobileUpdate = (MaterialEditText)layout_pwd.findViewById(R.id.edtMobile);
        final MaterialEditText edtEmailUpdate = (MaterialEditText)layout_pwd.findViewById(R.id.edtEmail);
        final ImageView image_upload = (ImageView) layout_pwd.findViewById(R.id.image_upload);
        image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        alertDialog.setView(layout_pwd);

        //Set Button
        alertDialog.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    final android.app.AlertDialog waitingDialog = new SpotsDialog(Home.this);
                    waitingDialog.show();

                    String Mobile = edtMobileUpdate.getText().toString();
                    String Email = edtEmailUpdate.getText().toString();
                    Map<String,Object> updateInfo = new HashMap<>();
                    if (!TextUtils.isEmpty(Mobile))
                        updateInfo.put("Mobile",Mobile);
                    if (!TextUtils.isEmpty(Email))
                        updateInfo.put("Email",Email);

                DatabaseReference empInformation = FirebaseDatabase.getInstance().getReference(Common.user_emp_tb1);
                empInformation.child("1505574")
                        .updateChildren(updateInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(Home.this, "Information Updated", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(Home.this, "Information Upload failed", Toast.LENGTH_SHORT).show();
                                waitingDialog.dismiss();
                            }
                        });





            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture :"),Common.PICK_IMAGE_REQUEST);

    }

    //Ctrl+O

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Common.PICK_IMAGE_REQUEST && requestCode == RESULT_OK
                && data !=null && data.getData() !=null);
        {
            Uri saveUri = data.getData();
            if (saveUri != null)
            {
                final ProgressDialog mDialog = new ProgressDialog(this);
                mDialog.setMessage("Uploading...");
                mDialog.show();

                String imageName = UUID.randomUUID().toString(); //Random name image upload
                final StorageReference imageFolder = storageReference.child("images/"+imageName);
                imageFolder.putFile(saveUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mDialog.dismiss();
                              //  Toast.makeText(Home.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String s = getIntent().getStringExtra("EXTRA_SESSION_ID");

                                        //Update this url to avatar property of User
                                        Map<String,Object> AvatarUpdate = new HashMap<>();
                                        AvatarUpdate.put("AvatarUrl",uri.toString());

                                        DatabaseReference empInformation = FirebaseDatabase.getInstance().getReference(Common.user_emp_tb1);
                                        empInformation.child(s)
                                                .updateChildren(AvatarUpdate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful())
                                                            Toast.makeText(Home.this, "Uploaded", Toast.LENGTH_SHORT).show();
                                                        else
                                                            Toast.makeText(Home.this, "Upload error", Toast.LENGTH_SHORT).show();

                                                    }
                                                });


                                    }
                                });

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                mDialog.setMessage("Uploaded"+progress+"%");
                            }
                        });

            }

        }


    }
}
