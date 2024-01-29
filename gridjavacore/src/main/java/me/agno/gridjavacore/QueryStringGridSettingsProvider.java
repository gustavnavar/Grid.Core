package me.agno.gridjavacore;

import lombok.Getter;
import me.agno.gridjavacore.filtering.QueryStringFilterSettings;
import me.agno.gridjavacore.searching.QueryStringSearchSettings;
import me.agno.gridjavacore.sorting.QueryStringSortSettings;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * The QueryStringGridSettingsProvider class is an implementation of the IGridSettingsProvider interface.
 * It provides the sort, filter, and search settings for a grid based on the query string parameters.
 *
 * This class has the following members:
 * - filterSettings: an instance of QueryStringFilterSettings that represents the filter settings for the grid.
 * - sortSettings: an instance of QueryStringSortSettings that represents the sort settings for the grid.
 * - searchSettings: an instance of QueryStringSearchSettings that represents the search settings for the grid.
 *
 * The QueryStringGridSettingsProvider class is constructed with a LinkedHashMap instance that represents the query string parameters.
 * It initializes the filterSettings, sortSettings, and searchSettings based on the query string parameters.
 */
@Getter
public class QueryStringGridSettingsProvider implements IGridSettingsProvider
{

    private QueryStringFilterSettings filterSettings;
    private QueryStringSortSettings sortSettings;
    private QueryStringSearchSettings searchSettings;

    /**
     * The QueryStringGridSettingsProvider class is an implementation of the IGridSettingsProvider interface.
     * It provides the sort, filter, and search settings for a grid based on the query string parameters.
     */
    public QueryStringGridSettingsProvider(LinkedHashMap<String, List<String>> query)
    {
        this.sortSettings = new QueryStringSortSettings(query);
        //add additional header renderer for filterable columns:
        this.filterSettings = new QueryStringFilterSettings(query);
        this.searchSettings = new QueryStringSearchSettings(query);
    }
}