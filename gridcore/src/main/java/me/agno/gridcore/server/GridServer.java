package me.agno.gridcore.server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import me.agno.gridcore.Grid;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.IGridColumnCollection;
import me.agno.gridcore.SearchOptions;
import me.agno.gridcore.columns.GridCoreColumn;
import me.agno.gridcore.columns.IColumnBuilder;
import me.agno.gridcore.columns.IGridColumn;
import me.agno.gridcore.pagination.GridPager;
import me.agno.gridcore.pagination.PagerDTO;
import me.agno.gridcore.pagination.PagingType;
import me.agno.gridcore.sorting.GridSortMode;
import me.agno.gridcore.totals.TotalsDTO;
import me.agno.gridcore.utils.ItemsDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

public class GridServer<T> implements IGridServer<T> {

    @Getter
    protected IGrid<T> grid;

    public GridServer()
    { }

    public GridServer(EntityManager entityManager, Class<T> targetType, Predicate predicate, List<Order> orderList,
                      LinkedHashMap<String, List<String>> query, boolean renderOnlyRows,
                      String gridName, Consumer<IGridColumnCollection<T>> columns, int pageSize,
                      IColumnBuilder<T> columnBuilder) {

        this.grid = new Grid<T>(entityManager, targetType, predicate, orderList, query, columnBuilder);

        if(columns != null)
            columns.accept(this.grid.getColumns());

        if (pageSize != 0)
            withPaging(pageSize);
    }

    public IGridServer<T> setColumns(Consumer<IGridColumnCollection<T>> columnBuilder) {
        columnBuilder.accept(this.grid.getColumns());
        return this;
    }

    public IGridServer<T> withPaging(int pageSize) {
        return withPaging(pageSize, 0);
    }

    public IGridServer<T> withPaging(int pageSize, int maxDisplayedItems) {
        return withPaging(pageSize, maxDisplayedItems, null);
    }

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

    public IGridServer<T> sortable() {
        return sortable(true, GridSortMode.THREE_STATE);
    }

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

    public IGridServer<T> filterable() {
        return filterable(true);
    }

    public IGridServer<T> filterable(boolean enable) {

        this.grid.setDefaultFilteringEnabled(enable);
        for (IGridColumn<T> column : this.grid.getColumns().values()) {
            if (column == null) continue;
            ((GridCoreColumn)column).filterable(enable);
        }
        return this;
    }

    public IGridServer<T> searchable()
    {
        return searchable(true, true);
    }

    public IGridServer<T> searchable(boolean enable)
    {
        return searchable(enable, true);
    }

    public IGridServer<T> searchable(boolean enable, boolean onlyTextColumns) {
        return searchable(enable, onlyTextColumns, false);
    }

    public IGridServer<T> searchable(boolean enable, boolean onlyTextColumns, boolean hiddenColumns) {

        return searchable(o -> {
            o.setEnabled(enable);
            o.setOnlyTextColumns(onlyTextColumns);
            o.setHiddenColumns(hiddenColumns);
            o.setSplittedWords(false);
        });
    }

    public IGridServer<T> searchable(Consumer<SearchOptions> searchOptions) {
        var options = new SearchOptions();
        if(searchOptions != null)
            searchOptions.accept(options);

        this.grid.setSearchOptions(options);
        return this;
    }

    public IGridServer<T> extSortable()
    {
        return extSortable(true);
    }

    public IGridServer<T> extSortable(boolean enable) {
        this.grid.setExtSortingEnabled(enable);
        return this;
    }

    public IGridServer<T> extSortable(boolean enable, boolean hidden) {
        this.grid.setExtSortingEnabled(enable);
        this.grid.setHiddenExtSortingHeader(hidden);
        return this;
    }

    public IGridServer<T> groupable()
    {
        return groupable(true);
    }

    public IGridServer<T> groupable(boolean enable) {
        this.grid.setExtSortingEnabled(enable);
        this.grid.setGroupingEnabled(enable);
        return this;
    }

    public IGridServer<T> groupable(boolean enable, boolean hidden) {
        this.grid.setExtSortingEnabled(enable);
        this.grid.setGroupingEnabled(enable);
        this.grid.setHiddenExtSortingHeader(hidden);
        return this;
    }

    public IGridServer<T> autoGenerateColumns() {
        this.grid.autoGenerateColumns();
        return this;
    }

    public IGridServer<T> setRemoveDiacritics(String removeDiacritics) {
        this.grid.setRemoveDiacritics(removeDiacritics);
        return this;
    }

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