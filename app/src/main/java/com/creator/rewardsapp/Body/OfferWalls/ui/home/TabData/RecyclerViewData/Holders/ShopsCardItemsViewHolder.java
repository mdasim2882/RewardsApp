package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.R;

public class ShopsCardItemsViewHolder extends RecyclerView.ViewHolder{
    public CardView productCard;
    public ImageView imgCard;
    public TextView shopName;
    public Button participateButton;
    public TextView personProfession;

    public ShopsCardItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        shopName = itemView.findViewById(R.id.shopname);
        participateButton = itemView.findViewById(R.id.participateBtn);
        productCard = itemView.findViewById(R.id.shopCard);
        // TODO: Find and store views from itemView
    }
}
