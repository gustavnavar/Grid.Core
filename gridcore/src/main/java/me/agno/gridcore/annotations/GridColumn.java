package me.agno.gridcore.annotations;

import me.agno.gridcore.sorting.GridSortDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GridColumn {

    public boolean SortEnabled() default false;

    public boolean FilterEnabled() default false;

    public GridSortDirection SortInitialDirection() default GridSortDirection.Ascending;
}
