package me.agno.gridcore;

import me.agno.gridcore.columns.IGridColumn;
import me.agno.gridcore.utils.IGrid;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Function;

public interface IGridColumnCollection<T> extends Collection<IGridColumn<T>>
{
    IGrid<T> getGrid();

    IGridColumn<T> Add(IGridColumn<T> column);

    IGridColumn<T> Add();

    IGridColumn<T> Add(boolean hidden);

    IGridColumn<T> Add(String columnName);

    IGridColumn<T> Add(boolean hidden, String columnName);

    <TKey> IGridColumn<T> Add(Function<T, TKey> expression);

    <TKey> IGridColumn<T> Add(Function<T, TKey> expression, String columnName);

    <TKey> IGridColumn<T> Add(Function<T, TKey> expression, boolean hidden);

    <TKey> IGridColumn<T> Add(Function<T, TKey> expression, Comparator<TKey> comparer);

    <TKey> IGridColumn<T> Add(Function<T, TKey> expression, Comparator<TKey> comparer, String columnName);

    <TKey> IGridColumn<T> Add(Function<T, TKey> expression, Comparator<TKey> comparer,  boolean hidden);

    <TKey> IGridColumn<T> Get(Function<T, TKey> expression);

    IGridColumn<T> Get(String name);

    IGridColumn<T> GetByName(String name);
}
