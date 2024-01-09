package me.agno.gridcore;

import me.agno.gridcore.pagination.IGridPager;
import me.agno.gridcore.sorting.GridSortMode;
import me.agno.gridcore.totals.TotalsDTO;
import org.jinq.orm.stream.JinqStream;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public interface ISGrid<T> extends IGrid<T>, IGridOptions {

        IGridColumnCollection<T> getColumns();

        IGridItemsProcessor<T> getPagerProcessor();

        IGridItemsProcessor<T> getSearchProcessor();

        IGridItemsProcessor<T> getFilterProcessor();

        IGridItemsProcessor<T> getSortProcessor();

        IGridItemsProcessor<T> getTotalsProcessor();

        Collection<T> getItemsToDisplay();

        Collection<T> getItemsToDisplayAsync(Function<JinqStream<T>, List<T>> toList);

        void setToListAsyncFunc(Function<JinqStream<T>, List<T>> toList);

        IGridPager<T> getPager();

        void setPager(IGridPager<T> pager);

        IGridSettingsProvider getSettings();

        TotalsDTO GetTotals();

        boolean isDefaultSortEnabled();

        void setDefaultSortEnabled(boolean defaultFilteringEnabled);

        GridSortMode getGridSortMode();

        void setGridSortMode(GridSortMode gridSortMode);

        boolean isDefaultFilteringEnabled();

        void setDefaultFilteringEnabled(boolean defaultFilteringEnabled);

        void AutoGenerateColumns();

        Collection<T> GetItemsToDisplay();

        int getDisplayingItemsCount();
}