package me.agno.gridcore.pagination;

import me.agno.gridcore.IGrid;

public interface IGridPager<T> {

    IGrid<T> getGrid();

    void Initialize(int count);

    int getPageSize();

    void setPageSize(int pageSize);

    int getQueryPageSize();

    void setQueryPageSize(int queryPageSize);

    int getCurrentPage();

    int getItemsCount();

    void setItemsCount(int itemsCount);

    int getStartIndex();

    int getVirtualizedCount();

    boolean isNoTotals();
}
