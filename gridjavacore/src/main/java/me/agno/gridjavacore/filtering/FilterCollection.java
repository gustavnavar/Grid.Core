package me.agno.gridjavacore.filtering;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that extends ArrayList to represent a collection of filters.
 */
public class FilterCollection extends ArrayList<Filter> {

    /**
     * A class that extends ArrayList to represent a collection of filters.
     */
    public FilterCollection() {
        super();
    }

    /**
     * Adds a new filter to the collection based on the provided type and value.
     *
     * @param type  the type of the filter
     * @param value the value of the filter
     */
    public FilterCollection(String  type, String value)
    {
        super();
        add(type, value);
    }

    /**
     * Filters the given array of filters and adds the valid filters to the collection.
     *
     * @param filters an array of Filter objects representing the filters to be filtered and added
     */
    public FilterCollection(Filter[] filters)
    {
        super();
        for (var filter : filters)
        {
            if(filter.getType() != null && !filter.getType().trim().isEmpty())
                add(filter.getType(), filter.getValue());
        }
    }

    /**
     * Filters the provided list of filters and adds the valid filters to the collection.
     *
     * @param filters a list of Filter objects representing the filters to be filtered and added
     */
    public FilterCollection(List<Filter> filters)
    {
        super();
        for (Filter filter : filters)
        {
            if (filter.getType() != null && !filter.getType().trim().isEmpty())
                add(filter.getType(), filter.getValue());
        }
    }

    /**
     * Adds a new filter to the collection based on the provided type and value.
     *
     * @param type  the type of the filter
     * @param value the value of the filter
     */
    public void add(String type, String value)
    {
        add(new Filter(type, value));
    }
}
