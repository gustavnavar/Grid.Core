package me.agno.gridcore;

import me.agno.gridcore.filtering.IGridFilterSettings;
import me.agno.gridcore.searching.IGridSearchSettings;
import me.agno.gridcore.sorting.IGridSortSettings;

public interface IGridSettingsProvider {

    IGridSortSettings getSortSettings();

    IGridFilterSettings getFilterSettings();

    IGridSearchSettings getSearchSettings();
}
