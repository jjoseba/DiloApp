package com.jjoseba.pecsmobile.ui.displaymode;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.BaseActivity;
import com.jjoseba.pecsmobile.activity.ShowTextActivity;
import com.jjoseba.pecsmobile.app.PECSMobile;
import com.jjoseba.pecsmobile.model.Card;
import com.jjoseba.pecsmobile.ui.cards.CardTempPECS;

import java.util.ArrayList;

public class DisplayTextStrategy implements DisplayModeStrategy {

    protected ArrayList<Card> navCards;
    private TextView selectedCardsText;
    protected ArrayList<Card> selectedCards = new ArrayList<>();
    protected ImageButton removeCardBtn;
    private ResetListener listener;

    @Override
    public int getDisplayMode() {
        return PECSMobile.DISPLAY_MODE_CARDS;
    }

    @Override
    public void initialize(BaseActivity activity, ArrayList<Card> navigationCards) {
        this.navCards = navigationCards;
        selectedCardsText = (TextView) activity.findViewById(R.id.selected_cards_text);
        removeCardBtn = (ImageButton) activity.findViewById(R.id.removeLastCard);
    }

    @Override
    public void onActivityResult(BaseActivity activity, int requestCode, int resultCode) {

    }

    @Override
    public void onResume(final BaseActivity activity, ArrayList<Card> navigationCards) {
        this.navCards = navigationCards;
        activity.findViewById(R.id.selected_cards_list).setVisibility(View.GONE);

        listener.resetCards();
        selectedCards.clear();
        selectedCardsText.setText("");

        selectedCardsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = selectedCardsText.getText().toString();
                if (text.trim()!=""){
                    Intent i = new Intent(activity, ShowTextActivity.class);
                    i.putExtra("text", text);
                    activity.startActivity(i);
                }
            }
        });
        removeCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCards.size() > 0) {
                    selectedCards.remove(selectedCards.size() - 1);
                    updateTextDisplay();
                }
            }
        });
    }

    @Override
    public void onCardSelected(final BaseActivity activity, Card selectedCard) {
        if (selectedCards.isEmpty() || !selectedCards.get(selectedCards.size()-1).equals(selectedCard)){
            //The last added card is different to the new one or there are no selected cards yet
            selectedCards.add(selectedCard);
            updateTextDisplay();
        }

    }

    @Override
    public void onNewTempTextCard(String cardLabel) {
        CardTempPECS card = new CardTempPECS();
        if (navCards.size()>1)
            card.setCardColor(navCards.get(navCards.size() - 1).getHexCardColor());
        card.setLabel(cardLabel);
        selectedCards.add(card);
        updateTextDisplay();
    }

    @Override
    public void onSelectedCardsChanged() {

    }

    @Override
    public void setResetListener(ResetListener listener) {
        this.listener = listener;
    }

    private String updateTextDisplay(){
        String message = "";
        for (Card card : selectedCards){
            message += card.getLabel()==null ? "" : card.getLabel() + " ";
        }
        selectedCardsText.setText(message);
        return message;
    }
}
