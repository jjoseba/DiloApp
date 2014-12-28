package com.jjoseba.pecsmobile.model;

/**
 * Created by Joseba on 28/12/2014.
 */
public class CardPECS {

    private String label;
    private String imagePath;
    private String name;
    private boolean isCategory;

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
}
