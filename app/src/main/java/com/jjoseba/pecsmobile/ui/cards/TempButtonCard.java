package com.jjoseba.pecsmobile.ui.cards;

import android.content.Context;
import android.view.View;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardGridAdapter;

/**
 * Created by Joseba on 17/12/2015.
 */
public class TempButtonCard extends ButtonCard{

    @Override
    public void inflateCard(CardGridAdapter.CardViewHolder holder, Context ctx) {

        holder.cardFrame.setVisibility(View.INVISIBLE);
        holder.addButton.setImageResource(R.drawable.keyboard);
        holder.addButton.setVisibility(View.VISIBLE);
        holder.addButton.setBackgroundColor(CardGridAdapter.bgOverlayColor);
    }
}
