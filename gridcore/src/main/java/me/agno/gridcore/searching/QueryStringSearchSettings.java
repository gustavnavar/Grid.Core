package me.agno.gridcore.searching;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

public class QueryStringSearchSettings implements IGridSearchSettings {
    
    public static final String DefaultSearchQueryParameter = "grid-search";

    @Getter
    private LinkedHashMap<String, List<String>> Query;

    @Getter
    private String SearchValue;

    public QueryStringSearchSettings(LinkedHashMap<String, List<String>> query) {

        if (query == null)
            throw new IllegalArgumentException("No http context here!");
        Query = query;

        var search = Query.get(DefaultSearchQueryParameter);
        if (!search.isEmpty()) {
            SearchValue = search.get(0);
        }
    }
}