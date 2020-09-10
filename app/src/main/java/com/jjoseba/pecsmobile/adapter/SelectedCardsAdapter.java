package com.jjoseba.pecsmobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.cards.CardTempPECS;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class SelectedCardsAdapter  extends RecyclerView.Adapter<SelectedCardsAdapter.CardViewHolder> {

    private ArrayList<Card> cards;
    private OnClickListener listener;
    private Context ctx;

    public interface OnClickListener{
        void onClick();
    }

    public SelectedCardsAdapter(Context context, ArrayList<Card> cards){
        this.cards = cards;
        this.ctx = context;
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        private TextView label;
        private ImageView image;
        private View cardFrame;

        CardViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.card_image);
            label = itemView.findViewById(R.id.card_label);
            cardFrame = itemView.findViewById(R.id.card_frame);
            itemView.setOnClickListener(view -> {
                if (listener != null) listener.onClick();
            });
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(ctx).inflate(R.layout.card_small, parent, false);
        return new CardViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder viewHolder, final int position) {
        Card card = cards.get(position);

        viewHolder.cardFrame.setBackgroundColor(card.getCardColor());
        viewHolder.cardFrame.setVisibility(View.VISIBLE);
        if (card instanceof CardTempPECS){
            //holder.image.setVisibility(View.INVISIBLE);
            viewHolder.image.setImageDrawable(null);
            viewHolder.label.setVisibility(View.VISIBLE);
            viewHolder.label.setTextColor(card.getCardColor());
            viewHolder.label.setText(card.getLabel());
        }
        else{
            viewHolder.image.setVisibility(View.VISIBLE);
            viewHolder.label.setVisibility(View.GONE);
            File imageFile = new File(card.getImagePath());
            Picasso.with(ctx).load(imageFile).placeholder(R.drawable.empty).error(R.drawable.empty).into(viewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

}
