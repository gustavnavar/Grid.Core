package me.agno.gridcore.sorting;

import java.util.LinkedHashMap;
import java.util.List;

public interface IGridSortSettings {

    LinkedHashMap<String, List<String>> getQuery();

    String getColumnName();

    void setColumnName(String columnName);

    GridSortDirection getDirection();

    void setDirection(GridSortDirection direction);

    DefaultOrderColumnCollection getSortValues();
}
