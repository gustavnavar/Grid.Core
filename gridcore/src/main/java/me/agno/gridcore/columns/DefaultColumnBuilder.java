package me.agno.gridcore.columns;

import lombok.Getter;
import lombok.Setter;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.annotations.GridColumn;
import me.agno.gridcore.annotations.IGridAnnotationsProvider;
import me.agno.gridcore.sorting.GridSortDirection;
import me.agno.gridcore.sorting.GridSortMode;

public class DefaultColumnBuilder<T> implements IColumnBuilder<T>
{
    protected IGridAnnotationsProvider<T> annotations;
    protected IGrid<T> grid;

    @Getter
    @Setter
    private boolean defaultSortEnabled;

    @Getter
    @Setter
    private GridSortMode defaultGridSortMode = GridSortMode.THREE_STATE;

    @Getter
    @Setter
    private boolean defaultFilteringEnabled;

    public DefaultColumnBuilder(IGrid<T> grid, IGridAnnotationsProvider<T> annotations) {
        this.grid = grid;
        this.annotations = annotations;
    }


    public <TData> IGridColumn<T> createColumn(String expression, Class<TData> targetType, boolean hidden) {

        var column = new GridCoreColumn<T, TData>(expression, targetType, this.grid);
        column.setHidden(hidden);
        GridColumn columnOpt = this.annotations.getAnnotationForColumn(expression, this.grid.getTargetType());
        if (columnOpt != null)
            applyColumnAnnotationSettings(column, columnOpt);
        return column;
    }

    private void applyColumnAnnotationSettings(IGridColumn<T> column, GridColumn options) {
        ((GridCoreColumn<T,?>)column
                .filterable(options.filterEnabled()))
                .internalSortable(options.sortEnabled());

        GridSortDirection initialDirection = options.sortInitialDirection();
        column.sortInitialDirection(initialDirection);
    }
}
