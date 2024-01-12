package me.agno.gridcore.sorting;

import jakarta.persistence.criteria.Order;
import lombok.Setter;
import me.agno.gridcore.IGrid;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SortProcessor<T>
{
    private final IGrid<T> grid;
    private IGridSortSettings settings;
    @Setter
    private Function<List<Order>, List<Order>> process;

    public SortProcessor(IGrid<T> grid, IGridSortSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");

        this.grid = grid;
        this.settings = settings;
    }

    public void updateSettings(IGridSortSettings settings) {
        if (settings == null)
            throw new IllegalArgumentException("settings");
        this.settings = settings;
    }

    public List<Order> process(List<Order> orderList) {

        if (this.process != null)
            return this.process.apply(orderList);

        var sortValues = this.settings.getSortValues();
        if (sortValues != null && !sortValues.isEmpty()) {

            var sortedColumns = sortValues.stream().sorted(new Comparator<ColumnOrderValue>() {
                @Override
                public int compare(ColumnOrderValue c1, ColumnOrderValue c2) {
                    return Integer.compare(c1.getId(), c2.getId());
                }
            }).toList();

            var firstSortedColumn = sortedColumns.get(0);
            var gridColumn = this.grid.getColumns().values().stream()
                    .filter(c -> Objects.equals(c.getName(), firstSortedColumn.getColumnName()))
                    .findFirst();
            if(gridColumn.isEmpty())
                return orderList;

            var columnOrderer = gridColumn.get().getOrderers().get(0);
            var order = columnOrderer.applyOrder(this.grid.getCriteriaBuilder(), this.grid.getRoot(),
                    firstSortedColumn.getDirection());
            if (order != null)
                orderList.add(order);

            for (int i = 1; i < gridColumn.get().getOrderers().size(); i++) {
                var orderer = gridColumn.get().getOrderers().get(i);
                order = orderer.applyOrder(this.grid.getCriteriaBuilder(), this.grid.getRoot(), GridSortDirection.ASCENDING);
                if (order != null)
                    orderList.add(order);
            }

            if(sortedColumns.size() > 1) {
                for(int i = 1; i < sortedColumns.size(); i++) {

                    int finalI = i;
                    var gridCol = this.grid.getColumns().values().stream()
                            .filter(r -> Objects.equals(r.getName(), sortedColumns.get(finalI).getColumnName())).findFirst();
                    if(gridCol.isEmpty() || gridCol.get().getOrderers().isEmpty())
                        continue;

                    order = gridCol.get().getOrderers().get(0).applyOrder(this.grid.getCriteriaBuilder(),
                            this.grid.getRoot(), sortedColumns.get(finalI).getDirection());
                    if (order != null)
                        orderList.add(order);

                    for (int j = 1; j < gridCol.get().getOrderers().size(); j++) {
                        var orderer = gridCol.get().getOrderers().get(j);
                        order = orderer.applyOrder(this.grid.getCriteriaBuilder(), this.grid.getRoot(),
                                GridSortDirection.ASCENDING);
                        if (order != null)
                            orderList.add(order);
                    }
                }
            }

            if (this.settings.getColumnName() == null || this.settings.getColumnName().isEmpty())
                return orderList;

            //determine gridColumn sortable:
            gridColumn = this.grid.getColumns().values().stream()
                    .filter(c -> Objects.equals(c.getName(), this.settings.getColumnName())).findFirst();
            if (gridColumn.isEmpty() || !gridColumn.get().isSortEnabled())
                return orderList;

            for (var colOrderer : gridColumn.get().getOrderers()) {
                order = colOrderer.applyOrder(this.grid.getCriteriaBuilder(), this.grid.getRoot(),
                        this.settings.getDirection());
                if (order != null)
                    orderList.add(order);
            }
        }
        else {
            if (this.settings.getColumnName() == null || this.settings.getColumnName().isEmpty())
                return orderList;

            //determine gridColumn sortable:
            var gridColumn = this.grid.getColumns().values().stream()
                    .filter(c -> Objects.equals(c.getName(), this.settings.getColumnName())).findFirst();
            if (gridColumn.isEmpty() || !gridColumn.get().isSortEnabled())
                return orderList;

            for (var columnOrderer : gridColumn.get().getOrderers()) {
                var order = columnOrderer.applyOrder(this.grid.getCriteriaBuilder(), this.grid.getRoot(),
                        this.settings.getDirection());
                if (order != null)
                    orderList.add(order);
            }
        }
        return orderList;
    }
}