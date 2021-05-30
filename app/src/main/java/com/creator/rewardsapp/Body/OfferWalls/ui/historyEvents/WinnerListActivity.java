package com.creator.rewardsapp.Body.OfferWalls.ui.historyEvents;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.HelperClasses.OffersEntry;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters.WinnnerListRecyclerViewAdapter;
import com.creator.rewardsapp.R;

import java.util.ArrayList;
import java.util.List;

public class WinnerListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    WinnnerListRecyclerViewAdapter adapter;
    List<OffersEntry> shops;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_list);
        Intent i=getIntent();
        String  shopName=i.getStringExtra("ShopName");
        TextView myShop=findViewById(R.id.winnerShoppername);
        myShop.setText(shopName);
        shops = new ArrayList<>();
        shops.add(new OffersEntry("Vishal Chaurasia"));
        shops.add(new OffersEntry("Vaibhav Agarwal"));
        shops.add(new OffersEntry("Pranjal Srivastava"));
        shops.add(new OffersEntry("Surya"));
        setRecyclerView();
    }
    private void setRecyclerView() {

        // Set up the RecyclerView
        recyclerView =findViewById(R.id.winner_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        adapter = new WinnnerListRecyclerViewAdapter(this, shops);
        recyclerView.setAdapter(adapter);
        /*
         * Pass parameter as list of type ProductEntry
         * Must be retrieved from database to here only
         * ProductEntry contains three fields:
         * ImageView productImage
         * TextView productName, productCost;
         * */
    }

}