package me.agno.gridjavacore.searching;

import java.util.LinkedHashMap;
import java.util.List;

public interface IGridSearchSettings {
    LinkedHashMap<String, List<String>> getQuery();
    String getSearchValue();
}
