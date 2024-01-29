package me.agno.gridjavacore;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import me.agno.gridjavacore.columns.GridColumnCollection;
import me.agno.gridjavacore.filtering.FilterProcessor;
import me.agno.gridjavacore.pagination.IGridPager;
import me.agno.gridjavacore.pagination.IPagerProcessor;
import me.agno.gridjavacore.pagination.PagingType;
import me.agno.gridjavacore.searching.SearchProcessor;
import me.agno.gridjavacore.sorting.GridSortMode;
import me.agno.gridjavacore.sorting.SortProcessor;
import me.agno.gridjavacore.totals.CountProcessor;
import me.agno.gridjavacore.totals.TotalsDTO;
import me.agno.gridjavacore.totals.TotalsProcessor;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Interface representing a grid that displays data based on a given query and provides filtering, sorting, searching, and pagination functionality.
 *
 * @param <T> the type of data displayed in the grid
 */
public interface IGrid<T> extends IGridOptions{

    /**
     * Returns the query parameters used for filtering, sorting, searching, and pagination.
     *
     * @return a LinkedHashMap containing the query parameters as key-value pairs, where the key is the parameter name and the value is a list of parameter values
     */
    LinkedHashMap<String, List<String>> getQuery();

    /**
     * Sets the query parameters used for filtering, sorting,
     * searching, and pagination.
     *
     * @param query the query parameters as a LinkedHashMap, where the
     *              key is the parameter name and the value is a list of
     *              parameter values
     */
    void setQuery(LinkedHashMap<String, List<String>> query);

    /**
     * Retrieves the collection of columns in the grid.
     *
     * @return the GridColumnCollection object containing the columns in the grid
     */
    GridColumnCollection<T> getColumns();

    /**
     * Retrieves the items to display in the grid.
     *
     * @return the list of items to display
     */
    List<T> getItemsToDisplay();

    /**
     * Retrieves the count of items currently being displayed in the grid.
     *
     * @return the count of items being displayed
     */
    long getDisplayingItemsCount();

    /**
     * Retrieves the paging type of the grid.
     *
     * @return the paging type of the grid
     */
    PagingType getPagingType();

    /**
     * Sets the paging type for the grid.
     *
     * @param pagingType the paging type to set
     */
    void setPagingType(PagingType pagingType);

    /**
     * Retrieves the search options for the grid.
     *
     * @return the search options for the grid
     */
    SearchOptions getSearchOptions();

    /**
     * Sets the search options for the grid.
     *
     * @param searchOptions the search options to set
     */
    void setSearchOptions(SearchOptions searchOptions);

    /**
     * Retrieves the database stored procedure name used to remove diacritics for filtering purposes.
     * Diacritics are accents or special characters added to letters in some languages.
     * This method returns the database stored procedure name that is used to remove diacritics from grid filters.
     *
     * @return the string used to remove diacritics
     */
    String getRemoveDiacritics();

    /**
     * Sets the database stored procedure name used to remove diacritics for filtering purposes.
     * Diacritics are accents or special characters added to letters in some languages.
     * This method sets the database stored procedure name that is used to remove diacritics from grid filters.
     *
     * @param removeDiacritics the string used to remove diacritics
     */
    void setRemoveDiacritics(String removeDiacritics);

    /**
     * Retrieves the count of items to be displayed in the grid.
     *
     * @return the count of items to be displayed
     */
    long getItemsCount();

    /**
     * Retrieves the pager processor used for paging the grid data.
     *
     * @return the pager processor
     */
    IPagerProcessor<T> getPagerProcessor();

    /**
     * Retrieves the search processor used for searching grid data based on search criteria.
     *
     * @return the search processor for grid data searching
     */
    SearchProcessor<T> getSearchProcessor();

    /**
     * Retrieves the filter processor used for filtering the grid data based on filter criteria.
     *
     * @return the filter processor for grid data filtering
     */
    FilterProcessor<T> getFilterProcessor();

