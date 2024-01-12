package me.agno.gridcore.annotations;

import me.agno.gridcore.utils.Tuple;

import java.util.List;

public interface IGridAnnotationsProvider<T> {
    GridColumn getAnnotationForColumn(String name, Class<T> type);
    boolean isColumnMapped(String name, Class<T> type);
    GridTable getAnnotationForTable(Class<T> type);
    List<Tuple<String, Boolean, GridColumn>> getAnnotationsForTableColumns(Class<T> type);
}
