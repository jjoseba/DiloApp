package com.jjoseba.pecsmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.model.CardPECS;
import com.jjoseba.pecsmobile.ui.ButtonCard;
import com.jjoseba.pecsmobile.util.ReverseInterpolator;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class CardGridAdapter extends ArrayAdapter<CardPECS> {

    private List<CardPECS> cards;
    private Context _ctx;
    private int layout;

    private static int bgOverlayColor;
    private static int transparentColor;

    public CardGridAdapter(Context context, int layout, List<CardPECS> cards) {
        super(context, layout, cards);
        this.layout = layout;
        this.cards = cards;
        this._ctx = context;

        bgOverlayColor = _ctx.getResources().getColor(R.color.black_overlay);
        transparentColor = _ctx.getResources().getColor(R.color.transparent);
    }

    protected class CardViewHolder{
        TextView label;
        ImageView image;
        View cardFrame;
        ImageView addButton;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        CardViewHolder holder;
        final CardPECS card = cards.get(position);

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
        if (card instanceof ButtonCard){
            holder.cardFrame.setVisibility(View.INVISIBLE);
            holder.addButton.setImageResource(R.drawable.newcard);
            holder.addButton.setVisibility(View.VISIBLE);

            holder.addButton.setBackgroundColor(bgOverlayColor);
        }
        else{
            holder.cardFrame.setBackgroundColor(card.getCardColor());
            holder.label.setText(card.getLabel());
            holder.cardFrame.setVisibility(View.VISIBLE);
            holder.addButton.setVisibility(View.GONE);
            File imageFile = new File( card.getImagePath());
            Picasso.with(_ctx).load(imageFile).placeholder(R.drawable.empty).error(R.drawable.empty).into(holder.image);

            if (card.isDisabled()){
                holder.addButton.setBackgroundColor(transparentColor);
                holder.addButton.setVisibility(View.VISIBLE);
                holder.addButton.setImageResource(R.drawable.disabledcard);
            }

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

    public void removeCard(CardPECS card) {
        card.animateDeletion = true;
        notifyDataSetChanged();
    }
}
