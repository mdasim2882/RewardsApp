package com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses;

import android.app.Activity;
import android.widget.Toast;

public class FixedVariable {
    public static final String SHOP_NAME="ShopName";
    public static final String SHOP_ID="ShopId";
    public static final int GALLERY_CODE = 1001;
    public static final String CURRENT_OFFER_ID = "getOfferID";

    public static void showToaster(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
