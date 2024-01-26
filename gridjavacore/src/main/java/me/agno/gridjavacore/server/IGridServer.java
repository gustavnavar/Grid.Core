package me.agno.gridjavacore.server;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import me.agno.gridjavacore.IGrid;
import me.agno.gridjavacore.IGridColumnCollection;
import me.agno.gridjavacore.SearchOptions;
import me.agno.gridjavacore.sorting.GridSortMode;
import me.agno.gridjavacore.utils.ItemsDTO;

import java.util.List;
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

    IGridServer<T> autoGenerateColumns();

    IGridServer<T> setRemoveDiacritics(String methodName);

    IGridServer<T> setPredicate(Predicate predicate);

    IGridServer<T> setOrder(List<Order> orderList);

    ItemsDTO<T> getItemsToDisplay();

    IGrid<T> getGrid();
}
