package com.jjoseba.pecsmobile.ui;

import com.jjoseba.pecsmobile.model.Card;

public interface NewCardListener {
    void onNewCard(Card card);
    void onCancel();
}
