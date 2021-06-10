package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.R;

public class WinnerListCardItemsViewHolder extends RecyclerView.ViewHolder{
    public CardView productCard;
    public TextView winnerName;
    public TextView winnerContactno;
    public Button donloadReciptbtn;
    public TextView winnerAmount;

    public WinnerListCardItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        winnerName = itemView.findViewById(R.id.winnerName);
        donloadReciptbtn = itemView.findViewById(R.id.dlReceiptBtn);
        productCard = itemView.findViewById(R.id.myShopWinnerListCards);

        winnerContactno = itemView.findViewById(R.id.winnerContactno);
        winnerAmount = itemView.findViewById(R.id.winnerAmount);


        // TODO: Find and store views from itemView
    }
}
