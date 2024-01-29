package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.sorting.GridSortDirection;
import me.agno.gridjavacore.sorting.GridSortMode;
import me.agno.gridjavacore.sorting.IColumnOrderer;

import java.util.List;
import java.util.Optional;

/**
 * Represents a sortable column in a grid.
 *
 * @param <T> the type of data in the column
 */
public interface ISortableColumn<T> extends IColumn<T> {

    /**
     * Returns the list of column orderers for this sortable column.
     * The column orderers determine the order in which the column's data is sorted.
     *
     * @return the list of column orderers
     */
    List<IColumnOrderer<T>> getOrderers();

    /**
     * Checks if the column has a defined sort order.
     *
     * @return true if the column sort order is defined, false otherwise
     */
    boolean isColumnSortDefined();

    /**
     * Determines if sorting is enabled for the column.
     *
     * @return true if sorting is enabled, false otherwise
     */
    boolean isSortEnabled();

    /**
     * Retrieves the sort mode of the grid column.
     *
     * @return the sort mode of the column
     */
    GridSortMode getSortMode();

    /**
     * Checks if the column's data is sorted.
     *
     * @return true if the column's data is sorted, false otherwise
     */
    boolean isSorted();

    /**
     * Sets the sorted state of the column.
     *
     * @param sorted true if the column's data is sorted, false otherwise
     */
    void setSorted(boolean sorted);

    /**
     * Retrieves the sorting direction of the grid column.
     *
     * @return an Optional containing the sorting direction of the grid column if it is defined,
     *         or an empty Optional if it is not defined
     */
    Optional<GridSortDirection> getDirection();

    /**
     * Sets the sorting direction for the grid column.
     *
     * @param direction the sorting direction of the grid column
     */
    void setDirection(GridSortDirection direction);

    /**
     * Retrieves the initial sorting direction for the grid column.
     *
     * @return an Optional containing the initial sorting direction of the grid column if it is defined,
     *         or an empty Optional if it is not defined
     */
    Optional<GridSortDirection> getInitialDirection();

    /**
     * Sets the initial sorting direction for the grid column.
     *
     * @param initialDirection the initial sorting direction of the grid column
     */
    void setInitialDirection(GridSortDirection initialDirection);

    /**
     * Defines whether a column is sortable.
     *
     * @param sort true if the column should be sortable, false otherwise
     * @return the instance of the grid column
     */
    IGridColumn<T> sortable(boolean sort);

    /**
     * Sets the sortable property of the grid column with the specified sort mode.
     *
     * @param sort           true to enable sorting, false otherwise
     * @param gridSortMode   the sort mode of the grid column (THREE_STATE or TWO_STATE)
     * @return the updated IGridColumn instance
     */
    IGridColumn<T> sortable(boolean sort, GridSortMode gridSortMode);

    /**
     * Sets the initial sorting direction for the grid column.
     *
     * @param direction the initial sorting direction of the grid column
     * @return the updated IGridColumn instance
     */
    IGridColumn<T> sortInitialDirection(GridSortDirection direction);

    /**
     * Sets the sorting expression for the grid column and returns the instance of the grid column.
     *
     * @param expression the sorting expression for the grid column
     * @return the instance of the grid column
     */
    IGridColumn<T> thenSortBy(String expression);

    /**
     * Sorts the grid column in descending order based on the given sorting expression.
     *
     * @param expression the sorting expression for the grid column
     * @return the instance of the grid column after sorting in descending order
     */
    IGridColumn<T> thenSortByDescending(String expression);
}
