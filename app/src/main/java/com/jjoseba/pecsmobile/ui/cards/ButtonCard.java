package com.jjoseba.pecsmobile.ui.cards;

import android.content.Context;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardsAdapter;
import com.jjoseba.pecsmobile.model.Card;

public class ButtonCard extends Card {

    @Override
    public int getLayoutResource() {
        return R.layout.griditem_button;
    }

    @Override
    public void updateHolder(CardsAdapter.CardViewHolder holder, Context ctx) {
        holder.buttonImage.setImageResource(R.drawable.ic_card_add);
    }
}
