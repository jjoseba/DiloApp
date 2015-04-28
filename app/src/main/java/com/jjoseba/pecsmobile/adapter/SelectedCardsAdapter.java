package com.jjoseba.pecsmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.model.CardPECS;
import com.jjoseba.pecsmobile.ui.ButtonCard;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


public class SelectedCardsAdapter  extends BaseAdapter {

    private ArrayList<CardPECS> cards;
    private Context ctx;

    public SelectedCardsAdapter(Context context, ArrayList<CardPECS> cards){
        this.cards = cards;
        this.ctx = context;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected class CardViewHolder{
        TextView label;
        ImageView image;
        View cardFrame;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardViewHolder holder;
        CardPECS card = cards.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.card_small, null);

            // Set up the ViewHolder
            holder = new CardViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.card_image);
            holder.cardFrame = convertView.findViewById(R.id.card_frame);

            convertView.setTag(holder);
        } else {
            holder = (CardViewHolder) convertView.getTag();
        }

        holder.cardFrame.setBackgroundColor(card.getCardColor());
        holder.cardFrame.setVisibility(View.VISIBLE);
        String imagePath = card.getImagePath();
        if (imagePath != null){
            File image = new File(imagePath);
            if (image.exists()) {
                Picasso.with(ctx).load(image).into(holder.image);
            }
        }

        return convertView;

    }
}
