package me.agno.gridjavacore.filtering;

import java.util.ArrayList;
import java.util.List;

public class FilterCollection extends ArrayList<Filter> {

    public FilterCollection() {
        super();
    }

    public FilterCollection(String  type, String value)
    {
        super();
        add(type, value);
    }

    public FilterCollection(Filter[] filters)
    {
        super();
        for (var filter : filters)
        {
            if(filter.getType() != null && !filter.getType().trim().isEmpty())
                add(filter.getType(), filter.getValue());
        }
    }

    public FilterCollection(List<Filter> filters)
    {
        super();
        for (Filter filter : filters)
        {
            if (filter.getType() != null && !filter.getType().trim().isEmpty())
                add(filter.getType(), filter.getValue());
        }
    }

    public void add(String type, String value)
    {
        add(new Filter(type, value));
    }
}
