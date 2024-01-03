package me.agno.gridcore.sorting;

import me.agno.gridcore.utils.IQueryDictionary;

public interface IGridSortSettings {

    IQueryDictionary<String[]> getQuery();

    String getColumnName();

    void setColumnName(String columnName);

    GridSortDirection getDirection();

    void setDirection(GridSortDirection direction);

    DefaultOrderColumnCollection getSortValues();
}
