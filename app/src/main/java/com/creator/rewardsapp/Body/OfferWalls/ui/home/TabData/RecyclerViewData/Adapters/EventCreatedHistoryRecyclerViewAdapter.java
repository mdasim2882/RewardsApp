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

import com.creator.rewardsapp.Body.OfferWalls.ui.historyEvents.WinnerListActivity;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders.EventCreatedHistoryCardItemsViewHolder;
import com.creator.rewardsapp.Common.CreateOfferObject;
import com.creator.rewardsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventCreatedHistoryRecyclerViewAdapter extends RecyclerView.Adapter<EventCreatedHistoryCardItemsViewHolder> implements Filterable {

    public final String TAG = getClass().getSimpleName();
    Context context;
    private List<CreateOfferObject> productList;
    private List<CreateOfferObject> productListall;
    Activity activity;
    FirebaseAuth fAuth;

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
        holder.deleteBtn.setOnClickListener(v->{

            deleteitemAt(position);
        });
        holder.winnerListbtn.setOnClickListener(v -> {

            Intent i = new Intent(v.getContext(), WinnerListActivity.class);
            i.putExtra("ShopName", offerShopName);
            context.startActivity(i);
        });
        Log.d(TAG, "onBindViewHolder: Change with product list size= "+productList.size());

    }

    private void deleteitemAt(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
        productList.remove(position);
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