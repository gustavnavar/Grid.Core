package me.agno.gridjavacore.pagination;

import me.agno.gridjavacore.IGrid;

/**
 * Interface representing a pager for a grid.
 */
public interface IGridPager<T> {

    /**
     * Retrieves the grid associated with the pager.
     *
     * @return the IGrid object representing the grid associated with the pager
     */
    IGrid<T> getGrid();

    /**
     * Initializes the pager with the specified count.
     *
     * @param count the total number of items in the grid
     */
    void initialize(long count);

    /**
     * Retrieves the page size used for pagination in the grid pager.
     *
     * @return The page size.
     */
    int getPageSize();

    /**
     * Sets the page size for pagination in the grid pager.
     *
     * @param pageSize the page size to set
     */
    void setPageSize(int pageSize);

    /**
     * Retrieves the page size used for pagination in the grid pager.
     *
     * @return The page size.
     */
    int getQueryPageSize();

    /**
     * Sets the query page size for pagination in the grid pager.
     *
     * @param queryPageSize the query page size to set
     */
    void setQueryPageSize(int queryPageSize);

    /**
     * Retrieves the current page number.
     *
     * @return the current page number
     */
    int getCurrentPage();

    /**
     * Retrieves the total count of items in the grid.
     *
     * @return the total count of items in the grid
     */
    long getItemsCount();

    /**
     * Sets the number of items in the grid.
     *
     * @param itemsCount the number of items to set
     */
    void setItemsCount(long itemsCount);

    /**
     * Retrieves the start index used for pagination in the grid pager.
     *
     * @return The start index value.
     */
    int getStartIndex();

    /**
     * Retrieves the virtualized count for pagination in the grid pager.
     *
     * @return The virtualized count.
     */
    int getVirtualizedCount();

    /**
     * Checks if the grid pager has no totals.
     *
     * @return true if the grid pager has no totals, false otherwise
     */
    boolean isNoTotals();
}
