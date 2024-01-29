package me.agno.gridjavacore;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.Setter;
import me.agno.gridjavacore.annotations.GridColumn;
import me.agno.gridjavacore.annotations.GridCoreAnnotationsProvider;
import me.agno.gridjavacore.annotations.GridTable;
import me.agno.gridjavacore.annotations.IGridAnnotationsProvider;
import me.agno.gridjavacore.columns.*;
import me.agno.gridjavacore.filtering.FilterProcessor;
import me.agno.gridjavacore.pagination.GridPager;
import me.agno.gridjavacore.pagination.IGridPager;
import me.agno.gridjavacore.pagination.IPagerProcessor;
import me.agno.gridjavacore.pagination.PagerProcessor;
import me.agno.gridjavacore.pagination.PagingType;
import me.agno.gridjavacore.searching.SearchProcessor;
import me.agno.gridjavacore.sorting.GridSortMode;
import me.agno.gridjavacore.sorting.SortProcessor;
import me.agno.gridjavacore.totals.CountProcessor;
import me.agno.gridjavacore.totals.TotalsDTO;
import me.agno.gridjavacore.totals.TotalsProcessor;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Represents a grid that displays and manages a collection of items of type T.
 * The grid provides functionality for filtering, searching, sorting, and pagination.
 *
 * @param <T> the type of items in the grid
 */
public class Grid<T> implements IGrid<T> {

    private final IGridAnnotationsProvider<T> annotations;
    private final IColumnBuilder<T> columnBuilder;

    private long itemsCount = -1; // total items count on collection
    private long displayingItemsCount = -1; // count of displaying items (if using pagination)
    private List<T> itemsToList; //items after processors
    private boolean itemsPreProcessed; //is preprocessors launched?
    private boolean itemsProcessed; //is processors launched?


    /**
     * A variable that represents an EntityManager instance.
     *
     * EntityManager is an interface used to interact with the persistence context. It
     * manages the lifecycle of entities, performs database operations, and
     * facilitates JPA (Java Persistence API) operations.
     */
    @Getter
    private EntityManager entityManager;

    /**
     * Represents a Criteria Builder for filtering and searching grid data.
     */
    @Getter
    private CriteriaBuilder criteriaBuilder;

    /**
     * Get the criteria query used by the Grid.
     *
     * @return the criteria query used by the Grid
     */
    @Getter
    private CriteriaQuery<T> criteriaQuery;

    /**
     * This variable represents the root element of a tree-like structure.
     * The type parameter T represents the type of the elements contained in the root.
     *
     * The root element is annotated with the @Getter annotation, which generates a getter method
     * for accessing the root element.
     *
     * In the context of the Grid class, the root element is used to build and process the grid data,
     * such as filtering, searching, sorting, and pagination.
     *
     * @param <T> the type of elements contained in the root
     */
    @Getter
    private Root<T> root;

    /**
     * Represents the target type used in the Grid class.
     *
     * @param <T> the type of data displayed in the grid
     */
    @Getter
    private Class<T> targetType;

    /**
     * Represents a Predicate used for filtering and searching the grid data.
     */
    @Setter
    private Predicate predicate;

    /**
     * Retrieves the predicate used for filtering and searching the grid data.
     *
     * @return the predicate used for filtering and searching
     */
    public Predicate getPredicate() {
        preProcess();
        return predicate;
    }

    /**
     * Represents a List of Order objects.
     *
     * The orderList variable is a List that stores instances of the Order class. It is used to hold a collection of orders.
     */
    @Getter
    @Setter
    private List<Order> orderList;

    /**
     * The FilterProcessor class is responsible for processing filters in a grid.
     * It applies the filter settings to the grid data and generates the corresponding predicate.
     *
     * @param <T> the type of data displayed in the grid
     */
    @Getter
    private FilterProcessor<T> filterProcessor;

