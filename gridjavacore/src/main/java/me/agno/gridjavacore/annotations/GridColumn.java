package me.agno.gridjavacore.annotations;

import me.agno.gridjavacore.sorting.GridSortDirection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a field represents a column in a grid.
 * This annotation can be used to customize the behavior and appearance of the column.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GridColumn {

    /**
     * Returns a boolean value indicating whether the field represents a key.
     * This method is used to retrieve the value of the key attribute in the {@link GridColumn} annotation.
     * If the key attribute is set to true, it indicates that the field represents a primary key column in a grid.
     * Returns false if the key attribute is not set or if the annotation is not present.
     *
     * @return a boolean value indicating whether the field represents a key
     */
    boolean key() default false;

    /**
     * Returns a boolean value indicating whether the field should be hidden in a grid.
     * This method is used to retrieve the value of the hidden attribute in the {@link GridColumn} annotation.
     * If the hidden attribute is set to true, it indicates that the field should be hidden in a grid.
     * Returns false if the hidden attribute is not set or if the annotation is not present.
     *
     * @return a boolean value indicating whether the field should be hidden in a grid
     */
    boolean hidden() default false;

    /**
     * Returns the position of the column in a grid.
     * This method is used to retrieve the value of the position attribute in the {@link GridColumn} annotation.
     * The position value determines the order of the column in the grid.
     *
     * @return the position of the column in a grid.
     */
    int position() default 2147483647;

    /**
     * Returns the class type of the annotated column.
     *
     * @return the type of the annotated column
     */
    Class<?> type();

    /**
     * Indicates whether sorting is enabled for the column.
     *
     * @return true if sorting is enabled for the column, false otherwise
     */
    boolean sortEnabled() default false;

    /**
     *
     * Retrieves the value indicating whether filtering is enabled for the column.
     *
     * @return true if filtering is enabled for the column, false otherwise
     */
    boolean filterEnabled() default false;

    /**
     * Returns the initial sorting direction for a grid column.
     *
     * @return the initial sorting direction for a grid column
     */
    GridSortDirection sortInitialDirection() default GridSortDirection.ASCENDING;
}
