package me.agno.gridjavacore.server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import me.agno.gridjavacore.Grid;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.IGridColumnCollection;
import me.agno.gridjavacore.SearchOptions;
import me.agno.gridjavacore.columns.GridCoreColumn;
import me.agno.gridjavacore.columns.IColumnBuilder;
import me.agno.gridjavacore.columns.IGridColumn;
import me.agno.gridjavacore.pagination.GridPager;
import me.agno.gridjavacore.pagination.PagerDTO;
import me.agno.gridjavacore.pagination.PagingType;
import me.agno.gridjavacore.sorting.GridSortMode;
import me.agno.gridjavacore.totals.TotalsDTO;
import me.agno.gridjavacore.utils.ItemsDTO;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The GridServer class represents a server-side component that provides functionality for working with grids.
 */
@Getter
public class GridServer<T> implements IGridServer<T> {

    /**
     * The private variable grid of type IGrid<T>.
     */
    private IGrid<T> grid;

    /**
     * This class represents a GridServer object.
     * GridServer is used to configure and fetch data for a grid.
     */
    public GridServer()
    { }

    /**
     * Initializes a GridServer object.
     *
     * @param entityManager the entity manager used to execute queries
     * @param targetType the class representing the target type of the grid data
     * @param query the query parameters for filtering, sorting, and searching the grid data
     * @param columns a consumer function used to configure the grid columns
     */
    public GridServer(EntityManager entityManager, Class<T> targetType, Map<String, String[]> query,
                      Consumer<IGridColumnCollection<T>> columns) {
        this(entityManager, targetType, query, columns,0, null);
    }

    /**
     * Represents a GridServer object.
     * GridServer is used to configure and fetch data for a grid.
     *
     * @param entityManager the entity manager used to execute queries
     * @param targetType the class representing the target type of the grid data
     * @param query the query parameters for filtering, sorting, and searching the grid data
     * @param columns a consumer function used to configure the grid columns
     * @param pageSize the number of items to display per page
     */
    public GridServer(EntityManager entityManager, Class<T> targetType,  Map<String, String[]> query,
                      Consumer<IGridColumnCollection<T>> columns, int pageSize) {
        this(entityManager, targetType, query, columns, pageSize, null);
    }

