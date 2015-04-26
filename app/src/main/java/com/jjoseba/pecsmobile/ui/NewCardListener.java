package com.jjoseba.pecsmobile.ui;

import com.jjoseba.pecsmobile.model.CardPECS;

public interface NewCardListener {
    public void onNewCard(CardPECS card);
    public void onCancel();
}
