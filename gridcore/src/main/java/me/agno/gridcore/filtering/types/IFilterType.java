package me.agno.gridcore.filtering.types;

import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.orm.stream.JinqStream;

import java.util.function.Function;
import java.util.function.Predicate;

public interface IFilterType<T, TData> {

    Class getTargetType();

    GridFilterType GetValidType(GridFilterType type);

    TData GetTypedValue(String value);

    Predicate<T> GetFilterExpression(Function<T, TData> leftExpr, String value, GridFilterType filterType, JinqStream<T> source);

    Predicate<T> GetFilterExpression(Function<T, TData>leftExpr, String value, GridFilterType filterType, JinqStream<T> source,
                                     String removeDiacritics);
}
