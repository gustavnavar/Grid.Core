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

    private final IGridAnnotationsProvider<T> _annotations;
    private final IColumnBuilder<T> _columnBuilder;

    private long _itemsCount = -1; // total items count on collection
    private long _displayingItemsCount = -1; // count of displaying items (if using pagination)
    private List<T> _itemsToList; //items after processors
    private boolean _itemsPreProcessed; //is preprocessors launched?
    private boolean _itemsProcessed; //is processors launched?


    @Getter
    public EntityManager EntityManager;

    @Getter
    public CriteriaBuilder CriteriaBuilder;

    @Getter
    public CriteriaQuery<T> CriteriaQuery;

    @Getter
    public Root<T> Root;

    @Getter
    public Class<T> TargetType;

    private Predicate Predicate;

    public Predicate getPredicate() {
        //call preprocessors before:
        preProcess();
        return Predicate;
    }

    @Getter
    private List<Order> OrderList;

    @Getter
    private FilterProcessor<T> FilterProcessor;

    @Getter
    private SearchProcessor<T> SearchProcessor;

    @Getter
    private TotalsProcessor<T> TotalsProcessor;

    @Getter
    private CountProcessor<T> CountProcessor;

    @Getter
    private SortProcessor<T> SortProcessor;

    @Getter
    private IPagerProcessor<T> PagerProcessor;

    @Getter
    @Setter
    private LinkedHashMap<String, List<String>> Query;

    @Getter
    private IGridSettingsProvider Settings;

    public void setSettings(IGridSettingsProvider value) {
        Settings = value;
        SortProcessor.UpdateSettings(Settings.getSortSettings());
        FilterProcessor.UpdateSettings(Settings.getFilterSettings());
        SearchProcessor.UpdateSettings(Settings.getSearchSettings());
    }

    @Getter
    private GridColumnCollection<T> Columns;

    @Getter
    private PagingType PagingType = me.agno.gridcore.pagination.PagingType.None;

    public void setPagingType(PagingType value) {
        if (PagingType == value)
            return;
        else
            PagingType = value;

        if (PagingType != me.agno.gridcore.pagination.PagingType.None) {
            if (PagerProcessor == null)
                PagerProcessor = new PagerProcessor<T>(this);
        }
        else {
            PagerProcessor = null;
        }
    }
    @Getter
    @Setter
    private IGridPager<T> Pager = new GridPager<T>(this);

    @Getter
    @Setter
    private SearchOptions SearchOptions = new SearchOptions(false);

    @Getter
    @Setter
    private boolean ExtSortingEnabled;

    @Getter
    @Setter
    private boolean HiddenExtSortingHeader = false;

    @Getter
    @Setter
    private boolean GroupingEnabled;

    @Getter
    @Setter
    private String RemoveDiacritics = null;

    public Grid(EntityManager entityManager, Class<T> targetType, Predicate predicate, List<Order> orderList,
                LinkedHashMap<String, List<String>> query, IColumnBuilder<T> columnBuilder) {

        EntityManager = entityManager;
        TargetType = targetType;
        CriteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery = CriteriaBuilder.createQuery(TargetType);
        Root = CriteriaQuery.from(TargetType);

        Predicate = predicate;
        OrderList = orderList;

        Query = query;

        //set up sort settings:
        Settings = new QueryStringGridSettingsProvider(query);

        SortProcessor = new SortProcessor<T>(this, Settings.getSortSettings());
        FilterProcessor = new FilterProcessor<T>(this, Settings.getFilterSettings());
        SearchProcessor = new SearchProcessor<T>(this, Settings.getSearchSettings());
        TotalsProcessor = new TotalsProcessor<T>(this);

        _annotations = new GridCoreAnnotationsProvider<T>();

        //Set up column collection:
        if (columnBuilder == null)
            _columnBuilder = new DefaultColumnBuilder<T>(this, _annotations);
        else
            _columnBuilder = columnBuilder;
        Columns = new GridColumnCollection<T>(this, _columnBuilder, Settings.getSortSettings());

        applyGridSettings();

        int page = 0;
        int startIndex = 0;
        int virtualizedCount = 0;
        boolean noTotals = false;

        var startIndexParameter = query.get(GridPager.DefaultStartIndexQueryParameter);
        var virtualizedCountParameter = query.get(GridPager.DefaultVirtualizedCountQueryParameter);
        var noTotalsParameter = query.get(GridPager.DefaultNoTotalsParameter);
        if (startIndexParameter != null && ! startIndexParameter.isEmpty() &&
                startIndexParameter.get(0) != null && ! startIndexParameter.get(0).trim().isEmpty() &&
                virtualizedCountParameter != null && ! virtualizedCountParameter.isEmpty() &&
                virtualizedCountParameter.get(0) != null && ! virtualizedCountParameter.get(0).trim().isEmpty() &&
                noTotalsParameter != null && ! noTotalsParameter.isEmpty() &&
                noTotalsParameter.get(0) != null && ! noTotalsParameter.get(0).trim().isEmpty()) {

            PagingType = me.agno.gridcore.pagination.PagingType.Virtualization;
            try {
                startIndex = Integer.parseInt(startIndexParameter.get(0).trim());
            }
            catch (Exception ignored) {}
            ((GridPager<T>)Pager).setStartIndex(startIndex);
            try {
                virtualizedCount = Integer.parseInt(virtualizedCountParameter.get(0).trim());
            }
            catch (Exception ignored) {}
            ((GridPager<T>)Pager).setVirtualizedCount(virtualizedCount);
            try {
                noTotals = Boolean.parseBoolean(noTotalsParameter.get(0).trim());
            }
            catch (Exception ignored) {}
            ((GridPager<T>)Pager).setNoTotals(noTotals);
        }
        else {
            var pageParameter = query.get(((GridPager<T>)Pager).getParameterName());
            if (pageParameter != null && ! pageParameter.isEmpty() &&
                    pageParameter.get(0) != null && ! pageParameter.get(0).trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageParameter.get(0).trim());
                } catch (Exception ignored) {}
            }
            if (page == 0)
                page++;
            ((GridPager<T>)Pager).setCurrentPage(page);
        }
    }

    private void preProcess() {
        if (!_itemsPreProcessed) {
            _itemsPreProcessed = true;
            Predicate = FilterProcessor.Process(Predicate);
            Predicate = SearchProcessor.Process(Predicate);

            // added to avoid 2nd EF opened task if counting later
            _itemsCount = CountProcessor.Process(Predicate);

            // calculate totals
            TotalsProcessor.Process(Predicate);
        }
    }

    public long getItemsCount() {
        //call preprocessors before:
        preProcess();
        return _itemsCount;
    }

    public void setItemsCount(int value) {
        _itemsCount = value; //value can be set by pager (for minimizing db calls)
    }

    public long getDisplayingItemsCount() {
        if (_displayingItemsCount >= 0)
            return _displayingItemsCount;
        _displayingItemsCount = getItemsToDisplay().size();
        return _displayingItemsCount;
    }

    public List<T> getItemsToDisplay() {
        prepareItemsToDisplay();
        return _itemsToList;
    }

    protected void prepareItemsToDisplay() {
        if (!_itemsProcessed) {
            _itemsProcessed = true;
            CriteriaQuery.select(Root);
            CriteriaQuery.where(getPredicate());
            OrderList = SortProcessor.Process(OrderList);
            CriteriaQuery.orderBy(OrderList);
            _itemsToList = PagerProcessor.Process(CriteriaQuery).getResultList();
        }
    }

    void applyGridSettings() {
        GridTable opt = _annotations.getAnnotationForTable(getTargetType());
        if (opt == null) return;
        PagingType = opt.getPagingType();

        if (PagingType == me.agno.gridcore.pagination.PagingType.Pagination)
        {
            if (opt.getPageSize() > 0)
                Pager.setPageSize(opt.getPageSize());

            if (opt.getPagingMaxDisplayedPages() > 0 && Pager instanceof GridPager)
                ((GridPager<T>)Pager).setMaxDisplayedPages(opt.getPagingMaxDisplayedPages());
        }
    }

    public void autoGenerateColumns() {

        var tuples = _annotations.getAnnotationsForTableColumns(TargetType);

        for (var tuple : tuples) {
            String name = tuple.getFirst();
            Boolean isKey = tuple.getSecond();
            GridColumn annotations = tuple.getThird();

            if(isKey)
                Columns.add(name, annotations.getType()).setPrimaryKey(true);
            else
                Columns.add(name, annotations.getType());
        }
    }

    public TotalsDTO getTotals()
    {
        var totals = new TotalsDTO();
        if (isSumEnabled())
            for (IGridColumn<T> column : Columns.values()) {
                if (column.isSumEnabled())
                    totals.getSum().put(column.getName(), column.getSumValue());
            }

        if (isAverageEnabled())
            for (IGridColumn<T> column : Columns.values()) {
                if (column.isAverageEnabled())
                    totals.getAverage().put(column.getName(), column.getAverageValue());
            }

        if (isMaxEnabled())
            for (IGridColumn<T> column : Columns.values()) {
                if (column.isMaxEnabled())
                    totals.getMax().put(column.getName(), column.getMaxValue());
        }

        if (isMinEnabled())
            for (IGridColumn<T> column : Columns.values()) {
                if (column.isMinEnabled())
                    totals.getMin().put(column.getName(), column.getMinValue());
        }

        if (isCalculationEnabled())
            for (IGridColumn<T> column : Columns.values()) {
                if (column.isCalculationEnabled())
                    totals.getCalculations().put(column.getName(), column.getCalculationValues());
        }

        return totals;
    }

    public boolean isDefaultSortEnabled() {
        return _columnBuilder.isDefaultSortEnabled();
    }

    public void setDefaultSortEnabled(boolean value) {
        _columnBuilder.setDefaultSortEnabled(value);
    }

    public GridSortMode getGridSortMode() {
        return _columnBuilder.getDefaultGridSortMode();
    }

    public void setGridSortMode(GridSortMode value) {
        _columnBuilder.setDefaultGridSortMode(value);
    }

    public boolean isDefaultFilteringEnabled() {
        return _columnBuilder.isDefaultFilteringEnabled();
    }

    public void setDefaultFilteringEnabled(boolean value) {
        _columnBuilder.setDefaultFilteringEnabled(value);
    }

    public boolean isSumEnabled() {
        return Columns.values().stream().anyMatch(ITotalsColumn::isSumEnabled);
    }

    public boolean isAverageEnabled() {
        return Columns.values().stream().anyMatch(ITotalsColumn::isAverageEnabled);
    }

    public boolean isMaxEnabled() {
        return Columns.values().stream().anyMatch(ITotalsColumn::isMaxEnabled);
    }

    public boolean isMinEnabled() {
        return Columns.values().stream().anyMatch(ITotalsColumn::isMinEnabled);
    }

    public boolean isCalculationEnabled() {
        return Columns.values().stream().anyMatch(ITotalsColumn::isCalculationEnabled);
    }
}
