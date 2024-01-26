package me.agno.gridjavacore;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import me.agno.gridjavacore.columns.GridColumnCollection;
import me.agno.gridjavacore.filtering.FilterProcessor;
import me.agno.gridjavacore.pagination.IGridPager;
import me.agno.gridjavacore.pagination.IPagerProcessor;
import me.agno.gridjavacore.pagination.PagingType;
import me.agno.gridjavacore.searching.SearchProcessor;
import me.agno.gridjavacore.sorting.GridSortMode;
import me.agno.gridjavacore.sorting.SortProcessor;
import me.agno.gridjavacore.totals.CountProcessor;
import me.agno.gridjavacore.totals.TotalsDTO;
import me.agno.gridjavacore.totals.TotalsProcessor;

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

    List<Order> getOrderList();
}
