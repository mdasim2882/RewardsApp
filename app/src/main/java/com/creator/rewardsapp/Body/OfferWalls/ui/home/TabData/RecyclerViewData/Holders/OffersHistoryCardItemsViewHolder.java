package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.R;

public class OffersHistoryCardItemsViewHolder extends RecyclerView.ViewHolder{
    public CardView productCard;

    public TextView shopName,off_endDate;
    public TextView off_startDate,off_FirstOffer;
    public TextView off_sencondOffer,off_result;

    public TextView personProfession;

    public OffersHistoryCardItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        shopName = itemView.findViewById(R.id.off_shopname);
        off_startDate = itemView.findViewById(R.id.off_start_date);
        off_endDate = itemView.findViewById(R.id.off_end_date);
        off_FirstOffer = itemView.findViewById(R.id.off_first_offer);
        off_sencondOffer = itemView.findViewById(R.id.off_second_offer);
        off_result = itemView.findViewById(R.id.off_result);

        productCard = itemView.findViewById(R.id.off_shopCard);
        // TODO: Find and store views from itemView
    }
}
