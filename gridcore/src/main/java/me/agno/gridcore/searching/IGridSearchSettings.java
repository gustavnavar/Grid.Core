package me.agno.gridcore.searching;

import me.agno.gridcore.utils.IQueryDictionary;

public interface IGridSearchSettings {
    IQueryDictionary<String[]> getQuery();
    String getSearchValue();
}
