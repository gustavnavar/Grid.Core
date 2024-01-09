package me.agno.gridcore.columns;

import me.agno.gridcore.filtering.ColumnFilterValue;
import me.agno.gridcore.filtering.GridFilterType;
import me.agno.gridcore.filtering.IColumnFilter;

public interface IFilterableColumn<T> extends IColumn<T> {

    IColumnFilter getFilter();

    boolean isFilterEnabled();

    ColumnFilterValue getInitialFilterSettings();

    void setInitialFilterSettings(ColumnFilterValue initialFilterSettings);

    String getFilterWidgetTypeName();

    Object getFilterWidgetData();

    IGridColumn<T> filterable(boolean enabled);

    IGridColumn<T> SetInitialFilter(GridFilterType type, String value);

    IGridColumn<T> SetFilterWidgetType(Object widgetData);
}
