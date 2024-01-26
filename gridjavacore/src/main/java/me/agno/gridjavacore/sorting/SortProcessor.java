package me.agno.gridjavacore.sorting;

import jakarta.persistence.criteria.Order;
import lombok.Setter;
import me.agno.gridjavacore.IGrid;

import java.util.ArrayList;
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

        if (orderList == null)
            orderList = new ArrayList<>();

        var sortValues = this.settings.getSortValues();
        if (sortValues != null && !sortValues.isEmpty()) {

            var sortedColumns = sortValues.stream().sorted(new Comparator<ColumnOrderValue>() {
                @Override
                public int compare(ColumnOrderValue c1, ColumnOrderValue c2) {
                    return Integer.compare(c1.getId(), c2.getId());
                }
            }).toList();

            for(var sortedColumn : sortedColumns) {

                var column = this.grid.getColumns().values().stream()
                        .filter(r -> Objects.equals(r.getName().toUpperCase(),
                                sortedColumn.getColumnName().toUpperCase())).findFirst();
                if(column.isEmpty() || column.get().getOrderers().isEmpty())
                    continue;

                var order = column.get().getOrderers().get(0).applyOrder(this.grid.getCriteriaBuilder(),
                        this.grid.getRoot(), sortedColumn.getDirection());
                if (order != null && orderList.stream()
                        .noneMatch(r -> r.getExpression().equals(order.getExpression()))) {
                    orderList.add(order);
                }

                for (var orderer : column.get().getOrderers()) {
                   var order2 = orderer.applyOrder(this.grid.getCriteriaBuilder(), this.grid.getRoot(),
                            GridSortDirection.ASCENDING);
                    if (order2 != null && orderList.stream()
                            .noneMatch(r -> r.getExpression().equals(order2.getExpression()))) {
                        orderList.add(order2);
                    }
                }
            }
        }

        if (this.settings.getColumnName() == null || this.settings.getColumnName().isEmpty())
            return orderList;

        //determine gridColumn sortable:
        var column = this.grid.getColumns().values().stream()
                .filter(c -> Objects.equals(c.getName().toUpperCase(),
                        this.settings.getColumnName().toUpperCase())).findFirst();
        if (column.isEmpty() || !column.get().isSortEnabled())
            return orderList;

        for (var colOrderer : column.get().getOrderers()) {
            var order = colOrderer.applyOrder(this.grid.getCriteriaBuilder(), this.grid.getRoot(),
                    this.settings.getDirection());
            if (order != null && orderList.stream()
                    .noneMatch(r -> r.getExpression().equals(order.getExpression()))) {
                orderList.add(order);
            }
        }
        return orderList;
    }
}