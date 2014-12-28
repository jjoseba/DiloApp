package com.jjoseba.pecsmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.model.CardPECS;

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

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) _ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        CardPECS card = cards.get(position);

        if (convertView == null) {

            gridView = new View(_ctx);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.card_gridview, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.card_label);
            textView.setText(card.getLabel());

            // set image based on selected text
            //ImageView imageView = (ImageView) gridView
            //        .findViewById(R.id.grid_item_image);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return cards.size();
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
