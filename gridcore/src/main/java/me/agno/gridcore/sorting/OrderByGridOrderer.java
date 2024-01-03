package me.agno.gridcore.sorting;

import org.jinq.orm.stream.JinqStream;

import java.util.function.Function;

public class OrderByGridOrderer<T, TKey extends Comparable<TKey>> implements IColumnOrderer<T> {
    private final Function<T, TKey> _expression;

    public OrderByGridOrderer(Function<T, TKey> expression) {
        _expression = expression;
    }

    public JinqStream<T> ApplyOrder(JinqStream<T> items, GridSortDirection direction)
    {
        switch (direction)
        {
            case Ascending:
                return items.sortedBy(_expression::apply);
            case Descending:
                return items.sortedDescendingBy(_expression::apply);
            default:
                throw new IllegalArgumentException("direction");
        }
    }
}
