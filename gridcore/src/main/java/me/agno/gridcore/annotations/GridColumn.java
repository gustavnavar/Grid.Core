package me.agno.gridcore.annotations;

import me.agno.gridcore.sorting.GridSortDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GridColumn {

    int getPosition() default 2147483647;

    boolean isSortEnabled() default false;

    boolean isFilterEnabled() default false;

    GridSortDirection getSortInitialDirection() default GridSortDirection.ASCENDING;

    Class<?> getType();
}
