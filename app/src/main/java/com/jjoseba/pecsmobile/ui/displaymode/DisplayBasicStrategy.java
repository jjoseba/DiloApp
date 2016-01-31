package com.jjoseba.pecsmobile.ui.displaymode;

import android.content.Intent;

import com.jjoseba.pecsmobile.activity.BaseActivity;
import com.jjoseba.pecsmobile.activity.ShowCardsActivity;
import com.jjoseba.pecsmobile.app.PECSMobile;
import com.jjoseba.pecsmobile.model.Card;

import java.util.ArrayList;

public class DisplayBasicStrategy extends DisplayCardsStrategy {
    @Override
    public int getDisplayMode() {
        return PECSMobile.DISPLAY_MODE_BASIC;
    }

    @Override
    public void onCardSelected(final BaseActivity activity, Card selectedCard) {
        if (!selectedCard.isCategory()){
            Intent i = new Intent(activity, ShowCardsActivity.class);
            ArrayList<Card> viewCard = new ArrayList<>();
            viewCard.add(selectedCard);
            i.putExtra("result", viewCard);
            activity.startActivity(i);
        }
    }

    @Override
    public void setResetListener(ResetListener listener) {

    }
}
