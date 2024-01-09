package me.agno.gridcore;

import me.agno.gridcore.pagination.PagingType;
import me.agno.gridcore.utils.QueryDictionary;

import java.util.Collection;

public interface IGrid<T> {

    QueryDictionary<String[]> getQuery();

    void setQuery(QueryDictionary<String[]> query);

    IGridColumnCollection getColumns();

    Collection<T> getItemsToDisplay();

    int getDisplayingItemsCount();

    PagingType getPagingType();

    void setPagingType(PagingType pagingType);

    SearchOptions getSearchOptions();

    void setSearchOptions(SearchOptions searchOptions);

    boolean isExtSortingEnabled();

    void setExtSortingEnabled(boolean extSortingEnabled);

    boolean isHiddenExtSortingHeader();

    void setHiddenExtSortingHeader(boolean hiddenExtSortingHeader);

    boolean isGroupingEnabled();

    void setGroupingEnabled(boolean groupingEnabled);

    String getRemoveDiacritics();

    void setRemoveDiacritics(String removeDiacritics);

    int getItemsCount();
}
