package com.creator.rewardsapp.Body.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.creator.rewardsapp.Body.OfferWalls.HomeActivity;
import com.creator.rewardsapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.FileNotFoundException;

public class SignUpActivity extends AppCompatActivity {
    public static final String MANDATORY = "All fields are mandatory";
    public final String TAG = getClass().getSimpleName();
    EditText fullname, mobileno, emailId, password;
    TextInputLayout lMobno, lEmailId, lFullname, lPassword;
    String name, email, phoneno, pword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);
        intitializeView();
        mAuth = FirebaseAuth.getInstance();
    }

    private void intitializeView() {
        mobileno = findViewById(R.id.contactno);
        emailId = findViewById(R.id.emailid);
        fullname = findViewById(R.id.fullname);
        password = findViewById(R.id.password);

        lMobno = findViewById(R.id.ll_mobno);
        lEmailId = findViewById(R.id.ll_email_id);
        lFullname = findViewById(R.id.ll_fullname);
        lPassword = findViewById(R.id.ll_password);
    }

    public void registerMe(View view) {
        if (isFullnameValid() && isEmailIdValid()
                && isMobileNoValid() && isPasswordValid()) {
            //TODO : Start registration here
            retrieveInputs();
        } else {
            showToaster("All fields are mandatory!");
        }
    }

    private boolean isFullnameValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(fullname.getText().toString())) {
            lFullname.setErrorEnabled(true);
            lFullname.setError(MANDATORY);
            contactStatus = false;
        } else {
            lFullname.setErrorEnabled(false);
            lFullname.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    private boolean isEmailIdValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(emailId.getText().toString())) {
            lEmailId.setErrorEnabled(true);
            lEmailId.setError(MANDATORY);
            contactStatus = false;
        } else {

            lEmailId.setErrorEnabled(false);
            lEmailId.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    private boolean isMobileNoValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(mobileno.getText().toString())) {
            lMobno.setErrorEnabled(true);
            lMobno.setError(MANDATORY);
            contactStatus = false;
        } else {

            lMobno.setErrorEnabled(false);
            lMobno.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;
    }

    private boolean isPasswordValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(password.getText().toString())) {
            lPassword.setErrorEnabled(true);
            lPassword.setError(MANDATORY);
            contactStatus = false;
        } else {
            lPassword.setErrorEnabled(false);
            lPassword.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;
    }

    private void extractInputs() {
        String getfullname = fullname.getText().toString();
        String getemail = emailId.getText().toString();
        String getPhone = mobileno.getText().toString();
        String getconfirmPassword = password.getText().toString();
        createAccount(getemail, getconfirmPassword, getfullname, getPhone);
    }

    private void createAccount(String getemail, String getconfirmPassword, String getfullname, String getPhone) {
        mAuth.createUserWithEmailAndPassword(getemail, getconfirmPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        try {
                            setAdminName(user, getPhone, getfullname);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        Log.e(TAG, "Create Account: \n " +
                                "UID : " + mAuth.getUid() +
                                "Current User UID : " + user.getUid() +
                                "\n EMAIL: " + user.getEmail() +
                                "\n NAME: " + user.getDisplayName() +
                                "\n PROVIDER: " + user.getProviderData() +
                                "\n PHOTO: " + getPhone);

                        Toast.makeText(this, "Registration Done.", Toast.LENGTH_SHORT).show();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(this, "Authentication failed: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setAdminName(FirebaseUser user, String getPhone, String getfullname) throws FileNotFoundException {
        Log.e(TAG, "updateUI: NAME TO BE SAVED: " + getfullname);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(getfullname)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "User profile updated.\n" +
                                "Email:" + user.getEmail() + "\n" +
                                "Name: " + user.getDisplayName() + "\n" +
                                "Photo URL: " + getPhone);
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

    }

    private void retrieveInputs() {
        showToaster("Registering...");
        extractInputs();
    }

    private void showToaster(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}