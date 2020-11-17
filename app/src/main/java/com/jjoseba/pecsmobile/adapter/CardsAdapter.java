package com.jjoseba.pecsmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.cards.ButtonCard;
import com.jjoseba.pecsmobile.util.ReverseInterpolator;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardViewHolder> {

    private static final int CARD_TYPE_PECS = 0;
    private static final int CARD_TYPE_BUTTON = 1;

    private CardListener listener;
    private final List<Card> cards;
    private final Context ctx;


    public interface CardListener {
        void cardClicked(Card card);
        void cardLongClicked(Card card);
    }

    public CardsAdapter(Context context, List<Card> cards){
        this.cards = cards;
        this.ctx = context;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public TextView label;
        public TextView imageText;
        public ImageView image;
        public View cardFrame;
        public ImageView buttonImage;

        CardViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_image);
            imageText = itemView.findViewById(R.id.card_imageText);
            label = itemView.findViewById(R.id.card_label);
            cardFrame = itemView.findViewById(R.id.card_frame);
            buttonImage = itemView.findViewById(R.id.button_image);
        }
    }

    private @LayoutRes int getLayoutByViewType(int viewType){
        switch (viewType) {
            case CARD_TYPE_PECS:
                return R.layout.griditem_card;
            case CARD_TYPE_BUTTON:
                return R.layout.griditem_button;
        }
        return R.layout.griditem_card;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(ctx).inflate(getLayoutByViewType(viewType), parent, false);
        return new CardViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder viewHolder, final int position) {
        Card card = cards.get(position);
        card.updateHolder(viewHolder, ctx);

        viewHolder.itemView.setOnClickListener(view -> {
            if (listener != null) listener.cardClicked(card);
        });
        viewHolder.itemView.setOnLongClickListener(view -> {
            if (listener != null) listener.cardLongClicked(card);
            return true;
        });

        if (card.animateOnAppear || card.animateDeletion){
            Animation anim = AnimationUtils.loadAnimation(ctx, R.anim.card_appear);
            anim.setDuration(750);
            if (card.animateDeletion) {
                anim.setInterpolator(new ReverseInterpolator(new DecelerateInterpolator()));
            }
            final View finalConvertView = viewHolder.itemView;
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
                        notifyDataSetChanged();
                    }
                    finalConvertView.setHasTransientState(false);
                }
            });
            finalConvertView.startAnimation(anim);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Define a way to determine which layout to use, here it's just evens and odds.
        Card card = cards.get(position);
        return (card instanceof ButtonCard ? CARD_TYPE_BUTTON : CARD_TYPE_PECS);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void setOnClickListener(CardListener listener){
        this.listener = listener;
    }

}
