package me.agno.gridjavacore.filtering;

import me.agno.gridjavacore.columns.IGridColumn;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * The IGridFilterSettings interface represents the settings for filtering data in a grid.
 * It provides methods to retrieve the filter query, the filtered columns, and check if a column is in its initial state.
 */
public interface IGridFilterSettings {

    /**
     * Retrieves the filter query used for filtering data in a grid.
     *
     * The filter query is represented as a LinkedHashMap, where the key is the name of the column
     * and the value is a list of filter values applied to that column.
     *
     * @return the filter query
     */
    LinkedHashMap<String, List<String>> getQuery();

    /**
     * Retrieves the filtered columns from the IGridFilterSettings.
     *
     * @return the filtered columns as an IFilterColumnCollection
     */
    IFilterColumnCollection getFilteredColumns();

    /**
     * Checks if a column is in its initial state.
     *
     * @param column the IGridColumn to check
     * @return true if the column is in its initial state, false otherwise
     */
    boolean isInitState(IGridColumn column);
}
