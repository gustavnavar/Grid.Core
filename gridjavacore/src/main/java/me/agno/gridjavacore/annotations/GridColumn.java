package me.agno.gridjavacore.annotations;

import me.agno.gridjavacore.sorting.GridSortDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GridColumn {

    boolean key() default false;

    boolean hidden() default false;

    int position() default 2147483647;

    Class<?> type();

    boolean sortEnabled() default false;

    boolean filterEnabled() default false;

    GridSortDirection sortInitialDirection() default GridSortDirection.ASCENDING;
}
