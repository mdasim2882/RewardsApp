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
import com.creator.rewardsapp.Body.OfferWalls.Interfaces.LoadMyCreatedEvents;
import com.creator.rewardsapp.Body.OfferWalls.Interfaces.LoadNearbyEvents;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.OffersEntry;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters.EventCreatedHistoryRecyclerViewAdapter;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.ProductGridItemDecoration;
import com.creator.rewardsapp.Common.CreateOfferObject;
import com.creator.rewardsapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class EventCreatedHistory extends Fragment implements LoadNearbyEvents, LoadMyCreatedEvents {
    RecyclerView recyclerView;
    EventCreatedHistoryRecyclerViewAdapter adapter;
    TextView emptyText;
    List<OffersEntry> shops;

    //Database
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String TAG = "dataview";
    LoadNearbyEvents myEvents;
    LoadMyCreatedEvents myCreatedEvents;

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
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        shops = new ArrayList<>();
        myEvents = this;
        myCreatedEvents = this;
       /* shops.add(new OffersEntry("Confectionary Shop"));
        shops.add(new OffersEntry("Baba Lassi"));
        shops.add(new OffersEntry("Pakeezah Juice Corner"));
        shops.add(new OffersEntry("Sumit General Store"));*/


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

        mAuth = FirebaseAuth.getInstance();

        db.collection("Shops")
                .document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                List<String> shopsId = (List<String>) document.get("shopId");
                Log.e(TAG, "onCreateView: " + shopsId);
                myCreatedEvents.onLoadMyCreatedEventsSuccess(shopsId);
            }
        });


        return root;
    }

    private void loadTemplates(List<String> shopsId) {
        Log.e(TAG, "loadTemplates Filter List: called with=" + shopsId);
        if(shopsId==null)
            return;
        db.collection("Offers")
                .whereIn("offerId", shopsId) //Shows only current user shops
                .get().addOnCompleteListener(task -> {
            Log.e(TAG, "loadTemplates: shopsId[]= " + shopsId);
            Log.e(TAG, "loadTemplates: QUERY " + task.getResult().getDocuments());
            List<CreateOfferObject> userEvent = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot bannerSnapshot : task.getResult()) {
                    Log.e("checker", "loadTemplates: " + bannerSnapshot.getData());
                    CreateOfferObject product = bannerSnapshot.toObject(CreateOfferObject.class);
                    userEvent.add(product);
                }
                myEvents.onNearbyLoadSuccess(userEvent);
            }
        }).addOnFailureListener(e -> Log.e(TAG, "onFailure: " + e.getMessage()))
                .addOnSuccessListener(documentSnapshots -> Log.e(TAG, "onSuccess: Array Filtered"));


    }

    private void setRecyclerView(View view) {

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.event_created_history_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));

    }


    @Override
    public void onNearbyLoadSuccess(List<CreateOfferObject> templates) {
        adapter = new EventCreatedHistoryRecyclerViewAdapter(getActivity(), templates);
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.updown_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.side_product_grid_spacing_small);
        recyclerView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

    }

    @Override
    public void onNearbyLoadFailed(String message) {

    }

    @Override
    public void onLoadMyCreatedEventsSuccess(List<String> myevents) {
        loadTemplates(myevents);
    }

    @Override
    public void onLoadMyCreatedEventsFailed(String message) {

    }
}