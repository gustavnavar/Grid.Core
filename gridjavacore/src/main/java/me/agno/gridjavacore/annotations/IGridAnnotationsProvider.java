package me.agno.gridjavacore.annotations;

import java.util.List;
import java.util.Map;

/**
 * The {@code IGridAnnotationsProvider} interface provides methods to retrieve grid annotations for a given class representing a grid.
 *
 * @param <T> the type of the grid class
 */
public interface IGridAnnotationsProvider<T> {

    /**
     * Retrieves the {@link GridColumn} annotation for a given column name and type.
     * This method is used to customize the behavior and appearance of a column in a grid.
     *
     * @param name the name of the column
     * @param type the class type of the grid
     * @return the {@link GridColumn} annotation for the column, or null if not found
     */
    GridColumn getAnnotationForColumn(String name, Class<T> type);

    /**
     * Determines whether a column is mapped based on the given name and type.
     *
     * @param name the name of the column
     * @param type the class type of the grid
     * @return true if the column is mapped, false otherwise
     */
    boolean isColumnMapped(String name, Class<T> type);

    /**
     * Retrieves the {@link GridTable} annotation for a given class representing a grid table.
     * This method is used to retrieve the grid table settings defined by the annotation.
     *
     * @param type the class type of the grid table
     * @return the {@link GridTable} annotation for the grid table, or null if not found
     */
    GridTable getAnnotationForTable(Class<T> type);

    /**
     * Retrieves the annotations for table columns of a given class representing a grid.
     *
     * @param type the class type of the grid
     * @return a list of map entries containing the column name as the key and the {@link GridColumn} annotation as the value
     */
    List<Map.Entry<String, GridColumn>> getAnnotationsForTableColumns(Class<T> type);
}
