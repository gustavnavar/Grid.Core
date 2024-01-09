package me.agno.gridcore.columns;


import me.agno.gridcore.IGrid;
import me.agno.gridcore.IGridColumnCollection;
import me.agno.gridcore.ISGrid;
import me.agno.gridcore.filtering.DefaultColumnFilter;
import me.agno.gridcore.filtering.IColumnFilter;
import me.agno.gridcore.searching.DefaultColumnSearch;
import me.agno.gridcore.searching.IColumnSearch;
import me.agno.gridcore.sorting.GridSortDirection;
import me.agno.gridcore.sorting.GridSortMode;
import me.agno.gridcore.sorting.IColumnOrderer;
import me.agno.gridcore.sorting.OrderByGridOrderer;
import me.agno.gridcore.sorting.ThenByColumnOrderer;
import me.agno.gridcore.totals.DefaultColumnTotals;
import me.agno.gridcore.totals.IColumnTotals;
import me.agno.gridcore.totals.Total;
import me.agno.gridcore.utils.QueryDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class GridCoreColumn<T, TData> extends GridCoreColumnBase<T, TData> {

    protected Class _targetType;

    protected Function<T, TData> _expression;

    protected IColumnFilter<T> _filter;

    protected IColumnSearch<T> _search;

    protected IColumnTotals<T, TData> _totals;

    protected final ISGrid<T> _grid;

    protected final List<IColumnOrderer<T>> _orderers = new ArrayList<IColumnOrderer<T>>();

    protected String _width;

    public GridCoreColumn(Function<T, TData> expression, Class targetType, ISGrid<T> grid)  {

        setSortEnabled(false);
        setHidden(false);

        _targetType = targetType;
        _grid = grid;

        if (expression == null) {
            _expression = expression;
            _orderers.add(0, new OrderByGridOrderer(expression));
            _filter = new DefaultColumnFilter(expression, targetType);
            _search = new DefaultColumnSearch(expression, targetType);
            _totals = new DefaultColumnTotals(expression);

            //Generate unique column name:
            setFieldName(expression.toString());
            setName(getFieldName());
        }

        setCalculations(new QueryDictionary<Function<IGridColumnCollection<T>, Object>>());
        setCalculationValues(new QueryDictionary<Total>());
    }

    @Override
    public Collection<IColumnOrderer<T>> getOrderers() {
        return _orderers;
    }

    @Override
    public IColumnFilter<T> getFilter() {
        return _filter;
    }

    @Override
    public IColumnSearch<T> getSearch() {
        return _search;
    }

    @Override
    public IColumnTotals<T, TData> getTotals() {
        return _totals;
    }

    @Override
    public IGrid<T> getParentGrid() {
        return _grid;
    }

    @Override
    public IGridColumn<T> SetFilterWidgetType(Object widgetData) {
        if (widgetData != null)
            setFilterWidgetData(widgetData);
        return this;
    }


    @Override
    public IGridColumn<T> SortInitialDirection(GridSortDirection direction) {

        setInitialDirection(direction);

        var sortSettings = _grid.getSettings().getSortSettings();
        if (sortSettings.getColumnName() == null || sortSettings.getColumnName().trim() == "") {

            setSorted(true);
            setDirection(direction);

            // added to enable initial sorting
            sortSettings.setColumnName(getName());
            sortSettings.setDirection(direction);
        }

        return this;
    }

    @Override
    public <TData> IGridColumn<T> thenSortBy(Function<T, TData> expression) {
        _orderers.add(new ThenByColumnOrderer(expression, GridSortDirection.Ascending));
        return this;
    }

    @Override
    public <TData> IGridColumn<T> thenSortByDescending(Function<T, TData> expression) {
        _orderers.add(new ThenByColumnOrderer(expression, GridSortDirection.Descending));
        return this;
    }

    @Override
    public IGridColumn<T> sortable(boolean sort) {
        return sortable(sort, null);
    }

    @Override
    public IGridColumn<T> sortable(boolean sort, GridSortMode gridSortMode) {

        if (gridSortMode == null)
            gridSortMode= GridSortMode.ThreeState;

        if (sort && _expression == null) {
            return this; //cannot enable sorting for column without expression
        }
        setColumnSortDefined(true);
        setSortEnabled(sort);
        setSortMode(gridSortMode);
        return this;
    }

    @Override
    IGridColumn<T> internalSortable(boolean sort) {
        return internalSortable(sort, null);
    }

    @Override
    IGridColumn<T> internalSortable(boolean sort, GridSortMode gridSortMode) {

        if (gridSortMode == null)
            gridSortMode= GridSortMode.ThreeState;

        if (sort && _expression == null) {
            return this; //cannot enable sorting for column without expression
        }

        if (!isColumnSortDefined()) {
            setSortEnabled(sort);
            setSortMode(gridSortMode);
        }
        return this;
    }

    @Override
    public IGridColumn<T> filterable(boolean enable) {
        if (enable && _expression == null) {
            return this; //cannot enable filtering for column without expression
        }

        setFilterEnabled(enable);
        return this;
    }

    @Override
    public Class getTargetType() {
        return _targetType;
    }
}
