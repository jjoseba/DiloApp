package com.jjoseba.pecsmobile.ui.cards;

import android.content.Context;
import android.view.View;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardGridAdapter;

public class TempButtonCard extends ButtonCard{

    @Override
    public void inflateCard(CardGridAdapter.CardViewHolder holder, Context ctx) {

        holder.cardFrame.setVisibility(View.INVISIBLE);
        holder.buttonImage.setImageResource(R.drawable.keyboard);
        holder.buttonImage.setVisibility(View.VISIBLE);
        holder.buttonImage.setBackgroundColor(CardGridAdapter.bgOverlayColor);
    }
}
