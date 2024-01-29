package me.agno.gridjavacore.filtering;

import me.agno.gridjavacore.columns.IGridColumn;

import java.util.List;

/**
 * The IFilterColumnCollection interface represents a collection of column filter values.
 * It extends the List interface and provides an additional method for retrieving filter values by column.
 */
public interface IFilterColumnCollection extends List<ColumnFilterValue> {

    /**
     * Retrieves a list of ColumnFilterValues from the IFilterColumnCollection by column.
     *
     * @param column the IGridColumn representing the column to retrieve the filter values for
     * @return a list of ColumnFilterValues for the specified column
     */
    List<ColumnFilterValue> getByColumn(IGridColumn column);
}
