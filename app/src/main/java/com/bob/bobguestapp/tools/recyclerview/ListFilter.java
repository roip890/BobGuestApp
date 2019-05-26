package com.bob.bobguestapp.tools.recyclerview;

import java.util.ArrayList;
import java.util.List;

public abstract class ListFilter<ObjectType> {

    public abstract ArrayList<ObjectType> getFilteredList(List<ObjectType> objectsList);

}
