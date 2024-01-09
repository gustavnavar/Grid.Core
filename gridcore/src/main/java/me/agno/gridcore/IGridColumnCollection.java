package me.agno.gridcore;

import me.agno.gridcore.columns.IGridColumn;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;

public interface IGridColumnCollection<T>
{
    IGrid<T> getGrid();

    IGridColumn<T> add(IGridColumn<T> column);

    IGridColumn<T> add();

    IGridColumn<T> add(boolean hidden);

    IGridColumn<T> add(String columnName);

    IGridColumn<T> add(boolean hidden, String columnName);

    <TKey> IGridColumn<T> add(Function<T, TKey> expression, Class targetType);

    <TKey> IGridColumn<T> add(Function<T, TKey> expression, Class targetType, String columnName);

    <TKey> IGridColumn<T> add(Function<T, TKey> expression, Class targetType, boolean hidden);

    IGridColumn<T> get(String name);
}
