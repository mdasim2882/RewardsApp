package com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses;

import android.app.Activity;
import android.widget.Toast;

public class FixedVariable {
    public static String mycontactno;

    public static final String SHOP_NAME="ShopName";
    public static final String SHOP_ID="ShopId";
    public static final String PHONE_NO="PhoneNo";
    public static final String START_DATE="StartDate";
    public static final String END_DATE="EndDate";
    public static final int GALLERY_CODE = 1001;
    public static final String CURRENT_OFFER_ID = "getOfferID";
    public static final  String getShopname="getShopname";
    public static final  String getReceiptUrl="getReceiptUrl";

    public static void showToaster(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
