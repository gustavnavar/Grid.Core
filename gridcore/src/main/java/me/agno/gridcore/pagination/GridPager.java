package me.agno.gridcore.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.utils.CustomQueryStringBuilder;

public class GridPager<T> implements IGridPager<T> {
    
    public final static int DEFAULT_MAX_DISPLAYED_PAGES = 5;
    public final static int DEFAULT_PAGE_SIZE = 20;

    public final static String DEFAULT_PAGE_QUERY_PARAMETER = "grid-page";
    public final static String DEFAULT_PAGE_SIZE_QUERY_PARAMETER = "grid-pagesize";
    public final static String DEFAULT_START_INDEX_QUERY_PARAMETER = "grid-start-index";
    public final static String DEFAULT_VIRTUALIZED_COUNT_QUERY_PARAMETER = "grid-virt-count";
    public final static String DEFAULT_NO_TOTALS_PARAMETER = "grid-no-totals";

    @Getter
    private final IGrid<T> grid;

    private final CustomQueryStringBuilder queryBuilder;

    private int currentPage;

    @Getter
    private int itemsCount;

    @Getter
    private int maxDisplayedPages = DEFAULT_MAX_DISPLAYED_PAGES;

    @Getter
    private int pageSize;

    @Getter
    private int queryPageSize;

    @Getter
    @Setter
    private int startIndex = 0;

    @Getter
    @Setter
    private int virtualizedCount = 0;

    @Getter
    @Setter
    private boolean noTotals = false;

    @Getter
    @Setter
    private String parameterName;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int pageCount;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int startDisplayedPage;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int endDisplayedPage;

    public GridPager(IGrid<T> grid) {

        this.grid = grid;

        if (grid.getQuery() == null)
            throw new IllegalArgumentException("No http context here!");

        this.currentPage = -1;
        this.queryBuilder = new CustomQueryStringBuilder(this.grid.getQuery());

        if (this.grid.getPagingType() == PagingType.VIRTUALIZATION)
        {
            var startIndexParameter = this.grid.getQuery().get(DEFAULT_START_INDEX_QUERY_PARAMETER);
            if (startIndexParameter != null && startIndexParameter.size() == 1)
                this.startIndex = Integer.parseInt(startIndexParameter.get(0));

            var virtualizedCountParameter = this.grid.getQuery().get(DEFAULT_VIRTUALIZED_COUNT_QUERY_PARAMETER);
            if (virtualizedCountParameter != null && virtualizedCountParameter.size() == 1)
                this.virtualizedCount = Integer.parseInt(virtualizedCountParameter.get(0));
        }
        else
        {
            this.parameterName = DEFAULT_PAGE_QUERY_PARAMETER;
            this.maxDisplayedPages = DEFAULT_MAX_DISPLAYED_PAGES;

            var pageSizeParameter = this.grid.getQuery().get(DEFAULT_PAGE_SIZE_QUERY_PARAMETER);
            int pageSize = 0;
            if (pageSizeParameter != null && pageSizeParameter.size() == 1)
                pageSize = Integer.parseInt(pageSizeParameter.get(0));
            this.queryPageSize = pageSize;

            this.pageSize = DEFAULT_PAGE_SIZE;
        }
    }

    public void initialize(int count) {
        this.itemsCount = count;
    }

    public void setPageSize(int value) {
        this.pageSize = value;
        RecalculatePages();
    }

    public void setQueryPageSize(int value) {
        this.queryPageSize = value;
        RecalculatePages();
    }

    public int getCurrentPage() {

        if (this.currentPage >= 0 || this.grid.getPagingType() == PagingType.VIRTUALIZATION) return this.currentPage;
        var currentPageParameter = this.grid.getQuery().get(this.parameterName);
        if(currentPageParameter != null && currentPageParameter.size() == 1)
            this.currentPage = Integer.parseInt(currentPageParameter.get(0));
        else
            this.currentPage = 1;

        if (this.currentPage > this.pageCount)
            this.currentPage = this.pageCount;
        return this.currentPage;
    }

    public void setCurrentPage(int value) {
        this.currentPage = value;
        RecalculatePages();
    }

    public void setItemsCount(int value) {
        this.itemsCount = value;
        RecalculatePages();
    }

    public void setMaxDisplayedPages(int value) {
        this.maxDisplayedPages = value;
        RecalculatePages();
    }

    protected void RecalculatePages() {
        if (this.grid.getPagingType() == PagingType.VIRTUALIZATION)
            return;

        if (this.itemsCount == 0) {
            this.pageCount = 0;
            return;
        }

        if (this.queryPageSize != 0)
            this.pageSize = this.queryPageSize;
        this.pageCount = (int) Math.ceil(this.itemsCount / (double) this.pageSize);

        if (getCurrentPage() > this.pageCount)
            setCurrentPage(this.pageCount);

        this.startDisplayedPage = (getCurrentPage() - this.maxDisplayedPages/2) < 1 ? 1 : getCurrentPage() - this.maxDisplayedPages/2;
        this.endDisplayedPage = (getCurrentPage() + this.maxDisplayedPages/2) > this.pageCount
                ? this.pageCount
                : getCurrentPage() + this.maxDisplayedPages/2;
    }

    public String GetLinkForPage(int pageIndex) {
        return this.queryBuilder.getQueryStringWithParameter(this.parameterName, Integer.toString(pageIndex));
    }

}