package me.agno.gridcore;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.Setter;
import me.agno.gridcore.annotations.GridColumn;
import me.agno.gridcore.annotations.GridCoreAnnotationsProvider;
import me.agno.gridcore.annotations.GridTable;
import me.agno.gridcore.annotations.IGridAnnotationsProvider;
import me.agno.gridcore.columns.*;
import me.agno.gridcore.filtering.FilterProcessor;
import me.agno.gridcore.pagination.GridPager;
import me.agno.gridcore.pagination.IGridPager;
import me.agno.gridcore.pagination.IPagerProcessor;
import me.agno.gridcore.pagination.PagerProcessor;
import me.agno.gridcore.pagination.PagingType;
import me.agno.gridcore.searching.SearchProcessor;
import me.agno.gridcore.sorting.GridSortMode;
import me.agno.gridcore.sorting.SortProcessor;
import me.agno.gridcore.totals.CountProcessor;
import me.agno.gridcore.totals.TotalsDTO;
import me.agno.gridcore.totals.TotalsProcessor;

import java.util.LinkedHashMap;
import java.util.List;

public class Grid<T> implements IGrid<T> {

    private final IGridAnnotationsProvider<T> annotations;
    private final IColumnBuilder<T> columnBuilder;

    private long itemsCount = -1; // total items count on collection
    private long displayingItemsCount = -1; // count of displaying items (if using pagination)
    private List<T> itemsToList; //items after processors
    private boolean itemsPreProcessed; //is preprocessors launched?
    private boolean itemsProcessed; //is processors launched?


    @Getter
    private EntityManager entityManager;

    @Getter
    private CriteriaBuilder criteriaBuilder;

    @Getter
    private CriteriaQuery<T> criteriaQuery;

    @Getter
    private Root<T> root;

    @Getter
    private Class<T> targetType;

    @Getter
    @Setter
    private Predicate predicate;

    @Getter
    @Setter
    private List<Order> orderList;

    @Getter
    private FilterProcessor<T> filterProcessor;

    @Getter
    private SearchProcessor<T> searchProcessor;

    @Getter
    private TotalsProcessor<T> totalsProcessor;

    @Getter
    private CountProcessor<T> countProcessor;

    @Getter
    private SortProcessor<T> sortProcessor;

    @Getter
    private IPagerProcessor<T> pagerProcessor;

    @Getter
    @Setter
    private LinkedHashMap<String, List<String>> query;

    @Getter
    private IGridSettingsProvider settings;

    public void setSettings(IGridSettingsProvider value) {
        this.settings = value;
        this.sortProcessor.updateSettings(this.settings.getSortSettings());
        this.filterProcessor.updateSettings(this.settings.getFilterSettings());
        this.searchProcessor.updateSettings(this.settings.getSearchSettings());
    }

    @Getter
    private GridColumnCollection<T> columns;

    @Getter
    private PagingType pagingType = PagingType.NONE;

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

    @Setter
    private IGridPager<T> pager;

    public IGridPager<T> getPager() {
        if (pager == null)
            pager = new GridPager(this);
        return pager;
    }

    @Getter
    @Setter
    private SearchOptions searchOptions = new SearchOptions(false);

    @Getter
    @Setter
    private String removeDiacritics = null;

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

    public long getItemsCount() {
        //call preprocessors before:
        preProcess();
        return this.itemsCount;
    }

    public void setItemsCount(int value) {
        this.itemsCount = value; //value can be set by pager (for minimizing db calls)
    }

    public long getDisplayingItemsCount() {
        if (this.displayingItemsCount >= 0)
            return this.displayingItemsCount;
        this.displayingItemsCount = getItemsToDisplay().size();
        return this.displayingItemsCount;
    }

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
            this.itemsToList = this.pagerProcessor.process(this.criteriaQuery).getResultList();
        }
    }

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

    public boolean isDefaultSortEnabled() {
        return this.columnBuilder.isDefaultSortEnabled();
    }

    public void setDefaultSortEnabled(boolean value) {
        this.columnBuilder.setDefaultSortEnabled(value);
    }

    public GridSortMode getGridSortMode() {
        return this.columnBuilder.getDefaultGridSortMode();
    }

    public void setGridSortMode(GridSortMode value) {
        this.columnBuilder.setDefaultGridSortMode(value);
    }

    public boolean isDefaultFilteringEnabled() {
        return this.columnBuilder.isDefaultFilteringEnabled();
    }

    public void setDefaultFilteringEnabled(boolean value) {
        this.columnBuilder.setDefaultFilteringEnabled(value);
    }

    public boolean isSumEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isSumEnabled);
    }

    public boolean isAverageEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isAverageEnabled);
    }

    public boolean isMaxEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isMaxEnabled);
    }

    public boolean isMinEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isMinEnabled);
    }

    public boolean isCalculationEnabled() {
        return this.columns.values().stream().anyMatch(ITotalsColumn::isCalculationEnabled);
    }
}
