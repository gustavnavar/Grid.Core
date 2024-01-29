package me.agno.gridjavacore.searching;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * The IGridSearchSettings interface defines the methods to retrieve query and search value settings for a grid search.
 */
public interface IGridSearchSettings {

    /**
     * Retrieves the query settings for a grid search.
     *
     * @return a LinkedHashMap representing the query settings. The keys are string values representing the query parameters,
     *         and the corresponding values are lists of string values representing the query parameter values.
     */
    LinkedHashMap<String, List<String>> getQuery();

    /**
     * Retrieves the search value for the grid search settings.
     *
     * @return the search value as a string
     */
    String getSearchValue();
}
