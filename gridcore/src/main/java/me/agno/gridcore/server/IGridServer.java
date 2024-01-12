package me.agno.gridcore.server;

import me.agno.gridcore.IGrid;
import me.agno.gridcore.IGridColumnCollection;
import me.agno.gridcore.SearchOptions;
import me.agno.gridcore.sorting.GridSortMode;
import me.agno.gridcore.utils.ItemsDTO;

import java.util.function.Consumer;

public interface IGridServer<T> {
    IGridServer<T> setColumns(Consumer<IGridColumnCollection<T>> columnBuilder);

    IGridServer<T> withPaging(int pageSize);

    IGridServer<T> withPaging(int pageSize, int maxDisplayedItems);

    IGridServer<T> withPaging(int pageSize, int maxDisplayedItems, String queryStringParameterName);

    IGridServer<T> sortable();

    IGridServer<T> sortable(boolean enable, GridSortMode gridSortMode);

    IGridServer<T> filterable();

    IGridServer<T> filterable(boolean enable);

    IGridServer<T> searchable();

    IGridServer<T> searchable(boolean enable);

    IGridServer<T> searchable(boolean enable, boolean onlyTextColumns);

    IGridServer<T> searchable(boolean enable, boolean onlyTextColumns, boolean hiddenColumns);

    IGridServer<T> searchable(Consumer<SearchOptions> searchOptions);

    IGridServer<T> extSortable();

    IGridServer<T> extSortable(boolean enable);

    IGridServer<T> extSortable(boolean enable, boolean hidden);

    IGridServer<T> groupable();

    IGridServer<T> groupable(boolean enable);

    IGridServer<T> groupable(boolean enable, boolean hidden);

    IGridServer<T> autoGenerateColumns();

    IGridServer<T> setRemoveDiacritics(String methodName);

    ItemsDTO<T> getItemsToDisplay();

    IGrid<T> getGrid();
}
