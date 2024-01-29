package me.agno.gridjavacore.columns;

import lombok.Getter;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.IGridColumnCollection;
import me.agno.gridjavacore.sorting.IGridSortSettings;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Represents a collection of grid columns in a grid.
 * @param <T> the type of the grid data
 */
public class GridColumnCollection<T> extends LinkedHashMap<String, IGridColumn<T>> implements IGridColumnCollection<T> {

    private final IColumnBuilder<T> columnBuilder;
    private final IGridSortSettings sortSettings;

    /**
     * Represents a collection of grid columns in a grid.
     */
    public GridColumnCollection(IGrid<T> grid, IColumnBuilder<T> columnBuilder, IGridSortSettings sortSettings) {
        Grid = grid;
        this.columnBuilder = columnBuilder;
        this.sortSettings = sortSettings;
    }

    /**
     * Represents a grid in a grid column collection.
     */
    @Getter
    public IGrid<T> Grid;

    /**
     * Adds a new column to the grid column collection with the default hidden state.
     *
     * @return the added grid column
     */
    public IGridColumn<T> add() {
        return add(false);
    }

    /**
     * Adds a column to the grid column collection with the specified hidden state.
     *
     * @param hidden true if the column should be hidden, false otherwise
     * @return the added grid column
     */
    public IGridColumn<T> add(boolean hidden) {
        return add(null, null, hidden);
    }

    /**
     * Adds a column to the grid column collection with the specified column name.
     *
     * @param columnName the name of the column to be added
     * @return the added grid column
     */
    public IGridColumn<T> add(String columnName) {
        return add(false, columnName);
    }

    /**
     * Adds a column to the grid column collection with the specified hidden state and column name.
     *
     * @param hidden true if the column should be hidden, false otherwise
     * @param columnName the name of the column to be added
     * @return the added grid column
     */
    public IGridColumn<T> add(boolean hidden, String columnName) {
        IGridColumn<T> newColumn = createColumn(null, null, hidden, columnName);
        return add(newColumn);
    }

    /**
     * Adds a new column to the grid column collection with the specified expression and target type.
     *
     * @param expression the expression used to generate the column for the grid
     * @param targetType the class representing the target type of the column's data
     * @param <TData>    the data type of the column
     * @return the added grid column
     */
    public <TData> IGridColumn<T> add(String expression, Class<TData> targetType) {
        return add(expression, targetType, false);
    }

    /**
     * Adds a new column to the grid column collection with the specified expression, target type, and column name.
     *
     * @param expression  the expression used to generate the column for the grid
     * @param targetType the class representing the target type of the column's data
     * @param columnName the name of the column to be added
     * @param <TData>    the type of the column's data
     * @return the added grid column
     */
    public <TData> IGridColumn<T> add(String expression, Class<TData> targetType, String columnName){
        IGridColumn<T> newColumn = createColumn(expression, targetType,false, columnName);
        return add(newColumn);
    }

    /**
     * Adds a new column to the grid column collection with the specified expression, target type, and hidden state.
     *
     * @param expression the expression used to generate the column for the grid
     * @param targetType the class representing the target type of the column's data
     * @param hidden true if the column should be hidden, false otherwise
     * @param <TData> the type of the column's data
     * @return the added grid column
     */
    public <TData> IGridColumn<T> add(String expression, Class<TData> targetType, boolean hidden){
        IGridColumn<T> newColumn = createColumn(expression, targetType, hidden, "");
        return add(newColumn);
    }

    /**
     * Adds a column to the grid column collection.
     *
     * @param column the column to be added
     * @return the added grid column
     * @throws NullPointerException if the specified column is null
     * @throws IllegalArgumentException if the specified column already exists in the grid
     */
    public IGridColumn<T> add(IGridColumn<T> column) {

        if (column == null)
            throw new NullPointerException("column");

        try {
            put(column.getName(), column);
        }
        catch (Exception e) {
                throw new IllegalArgumentException("Column '" + column.getName() + "' already exist in the grid");
        }
        UpdateColumnsSorting();
        return column;
    }

    /**
     * Retrieves a grid column with the specified name.
     *
     * @param name the name of the grid column to retrieve
     * @return the grid column with the specified name
     * @throws IllegalArgumentException if the name is null or empty
     */
    @Override
    public IGridColumn<T> get(String name) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("name");
        return super.get(name);
    }

    private <TData> IGridColumn<T> createColumn(String expression, Class<TData> targetType, boolean hidden, String columnName) {
        IGridColumn<T> newColumn = this.columnBuilder.createColumn(expression, targetType, hidden);
        if (columnName != null && !columnName.trim().isEmpty())
            ((GridCoreColumn<T, TData>)newColumn).setName(columnName);
        return newColumn;
    }

    void UpdateColumnsSorting() {
        for (IGridColumn<T> gridColumn : this.values()) {
            gridColumn.setSorted(Objects.equals(gridColumn.getName(), this.sortSettings.getColumnName()));
            if (Objects.equals(gridColumn.getName(), this.sortSettings.getColumnName()))
                gridColumn.setDirection(this.sortSettings.getDirection());
            else
                gridColumn.setDirection(null);
        }
    }
}
