package com.jjoseba.pecsmobile.ui;

import com.jjoseba.pecsmobile.model.CardPECS;

/**
 * Created by Joseba on 28/12/2014.
 */
public interface CardsGridListener {
    void onCardSelected(CardPECS clicked);
    void onCardLongClick(CardPECS clicked);
    void onAddCardButton(CardPECS clicked);
}