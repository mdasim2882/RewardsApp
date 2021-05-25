package com.creator.rewardsapp.Body.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.creator.rewardsapp.Body.OfferWalls.HomeActivity;
import com.creator.rewardsapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.creator.rewardsapp.Body.Authentication.SignUpActivity.MANDATORY;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private FirebaseAuth fAuth;
    TextInputEditText loginEmail, loginPassword;
    TextInputLayout lLoginEmail, lLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        initializeViews();
    }

    private void initializeViews() {
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        lLoginEmail = findViewById(R.id.ll_login_emailid);
        lLoginPassword = findViewById(R.id.ll_login_password);
    }

    private boolean isEmailIdValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(loginEmail.getText().toString())) {

            lLoginEmail.setErrorEnabled(true);
            lLoginEmail.setError(MANDATORY);
            contactStatus = false;
        } else {
            lLoginEmail.setErrorEnabled(false);
            lLoginEmail.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    private boolean isPasswordValid() {

        boolean contactStatus;

        if (TextUtils.isEmpty(loginPassword.getText().toString())) {

            lLoginPassword.setErrorEnabled(true);
            lLoginPassword.setError(MANDATORY);
            contactStatus = false;
        } else {

            lLoginPassword.setErrorEnabled(false);
            lLoginPassword.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;
    }

    public void loginMe(View view) {
        if (isEmailIdValid() && isPasswordValid()) {
            //TODO : Start Login here
            retrieveInputs();
        } else {
            showToaster("All fields are mandatory!");
        }
    }
    private void retrieveInputs() {
        showToaster("Logging in...");
        extractInputs();
    }

    private void extractInputs() {
        String getEmail = loginEmail.getText().toString();
        String getPassword = loginPassword.getText().toString();
        signInwithEmailPassword(getEmail, getPassword);
    }
    private void signInwithEmailPassword(String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAGGY", "LoginWithEmail:success");
                        FirebaseUser user = fAuth.getCurrentUser();
//                        Toast.makeText(UserLoginActivity.this, "Email: " + user.getEmail() + "\n" +
//                                "Name: " + user.getDisplayName() + "\n" +
//                                "Photo: " + user.getPhotoUrl(), Toast.LENGTH_LONG).show();
//                        Picasso.get().load(user.getPhotoUrl()).into(profilePic);
                        Log.e(TAG, "signInwithEmailPassword: \n" +
                                "Name:" + user.getDisplayName() + "\n" +
                                "Email: " + user.getEmail() + "\n"
                        );
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

//                        SharedPreferences sharedPreferences = getSharedPreferences(PrefVariables.LOGIN_STATS, Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean(PrefVariables.ISLOGIN, true);
//                        editor.putBoolean(PrefVariables.IS_REGISTERED, true);
//                        editor.commit();
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "LoginWithEmail:failure", task.getException());
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showToaster(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}