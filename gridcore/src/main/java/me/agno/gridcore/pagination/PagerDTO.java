package me.agno.gridcore.pagination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagerDTO {

    private PagingType PagingType;
    private int PageSize;
    private int CurrentPage;
    private int ItemsCount;
    private int StartIndex;
    private int VirtualizedCount;

    public PagerDTO()
    { }

    public PagerDTO(PagingType pagingType, int pageSize, int currentPage, int itemsCount, int startIndex, int virtualizedCount)
    {
        PagingType = pagingType;
        PageSize = pageSize;
        CurrentPage = currentPage;
        ItemsCount = itemsCount;
        StartIndex = startIndex;
        VirtualizedCount = virtualizedCount;
    }
}
