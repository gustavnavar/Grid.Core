package me.agno.gridcore.sorting;

import org.jinq.orm.stream.JinqStream;

import java.util.function.Function;

public class ThenByColumnOrderer <T, TKey extends Comparable<TKey>> implements IColumnOrderer<T> {
    private final Function<T, TKey> _expression;
    private final GridSortDirection _initialDirection;

    public ThenByColumnOrderer(Function<T, TKey> expression, GridSortDirection initialDirection) {
        _expression = expression;
        _initialDirection = initialDirection;
    }

    private JinqStream<T> Apply(JinqStream<T> items) {

        if (items == null) return items; //not ordered collection

        switch (_initialDirection) {
            case Ascending:
                return items.sortedBy(_expression::apply);
            case Descending:
                return items.sortedDescendingBy(_expression::apply);
            default:
                throw new IllegalArgumentException();
            }
    }

    public JinqStream<T> ApplyOrder(JinqStream<T> items, GridSortDirection direction) {
        return Apply(items);
    }

    public JinqStream<T> ApplyThenBy(JinqStream<T> items, GridSortDirection direction) {
        return Apply(items);
    }
}