package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.FixedVariable;
import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.MyCollectionNames;
import com.creator.rewardsapp.Body.OfferWalls.ui.historyEvents.WinnerListActivity;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders.EventCreatedHistoryCardItemsViewHolder;
import com.creator.rewardsapp.Common.CreateOfferObject;
import com.creator.rewardsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventCreatedHistoryRecyclerViewAdapter extends RecyclerView.Adapter<EventCreatedHistoryCardItemsViewHolder> implements Filterable {

    public final String TAG = getClass().getSimpleName();
    Context context;
    private List<CreateOfferObject> productList;
    private List<CreateOfferObject> productListall;
    Activity activity;
    FirebaseAuth fAuth;
    private FirebaseFirestore db;

    // Add search filter here
    Filter filter = new Filter() {
        // Runs on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CreateOfferObject> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(productListall);
            } else {
                for (CreateOfferObject searchProduct : productListall) {
                    String prod = searchProduct.getShopname().toLowerCase();
                    if (prod.contains(constraint.toString().toLowerCase())) {
                        filteredList.add(searchProduct);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        // Runs on ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            productList.clear();
            productList.addAll((Collection<? extends CreateOfferObject>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public EventCreatedHistoryRecyclerViewAdapter(Context context, List<CreateOfferObject> actualCards) {
        this.productList = actualCards;
        this.productListall = new ArrayList<>(actualCards);
        this.context = context;
        activity = (Activity) context;
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public EventCreatedHistoryCardItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_created_history_recview_card, parent, false);

        return new EventCreatedHistoryCardItemsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCreatedHistoryCardItemsViewHolder holder, int position) {
        String offerShopName = productList.get(position).getShopname();
        holder.shopName.setText(offerShopName);
        String startDate = productList.get(position).getStartDate();
        String endDate = productList.get(position).getEndDate();
        String firstOffer = productList.get(position).getFirstOffer();
        String secondOffer = productList.get(position).getFirstOffer();
        String offerId = productList.get(position).getOfferId();
        String totalParticipants = "" + (productList.get(position).getMaxParticipants());


        holder.sDate.setText(startDate);
        holder.eDate.setText(endDate);
        holder.secondOffer.setText(firstOffer);
        holder.firstOffer.setText(firstOffer);
        holder.shopName.setText(offerShopName);
        holder.totalParticipants.setText(totalParticipants);

        holder.deleteBtn.setOnClickListener(v -> {

            deleteitemAt(position);
        });
        boolean winnerDeclared = productList.get(position).isWinnerDeclared();
        holder.winnerListbtn.setOnClickListener(v -> {
            if (winnerDeclared) {
                Intent i = new Intent(v.getContext(), WinnerListActivity.class);
                i.putExtra(FixedVariable.SHOP_NAME, offerShopName);
                i.putExtra(FixedVariable.CURRENT_OFFER_ID, offerId);
                context.startActivity(i);
            } else
                FixedVariable.showToaster(activity, "Winners not declared yet.");

        });


        Log.d(TAG, "onBindViewHolder: Change with product list size= " + productList.size());

    }

    private void deleteitemAt(int position) {
        notifyItemRemoved(position);
        removeAndUpdateDocumentFromFirestore(position);
        notifyItemRangeChanged(position, productList.size());
        productList.remove(position);
    }

    private void removeAndUpdateDocumentFromFirestore(int position) {

        /*  Update OFFERS collection with offersId field
         *   winnersDeclared to false
         *
         *   */
        String myOfferId = productList.get(position).getOfferId();
        db.collection(MyCollectionNames.OFFERS)
                .document(myOfferId)
                .delete();
        //Map to remove user from array
        final Map<String, Object> removeUserFromArrayMap = new HashMap<>();
        removeUserFromArrayMap.put("shopId", FieldValue.arrayRemove(myOfferId));

        //use either maps to add or remove user
        db.collection(MyCollectionNames.SHOPS).document(productList.get(position).getCreatorId())
                .update(removeUserFromArrayMap);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
}