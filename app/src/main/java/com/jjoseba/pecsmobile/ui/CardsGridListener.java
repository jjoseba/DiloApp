package com.jjoseba.pecsmobile.ui;

import com.jjoseba.pecsmobile.model.Card;

public interface CardsGridListener {
    void onCardSelected(Card clicked);
    void onCardLongClick(Card clicked);
    void onAddCardButton(Card clicked);
    void onTempCardButton();
}
