package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.filtering.ColumnFilterValue;
import me.agno.gridjavacore.filtering.GridFilterType;
import me.agno.gridjavacore.filtering.IColumnFilter;

public interface IFilterableColumn<T> extends IColumn<T> {

    IColumnFilter<T> getFilter();

    boolean isFilterEnabled();

    ColumnFilterValue getInitialFilterSettings();

    void setInitialFilterSettings(ColumnFilterValue initialFilterSettings);

    IGridColumn<T> filterable(boolean enabled);

    IGridColumn<T> setInitialFilter(GridFilterType type, String value);
}
