package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.FixedVariable;
import com.creator.rewardsapp.Common.ParticipateOfferObject;
import com.creator.rewardsapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.creator.rewardsapp.Body.Authentication.SignUpActivity.MANDATORY;

public class ParticipationForm extends AppCompatActivity {
    private TextView pFormShopname;
    private ProgressDialog progressDialog;
    EditText fullname, contacno, billValue;
    TextInputLayout llfullname, llcontacno, llbillValue;


    private AlertDialog.Builder builder;
    private DialogInterface.OnClickListener dialogClickListener;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private Uri receiptImageUri;
    private final String TAG = getClass().getSimpleName();
    private String shopname, shopId;
    private String customerName, customerContacNo, customerBillValue;
    private String pbillValue;
    private String pContactNo;
    private String pFullName;
    private List<String> shopsId;
    private String startDate;
    private String endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation_form);
        pFormShopname = findViewById(R.id.pFormShopname);
        fullname = findViewById(R.id.pfullname);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fullname.setText(mAuth.getCurrentUser().getDisplayName());
        Intent passed = getIntent();
        initializeViews(passed);
    }

    private void initializeViews(Intent passed) {
        shopname = passed.getStringExtra(FixedVariable.SHOP_NAME);
        shopId = passed.getStringExtra(FixedVariable.SHOP_ID);
        startDate = passed.getStringExtra(FixedVariable.START_DATE);
        endDate = passed.getStringExtra(FixedVariable.END_DATE);



        if (!shopname.isEmpty())
            pFormShopname.setText(shopname);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading data...");
        setAlertDialog();

        billValue = findViewById(R.id.p_bill_value);
        fullname = findViewById(R.id.pfullname);
        contacno = findViewById(R.id.pcontactno);
        llbillValue = findViewById(R.id.ll_pbillvalue);
        llfullname = findViewById(R.id.ll_pfullname);
        llcontacno = findViewById(R.id.ll_pmobno);

        String helperText = "Bill date must belong from " + startDate + " to " + endDate;
        llbillValue.setHelperText(helperText);
        String phoneNo = mAuth.getCurrentUser().getPhotoUrl().toString();
        contacno.setText(phoneNo);
        Log.d(TAG, "initializeViews: DisplayName no: => "+mAuth.getCurrentUser().getDisplayName());
        Log.d(TAG, "initializeViews: Contact no: => "+phoneNo);
        billValue.requestFocus();
    }

    private void setAlertDialog() {
        dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(this, "Upoading...", Toast.LENGTH_SHORT).show();
                    //TODO: Start Participation here
                    CropImage.activity().setAspectRatio(1, 1).start(this);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;

            }
        };

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("You won't be able to revert back, once receipt starts uploading.  Are you sure want to upload ?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener);

    }

    private DialogInterface.OnClickListener performDialogOperations(String productID, String productName, String productCost) {
        return (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    Toast.makeText(this, "Yes Clicked", Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    Toast.makeText(this, "No Clicked", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
            }
        };
    }

    public void uploadReceiptbtn(View view) {
        FixedVariable.showToaster(this, "Updating Info...");
        if (isUserNameValid() && isContactnoValid() && isBillValueValid()) {
            pFullName = fullname.getText().toString();
            pContactNo = llcontacno.getPrefixText().toString() + " " + contacno.getText().toString();
            pbillValue = billValue.getText().toString();
            builder.show();
        }


    }

    private boolean isUserNameValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(fullname.getText().toString())) {

            llfullname.setErrorEnabled(true);
            llfullname.setError(MANDATORY);
            contactStatus = false;
        } else {
            llfullname.setErrorEnabled(false);
            llfullname.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    private boolean isBillValueValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(billValue.getText().toString())) {

            llbillValue.setErrorEnabled(true);
            llbillValue.setError(MANDATORY);
            contactStatus = false;
        } else {
            llbillValue.setErrorEnabled(false);
            llbillValue.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    private boolean isContactnoValid() {
        boolean contactStatus;

        if (TextUtils.isEmpty(contacno.getText().toString())) {

            llcontacno.setErrorEnabled(true);
            llcontacno.setError(MANDATORY);
            contactStatus = false;
        } else {
            llcontacno.setErrorEnabled(false);
            llcontacno.setError(null);
            contactStatus = true;
        }

        if (contactStatus == true)
            return true;
        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                receiptImageUri = result.getUri();
                //  profilePic.setImageURI(imageUri);
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Log.e(TAG, "onActivityResult: UID after authentication" + user.getUid());
                    try {
                        updateUI();
                    } catch (Exception e) {
                        FixedVariable.showToaster(this, "Size is too big to upload");
                    }
//                    updateUI(user, imageUri);
                }
            }
        }
    }

    private void updateUI() {
        StorageReference spaceRef = storageReference.child("USERS/" + mAuth.getCurrentUser().getUid() +
                "/" + shopId + "/shop-receipt.jpg");
        spaceRef.putFile(receiptImageUri).addOnSuccessListener(taskSnapshot -> {
            FixedVariable.showToaster(this, "Image Uploaded");
            spaceRef.getDownloadUrl().addOnSuccessListener(uri -> {

                Log.e(TAG, "updateUI: PHOTO URL TO BE SAVED: " + uri);

                updateToStorageDatabase(uri.toString());


            });
        }).addOnFailureListener(e -> FixedVariable.showToaster(this, "Failed."));
    }

    private void updateToStorageDatabase(String image) {
        /*
         *Upload works in this way:
         * First participate in the offer by filling details
         * Save receipt to Firebase Storage
         * Update the shop details like @participantsCount, @participantId in corresponding shops
         * Update the Participant details to retrieve participation history in future
         * Done
         * */
        //For Shop
        Log.d("maggi", "updateToStorageDatabase: shopId" + shopId);
        if (shopId != null) {
            // First, retrieve the creator of current offer, then add participant
            // in separate list of corresponding offer
            database.collection("Offers")
                    .document(shopId).get().addOnCompleteListener(task -> {
                String creatorId = task.getResult().getString("creatorId");
                if (creatorId != null) {
                    Map<String, Object> userShops = new HashMap<>();
                    userShops.put("currentOfferId", shopId);
                    userShops.put("customerId", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid()));
                    // Adding participants id of current offers's participants in each current Shop
                    database.collection("AllOffersCustomers")
                            .document(shopId).set(userShops, SetOptions.merge());


                }
            });
        }


        //For Participants
        HashMap<String, Object> receiptData = new HashMap<>();
        receiptData.put("shops", FieldValue.arrayUnion(shopId));
        receiptData.put("pUid", mAuth.getCurrentUser().getUid());
        if (receiptData.size() > 0) {
            ParticipateOfferObject p = new ParticipateOfferObject();
            p.setFullname(pFullName);
            p.setContactno(pContactNo);
            p.setBillvalue(pbillValue);
            p.setReceiptUrl(image);


            // Query to save current particpant data in particular shop,
            database.collection("Participants")
                    .document(mAuth.getCurrentUser().getUid())
                    .collection(shopId)
                    .document(shopId).set(p);

            // If not exists then create collection And then merge that data
            // Here, add the shops as list  for individual user participation
            setParticipantDetails(receiptData);


        }

    }

    private void setParticipantDetails(HashMap<String, Object> receiptData) {
        database.collection("Participants")
                .document(mAuth.getCurrentUser().getUid())
                .set(receiptData, SetOptions.merge())
                .addOnCompleteListener(task -> {

                    Map<String, Object> m = new HashMap<>();
                    m.put("maxParticipants", FieldValue.increment(1));
                    // Here, update the total participants count by 1
                    database.collection("Offers")
                            .document(shopId)
                            .set(m, SetOptions.merge());

                    finish();
                    progressDialog.dismiss();


                });
    }


}