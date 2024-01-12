package me.agno.gridcore.sorting;

import me.agno.gridcore.columns.IGridColumn;

import java.util.ArrayList;
import java.util.List;

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

    public List<ColumnOrderValue> getByColumn(IGridColumn column) {
        return this.stream().filter(c -> c.getColumnName().equalsIgnoreCase(column.getName())).toList();
    }
}
