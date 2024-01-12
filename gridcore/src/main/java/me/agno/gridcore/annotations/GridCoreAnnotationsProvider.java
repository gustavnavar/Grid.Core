package me.agno.gridcore.annotations;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import me.agno.gridcore.utils.Tuple;

import java.lang.reflect.Field;
import java.util.*;

public class GridCoreAnnotationsProvider<T> implements IGridAnnotationsProvider<T> {

    public GridColumn getAnnotationForColumn(String name, Class<T> type) {
        var field = Arrays.stream(type.getDeclaredFields()).filter(r -> r.getName().equals(name)).findFirst();
        return field.map(value -> value.getAnnotation(GridColumn.class)).orElse(null);
    }

    public boolean isColumnMapped(String name, Class<T> type) {
        return Arrays.stream(type.getDeclaredFields()).anyMatch(r -> r.getName().equals(name)
                && r.getAnnotation(NotMappedColumn.class) == null);
    }

    public GridTable getAnnotationForTable(Class<T> type) {
        return type.getAnnotation(GridTable.class);
    }

    public List<Tuple<String, Boolean, GridColumn>> getAnnotationsForTableColumns(Class<T> type) {
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
                    return Integer.compare(o1.getAnnotation(GridColumn.class).getPosition(),
                            o2.getAnnotation(GridColumn.class).getPosition());
            }
        }).toList();

        List<Tuple<String, Boolean, GridColumn>> annotations = new ArrayList<>();
        for(var field : fields) {
            var columnAnnotation = field.getAnnotation(Column.class);
            if(columnAnnotation != null) {
                var tuple = new Tuple<String, Boolean, GridColumn>(field.getName(),
                        field.getAnnotation(Id.class) != null,
                        field.getAnnotation(GridColumn.class));
                annotations.add(tuple);
            }
        }
        return annotations;
    }
}