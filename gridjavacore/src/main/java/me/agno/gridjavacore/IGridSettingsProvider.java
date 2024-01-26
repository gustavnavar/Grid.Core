package me.agno.gridjavacore;

import me.agno.gridjavacore.filtering.IGridFilterSettings;
import me.agno.gridjavacore.searching.IGridSearchSettings;
import me.agno.gridjavacore.sorting.IGridSortSettings;

public interface IGridSettingsProvider {

    IGridSortSettings getSortSettings();

    IGridFilterSettings getFilterSettings();

    IGridSearchSettings getSearchSettings();
}
