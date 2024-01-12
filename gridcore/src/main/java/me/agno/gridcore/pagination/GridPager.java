package me.agno.gridcore.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.agno.gridcore.IGrid;
import me.agno.gridcore.utils.CustomQueryStringBuilder;

public class GridPager<T> implements IGridPager<T> {
    
    public final int DefaultMaxDisplayedPages = 5;
    public final int DefaultPageSize = 20;

    public final static String DefaultPageQueryParameter = "grid-page";
    public final static String DefaultPageSizeQueryParameter = "grid-pagesize";
    public final static String DefaultStartIndexQueryParameter = "grid-start-index";
    public final static String DefaultVirtualizedCountQueryParameter = "grid-virt-count";
    public final static String DefaultNoTotalsParameter = "grid-no-totals";

    @Getter
    private final IGrid<T> Grid;

    private final CustomQueryStringBuilder _queryBuilder;

    private int _currentPage;

    @Getter
    private int ItemsCount;

    @Getter
    private int MaxDisplayedPages = DefaultMaxDisplayedPages;

    @Getter
    private int PageSize;

    @Getter
    private int QueryPageSize;

    @Getter
    @Setter
    private int StartIndex = 0;

    @Getter
    @Setter
    private int VirtualizedCount = 0;

    @Getter
    @Setter
    private boolean NoTotals = false;

    @Getter
    @Setter
    private String ParameterName;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int PageCount;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int StartDisplayedPage;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int EndDisplayedPage;

    public GridPager(IGrid<T> grid) {

        Grid = grid;

        if (grid.getQuery() == null)
            throw new IllegalArgumentException("No http context here!");

        _currentPage = -1;
        _queryBuilder = new CustomQueryStringBuilder(Grid.getQuery());

        if (Grid.getPagingType() == PagingType.Virtualization)
        {
            var startIndexParameter = Grid.getQuery().get(DefaultStartIndexQueryParameter);
            if (startIndexParameter != null && startIndexParameter.size() == 1)
                StartIndex = Integer.parseInt(startIndexParameter.get(0));

            var virtualizedCountParameter = Grid.getQuery().get(DefaultVirtualizedCountQueryParameter);
            if (virtualizedCountParameter != null && virtualizedCountParameter.size() == 1)
                VirtualizedCount = Integer.parseInt(virtualizedCountParameter.get(0));
        }
        else
        {
            ParameterName = DefaultPageQueryParameter;
            MaxDisplayedPages = MaxDisplayedPages;

            var pageSizeParameter = Grid.getQuery().get(DefaultPageSizeQueryParameter);
            int pageSize = 0;
            if (pageSizeParameter != null && pageSizeParameter.size() == 1)
                pageSize = Integer.parseInt(pageSizeParameter.get(0));
            QueryPageSize = pageSize;

            PageSize = DefaultPageSize;
        }
    }

    public void Initialize(int count) {
        ItemsCount = count;
    }

    public void setPageSize(int value) {
        PageSize = value;
        RecalculatePages();
    }

    public void setQueryPageSize(int value) {
        QueryPageSize = value;
        RecalculatePages();
    }

    public int getCurrentPage() {

        if (_currentPage >= 0 || Grid.getPagingType() == PagingType.Virtualization) return _currentPage;
        var currentPageParameter = Grid.getQuery().get(ParameterName);
        if(currentPageParameter != null && currentPageParameter.size() == 1)
            _currentPage = Integer.parseInt(currentPageParameter.get(0));
        else
            _currentPage = 1;

        if (_currentPage > PageCount)
            _currentPage = PageCount;
        return _currentPage;
    }

    public void setCurrentPage(int value) {
        _currentPage = value;
        RecalculatePages();
    }

    public void setItemsCount(int value) {
        ItemsCount = value;
        RecalculatePages();
    }

    public void setMaxDisplayedPages(int value) {
        MaxDisplayedPages = value;
        RecalculatePages();
    }

    protected void RecalculatePages() {
        if (Grid.getPagingType() == PagingType.Virtualization)
            return;

        if (ItemsCount == 0) {
            PageCount = 0;
            return;
        }

        if (QueryPageSize != 0)
            PageSize = QueryPageSize;
        PageCount = (int) Math.ceil(ItemsCount / (double) PageSize);

        if (getCurrentPage() > PageCount)
            setCurrentPage(PageCount);

        StartDisplayedPage = (getCurrentPage() - MaxDisplayedPages/2) < 1 ? 1 : getCurrentPage() - MaxDisplayedPages/2;
        EndDisplayedPage = (getCurrentPage() + MaxDisplayedPages/2) > PageCount
                ? PageCount
                : getCurrentPage() + MaxDisplayedPages/2;
    }

    public String GetLinkForPage(int pageIndex) {
        return _queryBuilder.GetQueryStringWithParameter(ParameterName, Integer.toString(pageIndex));
    }

}