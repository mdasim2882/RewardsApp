package com.creator.rewardsapp.Body.OfferWalls.ui.historyEvents;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.HomeActivity;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.HelperClasses.OffersEntry;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters.EventCreatedHistoryRecyclerViewAdapter;
import com.creator.rewardsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class EventCreatedHistory extends Fragment {
    RecyclerView recyclerView;
    EventCreatedHistoryRecyclerViewAdapter adapter;
    TextView emptyText;
    List<OffersEntry> shops;

    public EventCreatedHistory() {
    }

    public RecyclerView getRecView() {
        return recyclerView;
    }

    public TextView getEmptyTextView() {
        return emptyText;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
        Log.i("maggi", "onCreate: ");
        shops = new ArrayList<>();
        shops.add(new OffersEntry("Confectionary Shop"));
        shops.add(new OffersEntry("Baba Lassi"));
        shops.add(new OffersEntry("Pakeezah Juice Corner"));
        shops.add(new OffersEntry("Sumit General Store"));


//        if(shops.isEmpty()){
//
//            recyclerView.setVisibility(View.GONE);
//            emptyText.setVisibility(View.VISIBLE);
//        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_created_history, container, false);
        setRecyclerView(root);
        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        emptyText = root.findViewById(R.id.empty_view);
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
        return root;
    }

    private void setRecyclerView(View view) {

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.event_created_history_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        adapter = new EventCreatedHistoryRecyclerViewAdapter(getActivity(), shops);
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