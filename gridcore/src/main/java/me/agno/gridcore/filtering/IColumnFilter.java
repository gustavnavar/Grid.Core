package me.agno.gridcore.filtering;

import org.jinq.orm.stream.JinqStream;

import java.util.Collection;

public interface IColumnFilter<T> {

    boolean isNullable();

    JinqStream<T> ApplyFilter(JinqStream<T> items, Collection<ColumnFilterValue> values, JinqStream<T> source);

    JinqStream<T> ApplyFilter(JinqStream<T> items, Collection<ColumnFilterValue> values, JinqStream<T> source, String removeDiacritics);
}
