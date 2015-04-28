package com.jjoseba.pecsmobile.model;

import android.graphics.Color;

import com.jjoseba.pecsmobile.util.FileUtils;

import java.io.Serializable;

/**
 * Created by Joseba on 28/12/2014.
 */
public class CardPECS implements Serializable{

    public static int DEFAULT_COLOR = 0xFF555555;

    private String label;
    private String imagePath;
    private int cardId;
    private boolean isCategory;
    private String cardColor;

    //only for UI purposes
    public boolean animateOnAppear;
    public boolean animateDeletion;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImageFilename(){
        return imagePath;
    }

    public String getImagePath() {
        return (imagePath!=null && imagePath.length()>0)?(FileUtils.getImagesPath() + imagePath):"";
    }

    public void setImageFilename(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getCardId() {
        return cardId;
    }

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

}
