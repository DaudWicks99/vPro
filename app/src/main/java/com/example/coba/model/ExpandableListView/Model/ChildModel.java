package com.example.coba.model.ExpandableListView.Model;

public class ChildModel {
    String title;
    int color;
    boolean isSelected;

    public ChildModel(String title){
        this.title = title;
    }

    public ChildModel(String title, boolean isSelected){
        this.title = title;
        this.isSelected = isSelected;
    }
    public ChildModel(String title,int color, boolean isSelected){
        this.title = title;
        this.color=color;
        this.isSelected = isSelected;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
