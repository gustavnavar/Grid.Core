package me.agno.gridcore.filtering;

import me.agno.gridcore.columns.IGridColumn;

import java.util.ArrayList;
import java.util.List;

public class DefaultFilterColumnCollection extends ArrayList<ColumnFilterValue> implements IFilterColumnCollection {
    public DefaultFilterColumnCollection() {
        super();
    }

    public DefaultFilterColumnCollection(String name, GridFilterType type, String value)  {
        this();
        add(new ColumnFilterValue(name, type, value));
    }

    public void add(String name, GridFilterType type, String value)
    {
        add(new ColumnFilterValue(name, type, value));
    }

    public List<ColumnFilterValue> GetByColumn(IGridColumn column) {
        return this.stream().filter(c -> c.getColumnName().toUpperCase().equals(column.getName().toUpperCase())).toList();
    }
}
