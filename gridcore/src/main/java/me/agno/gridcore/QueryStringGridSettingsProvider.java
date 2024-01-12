package me.agno.gridcore;

import lombok.Getter;
import me.agno.gridcore.filtering.QueryStringFilterSettings;
import me.agno.gridcore.searching.QueryStringSearchSettings;
import me.agno.gridcore.sorting.QueryStringSortSettings;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class QueryStringGridSettingsProvider implements IGridSettingsProvider
{

    private QueryStringFilterSettings FilterSettings;
    private QueryStringSortSettings SortSettings;
    private QueryStringSearchSettings SearchSettings;

    public QueryStringGridSettingsProvider(LinkedHashMap<String, List<String>> query)
    {
        SortSettings = new QueryStringSortSettings(query);
        //add additional header renderer for filterable columns:
        FilterSettings = new QueryStringFilterSettings(query);
        SearchSettings = new QueryStringSearchSettings(query);
    }
}