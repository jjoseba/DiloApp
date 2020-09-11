package com.jjoseba.pecsmobile.model;

import android.content.Context;
import android.graphics.Color;

import com.jjoseba.pecsmobile.adapter.CardGridAdapter;
import com.jjoseba.pecsmobile.util.FileUtils;

import java.io.Serializable;

public abstract class Card implements Serializable{

    public static final int DEFAULT_COLOR = 0xFF555555;

    private String label;
    private String imagePath;
    private int cardId;
    private boolean isCategory;
    private boolean disabled;
    private String cardColor;
    private int parentID;

    //only for UI purposes
    public boolean animateOnAppear;
    public boolean animateDeletion;

    public abstract int getLayoutResource();

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getImageFilename(){
        return imagePath;
    }
    public void setImageFilename(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return (imagePath!=null && imagePath.length()>0)?(FileUtils.getImagesPath() + imagePath):"";
    }

    public int getCardId() { return cardId; }
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public boolean isCategory() {
        return isCategory;
    }
    public void setAsCategory(boolean category) {
        this.isCategory = category;
    }

    public String getHexCardColor() { return cardColor; }
    public void setCardColor(String cardColor) { this.cardColor = cardColor; }
    public int getCardColor(){ return ( cardColor!=null? Color.parseColor(cardColor) : DEFAULT_COLOR); }

    public void setDisabled(boolean state){ disabled = state; }
    public boolean isDisabled(){ return disabled; }

    public int getParentID() { return parentID; }
    public void setParentID(int parentID) { this.parentID = parentID; }

    public abstract void inflateCard(CardGridAdapter.CardViewHolder viewHolder, Context ctx);

}
