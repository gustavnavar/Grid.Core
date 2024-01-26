package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.searching.IColumnSearch;

public interface ISearchableColumn<T> {

    IColumnSearch<T> getSearch();
}
