package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.sorting.GridSortDirection;
import me.agno.gridjavacore.sorting.GridSortMode;
import me.agno.gridjavacore.sorting.IColumnOrderer;

import java.util.List;
import java.util.Optional;

public interface ISortableColumn<T> extends IColumn<T> {

    List<IColumnOrderer<T>> getOrderers();

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

    IGridColumn<T> sortInitialDirection(GridSortDirection direction);

    IGridColumn<T> thenSortBy(String expression);

    IGridColumn<T> thenSortByDescending(String expression);
}
