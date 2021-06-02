package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.HomeActivity;
import com.creator.rewardsapp.Body.OfferWalls.Interfaces.LoadNearbyEvents;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.HelperClasses.OffersHistory;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters.OffersHistoryRecyclerViewAdapter;
import com.creator.rewardsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ParticipationHistory extends Fragment {
    static ParticipationHistory instance;
    private RecyclerView historyRecyclerView;
    LoadNearbyEvents loadMyConcepts;
    List<OffersHistory> shopsHistory;
    OffersHistoryRecyclerViewAdapter historyAdapter;
    public ParticipationHistory() {
    }

    public static ParticipationHistory getInstance() {
        if (instance == null)
            instance = new ParticipationHistory();
        return instance;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
        shopsHistory = new ArrayList<>();

        shopsHistory.add(new OffersHistory("Manish General Store"));
        shopsHistory.add(new OffersHistory("Domino's Pizza"));
        shopsHistory.add(new OffersHistory("Burma & Sons."));
    }
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.participation_history, container, false);
        setRecyclerView(root);
        return root;
    }
    private void setRecyclerView(View view) {

        // Set up the RecyclerView
        historyRecyclerView = view.findViewById(R.id.shops_history_recycler_view);
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        historyAdapter = new OffersHistoryRecyclerViewAdapter(getActivity(), shopsHistory);
        historyRecyclerView.setAdapter(historyAdapter);
        /*
         * Pass parameter as list of type ProductEntry
         * Must be retrieved from database to here only
         * ProductEntry contains three fields:
         * ImageView productImage
         * TextView productName, productCost;
         * */
    }
    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton floatingActionButton = ((HomeActivity) getActivity()).getFloatingActionButton();
        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(null, null);
    }
}
