package me.agno.gridjavacore.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a field should not be mapped as a column in a grid.
 * This annotation is used to exclude certain fields from being considered as grid columns.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotMappedColumn {
}
