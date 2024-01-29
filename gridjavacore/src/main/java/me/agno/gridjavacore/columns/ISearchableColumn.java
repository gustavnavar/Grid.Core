package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.searching.IColumnSearch;

/**
 * Represents a searchable column in a grid.
 *
 * @param <T> the type of data in the column
 */
public interface ISearchableColumn<T> {

    /**
     * Retrieves the search object for the column.
     *
     * @return the search object for the column
     */
    IColumnSearch<T> getSearch();
}
