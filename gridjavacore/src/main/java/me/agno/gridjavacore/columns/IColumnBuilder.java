package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.sorting.GridSortMode;

/**
 * The IColumnBuilder interface provides methods for creating and configuring grid columns.
 *
 * @param <T> the type of the grid class
 */
public interface IColumnBuilder<T> {

    /**
     * Determines whether the default sort is enabled in the grid.
     *
     * @return true if the default sort is enabled, false otherwise
     */
    boolean isDefaultSortEnabled();

    /**
     * Sets the default sort enabled flag for the grid.
     *
     * @param defaultSortEnabled the value to set for the default sort enabled flag
     */
    void setDefaultSortEnabled(boolean defaultSortEnabled);

    /**
     * Retrieves the default sort mode for the grid.
     *
     * @return the default sort mode for the grid
     */
    GridSortMode getDefaultGridSortMode();

    /**
     * Sets the default sort mode for the grid.
     *
     * @param defaultGridSortMode the default sort mode to set for the grid
     */
    void setDefaultGridSortMode(GridSortMode defaultGridSortMode);

    /**
     * Determines whether the default filtering is enabled in the grid.
     *
     * @return true if default filtering is enabled, false otherwise
     */
    boolean isDefaultFilteringEnabled();

    /**
     * Sets whether the default filtering is enabled in the grid.
     *
     * @param defaultFilteringEnabled true to enable the default filtering, false to disable it
     */
    void setDefaultFilteringEnabled(boolean defaultFilteringEnabled);

    /**
     * Creates a new grid column with the specified expression, target type, and hidden state.
     *
     * @param expression the expression used to generate the column for the grid
     * @param targetType the class representing the target type of the column's data
     * @param hidden     true if the column should be hidden, false otherwise
     * @param <TData>    the type of the column's data
     * @return the added grid column
     * @throws IllegalArgumentException if the targetType or expression is null
     */
    <TData> IGridColumn<T> createColumn(String expression, Class<TData> targetType, boolean hidden);
}
