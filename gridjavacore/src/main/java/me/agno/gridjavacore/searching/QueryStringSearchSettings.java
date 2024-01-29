package me.agno.gridjavacore.searching;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * The QueryStringSearchSettings class represents the search settings for a grid based on query string parameters.
 * It implements the IGridSearchSettings interface.
 */
public class QueryStringSearchSettings implements IGridSearchSettings {

    /**
     * The DEFAULT_SEARCH_QUERY_PARAMETER represents the default query string parameter for grid search.
     * This parameter is used to specify the search value in the query string.
     */
    public static final String DEFAULT_SEARCH_QUERY_PARAMETER = "grid-search";

    /**
     * The QueryStringSearchSettings class represents the search settings for a grid based on query string parameters.
     * It implements the IGridSearchSettings interface.
     */
    @Getter
    private LinkedHashMap<String, List<String>> query;

    /**
     * The searchValue variable represents the search value for the grid search settings.
     * It is a string that is used to specify the value being searched for in the grid.
     *
     * Usage:
     * QueryStringSearchSettings settings = new QueryStringSearchSettings(query);
     * String searchValue = settings.getSearchValue();
     *
     * @see QueryStringSearchSettings
     * @see IGridSearchSettings
     */
    @Getter
    private String searchValue;

    /**
     * The QueryStringSearchSettings class represents the search settings for a grid based on query string parameters.
     * It implements the IGridSearchSettings interface.
     */
    public QueryStringSearchSettings(LinkedHashMap<String, List<String>> query) {

        if (query == null)
            throw new IllegalArgumentException("No http context here!");
        this.query = query;

        var search = this.query.get(DEFAULT_SEARCH_QUERY_PARAMETER);
        if (search != null && ! search.isEmpty()) {
            this.searchValue = search.get(0);
        }
    }
}