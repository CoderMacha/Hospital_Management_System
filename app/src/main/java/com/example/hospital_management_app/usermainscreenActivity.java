package com.example.hospital_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class usermainscreenActivity extends AppCompatActivity {

    private CircleImageView imageView;
    private FirebaseAuth authProfile;
    private String fullName;
    private TextView textViewWelcome;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;



    String[] names = {"Dr. Sandeep Vaishya","Dr. Naresh Tehran","Dr. Arun Prasad","Dr. Rana Patir","Dr. Sajan K Hedge","Dr. Aditya Gupta","Dr. Bipin Walia","Dr. Hermant Sharma","Dr. IPS Oberoi","Dr. Subhash Chandra","Dr. Aashish Shah","Dr. Abhijit Das","Dr. Abhishek Katha","Dr. M Kalyan Ravi Teja","Dr. Anil Madupu"};
    int [] imgs={R.drawable.doctor_1,R.drawable.doctor_2,R.drawable.doctor_1,R.drawable.doctor_2,R.drawable.doctor_1,R.drawable.doctor_2,R.drawable.doctor_1,R.drawable.doctor_2,R.drawable.doctor_1,R.drawable.doctor_2,R.drawable.doctor_1,R.drawable.doctor_2,R.drawable.doctor_1,R.drawable.doctor_2,R.drawable.doctor_1};

    String[] dept_names={"Gynecology","Neurology","Orthopedics","Surgery"};
    int[]  dept_imgs={R.drawable.gynecology_img,R.drawable.neurology_img,R.drawable.orthopedic_img,R.drawable.surgery_img};
    SearchView searchView;
    RecyclerView horizontal_recycler_view;
    RecyclerView vertical_recycler_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermainscreen);



        horizontal_recycler_view=(RecyclerView) findViewById(R.id.horizontal_recycler_view);
        vertical_recycler_view =(RecyclerView) findViewById(R.id.vertical_recycler_view);

        searchdocsadapter searchdocsadapter=new searchdocsadapter(imgs,names);
        SearchdeptsAdapter searchdeptsAdapter =new SearchdeptsAdapter(dept_imgs,dept_names);

        horizontal_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        horizontal_recycler_view.setAdapter(searchdocsadapter);

        vertical_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        vertical_recycler_view.setAdapter(searchdeptsAdapter);

        imageView=findViewById(R.id.imageView_usermainscreen);
        textViewWelcome = findViewById(R.id.textView_user_main_screen);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        frameLayout = findViewById(R.id.fragment_container);




       /* searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // searchList(newText)
                return true;
            }
        });


        */
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showUserProfile(firebaseUser);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId=item.getItemId();

                if(itemId == R.id.home_btm_menu){
                    loadFragment(new HomeFragment(),false);
                }else if(itemId == R.id.appointments_btm_menu){
                    loadFragment(new AppointmentsFragment(),false);

                }else if(itemId == R.id.medicine_btm_menu){
                    loadFragment(new medicineFragment(),false);

                }else{
                    loadFragment(new billFragment(),false);
                }
                return true;
            }
        });

        loadFragment(new HomeFragment(),true);

    }

    private  void loadFragment(Fragment fragment,boolean isAppInitialized){
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(isAppInitialized){
            fragmentTransaction.add(R.id.fragment_container,fragment);
        }else{
            fragmentTransaction.replace(R.id.fragment_container,fragment);
        }

        fragmentTransaction.commit();
    }

    private void showUserProfile( FirebaseUser firebaseUser) {
        String userID=firebaseUser.getUid();
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);

                if (readUserDetails != null) {
                    fullName = firebaseUser.getDisplayName();

                    Uri uri = firebaseUser.getPhotoUrl();

                    Picasso.with(usermainscreenActivity.this).load(uri).into(imageView);

                    textViewWelcome.setText("Hi, " + fullName);
                }else {
                    Toast.makeText(usermainscreenActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(usermainscreenActivity.this,"Something went wrong!",Toast.LENGTH_SHORT).show();
            }




        });
    }


    }