    /**
     * Retrieves the sort processor used for sorting the grid data based on sort criteria.
     *
     * @return the sort processor for grid data sorting
     */
    SortProcessor<T> getSortProcessor();

    /**
     * Returns the TotalsProcessor object used for calculating totals in the grid.
     * @return the TotalsProcessor object
     */
    TotalsProcessor<T> getTotalsProcessor();

    /**
     * Retrieves the CountProcessor object used for calculating the count of items in the grid.
     *
     * @return the CountProcessor object
     */
    CountProcessor<T> getCountProcessor();

    /**
     * Retrieves the pager object used for pagination in the grid.
     *
     * @return the pager object
     */
    IGridPager<T> getPager();

    /**
     * Sets the pager object used for pagination in the grid.
     *
     * @param pager the pager object to set
     */
    void setPager(IGridPager<T> pager);

    /**
     * Retrieves the grid settings provider, which contains information about the sort, filter, and search settings for the grid.
     *
     * @return the grid settings provider for the grid
     */
    IGridSettingsProvider getSettings();

    /**
     * Retrieves the TotalsDTO object containing the sum, average, max, min, and calculation totals for the grid.
     *
     * @return the TotalsDTO object containing the totals
     */
    TotalsDTO getTotals();

    /**
     * Checks if the default sorting is enabled for the grid.
     *
     * @return true if the default sorting is enabled, false otherwise
     */
    boolean isDefaultSortEnabled();

    /**
     * Sets whether the default sorting is enabled for the grid.
     *
     * @param defaultFilteringEnabled true to enable default sorting, false to disable
     */
    void setDefaultSortEnabled(boolean defaultFilteringEnabled);

    /**
     * Retrieves the sort mode for the grid.
     *
     * @return the GridSortMode enumeration value representing the sort mode for the grid.
     */
    GridSortMode getGridSortMode();

    /**
     * Sets the sort mode for the grid.
     *
     * @param gridSortMode the GridSortMode enumeration value representing the sort mode for the grid
     */
    void setGridSortMode(GridSortMode gridSortMode);

    /**
     * Retrieves whether the default filtering is enabled for the grid.
     *
     * @return true if default filtering is enabled, false otherwise
     */
    boolean isDefaultFilteringEnabled();

    /**
     * Sets whether the default filtering is enabled for the grid.
     *
     * @param defaultFilteringEnabled true to enable default filtering, false to disable
     */
    void setDefaultFilteringEnabled(boolean defaultFilteringEnabled);

    /**
     * Automatically generates columns for the grid.
     */
    void autoGenerateColumns();

    /**
     * Retrieves the EntityManager object used for managing the persistence context.
     * The EntityManager object is responsible for performing database operations, such as querying, persisting, and deleting entities.
     *
     * @return the EntityManager object
     */
    EntityManager getEntityManager();

    /**
     * Retrieves the CriteriaBuilder object used for creating criteria queries and expressions.
     * The CriteriaBuilder object is a factory that provides methods for constructing criteria queries,
     * predicates, expressions, and other criteria-related objects.
     *
     * @return the CriteriaBuilder object
     */
    CriteriaBuilder getCriteriaBuilder();

    /**
     * Retrieves the CriteriaQuery object used for creating criteria queries.
     * The CriteriaQuery object is used to define the query structure and restrictions.
     *
     * @return the CriteriaQuery object
     */
    CriteriaQuery<T> getCriteriaQuery();

    /**
     * Retrieves the root object for the criteria query.
     * The root object represents the entity type being queried.
     *
     * @return the root object
     */
    Root<T> getRoot();

    /**
     * Retrieves the target type of the grid.
     *
     * @return the Class object representing the target type of the grid
     */
    Class<T> getTargetType();

    /**
     * Retrieves the initial predicate used for filtering the grid data.
     *
     * @return the predicate used for filtering
     */
    Predicate getPredicate();

    /**
     * Retrieves the initial list of orders for sorting the grid data
     *
     * @return the list of orders
     */
    List<Order> getOrderList();
}
