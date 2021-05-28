package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.creator.rewardsapp.R;

public class ParticipationForm extends AppCompatActivity {
   private TextView pFormShopname;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;
    private DialogInterface.OnClickListener dialogClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation_form);
        pFormShopname = findViewById(R.id.pFormShopname);
        Intent passed = getIntent();
        initializeViews(passed);
    }

    private void initializeViews(Intent passed) {
        String shopname = passed.getStringExtra("ShopName");
        if (!shopname.isEmpty())
            pFormShopname.setText(shopname);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading data...");
        setAlertDialog();
    }
    private void setAlertDialog() {
        dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Toast.makeText(this, "Upoading...", Toast.LENGTH_SHORT).show();
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
        builder.show();
    }
}