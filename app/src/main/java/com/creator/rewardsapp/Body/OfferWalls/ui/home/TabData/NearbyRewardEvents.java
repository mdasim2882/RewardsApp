package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData;

import android.os.Bundle;
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
import com.creator.rewardsapp.Body.OfferWalls.Interfaces.LoadAllProfiles;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.HelperClasses.OffersEntry;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters.ShopsOffersRecyclerViewAdapter;
import com.creator.rewardsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NearbyRewardEvents extends Fragment {
    static NearbyRewardEvents instance;
    private RecyclerView recyclerView;
    ShopsOffersRecyclerViewAdapter adapter;
    LoadAllProfiles loadMyConcepts;
    List<OffersEntry> shops;
    Menu fBtnSearch;
    FloatingActionButton floatingActionButton;

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
        shops.add(new OffersEntry("Dinesh General Store"));
        shops.add(new OffersEntry("Ravi Cafe"));
        shops.add(new OffersEntry("Baldeo Medical Store"));
        shops.add(new OffersEntry("Vishal Mega Mart"));
        shops.add(new OffersEntry("Johnson's Shoppee"));
    }

    private void setRecyclerView(View view) {

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.shops_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        adapter = new ShopsOffersRecyclerViewAdapter(getActivity(), shops);
        recyclerView.setAdapter(adapter);
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
}
