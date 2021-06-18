package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.HomeActivity;
import com.creator.rewardsapp.Body.OfferWalls.Interfaces.LoadNearbyEvents;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.FixedVariable;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.OffersHistory;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters.OffersHistoryRecyclerViewAdapter;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.ProductGridItemDecoration;
import com.creator.rewardsapp.Common.CreateOfferObject;
import com.creator.rewardsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ParticipationHistory extends Fragment implements LoadNearbyEvents {
    private final String TAG = getClass().getSimpleName();
    static ParticipationHistory instance;
    private RecyclerView historyRecyclerView;
    LoadNearbyEvents loadMyConcepts;
    List<OffersHistory> shopsHistory;
    LoadNearbyEvents loadNearbyEvents;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    OffersHistoryRecyclerViewAdapter historyAdapter;
    private CircularProgressIndicator participationEventsLoader;
    private LinearLayout noHistoryonFailure;

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
/*
        shopsHistory.add(new OffersHistory("Manish General Store"));
        shopsHistory.add(new OffersHistory("Domino's Pizza"));
        shopsHistory.add(new OffersHistory("Burma & Sons."));*/

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        loadNearbyEvents = this;
    }

    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.participation_history, container, false);
        participationEventsLoader = root.findViewById(R.id.loader_pHistory_events);
        noHistoryonFailure = root.findViewById(R.id.no_pHistory_found);
        setRecyclerView(root);
        getParticipationHistory();
        return root;
    }

    private void setRecyclerView(View view) {

        // Set up the RecyclerView
        historyRecyclerView = view.findViewById(R.id.shops_history_recycler_view);
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));

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

    private void getParticipationHistory() {
        /*
         * Retrieve the current participant shop id in list=[]
         * Base on that id's, access the Offers collection shops
         * updateUi()
         * Done
         * */
        participationEventsLoader.show();
        Log.e("checker", "loadParticipationHistory: called");
        db.collection("Participants")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult().get("shops") != null) {
                                Log.e("checker", "onComplete: called" + task.getResult().get("shops"));

                                List<String> shops = (List<String>) task.getResult().get("shops");
                                Collections.sort(shops);
                                // Loading only participated offers id as per @shops returned
                                //TODO: WhereIn -> Participation History
                                db.collection("Offers")
                                        .get().addOnCompleteListener(task1 -> {
                                    List<CreateOfferObject> products = new ArrayList<>();
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot bannerSnapshot : task1.getResult()) {
                                            int pos = Collections.binarySearch(shops, bannerSnapshot.getId());

                                            if (pos > -1) {

                                                Log.e("errorLoad", "loadHistoryCards:\n " + bannerSnapshot.getData());
                                                CreateOfferObject product = bannerSnapshot.toObject(CreateOfferObject.class);
                                                Log.d("errorLoad", "card added :=> " + product.getOfferId() + " at pos: " + pos);

                                                products.add(product);
                                            }
                                        }

                                        loadNearbyEvents.onNearbyLoadSuccess(products);
                                    }

                                    participationEventsLoader.hide();
                                    if (products.isEmpty())
                                        noHistoryonFailure.setVisibility(View.VISIBLE);

                                });
                            } else {
                                Log.d(TAG, "getParticipationHistory() and given queryCancelled");
                                participationEventsLoader.hide();

                                noHistoryonFailure.setVisibility(View.VISIBLE);
                            }
                        }
                ).addOnFailureListener(e -> {
            loadNearbyEvents.onNearbyLoadFailed(e.getMessage());
            participationEventsLoader.hide();
        });
    }

    @Override
    public void onNearbyLoadSuccess(List<CreateOfferObject> templates) {
        historyAdapter = new OffersHistoryRecyclerViewAdapter(getActivity(), templates);
        historyRecyclerView.setAdapter(historyAdapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.updown_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.side_product_grid_spacing_small);
        historyRecyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
    }

    @Override
    public void onNearbyLoadFailed(String message) {
        FixedVariable.showToaster(getActivity(), message);
    }
}
