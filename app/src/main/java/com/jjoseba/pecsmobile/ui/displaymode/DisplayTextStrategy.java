package com.jjoseba.pecsmobile.ui.displaymode;


import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jjoseba.pecsmobile.R;
import com.jjoseba.pecsmobile.activity.BaseActivity;
import com.jjoseba.pecsmobile.activity.ShowCardsActivity;
import com.jjoseba.pecsmobile.activity.ShowTextActivity;
import com.jjoseba.pecsmobile.app.PECSMobile;
import com.jjoseba.pecsmobile.model.Card;

import java.util.ArrayList;

public class DisplayTextStrategy implements DisplayModeStrategy, TextToSpeech.OnInitListener {

    private ArrayList<Card> navCards;
    private TextView selectedCardsText;
    private TextToSpeech myTTS;
    private ResetListener listener;

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
    public void onResume(final BaseActivity activity, ArrayList<Card> navigationCards) {
        this.navCards = navigationCards;
        activity.findViewById(R.id.selected_cards_list).setVisibility(View.GONE);
        activity.findViewById(R.id.removeLastCard).setVisibility(View.GONE);

        listener.resetCards();
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
    }

    @Override
    public void onCardSelected(final BaseActivity activity, Card selectedCard) {
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
    public void setResetListener(ResetListener listener) {
        this.listener = listener;
    }

    @Override
    public void onInit(int status) {

    }

    private String updateTextDisplay(){
        String message = "";
        for (Card card : navCards){
            message += card.getLabel()==null ? "" : card.getLabel() + " ";
        }
        selectedCardsText.setText(message);
        return message;
    }
}
