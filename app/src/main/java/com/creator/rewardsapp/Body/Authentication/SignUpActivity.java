package com.creator.rewardsapp.Body.Authentication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.creator.rewardsapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    public static final String MANDATORY = "All fields are mandatory";
    EditText fullname, mobileno, emailId, password;
    TextInputLayout lMobno, lEmailId, lFullname, lPassword;
    String name, email, phoneno, pword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);
        intitializeView();
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

    private void retrieveInputs() {
        showToaster("Registering...");

    }

    private void showToaster(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}