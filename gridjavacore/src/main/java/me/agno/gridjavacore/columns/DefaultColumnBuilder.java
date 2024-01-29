package me.agno.gridjavacore.columns;

import lombok.Getter;
import lombok.Setter;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.annotations.GridColumn;
import me.agno.gridjavacore.annotations.IGridAnnotationsProvider;
import me.agno.gridjavacore.sorting.GridSortDirection;
import me.agno.gridjavacore.sorting.GridSortMode;

/**
 * The DefaultColumnBuilder class is an implementation of the IColumnBuilder interface that creates grid columns based on grid annotations.
 *
 * @param <T> the type of the grid class
 */
public class DefaultColumnBuilder<T> implements IColumnBuilder<T> {

    protected IGridAnnotationsProvider<T> annotations;
    protected IGrid<T> grid;

    /**
     * The defaultSortEnabled variable represents whether the default sorting is enabled for a grid column.
     * If defaultSortEnabled is set to true, the grid column will be sortable by default.
     * If defaultSortEnabled is set to false, the grid column will not be sortable by default.
     *
     * @getter
     * @setter
     * @see IColumnBuilder
     * @see DefaultColumnBuilder
     */
    @Getter
    @Setter
    private boolean defaultSortEnabled;

    /**
     * The defaultGridSortMode variable represents the default sorting mode for a grid column.
     * It can have two possible values: THREE_STATE and TWO_STATE.
     */
    @Getter
    @Setter
    private GridSortMode defaultGridSortMode = GridSortMode.THREE_STATE;

    /**
     * Represents the default filtering enabled flag for a grid column.
     *
     * The defaultFilteringEnabled flag determines whether the grid column is filterable by default.
     * If defaultFilteringEnabled is set to true, the grid column will be filterable by default.
     * If defaultFilteringEnabled is set to false, the grid column will not be filterable by default.
     *
     * @return true if default filtering is enabled for the grid column, false otherwise.
     * @setter Set the default filtering enabled flag for the grid column.
     */
    @Getter
    @Setter
    private boolean defaultFilteringEnabled;

    /**
     * Constructs a DefaultColumnBuilder object.
     *
     * @param grid the IGrid object to which the columns will be added.
     * @param annotations the IGridAnnotationsProvider object that provides annotations for the columns.
     */
    public DefaultColumnBuilder(IGrid<T> grid, IGridAnnotationsProvider<T> annotations) {
        this.grid = grid;
        this.annotations = annotations;
    }

    /**
     * Creates a grid column based on the given expression, target type, and whether it should be hidden.
     *
     * @param expression the expression used for mapping the column
     * @param targetType the class representing the target type of the grid
     * @param hidden indicates whether the column should be initially hidden
     * @param <TData> the type of data expected for the column
     * @return an instance of IGridColumn if the column is mapped, null otherwise
     */
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
