package com.jjoseba.pecsmobile.ui;

import com.jjoseba.pecsmobile.model.Card;

/**
 * Created by Joseba on 28/12/2014.
 */
public interface CardsGridListener {
    void onCardSelected(Card clicked);
    void onCardLongClick(Card clicked);
    void onAddCardButton(Card clicked);
    void onTempCardButton();
}
