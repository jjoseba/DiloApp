package com.jjoseba.pecsmobile.ui.displaymode;


import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.BaseActivity;
import com.jjoseba.pecsmobile.app.PECSMobile;
import com.jjoseba.pecsmobile.model.Card;

import java.util.ArrayList;

public class DisplayTextStrategy implements DisplayModeStrategy, TextToSpeech.OnInitListener {

    private ArrayList<Card> navCards;
    private TextView selectedCardsText;
    private TextToSpeech myTTS;

    @Override
    public int getDisplayMode() {
        return PECSMobile.DISPLAY_MODE_TEXT;
    }

    @Override
    public void initialize(BaseActivity activity, ArrayList<Card> navigationCards) {
        this.navCards = navigationCards;
        selectedCardsText = (TextView) activity.findViewById(R.id.selected_cards_text);
        myTTS = new TextToSpeech(activity, this);
    }

    @Override
    public void onActivityResult(BaseActivity activity, int requestCode, int resultCode) {
        if (requestCode == 0) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(activity, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                activity.startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void onResume(BaseActivity activity, ArrayList<Card> navigationCards) {
        this.navCards = navigationCards;
        activity.findViewById(R.id.selected_cards_list).setVisibility(View.GONE);
        activity.findViewById(R.id.removeLastCard).setVisibility(View.GONE);
        selectedCardsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTTS.speak(selectedCardsText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    @Override
    public void onCardSelected(Card selectedCard) {
        updateTextDisplay();
        String newMessage = selectedCardsText.getText().toString() + selectedCard.getLabel();
        selectedCardsText.setText(newMessage);
    }

    @Override
    public void onNewTempTextCard(String cardLabel) {
        updateTextDisplay();
        String cardsMessage = selectedCardsText.getText().toString() + cardLabel;
        selectedCardsText.setText(cardsMessage);
    }

    @Override
    public void onSelectedCardsChanged() {
        updateTextDisplay();
    }

    @Override
    public void onInit(int status) {

    }

    private void updateTextDisplay(){
        String message = "";
        for (Card card : navCards){
            message += card.getLabel()==null ? "" : card.getLabel() + " ";
        }
        selectedCardsText.setText(message);
    }
}
