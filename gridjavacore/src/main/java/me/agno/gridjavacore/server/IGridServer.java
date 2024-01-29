package me.agno.gridjavacore.server;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.IGridColumnCollection;
import me.agno.gridjavacore.SearchOptions;
import me.agno.gridjavacore.sorting.GridSortMode;
import me.agno.gridjavacore.utils.ItemsDTO;

import java.util.List;
import java.util.function.Consumer;

/**
 * The IGridServer interface represents a grid server that provides methods for configuring and retrieving grid data.
 */
public interface IGridServer<T> {

    /**
     * Sets the columns of the grid server using a column builder function.
     *
     * @param columnBuilder a consumer function that takes an IGridColumnCollection parameter, used to build the grid columns
     * @return the grid server instance with the updated columns
     */
    IGridServer<T> setColumns(Consumer<IGridColumnCollection<T>> columnBuilder);

    /**
     * Sets the page size for paging in the grid server instance.
     *
     * @param pageSize the number of items to display per page
     * @return the grid server instance with the updated page size
     */
    IGridServer<T> withPaging(int pageSize);

    /**
     * Sets the paging options for the grid server instance.
     *
     * @param pageSize           the number of items to display per page
     * @param maxDisplayedItems  the maximum number of items to display in the grid
     * @return the grid server instance with the updated paging options
     */
    IGridServer<T> withPaging(int pageSize, int maxDisplayedItems);

    /**
     * Sets the paging options for the grid server instance.
     *
     * @param pageSize                  the number of items to display per page
     * @param maxDisplayedItems         the maximum number of items to display in the grid
     * @param queryStringParameterName  the name of the query string parameter used for paging
     * @return the grid server instance with the updated paging options
     */
    IGridServer<T> withPaging(int pageSize, int maxDisplayedItems, String queryStringParameterName);

    /**
     * Returns a grid server instance that supports sorting functionality.
     *
     * @return the grid server instance with sorting enabled
     */
    IGridServer<T> sortable();

    /**
     * Returns a grid server instance that supports sorting functionality.
     *
     * @param enable         a boolean value indicating whether sorting should be enabled or not
     * @param gridSortMode   an enum value representing the sort mode for the grid server instance
     * @return the grid server instance with sorting enabled or disabled based on the parameters
     */
    IGridServer<T> sortable(boolean enable, GridSortMode gridSortMode);

    /**
     * Retrieves a grid server instance that supports filtering functionality.
     *
     * @return the grid server instance with filtering enabled
     */
    IGridServer<T> filterable();

    /**
     * Enables or disables filtering functionality for the grid server instance.
     *
     * @param enable a boolean value indicating whether filtering should be enabled or not
     * @return the grid server instance with filtering enabled or disabled based on the parameter
     */
    IGridServer<T> filterable(boolean enable);

    /**
     * Retrieves a grid server instance that supports searching functionality.
     *
     * @return the grid server instance with searching enabled
     */
    IGridServer<T> searchable();

    /**
     * Enables or disables the searching functionality for the grid server instance.
     *
     * @param enable a boolean value indicating whether searching should be enabled or not
     * @return the grid server instance with searching enabled or disabled based on the parameter
     */
    IGridServer<T> searchable(boolean enable);

    /**
     * Enables or disables the searchable functionality for the grid server instance.
     *
     * @param enable           a boolean value indicating whether searching should be enabled or not
     * @param onlyTextColumns  a boolean value indicating whether to search only in text columns or not
     * @return the grid server instance with searching enabled or disabled based on the parameters
     */
    IGridServer<T> searchable(boolean enable, boolean onlyTextColumns);

    /**
     * Enables or disables the searchable functionality for the grid server instance.
     * By default, the searchable functionality is enabled.
     *
     * @param enable           a boolean value indicating whether searching should be enabled or not
     * @param onlyTextColumns  a boolean value indicating whether to search only in text columns or not
     * @param hiddenColumns    a boolean value indicating whether to search hidden columns or not
     * @return the grid server instance with searching enabled or disabled based on the parameters
     */
    IGridServer<T> searchable(boolean enable, boolean onlyTextColumns, boolean hiddenColumns);

    /**
     * Enables or disables the searchable functionality for the grid server instance based on the specified search options.
     *
     * @param searchOptions a Consumer function that accepts a SearchOptions parameter and is used to set the search options for the grid server instance
     * @return the grid server instance with the searchable functionality enabled or disabled based on the search options
     */
    IGridServer<T> searchable(Consumer<SearchOptions> searchOptions);

    /**
     * Automatically generates columns for the grid server instance.
     *
     * @return the grid server instance with automatically generated columns
     */
    IGridServer<T> autoGenerateColumns();

    /**
     * Sets the method name for removing diacritics in the grid server instance.
     *
     * @param methodName the name of the method for removing diacritics
     * @return the grid server instance with the updated method name for removing diacritics
     */
    IGridServer<T> setRemoveDiacritics(String methodName);

    /**
     * Sets the predicate for filtering the data in the grid server instance.
     *
     * @param predicate the predicate used for filtering the data
     * @return the grid server instance with the updated predicate
     */
    IGridServer<T> setPredicate(Predicate predicate);

    /**
     * Sets the order of the grid server instance based on the provided list of orders.
     *
     * @param orderList the list of orders to set for the grid server
     * @return the grid server instance with the updated order
     */
    IGridServer<T> setOrder(List<Order> orderList);

    /**
     * Retrieves the items to display in the grid.
     *
     * @return an instance of ItemsDTO containing the items, totals, and pager information
     */
    ItemsDTO<T> getItemsToDisplay();

    /**
     * Retrieves the grid instance.
     *
     * @return the grid instance
     */
    IGrid<T> getGrid();
}
