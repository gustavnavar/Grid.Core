package me.agno.gridjavacore.filtering;

import me.agno.gridjavacore.columns.IGridColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * The DefaultFilterColumnCollection class represents a collection of column filter values.
 * It extends the ArrayList class and implements the IFilterColumnCollection interface.
 * It provides additional methods for adding filter values and retrieving filter values by column.
 */
public class DefaultFilterColumnCollection extends ArrayList<ColumnFilterValue> implements IFilterColumnCollection {

    /**
     * The DefaultFilterColumnCollection class represents a collection of column filter values.
     * It extends the ArrayList class and implements the IFilterColumnCollection interface.
     * It provides additional methods for adding filter values and retrieving filter values by column.
     */
    public DefaultFilterColumnCollection() {
        super();
    }

    /**
     * Creates a new instance of DefaultFilterColumnCollection with the specified name, type, and value.
     * Adds a new ColumnFilterValue to the collection with the specified name, type, and value.
     *
     * @param name  The name of the column.
     * @param type  The type of filter to apply on the column.
     * @param value The filter value for the column.
     */
    public DefaultFilterColumnCollection(String name, GridFilterType type, String value)  {
        this();
        add(new ColumnFilterValue(name, type, value));
    }

    /**
     * Adds a new column filter value to the collection.
     *
     * @param name  The name of the column.
     * @param type  The type of filter to apply on the column.
     * @param value The filter value for the column.
     */
    public void add(String name, GridFilterType type, String value)
    {
        add(new ColumnFilterValue(name, type, value));
    }

    /**
     * Retrieves a list of ColumnFilterValues by column.
     *
     * @param column The column to filter by.
     * @return A list of ColumnFilterValues that match the given column.
     */
    public List<ColumnFilterValue> getByColumn(IGridColumn column) {
        return this.stream().filter(c -> c.getColumnName().equalsIgnoreCase(column.getName())).toList();
    }
}
