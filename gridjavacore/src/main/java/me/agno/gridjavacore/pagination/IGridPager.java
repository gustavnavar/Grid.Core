package me.agno.gridjavacore.pagination;

import me.agno.gridjavacore.IGrid;

public interface IGridPager<T> {

    IGrid<T> getGrid();

    void initialize(long count);

    int getPageSize();

    void setPageSize(int pageSize);

    int getQueryPageSize();

    void setQueryPageSize(int queryPageSize);

    int getCurrentPage();

    long getItemsCount();

    void setItemsCount(long itemsCount);

    int getStartIndex();

    int getVirtualizedCount();

    boolean isNoTotals();
}
