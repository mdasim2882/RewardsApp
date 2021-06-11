package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.Body.OfferWalls.ui.HelperClasses.FixedVariable;
import com.creator.rewardsapp.Body.OfferWalls.ui.PictureReceiptActivity;
import com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders.WinnerListCardItemsViewHolder;
import com.creator.rewardsapp.Common.ParticipateOfferObject;
import com.creator.rewardsapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WinnnerListRecyclerViewAdapter extends RecyclerView.Adapter<WinnerListCardItemsViewHolder> implements Filterable {

    public final String TAG = getClass().getSimpleName();
    Context context;
    private List<ParticipateOfferObject> productList;
    private List<ParticipateOfferObject> productListall;
    private  String viewingShopName;
    Activity activity;
    FirebaseAuth fAuth;

    // Add search filter here
    Filter filter = new Filter() {
        // Runs on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ParticipateOfferObject> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(productListall);
            } else {
                for (ParticipateOfferObject searchProduct : productListall) {
                    String prod = searchProduct.getFullname().toLowerCase();
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
            productList.addAll((Collection<? extends ParticipateOfferObject>) filterResults.values);
            notifyDataSetChanged();
        }
    };


    public WinnnerListRecyclerViewAdapter(Context context, List<ParticipateOfferObject> actualCards,String viewingShopName) {
        this.productList = actualCards;
        this.productListall = new ArrayList<>(actualCards);
        this.context = context;
        activity = (Activity) context;
        fAuth = FirebaseAuth.getInstance();
        this.viewingShopName=viewingShopName;
//        activity.recreate();
    }


    @NonNull
    @Override
    public WinnerListCardItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.winner_recview_card, parent, false);

        return new WinnerListCardItemsViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull WinnerListCardItemsViewHolder holder, int position) {
        String winnerName = productList.get(position).getFullname();
        String winnerContactno = productList.get(position).getContactno();
        String winnerAmount = productList.get(position).getBillvalue();
        String winnerBillreceipt = productList.get(position).getReceiptUrl();

        holder.winnerName.setText(winnerName);
        holder.winnerAmount.setText(winnerAmount);
        holder.winnerContactno.setText(winnerContactno);


        holder.donloadReciptbtn.setOnClickListener(v -> {

            Intent i=new Intent(activity, PictureReceiptActivity.class);
            i.putExtra(FixedVariable.getShopname,viewingShopName);
            i.putExtra(FixedVariable.getReceiptUrl,winnerBillreceipt);
            v.getContext().startActivity(i);
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