package me.agno.gridcore.filtering.types;

import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.orm.stream.JinqStream;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class FilterTypeBase<T, TData> implements IFilterType<T, TData> {

    public abstract GridFilterType GetValidType(GridFilterType type);

    public abstract TData GetTypedValue(String value);

    public Predicate<T> GetFilterExpression(Function<T, TData> leftExpr, String value, GridFilterType filterType,
                                                JinqStream<T> source) {
        return GetFilterExpression(leftExpr, value, filterType, source, null);
    }

    protected JinqStream<TData> GetGroupBy(JinqStream<T> source, Function<T, TData> expression) {

        if (expression == null)
            return null;

        return source.group(expression::apply, (tdata, stream) -> stream.count())
                .where(c -> c.getTwo() >  1)
                .select(c -> c.getOne());
    }
}