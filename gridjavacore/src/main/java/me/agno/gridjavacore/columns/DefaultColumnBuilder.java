package me.agno.gridjavacore.columns;

import lombok.Getter;
import lombok.Setter;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.annotations.GridColumn;
import me.agno.gridjavacore.annotations.IGridAnnotationsProvider;
import me.agno.gridjavacore.sorting.GridSortDirection;
import me.agno.gridjavacore.sorting.GridSortMode;

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

        if (!this.annotations.isColumnMapped(expression, grid.getTargetType()))
            return null; //grid column not mapped

        var column = new GridCoreColumn<T, TData>(expression, targetType, this.grid);
        column.setHidden(hidden);

        GridColumn columnOpt = this.annotations.getAnnotationForColumn(expression, this.grid.getTargetType());
        if (columnOpt != null)
            applyColumnAnnotationSettings(column, columnOpt);
        return column;
    }

    private void applyColumnAnnotationSettings(IGridColumn<T> column, GridColumn options) {

        column.setHidden(options.hidden());
        column.setPrimaryKey(options.key());
        ((GridCoreColumn<T, ?>) column).setTargetType(options.type());

        if(!options.hidden()) {
            column.filterable(options.filterEnabled());
            ((GridCoreColumn<T, ?>) column).internalSortable(options.sortEnabled());

            GridSortDirection initialDirection = options.sortInitialDirection();
            column.sortInitialDirection(initialDirection);
        }
    }

}
