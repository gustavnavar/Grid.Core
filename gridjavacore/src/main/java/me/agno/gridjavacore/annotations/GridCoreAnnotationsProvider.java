package me.agno.gridjavacore.annotations;

import jakarta.persistence.Column;

import java.lang.reflect.Field;
import java.util.*;

/**
 * The GridCoreAnnotationsProvider class is an implementation of the IGridAnnotationsProvider interface.
 * It provides methods to access and manipulate grid annotations for a specific type.
 *
 * @param <T> the type of the data object for which annotations are provided
 */
public class GridCoreAnnotationsProvider<T> implements IGridAnnotationsProvider<T> {

    /**
     * Retrieves the {@link GridColumn} annotation for a specified column in a grid.
     *
     * @param name the name of the column
     * @param type the class representing the grid
     * @return the {@link GridColumn} annotation for the specified column, or null if not found
     */
    public GridColumn getAnnotationForColumn(String name, Class<T> type) {
        var names = name.split(".");
        if(names.length == 0)
            return null;
        else if(names.length == 1) {
            if(names[0] != null && ! names[0].isEmpty())
                return null;
            var field = Arrays.stream(type.getDeclaredFields()).filter(r -> r.getName().equals(names[0])).findFirst();
            return field.map(value -> value.getAnnotation(GridColumn.class)).orElse(null);
        }
        else {
            Class<?> capturedType = type;
            GridColumn annotation = null;
            for(int i = 0; i < names.length; i ++) {
                if(names[i] != null && ! names[i].isEmpty())
                    return null;
                var field = getField(names[i], capturedType);
                annotation = field.map(value -> value.getAnnotation(GridColumn.class)).orElse(null);
                if(annotation == null)
                    return null;
                capturedType = annotation.type();
            }
            return annotation;
        }
    }

    private Optional<Field> getField(String name, Class<?> type) {
        return Arrays.stream(type.getDeclaredFields()).filter(r -> r.getName().equals(name)).findFirst();
    }

    /**
     * Determines whether a column is mapped in a grid based on its name and type.
     *
     * @param name the name of the column
     * @param type the class representing the grid
     * @return true if the column is mapped, false otherwise
     */
    public boolean isColumnMapped(String name, Class<T> type)
    {
        var names = name.split(".");
        if(names.length == 0)
            return true;
        else if(names.length == 1) {
            if(names[0] != null && ! names[0].isEmpty())
                return true;
            var field = Arrays.stream(type.getDeclaredFields()).filter(r -> r.getName().equals(names[0])).findFirst();
            return field.map(value -> value.getAnnotation(NotMappedColumn.class)).isEmpty();
        }
        else {
            Class<?> capturedType = type;
            GridColumn columnAnnotation = null;
            for(int i = 0; i < names.length - 1 ; i ++) {
                if(names[i] != null && ! names[i].isEmpty())
                    return true;
                var field = getField(names[i], capturedType);

                var notMapped = field.map(value -> value.getAnnotation(NotMappedColumn.class)).isPresent();
                if(notMapped)
                    return false;

                columnAnnotation = field.map(value -> value.getAnnotation(GridColumn.class)).orElse(null);
                if(columnAnnotation == null)
                    return true;
                capturedType = columnAnnotation.type();
            }
            return true;
        }
    }

    /**
     * Retrieves the {@link GridTable} annotation for a specified class representing a grid.
     *
     * @param type the class representing the grid
     * @return the {@link GridTable} annotation for the specified grid class, or null if not found
     */
    public GridTable getAnnotationForTable(Class<T> type) {
        return type.getAnnotation(GridTable.class);
    }

    /**
     * Retrieves the annotations for table columns in a specified class representing a grid.
     *
     * @param type the class representing the grid
     * @return a list of map entries where each entry represents the name of a column and its corresponding {@link GridColumn} annotation
     */
    public List<Map.Entry<String, GridColumn>> getAnnotationsForTableColumns(Class<T> type) {
        // if any property has a position attribute it's necessary
        // to create a new array before adding columns to the collection
        var fields = Arrays.stream(type.getDeclaredFields()).sorted(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                if(o1.getAnnotation(GridColumn.class) == null && o2.getAnnotation(GridColumn.class) == null)
                    return 0;
                else if(o1.getAnnotation(GridColumn.class) == null)
                    return 1;
                else if(o2.getAnnotation(GridColumn.class) == null)
                    return -1;
                else
                    return Integer.compare(o1.getAnnotation(GridColumn.class).position(),
                            o2.getAnnotation(GridColumn.class).position());
            }
        }).toList();

        List<Map.Entry<String, GridColumn>> annotations = new ArrayList<>();
        for(var field : fields) {
            var columnAnnotation = field.getAnnotation(Column.class);
            if(columnAnnotation != null) {
                var tuple = new AbstractMap.SimpleEntry<>(field.getName(), field.getAnnotation(GridColumn.class));
                annotations.add(tuple);
            }
        }
        return annotations;
    }
}