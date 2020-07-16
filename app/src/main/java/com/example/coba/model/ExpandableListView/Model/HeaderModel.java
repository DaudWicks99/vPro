package com.example.coba.model.ExpandableListView.Model;

import java.util.ArrayList;
import java.util.List;

public class HeaderModel {
    String title;
    int resource = -1;
    int icon = -1;
    int color;
    boolean isNew = false;
    boolean hasChild = false;
    boolean isSelected = false;

    List<ChildModel> childModelList = new ArrayList<>();

    public HeaderModel(String title){
        this.title = title;
    }

    public HeaderModel(String title, int resource,int color){
        this.title = title;
        this.resource = resource;
    }
    public HeaderModel(int icon, String title,int color){
        this.icon=icon;
        this.title = title;
        this.color=color;
    }

    public HeaderModel(String title, int resource,int color, boolean hasChild){
        this.title = title;
        this.resource = resource;
        this.hasChild = hasChild;
        this.color=color;
    }

    public HeaderModel(int icon,String title, boolean hasChild, boolean isNew
            , boolean isSelected){
        this.title = title;
        this.icon=icon;
        this.isNew = isNew;
        this.hasChild = hasChild;
        this.isSelected = isSelected;
    }

    public HeaderModel(String title, int resource,int color, boolean hasChild, boolean isNew
            , boolean isSelected
    ){
        this.title = title;
        this.resource = resource;
        this.isNew = isNew;
        this.hasChild = hasChild;
        this.isSelected = isSelected;
        this.color=color;
    }
    public HeaderModel(int icon,String title, int resource,int color, boolean hasChild, boolean isNew
            , boolean isSelected
    ){
        this.icon=icon;
        this.title = title;
        this.resource = resource;
        this.isNew = isNew;
        this.hasChild = hasChild;
        this.isSelected = isSelected;
        this.color=color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public HeaderModel addChildModel(ChildModel childModel){
        this.childModelList.add(childModel);

        return this;
    }

    public List<ChildModel> getChildModelList() {
        return childModelList;
    }

    public void setChildModelList(List<ChildModel> childModelList) {
        this.childModelList = childModelList;
    }
}
