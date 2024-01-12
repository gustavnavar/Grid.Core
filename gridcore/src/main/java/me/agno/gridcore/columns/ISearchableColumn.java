package me.agno.gridcore.columns;

import me.agno.gridcore.searching.IColumnSearch;

public interface ISearchableColumn<T> {

    IColumnSearch<T> getSearch();
}
