package com.creator.rewardsapp.Body.OfferWalls.ui.historyEvents;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.Interfaces.LoadWinners;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.FixedVariable;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.MyCollectionNames;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters.WinnnerListRecyclerViewAdapter;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.ProductGridItemDecoration;
import com.creator.rewardsapp.Common.ParticipateOfferObject;
import com.creator.rewardsapp.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WinnerListActivity extends AppCompatActivity implements LoadWinners {
    private RecyclerView recyclerView;
    WinnnerListRecyclerViewAdapter adapter;
    List<ParticipateOfferObject> winnerHelperList;
    private int winCount = 0;
    private int winCountLimit = 0;
    LoadWinners offerWinners;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    private String currentOffer;
    private String winnersData;
    private String shopName;
    private CircularProgressIndicator winnerLoader;
    private LinearLayout noWinnerHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_list);
        Intent i = getIntent();
        shopName = i.getStringExtra("ShopName");
        currentOffer = i.getStringExtra(FixedVariable.CURRENT_OFFER_ID);
        TextView myShop = findViewById(R.id.winnerShoppername);
        winnerLoader = findViewById(R.id.loader_winner);
        noWinnerHistory = findViewById(R.id.no_winner_found);
        winnerLoader.show();


        myShop.setText(shopName);
        winnerHelperList = new ArrayList<>();
        offerWinners = this;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
/*        shops.add(new OffersEntry("Vishal Chaurasia"));
        shops.add(new OffersEntry("Sumit"));
        shops.add(new OffersEntry("Pranjal Srivastava"));
        shops.add(new OffersEntry("Surya"));*/
        setRecyclerView();
        loadAllWinners();
    }

    private void loadAllWinners() {

        /*
         * Load all the document of Offers collection one by one
         * and update the UI
         * Done
         * */
        winnersData = "winnersData";
        Log.d(winnersData, "loadAllWinners: called with current Offer= " + currentOffer);

        //TODO: Where EQUAL to -> WinnerListActivity
        db.collection(MyCollectionNames.ALLCUSTOMERS)
                .whereEqualTo("currentOfferId", currentOffer)
                .get()
                .addOnCompleteListener(task -> {
                    boolean isEmpty = false;
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Log.d(winnersData, "onComplete: called with DOCUMENT: => " + documentSnapshot.getId());
                            List<String> winnerList = (List<String>) documentSnapshot.get("winnerList");
                            loadParticipants(winnerList);
                            isEmpty = (winnerList == null || winnerList.isEmpty()) ? true : false;
                        }
                    }

                    winnerLoader.hide();
                    if (isEmpty)
                        noWinnerHistory.setVisibility(View.VISIBLE);


                })
                .addOnFailureListener(e -> offerWinners.onLoadWinnersFailed(e.getMessage()));
    }

    private void loadParticipants(List<String> winnerList) {
        if (winnerList == null)
            return;
        Log.d(winnersData, "loadParticipants: Called with winners :\n" + winnerList);
        winCountLimit = winnerList.size();
        for (String pUid : winnerList) {
            db.collection(MyCollectionNames.PARTICIPANTS)
                    .document(pUid)
                    .collection(currentOffer)
                    .get()
                    .addOnCompleteListener(task -> {
                        Log.d(winnersData, "Add on complete called to fetch Participants data for: =>" + pUid);

                        if (task.isSuccessful()) {
                            ParticipateOfferObject winner = new ParticipateOfferObject();
                            for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                                Log.d(winnersData, "DATA: \n" + bannerSnapshot.getData());
                                winner = bannerSnapshot.toObject(ParticipateOfferObject.class);
                            }
                            fetchWinners(winner);
                        }
                    })
                    .addOnFailureListener(e -> {
                        offerWinners.onLoadWinnersFailed(e.getMessage());
                    });
        }


    }

    private void fetchWinners(ParticipateOfferObject winner) {
        winnerHelperList.add(winner);
        winCount++;
        Log.d(winnersData, "fetchWinners: with winCount: " + winCount + " wincountLimit: " + winCountLimit);
        if (winCount == winCountLimit)
            offerWinners.onLoadWinnersSuccess(winnerHelperList);
    }

    private void setRecyclerView() {

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.winner_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));

        /*
         * Pass parameter as list of type ProductEntry
         * Must be retrieved from database to here only
         * ProductEntry contains three fields:
         * ImageView productImage
         * TextView productName, productCost;
         * */
    }

    @Override
    public void onLoadWinnersSuccess(List<ParticipateOfferObject> templates) {
        adapter = new WinnnerListRecyclerViewAdapter(this, templates, shopName);
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.updown_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.side_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
        //recreate new instance to load more winners if any

    }

    @Override
    public void onLoadWinnersFailed(String message) {
        Log.e(winnersData, "onLoadWinnersFailed: " + message);
        FixedVariable.showToaster(this, message);
    }
}