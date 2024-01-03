package me.agno.gridcore.annotations;

import me.agno.gridcore.pagination.PagingType;

public interface GridTable {

    PagingType getPagingType();

    void setPagingType(PagingType pagingType);

    int getPageSize();

    void setPageSize(int pageSize);

    int getPagingMaxDisplayedPages();

    void setPagingMaxDisplayedPages(int pagingMaxDisplayedPages);
}
