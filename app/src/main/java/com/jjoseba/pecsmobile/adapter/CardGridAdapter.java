package com.jjoseba.pecsmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.model.CardPECS;
import com.jjoseba.pecsmobile.ui.ButtonCard;

import java.util.List;

/**
 * Created by Joseba on 28/12/2014.
 */
public class CardGridAdapter extends ArrayAdapter<CardPECS> {

    private List<CardPECS> cards;
    private Context _ctx;

    public CardGridAdapter(Context context, int layout, List<CardPECS> cards) {
        super(context, layout, cards);
        this.cards = cards;
        this._ctx = context;
    }

    protected class CardViewHolder{
        TextView label;
        ImageView image;
        View cardFrame;
        View addButton;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        CardViewHolder holder;
        CardPECS card = cards.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(_ctx).inflate(R.layout.card_gridview, null);

            // Set up the ViewHolder
            holder = new CardViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.card_label);
            holder.image = (ImageView) convertView.findViewById(R.id.card_image);
            holder.cardFrame = convertView.findViewById(R.id.card_frame);
            holder.addButton = convertView.findViewById(R.id.addButton);

            convertView.setTag(holder);
        } else {
            holder = (CardViewHolder) convertView.getTag();
        }
        if (card instanceof ButtonCard){
            holder.cardFrame.setVisibility(View.INVISIBLE);
            holder.addButton.setVisibility(View.VISIBLE);
        }
        else{
            holder.cardFrame.setBackgroundColor(card.getCardColor());
            holder.label.setText(card.getLabel());
            holder.image.setImageResource(R.drawable.ic_launcher);
            holder.cardFrame.setVisibility(View.VISIBLE);
            holder.addButton.setVisibility(View.GONE);
        }
        return convertView;

    }

    @Override
    public int getCount() {
        return cards.size() ;
    }

    @Override
    public CardPECS getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
