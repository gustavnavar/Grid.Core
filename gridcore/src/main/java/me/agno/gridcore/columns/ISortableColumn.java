package me.agno.gridcore.columns;

import me.agno.gridcore.sorting.GridSortDirection;
import me.agno.gridcore.sorting.GridSortMode;
import me.agno.gridcore.sorting.IColumnOrderer;

import java.util.Collection;
import java.util.function.Function;

public interface ISortableColumn<T> extends IColumn<T> {

    Collection<IColumnOrderer<T>> getOrderers();

    void setOrderers(Collection<IColumnOrderer<T>> orderers);

    boolean isColumnSortDefined();

    boolean isSortEnabled();

    GridSortMode getSortMode();

    boolean isSorted();

    void setSorted(boolean sorted);

    GridSortDirection getDirection();

    void setDirection(GridSortDirection direction);

    GridSortDirection getInitialDirection();

    void setInitialDirection(GridSortDirection initialDirection);

    IGridColumn<T> Sortable(boolean sort);

    IGridColumn<T> Sortable(boolean sort, GridSortMode gridSortMode);

    IGridColumn<T> SortInitialDirection(GridSortDirection direction);

    <TKey> IGridColumn<T> ThenSortBy(Function<T, TKey> expression);

    <TKey> IGridColumn<T> ThenSortByDescending(Function<T, TKey> expression);
}
