package com.jjoseba.pecsmobile.ui.displaymode;

import com.jjoseba.pecsmobile.activity.BaseActivity;
import com.jjoseba.pecsmobile.model.Card;

import java.util.ArrayList;

public interface DisplayModeStrategy {

    interface ResetListener{ void resetCards(); }

    int getDisplayMode();
    void initialize(BaseActivity activity, ArrayList<Card> navigationCards);
    void onActivityResult(BaseActivity activity, int requestCode, int resultCode);
    void onResume(BaseActivity activity, ArrayList<Card> navigationCards);
    void onCardSelected(final BaseActivity activity, Card selectedCard);
    void onNewTempTextCard(String cardLabel);
    void onSelectedCardsChanged();
    void setResetListener(ResetListener listener);
}
