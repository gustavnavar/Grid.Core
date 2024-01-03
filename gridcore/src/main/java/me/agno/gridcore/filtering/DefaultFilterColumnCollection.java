package me.agno.gridcore.filtering;

import me.agno.gridcore.columns.IGridColumn;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultFilterColumnCollection extends ArrayList<ColumnFilterValue> implements IFilterColumnCollection {
    public DefaultFilterColumnCollection() {
        super();
    }

    public DefaultFilterColumnCollection(String name, GridFilterType type, String value)  {
        this();
        add(new ColumnFilterValue(name, type, value));
    }

    public void Add(String name, GridFilterType type, String value)
    {
        add(new ColumnFilterValue(name, type, value));
    }

    public Collection<ColumnFilterValue> GetByColumn(IGridColumn column) {
        return this.stream().filter(c -> c.getColumnName().toUpperCase() == column.getName().toUpperCase()).toList();
    }
}