    /**
     * A search processor that applies search functionality to a grid.
     *
     * @param <T> the type of data displayed in the grid
     */
    @Getter
    private SearchProcessor<T> searchProcessor;

    /**
     * The FilterProcessor class is responsible for processing totals in a grid.
     *
     * @param <T> the type of data displayed in the grid
     */
    @Getter
    private TotalsProcessor<T> totalsProcessor;

    /**
     * This class represents a processor responsible for counting items in a grid.
     * It is used by the Grid class to retrieve the count of items and can be customized with a custom process function.
     *
     * @param <T> the type of data displayed in the grid
     */
    @Getter
    private CountProcessor<T> countProcessor;

    /**
     * The SortProcessor class is responsible for processing sorting in a grid.
     *
     * @param <T> the type of data processed by the SortProcessor
     */
    @Getter
    private SortProcessor<T> sortProcessor;

    /**
     * Represents a variable named pagerProcessor.
     *
     * The pagerProcessor variable is of type IPagerProcessor<T>.
     * It is used to process pagination and retrieve the items for display in the grid.
     *
     * Usage:
     * You can access the pagerProcessor variable using its getter method.
     * To process pagination, you can call the process() method and pass a CriteriaQuery<T> object.
     * You can also set a custom process function using the setProcess() method.
     */
    @Getter
    private IPagerProcessor<T> pagerProcessor;

    /**
     * A class representing a query in a grid. This class is used to store a collection of sortings, filters, and searches and their values
     * for querying data in a grid.
     */
    @Getter
    @Setter
    private LinkedHashMap<String, List<String>> query;

    /**
     * Represents a variable that holds an instance of an object that implements the {@link IGridSettingsProvider} interface.
     * This variable is used to get the sort, filter, and search settings for a grid.
     */
    @Getter
    private IGridSettingsProvider settings;

    /**
     * Sets the settings for the grid.
     *
     * @param value the settings provider
     */
    public void setSettings(IGridSettingsProvider value) {
        this.settings = value;
        this.sortProcessor.updateSettings(this.settings.getSortSettings());
        this.filterProcessor.updateSettings(this.settings.getFilterSettings());
        this.searchProcessor.updateSettings(this.settings.getSearchSettings());
    }

    /**
     * Represents a collection of columns in a grid.
     *
     * @param <T> the type of data displayed in the grid
     */
    @Getter
    private GridColumnCollection<T> columns;

    /**
     * Represents the paging type for the grid.
     *
     * The PagingType enumeration defines the available types of paging for the grid:
     * - NONE: No paging is applied.
     * - PAGINATION: Paging is applied by dividing the data into pages.
     * - VIRTUALIZATION: Paging is applied by dynamically loading data as the user scrolls.
     */
    @Getter
    private PagingType pagingType = PagingType.NONE;

    /**
     * Sets the paging type for the grid.
     *
     * @param value the paging type to set
     */
    public void setPagingType(PagingType value) {
        if (this.pagingType == value)
            return;
        else
            this.pagingType = value;

        if (this.pagingType != PagingType.NONE) {
            if (this.pagerProcessor == null)
                this.pagerProcessor = new PagerProcessor<T>(this);
        }
        else {
            this.pagerProcessor = null;
        }
    }

    /**
     * Represents a variable that holds an instance of a Pager.
     */
    @Setter
    private IGridPager<T> pager;

    /**
     * Retrieves the pager for the grid. If the pager is not already initialized, it will be created.
     *
     * @return the pager for the grid
     */
    public IGridPager<T> getPager() {
        if (pager == null)
            pager = new GridPager(this);
        return pager;
    }

    /**
     * The search options for the grid.
     */
    @Getter
    @Setter
    private SearchOptions searchOptions = new SearchOptions(false);

    /**
     * Represents a string variable used to store the name of the database stored procedure to remove diacritics when filtering and searching.
     *
     * This variable is annotated with @Getter and @Setter to generate the corresponding getter and setter methods.
     */
    @Getter
    @Setter
    private String removeDiacritics = null;

