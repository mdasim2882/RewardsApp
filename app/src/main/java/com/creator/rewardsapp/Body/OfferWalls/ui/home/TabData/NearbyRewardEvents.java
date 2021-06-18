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
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters.ShopsOffersRecyclerViewAdapter;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.ProductGridItemDecoration;
import com.creator.rewardsapp.Common.CreateOfferObject;
import com.creator.rewardsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    private int totalWinners;

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

        try {
            if (floatingActionButton != null) {
                floatingActionButton.setOnClickListener(v -> {
                    try {
                        //Manually set click on menu item
                        fBtnSearch.performIdentifierAction(R.id.action_search, 0);
                    } catch (Exception e) {
                        FixedVariable.showToaster(getActivity(),"Please wait to load data");
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            FixedVariable.showToaster(getActivity(),"Please wait to load data");
            e.printStackTrace();
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

    }


    @Override
    public void onNearbyLoadFailed(String message) {
        Log.e("checker", "onNearbyLoadFailed: " + message);
        FixedVariable.showToaster(getActivity(), message);
    }
}
