package me.agno.gridcore.columns;

import me.agno.gridcore.sorting.GridSortDirection;
import me.agno.gridcore.sorting.GridSortMode;
import me.agno.gridcore.sorting.IColumnOrderer;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public interface ISortableColumn<T> extends IColumn<T> {

    Collection<IColumnOrderer<T>> getOrderers();

    boolean isColumnSortDefined();

    boolean isSortEnabled();

    GridSortMode getSortMode();

    boolean isSorted();

    void setSorted(boolean sorted);

    Optional<GridSortDirection> getDirection();

    void setDirection(GridSortDirection direction);

    Optional<GridSortDirection> getInitialDirection();

    void setInitialDirection(GridSortDirection initialDirection);

    IGridColumn<T> sortable(boolean sort);

    IGridColumn<T> sortable(boolean sort, GridSortMode gridSortMode);

    IGridColumn<T> SortInitialDirection(GridSortDirection direction);

    <TData> IGridColumn<T> thenSortBy(Function<T, TData> expression);

    <TData> IGridColumn<T> thenSortByDescending(Function<T, TData> expression);
}
