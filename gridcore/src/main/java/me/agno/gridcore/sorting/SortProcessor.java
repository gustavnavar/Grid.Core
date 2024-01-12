package me.agno.gridcore.sorting;

import jakarta.persistence.criteria.Order;
import me.agno.gridcore.IGrid;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SortProcessor<T>
{
    private final IGrid<T> _grid;
    private IGridSortSettings _settings;
    private Function<List<Order>, List<Order>> _process;

    public SortProcessor(IGrid<T> grid, IGridSortSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");

        _grid = grid;
        _settings = settings;
    }

    public void UpdateSettings(IGridSortSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        _settings = settings;
    }

    public List<Order> Process(List<Order> orderList) {

        if (_process != null)
            return _process.apply(orderList);

        var sortValues = _settings.getSortValues();
        if (sortValues != null && !sortValues.isEmpty()) {

            var sortedColumns = sortValues.stream().sorted(new Comparator<ColumnOrderValue>() {
                @Override
                public int compare(ColumnOrderValue c1, ColumnOrderValue c2) {
                    return Integer.compare(c1.getId(), c2.getId());
                }
            }).toList();

            var firstSortedColumn = sortedColumns.get(0);
            var gridColumn = _grid.getColumns().values().stream()
                    .filter(c -> Objects.equals(c.getName(), firstSortedColumn.getColumnName()))
                    .findFirst();
            if(gridColumn.isEmpty())
                return orderList;

            var columnOrderer = gridColumn.get().getOrderers().get(0);
            var order = columnOrderer.ApplyOrder(_grid.getCriteriaBuilder(), _grid.getRoot(), firstSortedColumn.getDirection());
            if (order != null)
                orderList.add(order);

            for (int i = 1; i < gridColumn.get().getOrderers().size(); i++) {
                var orderer = gridColumn.get().getOrderers().get(i);
                order = orderer.ApplyOrder(_grid.getCriteriaBuilder(), _grid.getRoot(), GridSortDirection.Ascending);
                if (order != null)
                    orderList.add(order);
            }

            if(sortedColumns.size() > 1) {
                for(int i = 1; i < sortedColumns.size(); i++) {

                    int finalI = i;
                    var gridCol = _grid.getColumns().values().stream()
                            .filter(r -> Objects.equals(r.getName(), sortedColumns.get(finalI).getColumnName())).findFirst();
                    if(gridCol.isEmpty() || gridCol.get().getOrderers().isEmpty())
                        continue;

                    order = gridCol.get().getOrderers().get(0).ApplyOrder(_grid.getCriteriaBuilder(),
                            _grid.getRoot(), sortedColumns.get(finalI).getDirection());
                    if (order != null)
                        orderList.add(order);

                    for (int j = 1; j < gridCol.get().getOrderers().size(); j++) {
                        var orderer = gridCol.get().getOrderers().get(j);
                        order = orderer.ApplyOrder(_grid.getCriteriaBuilder(), _grid.getRoot(),
                                GridSortDirection.Ascending);
                        if (order != null)
                            orderList.add(order);
                    }
                }
            }

            if (_settings.getColumnName() == null || _settings.getColumnName().isEmpty())
                return orderList;

            //determine gridColumn sortable:
            gridColumn = _grid.getColumns().values().stream()
                    .filter(c -> Objects.equals(c.getName(), _settings.getColumnName())).findFirst();
            if (gridColumn.isEmpty() || !gridColumn.get().isSortEnabled())
                return orderList;

            for (var colOrderer : gridColumn.get().getOrderers()) {
                order = colOrderer.ApplyOrder(_grid.getCriteriaBuilder(), _grid.getRoot(), _settings.getDirection());
                if (order != null)
                    orderList.add(order);
            }
        }
        else {
            if (_settings.getColumnName() == null || _settings.getColumnName().isEmpty())
                return orderList;

            //determine gridColumn sortable:
            var gridColumn = _grid.getColumns().values().stream()
                    .filter(c -> Objects.equals(c.getName(), _settings.getColumnName())).findFirst();
            if (gridColumn.isEmpty() || !gridColumn.get().isSortEnabled())
                return orderList;

            for (var columnOrderer : gridColumn.get().getOrderers()) {
                var order = columnOrderer.ApplyOrder(_grid.getCriteriaBuilder(), _grid.getRoot(),
                        _settings.getDirection());
                if (order != null)
                    orderList.add(order);
            }
        }
        return orderList;
    }

    public void SetProcess(Function<List<Order>, List<Order>> process)
    {
        _process = process;
    }
}