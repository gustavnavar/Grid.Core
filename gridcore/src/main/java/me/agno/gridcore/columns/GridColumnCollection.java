package me.agno.gridcore.columns;

import lombok.Getter;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.IGridColumnCollection;
import me.agno.gridcore.sorting.IGridSortSettings;

import java.util.LinkedHashMap;
import java.util.Objects;

public class GridColumnCollection<T> extends LinkedHashMap<String, IGridColumn<T>> implements IGridColumnCollection<T> {

    private final IColumnBuilder<T> columnBuilder;
    private final IGridSortSettings sortSettings;

    public GridColumnCollection(IGrid<T> grid, IColumnBuilder<T> columnBuilder, IGridSortSettings sortSettings) {
        Grid = grid;
        this.columnBuilder = columnBuilder;
        this.sortSettings = sortSettings;
    }

    @Getter
    public IGrid<T> Grid;

    public IGridColumn<T> add() {
        return add(false);
    }

    public IGridColumn<T> add(boolean hidden) {
        return add(null, null, hidden);
    }

    public IGridColumn<T> add(String columnName) {
        return add(false, columnName);
    }

    public IGridColumn<T> add(boolean hidden, String columnName) {
        IGridColumn<T> newColumn = createColumn(null, null, hidden, columnName);
        return add(newColumn);
    }

    public <TData> IGridColumn<T> add(String expression, Class<TData> targetType) {
        return add(expression, targetType, false);
    }

    public <TData> IGridColumn<T> add(String expression, Class<TData> targetType, String columnName){
        IGridColumn<T> newColumn = createColumn(expression, targetType,false, columnName);
        return add(newColumn);
    }
    
    public <TData> IGridColumn<T> add(String expression, Class<TData> targetType, boolean hidden){
        IGridColumn<T> newColumn = createColumn(expression, targetType, hidden, "");
        return add(newColumn);
    }

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
