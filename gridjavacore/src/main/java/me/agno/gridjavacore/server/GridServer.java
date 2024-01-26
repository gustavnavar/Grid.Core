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

@Getter
public class GridServer<T> implements IGridServer<T> {

    protected IGrid<T> grid;

    public GridServer()
    { }

    public GridServer(EntityManager entityManager, Class<T> targetType, Map<String, String[]> query,
                      Consumer<IGridColumnCollection<T>> columns) {
        this(entityManager, targetType, query, columns,0, null);
    }

    public GridServer(EntityManager entityManager, Class<T> targetType,  Map<String, String[]> query,
                      Consumer<IGridColumnCollection<T>> columns, int pageSize) {
        this(entityManager, targetType, query, columns, pageSize, null);
    }

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

    public IGridServer<T> autoGenerateColumns() {
        this.grid.autoGenerateColumns();
        return this;
    }

    public IGridServer<T> setRemoveDiacritics(String removeDiacritics) {
        this.grid.setRemoveDiacritics(removeDiacritics);
        return this;
    }

    public IGridServer<T> setPredicate(Predicate predicate) {
        ((Grid<T>)this.grid).setPredicate(predicate);
        return this;
    }

    public IGridServer<T> setOrder(List<Order> orderList) {
        ((Grid<T>)this.grid).setOrderList(orderList);
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