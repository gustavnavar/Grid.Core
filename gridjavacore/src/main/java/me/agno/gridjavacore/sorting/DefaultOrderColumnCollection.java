package me.agno.gridjavacore.sorting;

import me.agno.gridjavacore.columns.IGridColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a collection of order columns in a grid.
 */
public class DefaultOrderColumnCollection extends ArrayList<ColumnOrderValue> implements IOrderColumnCollection {

    /**
     * Represents a default order column collection in a grid.
     */
    public DefaultOrderColumnCollection() {
        super();
    }

    /**
     * Constructs a new DefaultOrderColumnCollection object.
     *
     * @param name      the name of the column
     * @param direction the sorting direction for the column
     * @param id        the ID of the column
     */
    public DefaultOrderColumnCollection(String name, GridSortDirection direction, int id) {
        super();
        add(new ColumnOrderValue(name, direction, id));
    }

    /**
     * Adds a column order value to the default order column collection.
     *
     * @param name      the name of the column
     * @param direction the sorting direction for the column
     * @param id        the ID of the column
     */
    public void add(String name, GridSortDirection direction, int id) {
        add(new ColumnOrderValue(name, direction, id));
    }

    /**
     * Retrieves a list of ColumnOrderValue objects from the DefaultOrderColumnCollection based on a given IGridColumn.
     *
     * @param column the IGridColumn used for filtering the ColumnOrderValue objects
     * @return a list of ColumnOrderValue objects that match the provided IGridColumn
     */
    public List<ColumnOrderValue> getByColumn(IGridColumn column) {
        return this.stream().filter(c -> c.getColumnName().equalsIgnoreCase(column.getName())).toList();
    }
}