    /**
     * Constructs a Grid object.
     *
     * @param entityManager the entity manager used for querying the database
     * @param targetType the class representing the entity type for the grid
     * @param query the query parameters for filtering, sorting, and searching the grid data
     * @param columnBuilder the column builder used for defining the grid columns (optional, if null a default column builder will be used)
     */
    public Grid(EntityManager entityManager, Class<T> targetType, LinkedHashMap<String, List<String>> query,
                IColumnBuilder<T> columnBuilder) {

        this.entityManager = entityManager;
        this.targetType = targetType;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
        this.criteriaQuery = this.criteriaBuilder.createQuery(this.targetType);
        this.root = this.criteriaQuery.from(this.targetType);

        this.query = query;

        //set up sort settings:
        this.settings = new QueryStringGridSettingsProvider(query);

        this.sortProcessor = new SortProcessor<T>(this, this.settings.getSortSettings());
        this.filterProcessor = new FilterProcessor<T>(this, this.settings.getFilterSettings());
        this.searchProcessor = new SearchProcessor<T>(this, this.settings.getSearchSettings());
        this.totalsProcessor = new TotalsProcessor<T>(this);
        this.countProcessor = new CountProcessor<T>(this);

        this.annotations = new GridCoreAnnotationsProvider<T>();

        //Set up column collection:
        if (columnBuilder == null)
            this.columnBuilder = new DefaultColumnBuilder<T>(this, this.annotations);
        else
            this.columnBuilder = columnBuilder;
        this.columns = new GridColumnCollection<T>(this, this.columnBuilder, this.settings.getSortSettings());

        applyGridSettings();

        int page = 0;
        int startIndex = 0;
        int virtualizedCount = 0;
        boolean noTotals = false;

        var startIndexParameter = query.get(GridPager.DEFAULT_START_INDEX_QUERY_PARAMETER);
        var virtualizedCountParameter = query.get(GridPager.DEFAULT_VIRTUALIZED_COUNT_QUERY_PARAMETER);
        var noTotalsParameter = query.get(GridPager.DEFAULT_NO_TOTALS_PARAMETER);
        if (startIndexParameter != null && ! startIndexParameter.isEmpty() &&
                startIndexParameter.get(0) != null && ! startIndexParameter.get(0).trim().isEmpty() &&
                virtualizedCountParameter != null && ! virtualizedCountParameter.isEmpty() &&
                virtualizedCountParameter.get(0) != null && ! virtualizedCountParameter.get(0).trim().isEmpty() &&
                noTotalsParameter != null && ! noTotalsParameter.isEmpty() &&
                noTotalsParameter.get(0) != null && ! noTotalsParameter.get(0).trim().isEmpty()) {

            setPagingType(PagingType.VIRTUALIZATION);
            try {
                startIndex = Integer.parseInt(startIndexParameter.get(0).trim());
            }
            catch (Exception ignored) {}
            ((GridPager<T>)this.getPager()).setStartIndex(startIndex);
            try {
                virtualizedCount = Integer.parseInt(virtualizedCountParameter.get(0).trim());
            }
            catch (Exception ignored) {}
            ((GridPager<T>)this.getPager()).setVirtualizedCount(virtualizedCount);
            try {
                noTotals = Boolean.parseBoolean(noTotalsParameter.get(0).trim());
            }
            catch (Exception ignored) {}
            ((GridPager<T>)this.getPager()).setNoTotals(noTotals);
        }
        else {
            var pageParameter = query.get(((GridPager<T>)this.getPager()).getParameterName());
            if (pageParameter != null && ! pageParameter.isEmpty() &&
                    pageParameter.get(0) != null && ! pageParameter.get(0).trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageParameter.get(0).trim());
                } catch (Exception ignored) {}
            }
            if (page == 0)
                page++;
            ((GridPager<T>)this.getPager()).setCurrentPage(page);
        }
    }

    private void preProcess() {
        if (!this.itemsPreProcessed) {
            this.itemsPreProcessed = true;
            this.predicate = this.filterProcessor.process(this.predicate);
            this.predicate = this.searchProcessor.process(this.predicate);

            this.criteriaQuery.select(this.root);
            var predicate = this.predicate;
            if(predicate != null)
                this.criteriaQuery.where(predicate);

            // added to avoid 2nd EF opened task if counting later
            this.itemsCount = this.countProcessor.process(this.predicate);

            // calculate totals
            this.totalsProcessor.process(this.predicate);
        }
    }

    /**
     * Retrieves the count of items in the grid.
     *
     * @return the count of items
     */
    public long getItemsCount() {
        //call preprocessors before:
        preProcess();
        return this.itemsCount;
    }

    /**
     * Sets the number of items in the grid.
     *
     * @param value the number of items to set
     */
    public void setItemsCount(int value) {
        this.itemsCount = value; //value can be set by pager (for minimizing db calls)
    }

    /**
     * Retrieves the count of items currently being displayed in the grid.
     *
     * @return the count of displaying items
     */
    public long getDisplayingItemsCount() {
        if (this.displayingItemsCount >= 0)
            return this.displayingItemsCount;
        this.displayingItemsCount = getItemsToDisplay().size();
        return this.displayingItemsCount;
    }

    /**
     * Retrieves the items to display in the grid.
     *
     * @return the list of items to display
     */
    public List<T> getItemsToDisplay() {
        prepareItemsToDisplay();
        return this.itemsToList;
    }

    protected void prepareItemsToDisplay() {
        preProcess();
        if (!this.itemsProcessed) {
            this.itemsProcessed = true;
            this.orderList = this.sortProcessor.process(this.orderList);
            if(this.orderList != null && ! this.orderList.isEmpty())
                this.criteriaQuery.orderBy(this.orderList);
            this.itemsToList = ((PagerProcessor<T>)this.pagerProcessor).process(this.criteriaQuery, getItemsCount()).getResultList();
        }
    }

    /**
     * Applies the grid settings defined by the {@link GridTable} annotation.
     * If the annotation is not present or the paging type is set to {@link PagingType#NONE},
     * no settings are applied.
     *
     * If the paging type is set to {@link PagingType#PAGINATION}, the method sets the page size
     * and maximum displayed pages on the grid's pager if the values in the annotation are greater than 0.
     *
     * @see GridTable
     * @see PagingType
     */
    void applyGridSettings() {
        GridTable opt = this.annotations.getAnnotationForTable(getTargetType());
        if (opt == null) return;
        setPagingType(opt.pagingType());

        if (getPagingType() == PagingType.PAGINATION)
        {
            if (opt.pageSize() > 0)
                this.getPager().setPageSize(opt.pageSize());

            if (opt.pagingMaxDisplayedPages() > 0 && this.getPager() instanceof GridPager)
                ((GridPager<T>)this.getPager()).setMaxDisplayedPages(opt.pagingMaxDisplayedPages());
        }
    }

    /**
     * Automatically generates columns for the grid based on the annotations present in the target type.
     * Each field annotated with the {@link GridColumn} annotation
     * will be added as a column to the grid.
     * If the annotation's key attribute is set to true, the column will be marked as a primary key column.
     */
    public void autoGenerateColumns() {

        var annotationsList = this.annotations.getAnnotationsForTableColumns(this.targetType);

        for (var item : annotationsList) {
            String name = item.getKey();
            GridColumn annotation = item.getValue();

            if(annotation.key())
                this.columns.add(name, annotation.type()).setPrimaryKey(true);
            else
                this.columns.add(name, annotation.type());
        }
    }

    /**
     * Retrieves the totals for the grid.
     *
     * @return the TotalsDTO object containing the sum, average, max, min, and calculations for each column
     */
    public TotalsDTO getTotals()
    {
        var totals = new TotalsDTO();
        if (isSumEnabled())
            for (IGridColumn<T> column : this.columns.values()) {
                if (column.isSumEnabled())
                    totals.getSum().put(column.getName(), column.getSumValue());
            }

        if (isAverageEnabled())
            for (IGridColumn<T> column : this.columns.values()) {
                if (column.isAverageEnabled())
                    totals.getAverage().put(column.getName(), column.getAverageValue());
            }

        if (isMaxEnabled())
            for (IGridColumn<T> column : this.columns.values()) {
                if (column.isMaxEnabled())
                    totals.getMax().put(column.getName(), column.getMaxValue());
        }

        if (isMinEnabled())
            for (IGridColumn<T> column : this.columns.values()) {
                if (column.isMinEnabled())
                    totals.getMin().put(column.getName(), column.getMinValue());
        }

        if (isCalculationEnabled())
            for (IGridColumn<T> column : this.columns.values()) {
                if (column.isCalculationEnabled())
                    totals.getCalculations().put(column.getName(), column.getCalculationValues());
        }

        return totals;
    }

    /**
     * Determines whether the default sort is enabled in the grid.
     * If the default sort is enabled, the grid will apply a predefined sorting order to the data.
     *
     * @return true if the default sort is enabled, false otherwise
     */
    public boolean isDefaultSortEnabled() {
        return this.columnBuilder.isDefaultSortEnabled();
    }

    /**
     * Sets whether the default sort is enabled in the grid.
     * If the default sort is enabled, the grid will apply a predefined sorting order to the data.
     *
     * @param value true to enable the default sort, false to disable it
     */
    public void setDefaultSortEnabled(boolean value) {
        this.columnBuilder.setDefaultSortEnabled(value);
    }

    /**
     * Returns the grid's sort mode.
     *
     * This method retrieves the default grid sort mode specified by the column builder.
     *
     * @return the grid's sort mode
     */
    public GridSortMode getGridSortMode() {
        return this.columnBuilder.getDefaultGridSortMode();
    }

    /**
     * Sets the sort mode for the grid. The sort mode determines the behavior of sorting in the grid.
     *
     * @param value the sort mode to set
     */
    public void setGridSortMode(GridSortMode value) {
        this.columnBuilder.setDefaultGridSortMode(value);
    }

    /**
     * Checks if the default filtering is enabled in the grid.
     *
     * @return true if default filtering is enabled, false otherwise
     */
    public boolean isDefaultFilteringEnabled() {
        return this.columnBuilder.isDefaultFilteringEnabled();
    }

    /**
     * Sets whether the default filtering is enabled in the grid.
     *
     * @param value true to enable the default filtering, false to disable it
     */
    public void setDefaultFilteringEnabled(boolean value) {
        this.columnBuilder.setDefaultFilteringEnabled(value);
    }

    /**
     * Checks if the sum is enabled for any column in the grid.
     *
     * @return true if the sum is enabled for any column, false otherwise
     */
    public boolean isSumEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isSumEnabled);
    }

    /**
     * Determines whether the average is enabled for any column in the grid.
     *
     * @return true if the average is enabled for any column, false otherwise
     */
    public boolean isAverageEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isAverageEnabled);
    }

    /**
     * Checks if the maximum value calculation is enabled for any column in the grid.
     *
     * @return true if the maximum value calculation is enabled for any column, false otherwise
     */
    public boolean isMaxEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isMaxEnabled);
    }

    /**
     * Checks if the minimum value calculation is enabled for any column in the grid.
     *
     * @return true if the minimum value calculation is enabled for any column, false otherwise
     */
    public boolean isMinEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isMinEnabled);
    }

    /**
     * Checks if the calculation is enabled for any column in the grid.
     *
     * @return true if the calculation is enabled for any column, false otherwise
     */
    public boolean isCalculationEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isCalculationEnabled);
    }
}
