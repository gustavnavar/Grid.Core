package me.agno.gridcore;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridcore.columns.GridColumnCollection;
import me.agno.gridcore.filtering.FilterProcessor;
import me.agno.gridcore.pagination.IGridPager;
import me.agno.gridcore.pagination.IPagerProcessor;
import me.agno.gridcore.pagination.PagingType;
import me.agno.gridcore.searching.SearchProcessor;
import me.agno.gridcore.sorting.GridSortMode;
import me.agno.gridcore.sorting.SortProcessor;
import me.agno.gridcore.totals.CountProcessor;
import me.agno.gridcore.totals.TotalsDTO;
import me.agno.gridcore.totals.TotalsProcessor;

import java.util.LinkedHashMap;
import java.util.List;

public interface IGrid<T> extends IGridOptions{

    LinkedHashMap<String, List<String>> getQuery();

    void setQuery(LinkedHashMap<String, List<String>> query);

    GridColumnCollection<T> getColumns();

    List<T> getItemsToDisplay();

    long getDisplayingItemsCount();

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

    long getItemsCount();

    IPagerProcessor<T> getPagerProcessor();

    SearchProcessor<T> getSearchProcessor();

    FilterProcessor<T> getFilterProcessor();

    SortProcessor<T> getSortProcessor();

    TotalsProcessor<T> getTotalsProcessor();

    CountProcessor<T> getCountProcessor();

    IGridPager<T> getPager();

    void setPager(IGridPager<T> pager);

    IGridSettingsProvider getSettings();

    TotalsDTO getTotals();

    boolean isDefaultSortEnabled();

    void setDefaultSortEnabled(boolean defaultFilteringEnabled);

    GridSortMode getGridSortMode();

    void setGridSortMode(GridSortMode gridSortMode);

    boolean isDefaultFilteringEnabled();

    void setDefaultFilteringEnabled(boolean defaultFilteringEnabled);

    void autoGenerateColumns();

    EntityManager getEntityManager();

    CriteriaBuilder getCriteriaBuilder();

    CriteriaQuery<T> getCriteriaQuery();

    Root<T> getRoot();

    Class<T> getTargetType();

    Predicate getPredicate();
}
