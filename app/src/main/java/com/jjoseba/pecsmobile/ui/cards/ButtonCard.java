package com.jjoseba.pecsmobile.ui.cards;

import android.content.Context;
import android.view.View;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardGridAdapter;
import com.jjoseba.pecsmobile.model.Card;


public class ButtonCard extends Card {
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.button_gridview;
    }

    @Override
    public void inflateCard(CardGridAdapter.CardViewHolder holder, Context ctx) {

        holder.cardFrame.setVisibility(View.INVISIBLE);
        holder.buttonImage.setImageResource(R.drawable.newcard);
        holder.buttonImage.setVisibility(View.VISIBLE);
        holder.buttonImage.setBackgroundColor(CardGridAdapter.bgOverlayColor);
    }
}
