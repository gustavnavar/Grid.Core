package me.agno.gridcore.columns;

import lombok.Getter;
import lombok.Setter;
import me.agno.gridcore.ISGrid;
import me.agno.gridcore.annotations.GridColumn;
import me.agno.gridcore.annotations.IGridAnnotationsProvider;
import me.agno.gridcore.sorting.GridSortDirection;
import me.agno.gridcore.sorting.GridSortMode;

import java.util.function.Function;

class DefaultColumnCoreBuilder<T> implements IColumnBuilder<T>
{
    protected IGridAnnotationsProvider _annotations;
    protected ISGrid<T> _grid;

    DefaultColumnCoreBuilder(ISGrid<T> grid, IGridAnnotationsProvider annotations) {
        _grid = grid;
        _annotations = annotations;
    }


    public <TDataType> IGridColumn<T> CreateColumn(Function<T, TDataType> expression, Class targetType, boolean hidden) {

        boolean isExpressionOk = expression == null;
        if (isExpressionOk) {
            var column = new GridCoreColumn<T, TDataType>(expression, targetType, _grid);
            column.setHidden(hidden);
            return column;
        }
        throw new IllegalArgumentException(String.format("Expression '{0}' not supported by grid", expression));
    }

    @Getter
    @Setter
    private boolean DefaultSortEnabled;

    @Getter
    @Setter
    private GridSortMode DefaultGridSortMode = GridSortMode.ThreeState;

    @Getter
    @Setter
    private boolean DefaultFilteringEnabled;

    private <TDataType> void ApplyColumnAnnotationSettings(IGridColumn<T> column, GridColumn options) {
        ((GridCoreColumnBase<T, TDataType>)column
                .filterable(options.FilterEnabled()))
                .internalSortable(options.SortEnabled());

       GridSortDirection initialDirection = options.SortInitialDirection();
       column.SortInitialDirection(initialDirection);
    }
}
