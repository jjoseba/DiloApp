package com.jjoseba.pecsmobile.ui.cards;

import android.content.Context;
import android.view.View;

import com.jjoseba.pecsmobile.adapter.CardsAdapter;

public class CardTempPECS extends CardPECS {
    @Override
    public void updateHolder(CardsAdapter.CardViewHolder holder, Context ctx) {

        holder.cardFrame.setBackgroundColor(this.getCardColor());
        holder.cardFrame.setVisibility(View.VISIBLE);
        holder.buttonImage.setVisibility(View.GONE);
        holder.imageText.setText(this.getLabel());
        holder.imageText.setVisibility(View.VISIBLE);
        holder.imageText.setTextColor(this.getCardColor());
        holder.image.setVisibility(View.INVISIBLE);
        holder.label.setText(this.getLabel());

    }
}
