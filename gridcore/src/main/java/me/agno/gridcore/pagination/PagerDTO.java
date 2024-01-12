package me.agno.gridcore.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagerDTO {

    private PagingType pagingType;
    private int pageSize;
    private int currentPage;
    private long itemsCount;
    private int startIndex;
    private int virtualizedCount;

    public PagerDTO()
    { }

    public PagerDTO(PagingType pagingType, int pageSize, int currentPage, long itemsCount, int startIndex, int virtualizedCount)
    {
        this.pagingType = pagingType;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.itemsCount = itemsCount;
        this.startIndex = startIndex;
        this.virtualizedCount = virtualizedCount;
    }
}
