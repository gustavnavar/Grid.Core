package me.agno.gridcore.searching;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;

public class QueryStringSearchSettings implements IGridSearchSettings {
    
    public static final String DEFAULT_SEARCH_QUERY_PARAMETER = "grid-search";

    @Getter
    private LinkedHashMap<String, List<String>> query;

    @Getter
    private String searchValue;

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