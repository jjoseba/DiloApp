package com.jjoseba.pecsmobile.ui.displaymode;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.BaseActivity;
import com.jjoseba.pecsmobile.activity.ShowCardsActivity;
import com.jjoseba.pecsmobile.adapter.SelectedCardsAdapter;
import com.jjoseba.pecsmobile.app.PECSMobile;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.cards.CardTempPECS;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class DisplayCardsStrategy implements DisplayModeStrategy {

    private ArrayList<Card> navCards;
    private ArrayList<Card> selectedCards = new ArrayList<>();
    private SelectedCardsAdapter selectedCardsAdapter;
    private ImageButton removeCardBtn;
    private ResetListener listener;

    @Override
    public int getDisplayMode() {
        return PECSMobile.DISPLAY_MODE_CARDS;
    }

    @Override
    public void initialize(BaseActivity activity, ArrayList<Card> navigationCards) {
        this.navCards = navigationCards;
        selectedCardsAdapter = new SelectedCardsAdapter(activity, selectedCards);
        removeCardBtn = activity.findViewById(R.id.removeLastCard);
        RecyclerView selectedCardsList = activity.findViewById(R.id.selected_cards_list);
        selectedCardsList.setAdapter(selectedCardsAdapter);
    }

    @Override
    public void onActivityResult(BaseActivity activity, int requestCode, int resultCode) {

    }

    @Override
    public void onResume(final BaseActivity activity, ArrayList<Card> navigationCards) {
        this.navCards = navigationCards;
        activity.findViewById(R.id.selected_cards_text).setVisibility(View.GONE);

        listener.resetCards();
        selectedCards.clear();
        selectedCardsAdapter.setOnClickListener(() -> {
            Intent i = new Intent(activity, ShowCardsActivity.class);
            i.putExtra("result", selectedCards);
            activity.startActivity(i);
        });

        removeCardBtn.setOnClickListener(v -> {
            if (selectedCards.size() > 0) {
                selectedCards.remove(selectedCards.size() - 1);
                selectedCardsAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCardSelected(final BaseActivity activity, Card selectedCard) {
        if (!selectedCard.isCategory() && !selectedCards.contains(selectedCard)){
            selectedCards.add(selectedCard);
            selectedCardsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNewTempTextCard(String cardLabel) {
        CardTempPECS card = new CardTempPECS();
        if (navCards.size()>1)
            card.setCardColor(navCards.get(navCards.size() - 1).getHexCardColor());
        card.setLabel(cardLabel);
        selectedCards.add(card);
        selectedCardsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSelectedCardsChanged() {
        selectedCardsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setResetListener(ResetListener listener) {
        this.listener = listener;
    }
}
