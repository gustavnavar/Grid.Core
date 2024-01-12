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
    protected IGridAnnotationsProvider<T> _annotations;
    protected IGrid<T> _grid;

    @Getter
    @Setter
    private boolean DefaultSortEnabled;

    @Getter
    @Setter
    private GridSortMode DefaultGridSortMode = GridSortMode.ThreeState;

    @Getter
    @Setter
    private boolean DefaultFilteringEnabled;

    public DefaultColumnBuilder(IGrid<T> grid, IGridAnnotationsProvider<T> annotations) {
        _grid = grid;
        _annotations = annotations;
    }


    public <TData> IGridColumn<T> CreateColumn(String expression, Class<TData> targetType, boolean hidden) {

        if (!_annotations.isColumnMapped(expression, _grid.getTargetType()))
            return null; //grid column not mapped

        boolean isExpressionOk = expression == null;
        if (isExpressionOk) {
            var column = new GridCoreColumn<T, TData>(expression, targetType, _grid);
            column.setHidden(hidden);
            GridColumn columnOpt = _annotations.getAnnotationForColumn(expression, _grid.getTargetType());
            if (columnOpt != null)
                applyColumnAnnotationSettings(column, columnOpt);
            return column;
        }
        throw new IllegalArgumentException("Expression '" + expression + "' not supported by grid");
    }

    private void applyColumnAnnotationSettings(IGridColumn<T> column, GridColumn options) {
        ((GridCoreColumn<T,?>)column
                .filterable(options.isFilterEnabled()))
                .internalSortable(options.isSortEnabled());

        GridSortDirection initialDirection = options.getSortInitialDirection();
        column.sortInitialDirection(initialDirection);
    }
}
