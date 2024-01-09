package me.agno.gridcore.sorting;

import me.agno.gridcore.columns.IGridColumn;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultOrderColumnCollection extends ArrayList<ColumnOrderValue> implements IOrderColumnCollection {

    public DefaultOrderColumnCollection() {
        super();
    }

    public DefaultOrderColumnCollection(String name, GridSortDirection direction, int id) {
        super();
        add(new ColumnOrderValue(name, direction, id));
    }

    public void add(String name, GridSortDirection direction, int id) {
        add(new ColumnOrderValue(name, direction, id));
    }

    public Collection<ColumnOrderValue> GetByColumn(IGridColumn column) {
        return this.stream().filter(c -> c.getColumnName().toUpperCase() == column.getName().toUpperCase()).toList();
    }
}
