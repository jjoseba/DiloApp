package com.jjoseba.pecsmobile.ui.cards;

import android.content.Context;
import android.view.View;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardsAdapter;
import com.jjoseba.pecsmobile.model.Card;
import com.squareup.picasso.Picasso;

import java.io.File;

public class CardPECS extends Card {

    @Override
    public int getLayoutResource() {
        return R.layout.griditem_card;
    }

    @Override
    public void updateHolder(CardsAdapter.CardViewHolder holder, Context ctx) {

        holder.cardFrame.setBackgroundColor(this.getCardColor());
        holder.label.setText(this.getLabel());
        holder.cardFrame.setVisibility(View.VISIBLE);
        holder.buttonImage.setVisibility(View.GONE);
        holder.imageText.setVisibility(View.GONE);
        holder.image.setVisibility(View.VISIBLE);
        File imageFile = new File( this.getImagePath());
        Picasso.with(ctx).load(imageFile).placeholder(R.drawable.empty).error(R.drawable.empty).into(holder.image);

        if (this.isDisabled()){
            holder.buttonImage.setVisibility(View.VISIBLE);
            holder.buttonImage.setImageResource(R.drawable.disabledcard);
        }
    }

}
