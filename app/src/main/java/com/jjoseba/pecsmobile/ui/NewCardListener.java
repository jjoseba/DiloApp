package com.jjoseba.pecsmobile.ui;

import com.jjoseba.pecsmobile.model.Card;

public interface NewCardListener {
    public void onNewCard(Card card);
    public void onCancel();
}
