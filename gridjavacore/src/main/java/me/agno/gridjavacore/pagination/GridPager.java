package me.agno.gridjavacore.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.utils.CustomQueryStringBuilder;

/**
 * The GridPager class is responsible for managing pagination functionality for a grid.
 */
public class GridPager<T> implements IGridPager<T> {

    /**
     * The maximum number of pages to be displayed in a grid pager.
     */
    public final static int DEFAULT_MAX_DISPLAYED_PAGES = 5;

    /**
     *
     * The {@code DEFAULT_PAGE_SIZE} variable represents the default page size for pagination.
     * It is an integer constant with a value of 20.
     *
     * <p>This variable is defined in the {@code GridPager} class and is used in various methods
     * for pagination and generating query strings.</p>
     */
    public final static int DEFAULT_PAGE_SIZE = 20;

    /**
     * The default query parameter for paging in a grid.
     */
    public final static String DEFAULT_PAGE_QUERY_PARAMETER = "grid-page";

    /**
     * The default query parameter for page size in a grid.
     */
    public final static String DEFAULT_PAGE_SIZE_QUERY_PARAMETER = "grid-pagesize";

    /**
     * The default query parameter used for specifying the start index of a grid pagination.
     * This value is set to "grid-start-index".
     *
     * @since GridJavaCore 1.0.0
     */
    public final static String DEFAULT_START_INDEX_QUERY_PARAMETER = "grid-start-index";

    /**
     * The default query parameter for virtualized count in the grid pagination.
     * This parameter is used to determine the total number of items in a virtualized grid.
     * It is used in conjunction with the {@link GridPager#getVirtualizedCount()} method.
     * The value of this parameter is "grid-virt-count".
     */
    public final static String DEFAULT_VIRTUALIZED_COUNT_QUERY_PARAMETER = "grid-virt-count";

    /**
     * The DEFAULT_NO_TOTALS_PARAMETER variable is a constant that represents the query parameter name for "grid-no-totals" in the CustomQueryStringBuilder class.
     * This parameter is used to control whether totals should be included in the grid or not.
     * If the value of this parameter is present and set to "true", totals will be excluded.
     *
     * Usage example:
     * CustomQueryStringBuilder builder = new CustomQueryStringBuilder(query);
     * String queryString = builder.getQueryStringWithParameter(DEFAULT_NO_TOTALS_PARAMETER, "true");
     *
     * @see GridPager
     * @see CustomQueryStringBuilder
     */
    public final static String DEFAULT_NO_TOTALS_PARAMETER = "grid-no-totals";

    private final CustomQueryStringBuilder queryBuilder;

    /**
     * This variable represents an instance of GridPager that contains a grid.
     * The grid is an object that displays data based on a given query and provides
     * filtering, sorting, searching, and pagination functionality.
     *
     * The grid variable is annotated with the @Getter annotation, indicating that it
     * has a getter method generated automatically.
     */
    @Getter
    private final IGrid<T> grid;

    private int currentPage;

    /**
     * Retrieves the current page number.
     *
     * @return the current page number
     */
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

    /**
     * Sets the current page number for the grid pager.
     *
     * @param value the page number to set
     */
    public void setCurrentPage(int value) {
        this.currentPage = value;
        RecalculatePages();
    }

    /**
     * Represents the count of items in the grid.
     */
    @Getter
    private long itemsCount;

    /**
     * Sets the number of items in the grid.
     *
     * @param value the number of items to set
     */
    public void setItemsCount(long value) {
        this.itemsCount = value;
        RecalculatePages();
    }

    /**
     * Specifies the maximum number of displayed pages in a grid pager.
     */
    @Getter
    private int maxDisplayedPages = DEFAULT_MAX_DISPLAYED_PAGES;

    /**
     * Sets the maximum number of displayed pages for the grid pager.
     *
     * @param value the maximum number of displayed pages to set
     */
    public void setMaxDisplayedPages(int value) {
        this.maxDisplayedPages = value;
        RecalculatePages();
    }

    /**
     * The pageSize variable represents the number of items to be displayed per page.
     */
    @Getter
    private int pageSize;

    /**
     * Sets the page size for the GridPager.
     *
     * @param value the value to set as the page size
     */
    public void setPageSize(int value) {
        this.pageSize = value;
        RecalculatePages();
    }

    /**
     * Gets the page size used for querying data.
     *
     * @return the query page size
     */
    @Getter
    private int queryPageSize;

    /**
     * Sets the query page size for the GridPager.
     *
     * @param value the value to set as the query page size
     */
    public void setQueryPageSize(int value) {
        this.queryPageSize = value;
        RecalculatePages();
    }

    /**
     * The startIndex variable represents the starting index for pagination.
     * It determines the first item index of the current page in a list of items.
     * By default, the startIndex is set to 0, indicating that the first item will be the starting point.
     * The value of startIndex can be modified to navigate to a different page in the list.
     */
    @Getter
    @Setter
    private int startIndex = 0;

    /**
     * Represents the count of virtualized items in the grid.
     * This count is used for virtualized pagination to determine the total number of items available.
     */
    @Getter
    @Setter
    private int virtualizedCount = 0;

    /**
     * Determines whether or not to display the totals of items in the grid.
     *
     * @return true if totals should not be displayed, false otherwise
     */
    @Getter
    @Setter
    private boolean noTotals = false;

    /**
     * Represents a parameter name used in the grid pager.
     *
     * This class provides a getter and setter for the parameter name. The parameter name is used
     * in the GridPager class to construct query strings for pagination.
     */
    @Getter
    @Setter
    private String parameterName;

    /**
     * The page count of the grid pager.
     * The page count represents the total number of pages available for the grid data.
     * It is calculated based on the number of items in the grid and the page size.
     * The page count is a positive integer value.
     */
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int pageCount;

    /**
     * Represents the starting displayed page for a GridPager.
     */
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int startDisplayedPage;

    /**
     * The variable `endDisplayedPage` represents the last page number that is displayed in the grid pager.
     * It is a private field in the class `GridPager`.
     *
     * The `GridPager` class is a pagination component that is used in conjunction with a grid to display paginated data.
     * It provides functionality for navigating between pages and setting the page size.
     *
     * Access to the `endDisplayedPage` variable is provided through the getter and setter methods, which are protected.
     *
     * Note: The `endDisplayedPage` variable is used internally by the `GridPager` class to determine the range of pages to display.
     * It is calculated based on the current page, page size, and total number of items.
     *
     * For more information about the `GridPager` class and its methods and fields, refer to its documentation.
     *
     * @see GridPager
     */
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int endDisplayedPage;

    /**
     * Constructs a GridPager object for the given grid.
     *
     * @param grid the grid instance to create the pager for
     * @throws IllegalArgumentException if the grid's query is null
     */
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

    /**
     * Initializes the GridPager with the specified count of items.
     *
     * @param count the number of items to initialize the GridPager with
     */
    public void initialize(long count) {
        setItemsCount(count);
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

    /**
     * Returns the link for a specific page index.
     *
     * @param pageIndex the index of the page
     * @return the link for the specified page index
     * @throws IllegalArgumentException if pageIndex is less than 0
     */
    public String GetLinkForPage(int pageIndex) {
        return this.queryBuilder.getQueryStringWithParameter(this.parameterName, Integer.toString(pageIndex));
    }

}