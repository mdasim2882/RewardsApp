package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.HelperClasses.OffersEntry;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders.ShopsCardItemsViewHolder;
import com.creator.rewardsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShopsOffersRecyclerViewAdapter extends RecyclerView.Adapter<ShopsCardItemsViewHolder> implements Filterable {

    public final String TAG = getClass().getSimpleName();
    Context context;
    private List<OffersEntry> productList;
    private List<OffersEntry> productListall;
    Activity activity;
    FirebaseAuth fAuth;

    // Add search filter here
    Filter filter = new Filter() {
        // Runs on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<OffersEntry> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(productListall);
            } else {
                for (OffersEntry searchProduct : productListall) {
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
            productList.addAll((Collection<? extends OffersEntry>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public ShopsOffersRecyclerViewAdapter(Context context, List<OffersEntry> actualCards) {
        this.productList = actualCards;
        this.productListall = new ArrayList<>(actualCards);
        this.context = context;
        activity = (Activity) context;
        fAuth = FirebaseAuth.getInstance();
    }


    @NonNull
    @Override
    public ShopsCardItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_details_card, parent, false);

        return new ShopsCardItemsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopsCardItemsViewHolder holder, int position) {
        String offerShopName = productList.get(position).getOfferShopName();
        holder.shopName.setText(offerShopName);
        holder.participateButton.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Fetching Participation Form for "
                    + productList.get(position).getOfferShopName(), Toast.LENGTH_SHORT).show();
        });
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