package me.agno.gridjavacore.annotations;

import me.agno.gridjavacore.pagination.PagingType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * The {@code GridTable} annotation is used to define the settings for a grid table.
 * It can be applied to a class representing the grid table.
 *
 * <p>The annotation can specify the paging type, page size, and maximum displayed pages for the grid table.
 * If the paging type is set to NONE, no settings are applied.
 * If the paging type is set to PAGINATION, the page size and maximum displayed pages can be set.</p>
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GridTable {

    /**
     * Returns the paging type for the grid table.
     *
     * <p>The paging type determines the type of pagination used for the grid table.
     * The available options are:</p>
     * <ul>
     *     <li>{@link PagingType#NONE} - No pagination settings are applied.</li>
     *     <li>{@link PagingType#PAGINATION} - Pagination settings can be set, such as page size and maximum displayed pages.</li>
     *     <li>{@link PagingType#VIRTUALIZATION} - Virtualization settings can be set to optimize performance for large datasets.</li>
     * </ul>
     *
     * @return the paging type for the grid table
     */
    PagingType pagingType() default PagingType.NONE;

    /**
     * Returns the page size for the grid table.
     *
     * <p>The page size determines the number of records displayed on each page of the grid table.</p>
     *
     * @return the page size for the grid table
     */
    int pageSize() default 0;

    /**
     * Retrieves the maximum number of displayed pages for the pagination in the grid table.
     * If the value is set to 0, no maximum limit is applied.
     *
     * @return the maximum number of displayed pages for the pagination
     */
    int pagingMaxDisplayedPages() default 0;
}
