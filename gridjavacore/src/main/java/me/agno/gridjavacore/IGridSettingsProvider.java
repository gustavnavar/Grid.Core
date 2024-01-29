package me.agno.gridjavacore;

import me.agno.gridjavacore.filtering.IGridFilterSettings;
import me.agno.gridjavacore.searching.IGridSearchSettings;
import me.agno.gridjavacore.sorting.IGridSortSettings;

/**
 * The interface IGridSettingsProvider defines the methods for retrieving the sort, filter, and search settings for a grid.
 * Implementations of this interface should provide the specific implementations for these settings.
 */
public interface IGridSettingsProvider {

    /**
     * Retrieves the sort settings for the grid.
     *
     * @return the sort settings for the grid
     */
    IGridSortSettings getSortSettings();

    /**
     * Retrieves the filter settings for the grid.
     *
     * @return the filter settings for the grid
     */
    IGridFilterSettings getFilterSettings();

    /**
     * Retrieves the search settings for the grid.
     *
     * @return the search settings for the grid
     */
    IGridSearchSettings getSearchSettings();
}
