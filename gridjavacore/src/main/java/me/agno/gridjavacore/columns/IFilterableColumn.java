package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.filtering.ColumnFilterValue;
import me.agno.gridjavacore.filtering.GridFilterType;
import me.agno.gridjavacore.filtering.IColumnFilter;

/**
 * Represents a filterable column in a grid.
 *
 * @param <T> the type of data in the column
 */
public interface IFilterableColumn<T> extends IColumn<T> {

    /**
     * Retrieves the filter of the column.
     *
     * @return the filter of the column
     */
    IColumnFilter<T> getFilter();

    /**
     * Checks whether the filter is enabled for the column.
     *
     * @return true if the filter is enabled, false otherwise
     */
    boolean isFilterEnabled();

    /**
     * Retrieves the initial filter settings of the column.
     *
     * @return the initial filter settings of the column
     */
    ColumnFilterValue getInitialFilterSettings();

    /**
     * Sets the initial filter settings for the grid column.
     *
     * @param initialFilterSettings the initial filter settings to apply
     */
    void setInitialFilterSettings(ColumnFilterValue initialFilterSettings);

    /**
     * Sets the filterable property of the grid column.
     *
     * @param enabled the boolean value indicating whether the filter should be enabled for the column
     * @return the modified grid column
     */
    IGridColumn<T> filterable(boolean enabled);

    /**
     * Sets the initial filter for the grid column.
     *
     * @param type  the type of filter to be applied
     * @param value the filter value
     * @return the modified grid column
     */
    IGridColumn<T> setInitialFilter(GridFilterType type, String value);
}
