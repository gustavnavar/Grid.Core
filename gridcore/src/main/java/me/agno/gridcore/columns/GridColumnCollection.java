package me.agno.gridcore.columns;

import lombok.Getter;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.IGridColumnCollection;
import me.agno.gridcore.sorting.IGridSortSettings;
import me.agno.gridcore.utils.QueryDictionary;

import java.util.function.Function;

public class GridColumnCollection<T> extends QueryDictionary<IGridColumn<T>> implements IGridColumnCollection<T> {

    private final IColumnBuilder<T> _columnBuilder;
    private final IGridSortSettings _sortSettings;

    public GridColumnCollection(IGrid<T> grid, IColumnBuilder<T> columnBuilder, IGridSortSettings sortSettings) {
        Grid = grid;
        _columnBuilder = columnBuilder;
        _sortSettings = sortSettings;
    }

    @Getter
    public IGrid<T> Grid;

    public IGridColumn<T> add() {
        return add(false);
    }

    public IGridColumn<T> add(boolean hidden) {
        return add((Function<T, String>)null, null, hidden);
    }

    public IGridColumn<T> add(String columnName) {
        return add(false, columnName);
    }

    public IGridColumn<T> add(boolean hidden, String columnName) {
        IGridColumn<T> newColumn = createColumn((Function<T, String>)null, null, hidden, columnName);
        return add(newColumn);
    }

    public <TKey> IGridColumn<T> add(Function<T, TKey> expression, Class targetType) {
        return add(expression, targetType, false);
    }

    public <TKey> IGridColumn<T> add(Function<T, TKey> expression, Class targetType, String columnName){
        IGridColumn<T> newColumn = createColumn(expression, targetType,false, columnName);
        return add(newColumn);
    }
    
    public <TKey> IGridColumn<T> add(Function<T, TKey> expression, Class targetType, boolean hidden){
        IGridColumn<T> newColumn = createColumn(expression, targetType, hidden, "");
        return add(newColumn);
    }

    public IGridColumn<T> add(IGridColumn<T> column) {

        if (column == null)
            throw new NullPointerException("column");

        try {
            AddOrSet(column.getName(), column);
        }
        catch (Exception e) {
                throw new IllegalArgumentException(String.format("Column '{0}' already exist in the grid", column.getName()));
        }
        UpdateColumnsSorting();
        return column;
    }

    @Override
    public IGridColumn<T> get(String name) {
        if (name == null || name.trim() == "")
            throw new IllegalArgumentException("name");
        return this.get(name);
    }

    private <TKey> IGridColumn<T> createColumn(Function<T, TKey> expression, Class targetType, boolean hidden, String columnName) {
        IGridColumn<T> newColumn = _columnBuilder.CreateColumn(expression, targetType, hidden);
        if (columnName != null && columnName.trim() != "")
            ((GridCoreColumn<T, TKey>)newColumn).setName(columnName);
        return newColumn;
    }

    void UpdateColumnsSorting() {
        for (IGridColumn<T> gridColumn : this.values()) {
            gridColumn.setSorted(gridColumn.getName() == _sortSettings.getColumnName());
            if (gridColumn.getName() == _sortSettings.getColumnName())
                gridColumn.setDirection(_sortSettings.getDirection());
            else
                gridColumn.setDirection(null);
        }
    }
}
