package me.agno.gridjavacore.annotations;

import java.util.List;
import java.util.Map;

public interface IGridAnnotationsProvider<T> {
    GridColumn getAnnotationForColumn(String name, Class<T> type);
    boolean isColumnMapped(String name, Class<T> type);
    GridTable getAnnotationForTable(Class<T> type);
    List<Map.Entry<String, GridColumn>> getAnnotationsForTableColumns(Class<T> type);
}
