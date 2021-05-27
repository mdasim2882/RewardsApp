package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.HelperClasses.OffersHistory;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders.OffersHistoryCardItemsViewHolder;
import com.creator.rewardsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OffersHistoryRecyclerViewAdapter extends RecyclerView.Adapter<OffersHistoryCardItemsViewHolder> implements Filterable {

    public final String TAG = getClass().getSimpleName();
    Context context;
    private List<OffersHistory> productList;
    private List<OffersHistory> productListall;
    Activity activity;
    FirebaseAuth fAuth;

    // Add search filter here
    Filter filter = new Filter() {
        // Runs on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<OffersHistory> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(productListall);
            } else {
                for (OffersHistory searchProduct : productListall) {
                    String prod = searchProduct.getOfferShopName().toLowerCase();
                    if (prod.contains(constraint.toString().toLowerCase())) {
                        filteredList.add(searchProduct);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredList;
            return filterResults;
        }

        // Runs on ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            productList.clear();
            productList.addAll((Collection<? extends OffersHistory>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public OffersHistoryRecyclerViewAdapter(Context context, List<OffersHistory> actualCards) {
        this.productList = actualCards;
        this.productListall = new ArrayList<>(actualCards);
        this.context = context;
        activity = (Activity) context;
        fAuth = FirebaseAuth.getInstance();
    }


    @NonNull
    @Override
    public OffersHistoryCardItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_history_details_card, parent, false);

        return new OffersHistoryCardItemsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersHistoryCardItemsViewHolder holder, int position) {
        String offerShopName = productList.get(position).getOfferShopName();
        holder.shopName.setText(offerShopName);
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