package com.mastercard.labs.mpqrmerchant.data.model;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/16/17
 */
public class Settings {
    private String title;
    private String value;
    private boolean isEditable;

    public Settings(String title, String value, boolean isEditable) {
        this.title = title;
        this.value = value;
        this.isEditable = isEditable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }
}
