package me.agno.gridcore.columns;

import me.agno.gridcore.sorting.GridSortMode;

import java.util.Comparator;
import java.util.function.Function;

public interface IColumnBuilder<T>
{
    boolean isDefaultSortEnabled();

    void setDefaultSortEnabled(boolean defaultFilteringEnabled);

    GridSortMode getDefaultGridSortMode();

    void setDefaultGridSortMode(GridSortMode defaultGridSortMode);

    boolean isDefaultFilteringEnabled();

    void setDefaultFilteringEnabled(boolean defaultFilteringEnabled);

    <TDataType> IGridColumn<T> CreateColumn(Function<T, TDataType> expression, boolean hidden);

    <TDataType> IGridColumn<T> CreateColumn(Function<T, TDataType> expression, Comparator<TDataType> comparer,
                                            boolean hidden);
}
