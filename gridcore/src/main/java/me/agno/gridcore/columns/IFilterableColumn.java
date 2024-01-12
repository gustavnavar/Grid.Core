package me.agno.gridcore.columns;

import me.agno.gridcore.filtering.ColumnFilterValue;
import me.agno.gridcore.filtering.GridFilterType;
import me.agno.gridcore.filtering.IColumnFilter;

public interface IFilterableColumn<T> extends IColumn<T> {

    IColumnFilter<T> getFilter();

    boolean isFilterEnabled();

    ColumnFilterValue getInitialFilterSettings();

    void setInitialFilterSettings(ColumnFilterValue initialFilterSettings);

    IGridColumn<T> filterable(boolean enabled);

    IGridColumn<T> setInitialFilter(GridFilterType type, String value);
}
