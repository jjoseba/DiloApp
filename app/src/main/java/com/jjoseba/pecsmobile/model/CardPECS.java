package com.jjoseba.pecsmobile.model;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by Joseba on 28/12/2014.
 */
public class CardPECS implements Serializable{

    public static int DEFAULT_COLOR = 0xFF555555;

    private String label;
    private String imagePath;
    private String name;
    private boolean isCategory;
    private String cardColor;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
