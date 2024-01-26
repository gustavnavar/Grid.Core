package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.sorting.GridSortMode;

public interface IColumnBuilder<T>
{
    boolean isDefaultSortEnabled();

    void setDefaultSortEnabled(boolean defaultFilteringEnabled);

    GridSortMode getDefaultGridSortMode();

    void setDefaultGridSortMode(GridSortMode defaultGridSortMode);

    boolean isDefaultFilteringEnabled();

    void setDefaultFilteringEnabled(boolean defaultFilteringEnabled);

    <TData> IGridColumn<T> createColumn(String expression, Class<TData> targetType, boolean hidden);
}
