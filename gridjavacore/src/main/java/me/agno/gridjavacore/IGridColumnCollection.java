package me.agno.gridjavacore;

import me.agno.gridjavacore.columns.IGridColumn;

public interface IGridColumnCollection<T>
{
    IGrid<T> getGrid();

    IGridColumn<T> add(IGridColumn<T> column);

    IGridColumn<T> add();

    IGridColumn<T> add(boolean hidden);

    IGridColumn<T> add(String columnName);

    IGridColumn<T> add(boolean hidden, String columnName);

    <TData> IGridColumn<T> add(String expression, Class<TData> targetType);

    <TData> IGridColumn<T> add(String expression, Class<TData> targetType, String columnName);

    <TData> IGridColumn<T> add(String expression, Class<TData> targetType, boolean hidden);

    IGridColumn<T> get(String name);
}
