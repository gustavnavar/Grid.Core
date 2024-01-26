package me.agno.gridjavacore;

import lombok.Getter;
import me.agno.gridjavacore.filtering.QueryStringFilterSettings;
import me.agno.gridjavacore.searching.QueryStringSearchSettings;
import me.agno.gridjavacore.sorting.QueryStringSortSettings;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class QueryStringGridSettingsProvider implements IGridSettingsProvider
{

    private QueryStringFilterSettings filterSettings;
    private QueryStringSortSettings sortSettings;
    private QueryStringSearchSettings searchSettings;

    public QueryStringGridSettingsProvider(LinkedHashMap<String, List<String>> query)
    {
        this.sortSettings = new QueryStringSortSettings(query);
        //add additional header renderer for filterable columns:
        this.filterSettings = new QueryStringFilterSettings(query);
        this.searchSettings = new QueryStringSearchSettings(query);
    }
}