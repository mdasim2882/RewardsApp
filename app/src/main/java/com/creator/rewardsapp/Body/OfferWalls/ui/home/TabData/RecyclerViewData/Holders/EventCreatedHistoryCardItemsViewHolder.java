package com.creator.rewardsapp.Body.OfferWalls.ui.home.TabData.RecyclerViewData.Holders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.creator.rewardsapp.R;
import com.github.clans.fab.FloatingActionButton;

public class EventCreatedHistoryCardItemsViewHolder extends RecyclerView.ViewHolder{
    public CardView productCard;
    public ImageView imgCard;
    public TextView shopName;
    public FloatingActionButton deleteBtn;
    public Button winnerListbtn;
    public TextView totalParticipants;

    public EventCreatedHistoryCardItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        shopName = itemView.findViewById(R.id.myShopName);
        winnerListbtn = itemView.findViewById(R.id.winnerListBtn);
        productCard = itemView.findViewById(R.id.myOfferCard);
       deleteBtn = itemView.findViewById(R.id.deletebutton);

        totalParticipants = itemView.findViewById(R.id.total_participants);
        // TODO: Find and store views from itemView
    }
}
