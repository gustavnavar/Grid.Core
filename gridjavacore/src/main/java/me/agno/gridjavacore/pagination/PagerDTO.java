package me.agno.gridjavacore.pagination;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a Data Transfer Object for a pager used in pagination or virtualization.
 */
@Getter
@Setter
public class PagerDTO {

    /**
     * Represents the type of paging used in pagination or virtualization.
     * The possible values are:
     * - NONE: Indicates that no paging is used.
     * - PAGINATION: Indicates that pagination is used.
     * - VIRTUALIZATION: Indicates that virtualization is used.
     */
    private PagingType pagingType;

    /**
     * Represents the page size for a pager used in pagination or virtualization.
     */
    private int pageSize;

    /**
     * Represents the current page number in a pager used for pagination or virtualization.
     *
     * The current page number represents the page that is currently being displayed or accessed.
     * It is an integer value greater than or equal to zero.
     */
    private int currentPage;

    /**
     * Represents the count of items in a pager used for pagination or virtualization.
     * The itemsCount variable stores the total number of items available for display or processing.
     * It is a long value that represents the count of items.
     */
    private long itemsCount;

    /**
     * Represents the starting index in a pager used for pagination or virtualization.
     *
     * The startIndex variable represents the index of the first item in the current page or virtualized view.
     * It is an integer value greater than or equal to zero.
     */
    private int startIndex;

    /**
     * Represents the count of items that are being virtually displayed or processed.
     *
     * This variable is used in conjunction with virtualization in a pager, where large datasets are
     * broken down into smaller chunks to improve performance. The virtualizedCount variable stores
     * the count of items in the virtualized view.
     */
    private int virtualizedCount;

    /**
     * Represents a Data Transfer Object for a pager used in pagination or virtualization.
     */
    public PagerDTO()
    { }

    /**
     * Represents a Data Transfer Object for a pager used in pagination or virtualization.
     *
     * This class is used to encapsulate the paging information for a grid or list. It includes the type of paging used,
     * the page size, the current page number, the total count of items, the starting index of the current page or virtualized view,
     * and the count of items being virtually displayed or processed.
     */
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
