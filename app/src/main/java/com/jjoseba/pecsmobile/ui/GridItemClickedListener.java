package com.jjoseba.pecsmobile.ui;

import com.jjoseba.pecsmobile.model.CardPECS;

/**
 * Created by Joseba on 28/12/2014.
 */
public interface GridItemClickedListener {
    public void onClick(CardPECS clicked, boolean addChildCard);
    public void onLongClick(CardPECS clicked);
}