    /**
     * Represents a GridServer object.
     * GridServer is used to configure and fetch data for a grid.
     *
     * @param entityManager the entity manager used to execute queries
     * @param targetType the class representing the target type of the grid data
     * @param query the query parameters for filtering, sorting, and searching the grid data
     * @param columns a consumer function used to configure the grid columns
     * @param pageSize the number of items to display per page
     * @param columnBuilder an object that creates grid columns based on grid annotations.
     */
    public GridServer(EntityManager entityManager, Class<T> targetType, Map<String, String[]> query,
                      Consumer<IGridColumnCollection<T>> columns, int pageSize, IColumnBuilder<T> columnBuilder) {

        var map = query.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Arrays.asList(entry.getValue())));

        var linkedHashMap = new LinkedHashMap<>(map);

        this.grid = new Grid<T>(entityManager, targetType, linkedHashMap, columnBuilder);

        if(columns != null)
            columns.accept(this.grid.getColumns());

        if (pageSize != 0)
            withPaging(pageSize);
    }

    /**
     * Sets the columns of the grid by executing the provided columnBuilder function.
     *
     * @param columnBuilder the function used to configure the grid columns
     * @return the instance of the IGridServer object
     */
    public IGridServer<T> setColumns(Consumer<IGridColumnCollection<T>> columnBuilder) {
        columnBuilder.accept(this.grid.getColumns());
        return this;
    }

    /**
     * Sets the paging options for the grid.
     *
     * @param pageSize the number of items to display per page
     * @return an instance of the IGridServer object
     */
    public IGridServer<T> withPaging(int pageSize) {
        return withPaging(pageSize, 0);
    }

    /**
     * Sets the paging options for the grid.
     *
     * @param pageSize            the number of items to display per page
     * @param maxDisplayedItems   the maximum number of pages to display in the pager control
     * @return an instance of the IGridServer object with paging options set
     */
    public IGridServer<T> withPaging(int pageSize, int maxDisplayedItems) {
        return withPaging(pageSize, maxDisplayedItems, null);
    }

    /**
     * Sets the paging options for the grid.
     *
     * @param pageSize                 the number of items to display per page
     * @param maxDisplayedItems        the maximum number of pages to display in the pager control
     * @param queryStringParameterName the name of the query string parameter for pagination
     * @return an instance of the IGridServer object with paging options set
     */
    public IGridServer<T> withPaging(int pageSize, int maxDisplayedItems, String queryStringParameterName) {

        this.grid.setPagingType(PagingType.PAGINATION);
        this.grid.getPager().setPageSize(pageSize);

        var pager = (GridPager<T>) this.grid.getPager(); //This setting can be applied only to default grid pager
        if (pager == null) return this;

        if (maxDisplayedItems > 0)
            pager.setMaxDisplayedPages(maxDisplayedItems);
        if (queryStringParameterName != null && ! queryStringParameterName.isEmpty())
            pager.setParameterName(queryStringParameterName);
        this.grid.setPager(pager);
        return this;
    }

    /**
     * Enables sorting functionality for the grid with the default sort mode.
     *
     * @return an instance of the IGridServer object with sorting enabled
     */
    public IGridServer<T> sortable() {
        return sortable(true, GridSortMode.THREE_STATE);
    }

    /**
     * Enables or disables sorting functionality for the grid with the specified sort mode.
     *
     * @param enable          {@code true} to enable sorting, {@code false} to disable sorting
     * @param gridSortMode    the GridSortMode indicating the sort mode for the grid
     * @return an instance of the IGridServer object
     */
    public IGridServer<T> sortable(boolean enable, GridSortMode gridSortMode) {

        this.grid.setDefaultSortEnabled(enable);
        if(gridSortMode == null)
            this.grid.setGridSortMode(GridSortMode.THREE_STATE);
        else
            this.grid.setGridSortMode(gridSortMode);

        for (IGridColumn<T> column : this.grid.getColumns().values()) {
            if (column == null) continue;
            ((GridCoreColumn)column).internalSortable(enable, gridSortMode);
        }
        return this;
    }

    /**
     * Enables or disables filtering functionality for the grid.
     *
     * @return an instance of the IGridServer object
     */
    public IGridServer<T> filterable() {
        return filterable(true);
    }

    /**
     * Sets whether the filtering functionality is enabled for the grid.
     *
     * @param enable {@code true} to enable filtering, {@code false} to disable filtering
     * @return an instance of the IGridServer object
     */
    public IGridServer<T> filterable(boolean enable) {

        this.grid.setDefaultFilteringEnabled(enable);
        for (IGridColumn<T> column : this.grid.getColumns().values()) {
            if (column == null) continue;
            ((GridCoreColumn)column).filterable(enable);
        }
        return this;
    }

    /**
     * Enables or disables the searchable functionality for the grid.
     * By default, the searchable functionality is enabled.
     *
     * @return an instance of the IGridServer object with the searchable functionality enabled or disabled
     */
    public IGridServer<T> searchable()
    {
        return searchable(true, true);
    }

    /**
     * Enables or disables the searchable functionality for the grid.
     * By default, the searchable functionality is enabled.
     *
     * @param enable            {@code true} to enable searching, {@code false} to disable searching
     * @return an instance of the IGridServer object with the searchable functionality enabled or disabled
     */
    public IGridServer<T> searchable(boolean enable)
    {
        return searchable(enable, true);
    }

    /**
     * Enables or disables the searchable functionality for the grid with the specified parameters.
     * By default, the searchable functionality is enabled.
     *
     * @param enable           {@code true} to enable searching, {@code false} to disable searching
     * @param onlyTextColumns  {@code true} to enable searching only in text columns, {@code false} to enable searching in all columns
     * @return an instance of the IGridServer object with the searchable functionality enabled or disabled
     */
    public IGridServer<T> searchable(boolean enable, boolean onlyTextColumns) {
        return searchable(enable, onlyTextColumns, false);
    }

    /**
     * Enables or disables the searchable functionality for the grid with the specified parameters.
     *
     * @param enable           {@code true} to enable searching, {@code false} to disable searching
     * @param onlyTextColumns  {@code true} to enable searching only in text columns, {@code false} to enable searching in all columns
     * @param hiddenColumns    {@code true} to include hidden columns in the search, {@code false} to exclude hidden columns from the search
     * @return an instance of the IGridServer object with the searchable functionality enabled or disabled
     */
    public IGridServer<T> searchable(boolean enable, boolean onlyTextColumns, boolean hiddenColumns) {

        return searchable(o -> {
            o.setEnabled(enable);
            o.setOnlyTextColumns(onlyTextColumns);
            o.setHiddenColumns(hiddenColumns);
            o.setSplittedWords(false);
        });
    }

    /**
     * Configures the searchable functionality for the grid.
     *
     * @param searchOptions a consumer function used to configure the search options
     * @return the instance of the IGridServer object
     */
    public IGridServer<T> searchable(Consumer<SearchOptions> searchOptions) {
        var options = new SearchOptions();
        if(searchOptions != null)
            searchOptions.accept(options);

        this.grid.setSearchOptions(options);
        return this;
    }

    /**
     * Automatically generates columns for the grid.
     *
     * @return an instance of the IGridServer object
     */
    public IGridServer<T> autoGenerateColumns() {
        this.grid.autoGenerateColumns();
        return this;
    }

    /**
     * Sets the diacritics removal configuration for the grid.
     *
     * @param removeDiacritics the value indicating whether diacritics should be removed or not
     * @return an instance of IGridServer with the diacritics removal configuration set
     */
    public IGridServer<T> setRemoveDiacritics(String removeDiacritics) {
        this.grid.setRemoveDiacritics(removeDiacritics);
        return this;
    }

    /**
     * Sets the predicate for filtering the grid data.
     *
     * @param predicate the predicate used for filtering
     * @return the instance of the IGridServer object
     */
    public IGridServer<T> setPredicate(Predicate predicate) {
        ((Grid<T>)this.grid).setPredicate(predicate);
        return this;
    }

    /**
     * Sets the order of the grid data based on the provided list of Order objects.
     *
     * @param orderList the list of Order objects representing the order of the grid data
     * @return an instance of the IGridServer object with the order set
     */
    public IGridServer<T> setOrder(List<Order> orderList) {
        ((Grid<T>)this.grid).setOrderList(orderList);
        return this;
    }

    /**
     * Retrieves the items to display in the grid along with totals and paging information.
     *
     * @return an instance of ItemsDTO containing the items, totals, and pager information
     */
    public ItemsDTO<T> getItemsToDisplay() {

        var items = this.grid.getItemsToDisplay();

        TotalsDTO totals;
        if (this.grid.getPagingType() == PagingType.VIRTUALIZATION && this.grid.getPager().isNoTotals())
            totals = null;
        else
            totals = this.grid.getTotals();

        return new ItemsDTO<T>(items, totals,
                new PagerDTO(this.grid.getPagingType(), this.grid.getPager().getPageSize(),
                        this.grid.getPager().getCurrentPage(), this.grid.getItemsCount(),
                        this.grid.getPager().getStartIndex(), this.grid.getPager().getVirtualizedCount()));
    }
}