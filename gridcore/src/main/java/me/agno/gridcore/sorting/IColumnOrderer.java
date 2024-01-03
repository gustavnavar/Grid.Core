package me.agno.gridcore.sorting;

import org.jinq.orm.stream.JinqStream;

public interface IColumnOrderer<T> {
    JinqStream<T> ApplyOrder(JinqStream<T> items, GridSortDirection direction);
}
