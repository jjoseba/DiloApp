package com.jjoseba.pecsmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.util.ReverseInterpolator;

import java.util.List;

public class CardGridAdapter extends ArrayAdapter<Card> {

    private List<Card> cards;
    private Context _ctx;
    private int layout;

    public static int bgOverlayColor;
    public static int transparentColor;

    public CardGridAdapter(Context context, int layout, List<Card> cards) {
        super(context, layout, cards);
        this.layout = layout;
        this.cards = cards;
        this._ctx = context;

        bgOverlayColor = _ctx.getResources().getColor(R.color.black_overlay);
        transparentColor = _ctx.getResources().getColor(R.color.transparent);
    }

    public class CardViewHolder{
        public TextView label;
        public ImageView image;
        public View cardFrame;
        public ImageView addButton;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        CardViewHolder holder;
        final Card card = cards.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(_ctx).inflate(layout, null);

            // Set up the ViewHolder
            holder = new CardViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.card_label);
            holder.image = (ImageView) convertView.findViewById(R.id.card_image);
            holder.cardFrame = convertView.findViewById(R.id.card_frame);
            holder.addButton = (ImageView) convertView.findViewById(R.id.addButton);

            convertView.setTag(holder);
        } else {
            holder = (CardViewHolder) convertView.getTag();
        }
        card.inflateCard(holder, _ctx);

        if (card.animateOnAppear || card.animateDeletion){
            Animation anim = AnimationUtils.loadAnimation(_ctx, R.anim.card_appear);
            anim.setDuration(750);
            if (card.animateDeletion) {
                anim.setInterpolator(new ReverseInterpolator(new DecelerateInterpolator()));
            }
            final View finalConvertView = convertView;
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    finalConvertView.setHasTransientState(true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (card.animateOnAppear){
                        card.animateOnAppear = false;
                    }
                    else if (card.animateDeletion){
                        cards.remove(card);
                        CardGridAdapter.this.notifyDataSetChanged();
                    }

                    finalConvertView.setHasTransientState(false);
                }
            });
            convertView.startAnimation(anim);
        }

        return convertView;

    }

    @Override
    public int getCount() {
        return cards.size() ;
    }

    @Override
    public Card getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void removeCard(Card card) {
        card.animateDeletion = true;
        notifyDataSetChanged();
    }
}
