package me.agno.gridjavacore.sorting;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * The {@code IGridSortSettings} interface represents the settings for sorting a grid.
 */
public interface IGridSortSettings {

    /**
     * Retrieves the query for sorting a grid.
     *
     * @return a LinkedHashMap containing the query parameters for sorting the grid. The keys are column names, and the values are Lists of query parameter values.
     */
    LinkedHashMap<String, List<String>> getQuery();

    /**
     * Retrieves the name of the column associated with this object.
     *
     * @return the name of the column
     */
    String getColumnName();

    /**
     * Sets the name of the column associated with this object.
     *
     * @param columnName the name of the column
     */
    void setColumnName(String columnName);

    /**
     * Retrieves the direction of sorting in a grid.
     *
     * @return the grid sort direction
     */
    GridSortDirection getDirection();

    /**
     * Sets the direction of sorting for the grid column.
     *
     * @param direction the direction of sorting for the grid column
     */
    void setDirection(GridSortDirection direction);

    /**
     * Retrieves the sort values associated with the grid.
     *
     * @return a DefaultOrderColumnCollection object containing the sort values for the grid
     */
    DefaultOrderColumnCollection getSortValues();
}
