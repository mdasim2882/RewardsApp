package com.creator.rewardsapp.Body.OfferWalls.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.FixedVariable;
import com.creator.rewardsapp.R;

public class PictureReceiptActivity extends AppCompatActivity {
    private  final String TAG=getClass().getSimpleName();
    TextView shopname;
    ImageView receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_receipt);
        initializeView();
        String shopnamer = getIntent().getStringExtra(FixedVariable.getShopname);
        String receiptUrl = getIntent().getStringExtra(FixedVariable.getReceiptUrl);
        Log.d(TAG, "onCreate() called with: Shopname = [" + shopnamer + "]"+"\nReceiptUrl = [" + receiptUrl + "]");
        if(receiptUrl!=null)
        Glide.with(this).load(receiptUrl).into(receipt);
        if (shopnamer != null)
            shopname.setText(shopnamer);
    }

    private void initializeView() {
        shopname = findViewById(R.id.receiptshopname);
        receipt = findViewById(R.id.receiptPhoto);
    }
}