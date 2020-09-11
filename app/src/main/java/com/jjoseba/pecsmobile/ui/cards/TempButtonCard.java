package com.jjoseba.pecsmobile.ui.cards;

import android.content.Context;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardsAdapter;

public class TempButtonCard extends ButtonCard{

    @Override
    public void updateHolder(CardsAdapter.CardViewHolder holder, Context ctx) {
        holder.buttonImage.setImageResource(R.drawable.ic_keyboard);
    }
}
