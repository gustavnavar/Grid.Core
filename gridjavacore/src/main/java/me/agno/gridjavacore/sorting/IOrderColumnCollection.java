package me.agno.gridjavacore.sorting;

import me.agno.gridjavacore.columns.IGridColumn;

import java.util.List;

/**
 * Represents a collection of order columns in a grid.
 */
public interface IOrderColumnCollection extends List<ColumnOrderValue> {

    /**
     * Retrieves a list of ColumnOrderValue objects based on the specified column.
     *
     * @param column the column to retrieve the list of ColumnOrderValue objects for
     * @return a list of ColumnOrderValue objects
     */
    List<ColumnOrderValue> getByColumn(IGridColumn column);
}
