package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.R;

public class OffersHistoryCardItemsViewHolder extends RecyclerView.ViewHolder{
    public CardView productCard;
    public ImageView imgCard;
    public TextView shopName;

    public TextView personProfession;

    public OffersHistoryCardItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        shopName = itemView.findViewById(R.id.off_shopname);
        productCard = itemView.findViewById(R.id.off_shopCard);
        // TODO: Find and store views from itemView
    }
}
