package com.example.hospital_management_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername,editTextEmail,editTextPassword,editTextconfirmPwd,editTextdob;
    private TextView login;
    private ProgressBar progressBar;
    private RadioGroup genderRadioGroup, roleRadioGroup;
    private RadioButton genderRadioGroupSelected;
    private RadioButton roleRadioGroupSelected;
    private DatePickerDialog picker;
    private static final String TAG="SignUpActivity";



    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toast.makeText(SignUpActivity.this,"You can register now", Toast.LENGTH_LONG).show();
        progressBar=findViewById(R.id.progressBar);

        editTextUsername = findViewById(R.id.edit_Text_Username);
        editTextEmail = findViewById(R.id.edit_Text_Email);
        editTextPassword = findViewById(R.id.edit_Text_Password);
        editTextconfirmPwd = findViewById(R.id.edit_Text_Password);
        editTextdob = findViewById(R.id.edit_Text_dob);

        login = findViewById(R.id.login);

        genderRadioGroup = findViewById(R.id.radio_group_register_gender);
        genderRadioGroup.clearCheck();
        roleRadioGroup = findViewById(R.id.radio_group_role_choice);
        roleRadioGroup.clearCheck();

        editTextdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.MONTH);
                int year=calendar.get(Calendar.YEAR);

                picker=new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextdob.setText(dayOfMonth + "/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                picker.show();
            }
        });
        Button signUpButton;

        signUpButton = findViewById(R.id.elevatedSignupButton);

      /*  login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                */

                signUpButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        registerUser();
                       }
                    });
                 }
                 public void registerUser(){


                        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                        genderRadioGroupSelected = findViewById(selectedGenderId);
                        int selectedRole = roleRadioGroup.getCheckedRadioButtonId();
                        roleRadioGroupSelected = findViewById(selectedRole);

                        String textUsername = editTextUsername.getText().toString();
                        String textEmail = editTextEmail.getText().toString();
                        String textPassword = editTextPassword.getText().toString();
                        String textconfirmPwd = editTextconfirmPwd.getText().toString();
                        String Textdob = editTextdob.getText().toString();
                        String textGender;
                        String textRole;
                        if (TextUtils.isEmpty(textUsername)) {

                            Toast.makeText(SignUpActivity.this, "Please Enter your name", Toast.LENGTH_LONG).show();
                            editTextUsername.setError("Your Name is required");
                            editTextUsername.requestFocus();
                        }
                        else if (TextUtils.isEmpty(textEmail)) {

                            Toast.makeText(SignUpActivity.this, "Please Enter your email", Toast.LENGTH_LONG).show();
                            editTextEmail.setError("Email is required");
                            editTextEmail.requestFocus();
                        } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                            
                            Toast.makeText(SignUpActivity.this,"Please re-enter your email",Toast.LENGTH_LONG).show();
                            editTextEmail.setError("Valid email is required");
                            editTextEmail.requestFocus();
                        } 
                        else if (TextUtils.isEmpty(Textdob)) {
                            Toast.makeText(SignUpActivity.this,"Please enter your date of birth",Toast.LENGTH_LONG).show();
                            editTextdob.setError("Date of birth is required");
                            editTextdob.requestFocus();
                        } 
                        else if (genderRadioGroup.getCheckedRadioButtonId()==-1) {
                            Toast.makeText(SignUpActivity.this,"Please Select your biological gender",Toast.LENGTH_LONG).show();
                            genderRadioGroupSelected.setError("Biological Gender is required");
                            genderRadioGroupSelected.requestFocus();
                        }

                        else if (roleRadioGroup.getCheckedRadioButtonId()==-1) {
                            Toast.makeText(SignUpActivity.this,"Please Select your respective role",Toast.LENGTH_LONG).show();
                           roleRadioGroupSelected.setError("Your Role is required");
                            roleRadioGroupSelected.requestFocus();
                        }

                        else if (TextUtils.isEmpty(textPassword)) {
                            Toast.makeText(SignUpActivity.this,"Please Enter your Password",Toast.LENGTH_LONG).show();
                            editTextPassword.setError("Password is required");
                            editTextPassword.requestFocus();
                            
                        } else if (textPassword.length() < 6) {

                            Toast.makeText(SignUpActivity.this,"Password should be atleast six digits",Toast.LENGTH_LONG).show();
                            
                            editTextPassword.setError("Password too weak");
                            editTextPassword.requestFocus();
                            
                        } else if (TextUtils.isEmpty(textconfirmPwd)) {
                            Toast.makeText(SignUpActivity.this,"Please confirm your password",Toast.LENGTH_LONG).show();
                            editTextconfirmPwd.setError("Password confirmation is required");
                            editTextconfirmPwd.requestFocus();
                        } else if (!textPassword.equals(textconfirmPwd)) {
                            Toast.makeText(SignUpActivity.this,"Please Enter the same Password as above",Toast.LENGTH_LONG).show();
                            editTextconfirmPwd.setError("Password Confirmation is required");
                            editTextconfirmPwd.requestFocus();
                            editTextPassword.clearComposingText();
                            editTextconfirmPwd.clearComposingText();
                        }
                        else {
                            textGender=genderRadioGroupSelected.getText().toString();
                            progressBar.setVisibility(View.VISIBLE);
                            registerUser(textUsername,textEmail,Textdob,textGender,textPassword);
                        }
                    }






   private void registerUser(String textUsername, String textEmail, String textdob, String textGender, String textPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPassword).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"User Registered Successfully",Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser= auth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new  UserProfileChangeRequest.Builder().setDisplayName(textUsername).build();
                    firebaseUser.updateProfile(profileChangeRequest);
                    ReadWriteUserDetails writeUserDetails=new ReadWriteUserDetails(textdob,textGender);
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                if(firebaseUser !=null) {

                                    firebaseUser.sendEmailVerification();
                                    Toast.makeText(SignUpActivity.this,"User Registered Successfully.Please verify your email.",Toast.LENGTH_LONG).show();

                                }

                                    Intent intent = new Intent(SignUpActivity.this,UserProfileActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();

                                }
                                else {
                                    Toast.makeText(SignUpActivity.this,"User Registered failed.Please try again.",Toast.LENGTH_LONG).show();

                                }
                            progressBar.setVisibility(View.GONE);

                            }


                    });


                }
                else {
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        editTextPassword.setError("Your password is weak.Kindly use a mix of alphabets, numbers and special characters");
                        editTextPassword.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editTextPassword.setError("Your email is invalid or already in use. Kindly use another email");
                        editTextPassword.requestFocus();
                    }catch(FirebaseAuthUserCollisionException e){
                        editTextPassword.setError("User is already registered with this email.Kindly use another email");
                        editTextPassword.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }

    private void signUp() {
            // Implement sign up logic here
        }



    }



