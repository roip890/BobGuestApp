package com.bob.bobguestapp.tools.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public abstract class SortFilterAdapter<ObjectType, ViewHolderType extends RecyclerView.ViewHolder, FilterType extends ListFilter<ObjectType>>
        extends RecyclerView.Adapter<ViewHolderType> {

    protected List<ObjectType> allObjects;
    protected List<ObjectType> objectsToShow;
    protected FilterType filter;
    protected String sortingType;
    protected boolean ascending;
    protected HashMap<String, Comparator<ObjectType>> comparators;

    

        public void setObjects(List<ObjectType> objects) {

            if (this.allObjects != null) {
                this.allObjects.clear();
            } else {
                this.allObjects = new ArrayList<ObjectType>();
            }

            if (objects != null) {
                this.allObjects.addAll(objects);
            }
            this.updateObjects();

        }

        public void addObjects(List<ObjectType> objects) {

            if (this.allObjects == null) {
                this.allObjects = new ArrayList<ObjectType>();
            }

        if (objects != null) {
            this.allObjects.addAll(objects);
        }
        this.updateObjects();

    }


    protected void updateObjects() {
        
        this.filterObjects();
        
        this.sortObjects();
        
    }
    
    public void filterObjects() {

        if (this.objectsToShow != null) {
            this.objectsToShow.clear();
        } else {
            this.objectsToShow = new ArrayList<ObjectType>();
        }

        if (this.allObjects != null) {
            if (this.filter != null) {
                this.objectsToShow.addAll(this.filter.getFilteredList(this.allObjects));
            } else {
                this.objectsToShow.addAll(this.allObjects);
            }
        }
        
    }

    protected void sortObjects() {
        
        if (this.objectsToShow != null && this.sortingType != null
            &&this.comparators.containsKey(sortingType)
            && this.comparators.get(this.sortingType) != null) {

            Comparator<ObjectType> comparator = this.comparators.get(sortingType);
            if (comparator != null) {
                Collections.sort(this.objectsToShow, comparator);
                if (!this.ascending) {
                    Collections.reverse(this.objectsToShow);
                }
            }

        }
        
    }


    public List<ObjectType> getAllObjects() {
        return allObjects;
    }

    public void setAllObjects(List<ObjectType> allObjects) {
        this.allObjects = allObjects;
    }

    public List<ObjectType> getObjectsToShow() {
        return objectsToShow;
    }

    public void setObjectsToShow(List<ObjectType> objectsToShow) {
        this.objectsToShow = objectsToShow;
    }

    public FilterType getFilter() {
        return filter;
    }

    public void setFilter(FilterType filter) {
        
        this.filter = filter;

        this.updateObjects();

        this.notifyDataSetChanged();
        
    }

    public String getSortingType() {
        return sortingType;
    }

    public void setSortingType(String sortingType) {
        
        this.sortingType = sortingType;

        this.updateObjects();

        this.notifyDataSetChanged();
        
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        
        this.ascending = ascending;
        
        this.updateObjects();
        
        this.notifyDataSetChanged();
        
    }

    public HashMap<String, Comparator<ObjectType>> getComparators() {
        return comparators;
    }

    public void setComparators(HashMap<String, Comparator<ObjectType>> comparators) {
        this.comparators = comparators;
    }
}
