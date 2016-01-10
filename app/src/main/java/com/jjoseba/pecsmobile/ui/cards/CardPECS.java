package com.jjoseba.pecsmobile.ui.cards;

import android.content.Context;
import android.view.View;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.adapter.CardGridAdapter;
import com.jjoseba.pecsmobile.model.Card;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Joseba on 17/12/2015.
 */
public class CardPECS extends Card {

    @Override
    public void inflateCard(CardGridAdapter.CardViewHolder holder, Context ctx) {

        holder.cardFrame.setBackgroundColor(this.getCardColor());
        holder.label.setText(this.getLabel());
        holder.cardFrame.setVisibility(View.VISIBLE);
        holder.addButton.setVisibility(View.GONE);
        File imageFile = new File( this.getImagePath());
        Picasso.with(ctx).load(imageFile).placeholder(R.drawable.empty).error(R.drawable.empty).into(holder.image);

        if (this.isDisabled()){
            holder.addButton.setBackgroundColor(CardGridAdapter.transparentColor);
            holder.addButton.setVisibility(View.VISIBLE);
            holder.addButton.setImageResource(R.drawable.disabledcard);
        }
    }

}
