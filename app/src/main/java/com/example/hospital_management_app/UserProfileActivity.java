package com.example.hospital_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome,textViewFullName,textViewEmail,textViewDoB,textViewGender;
    private ProgressBar progressBar;
    private String fullName,email,doB,gender;

    private FirebaseAuth authProfile;
    private CircleImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

       Button  btnnext=findViewById(R.id.btn_next_usermainscreen);
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, usermainscreenActivity.class);
                startActivity(intent);

            }
});


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        progressBar = findViewById(R.id.progressBar);

        imageView=findViewById(R.id.imageView_profile_dpp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(UserProfileActivity.this,UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });

        authProfile = FirebaseAuth.getInstance();
       FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser==null){
            Toast.makeText(UserProfileActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
        }else{
            checkIfEmailVerified(firebaseUser);

            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You will not be able to login next time without email verification.");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUserProfile( FirebaseUser firebaseUser) {
        String userID=firebaseUser.getUid();
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails !=null){
                    fullName=firebaseUser.getDisplayName();
                    email=firebaseUser.getEmail();
                    doB=readUserDetails.dob;
                    gender=readUserDetails.gender;

                    textViewWelcome.setText("Welcome  "+ fullName );
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDoB.setText(doB);
                    textViewGender.setText(gender);

                    Uri uri = firebaseUser.getPhotoUrl();

                    Picasso.with(UserProfileActivity.this).load(uri).into(imageView);
                }else {
                    Toast.makeText(UserProfileActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.menu_refresh){
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if(id==R.id.menu_update_profile){
            Intent intent = new Intent(UserProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }else if(id==R.id.menu_update_email){
            Intent intent = new Intent(UserProfileActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        }/*else if(R.id.menu_settings){
            Toast.makeText(UserProfileActivity.this,"menu_settings",Toast.LENGTH_SHORT).show();
        }else if(id==R.id.menu_change_password){
            Intent intent = new Intent(UserProfileActivity.this,ChangePasswordActivity.class);
            startActivity(intent);
        }else if(id==R.id.menu_delete_profile){
            Intent intent = new Intent(UserProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
        }*/else if(id==R.id.menu_logout){
            authProfile.signOut();
            Toast.makeText(UserProfileActivity.this,"Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(UserProfileActivity.this,LoginActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(UserProfileActivity.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}