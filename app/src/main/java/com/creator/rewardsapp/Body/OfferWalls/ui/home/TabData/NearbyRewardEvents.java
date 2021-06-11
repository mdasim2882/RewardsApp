package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.HomeActivity;
import com.creator.rewardsapp.Body.OfferWalls.Interfaces.LoadNearbyEvents;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.FixedVariable;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.MyCollectionNames;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters.ShopsOffersRecyclerViewAdapter;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.ProductGridItemDecoration;
import com.creator.rewardsapp.Common.CreateOfferObject;
import com.creator.rewardsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyRewardEvents extends Fragment implements LoadNearbyEvents {
    static NearbyRewardEvents instance;
    private RecyclerView recyclerView;
    ShopsOffersRecyclerViewAdapter adapter;
    LoadNearbyEvents loadNearbyEvents;
    List<CreateOfferObject> shops;
    Menu fBtnSearch;
    FloatingActionButton floatingActionButton;
    FirebaseFirestore db;
    private String expiryList;

    public NearbyRewardEvents() {
    }

    public static NearbyRewardEvents getInstance() {
        if (instance == null)
            instance = new NearbyRewardEvents();
        return instance;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        shops = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        loadNearbyEvents = this;

    }

    private void loadAllEvents() {
        /*
         * Load all the document of Offers collection one by one
         * and update the UI
         * Done
         * */
        Log.e("checker", "loadTemplates: called");
        db.collection("Offers")
                .get()
                .addOnCompleteListener(task -> {
                    Log.e("checker", "onComplete: called");

                    List<CreateOfferObject> products = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                            Log.e("checker", "loadTemplates: " + bannerSnapshot.getData());
                            CreateOfferObject product = bannerSnapshot.toObject(CreateOfferObject.class);
                            products.add(product);
                        }
                        loadNearbyEvents.onNearbyLoadSuccess(products);
                    }
                })
                .addOnFailureListener(e -> loadNearbyEvents.onNearbyLoadFailed(e.getMessage()));
    }

    private void setRecyclerView(View view) {

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.shops_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));

        /*
         * Pass parameter as list of type ProductEntry
         * Must be retrieved from database to here only
         * ProductEntry contains three fields:
         * ImageView productImage
         * TextView productName, productCost;
         * */
    }

    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nearby_reward_events, container, false);
        setRecyclerView(root);
        floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();

        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(v -> {
                //Manually set click on menu item
                fBtnSearch.performIdentifierAction(R.id.action_search, 0);
            });
        }
        loadAllEvents();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (floatingActionButton != null) {
            floatingActionButton.show();
        }
    }

    private void showSearchBarusingFloatingBtn(Menu fBtnSearch) {
        MenuItem item = fBtnSearch.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search Shop Name...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        fBtnSearch = menu;
        showSearchBarusingFloatingBtn(menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onNearbyLoadSuccess(List<CreateOfferObject> templates) {
        adapter = new ShopsOffersRecyclerViewAdapter(getActivity(), templates);
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.updown_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.side_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
        try {
            checkExpiryAndDeclareWinners(templates);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(expiryList, "Error in setting Offers: " + e.getMessage());
        }
    }

    private void checkExpiryAndDeclareWinners(List<CreateOfferObject> templates) throws ParseException {
        List<CreateOfferObject> expired = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy");
        for (CreateOfferObject c : templates) {
            String valid_until = c.getEndDate();
            Date strDate = sdf.parse(valid_until);

            assert strDate != null;
            if (System.currentTimeMillis() > strDate.getTime()) {
                expired.add(c);
            }
        }
        Log.d("c", "checkExpiryAndDeclareWinners: \n");
        List<String> expireOffers = new ArrayList<>();
        List<String> expiredShops = new ArrayList<>();
        for (CreateOfferObject x : expired) {
            expiryList = "expiryList";
            Log.d(expiryList, "Dates " + x.getEndDate());
            expireOffers.add(x.getOfferId());
            expiredShops.add(x.getCreatorId());
        }
        if (expireOffers.isEmpty())
            return;
        db.collection(MyCollectionNames.ALLCUSTOMERS)
                .whereIn("currentOfferId", expireOffers)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().getDocuments() != null) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String currentOfferId = documentSnapshot.get("currentOfferId").toString();
                    List<String> totalCustomers = (List<String>) documentSnapshot.get("customerId");

                    Log.d(expiryList, "creator: " + currentOfferId + " => " + totalCustomers);

                    Map<String, Object> winners = new HashMap<>();

                    winners.put("winnerList", totalCustomers);
                    db.collection(MyCollectionNames.ALLCUSTOMERS)
                            .document(currentOfferId)
                            .set(winners, SetOptions.merge());

                    if (totalCustomers != null)
                        winners.put("winnerDeclared", true);


                    winners.put("query", false);
                    db.collection("Offers")
                            .document(currentOfferId)
                            .set(winners, SetOptions.merge())
                            .addOnSuccessListener(unused -> {
                                Log.d(expiryList, "Winners in OfferList Set: SUCCESS ");
                            }).addOnFailureListener(e -> {
                        Log.d(expiryList, "Winners in OfferList Set: FAILED=> " + e.getMessage());
                        FixedVariable.showToaster(getActivity(), "checkExpiryAndDeclareWinners: FAILED=> " + e.getMessage());
                    })
                    ;

                    // Find n winners
//                                for(int  i=0;i<3; i++)
//                                    Log.d(expiryList, "winner- "+(i+1)+" => "+l.get(i));
//                              //  Setting winners
//                                db.collection("AllCustomerOffers")
//                                    .document(creatorId)
//                                    .set(expiredShops);
                }


            }
        });


    }

    @Override
    public void onNearbyLoadFailed(String message) {
        Log.e("checker", "onNearbyLoadFailed: " + message);
        FixedVariable.showToaster(getActivity(), message);
    }
}
