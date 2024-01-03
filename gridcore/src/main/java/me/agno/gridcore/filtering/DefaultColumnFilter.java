package me.agno.gridcore.filtering;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.agno.gridcore.filtering.types.FilterTypeResolver;
import me.agno.gridcore.filtering.types.IFilterType;
import org.jinq.orm.stream.JinqStream;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

public class DefaultColumnFilter<T, TData> implements IColumnFilter<T> {

    private final Function<T, TData> _expression;
    private final Class _targetType;
    private final FilterTypeResolver _typeResolver = new FilterTypeResolver();

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public boolean Nullable;

    public DefaultColumnFilter(Function<T, TData> expression, Class targetType, boolean nullable) {
        _expression = expression;
        _targetType = targetType;
        Nullable = nullable;
    }

    public JinqStream<T> ApplyFilter(JinqStream<T> items, Collection<ColumnFilterValue> values, JinqStream<T> source) {
        return ApplyFilter(items, values, source, null);
    }

    public JinqStream<T> ApplyFilter(JinqStream<T> items, Collection<ColumnFilterValue> values, JinqStream<T> source,
                                     String removeDiacritics) {
        if (values == null || values.stream().noneMatch(ColumnFilterValue::isNotNull))
            throw new IllegalArgumentException ("values");

        GridFilterCondition condition;
        var cond = values.stream().filter(r -> r.getFilterType() == GridFilterType.Condition).findAny();
        if (cond.isPresent()) {
            condition = GridFilterCondition.fromString(cond.get().getFilterValue());
            if(condition == null || condition.equals(GridFilterCondition.None))
                condition = GridFilterCondition.And;
        }
        else {
            condition = GridFilterCondition.And;
        }

        var filterValues = values.stream().filter(r -> r.isNotNull() && r.getFilterType() != GridFilterType.Condition).toList();

        Predicate<T> expr = GetFilterExpression(filterValues, condition, source, removeDiacritics);
        if (expr == null)
            return items;
        return items.where(expr::test);
    }

    private Predicate<T> GetFilterExpression(Collection<ColumnFilterValue> values,
                                             GridFilterCondition condition, JinqStream<T> source, String removeDiacritics) {
        Predicate<T> mainPredicate = null;
        for (var value : values) {
            if (! value.isNotNull())
                continue;

            Predicate<T> predicate = GetExpression(value, source, removeDiacritics);
            if (predicate != null) {
                if (mainPredicate == null)
                    mainPredicate = predicate;
                else if (condition.equals(GridFilterCondition.Or)) {
                    mainPredicate = mainPredicate.or(predicate);
                }
                else {
                    mainPredicate = mainPredicate.and(predicate);
                }
            }
        }

        //return filter expression
        return mainPredicate;
    }

    private Predicate<T> GetExpression(ColumnFilterValue value, JinqStream<T> source, String removeDiacritics)
    {
        // return expression for IsNull and IsNotNull
        if (value.getFilterType() == GridFilterType.IsNull) {
            if (_targetType.equals(String.class))
                return _expression == null ? null : c -> {
                    String  str = _expression.apply(c).toString();
                    return str == null || str.trim().isEmpty();
                };
            else
                return _expression == null ? null : c -> _expression.apply(c) == null;
        }
        else if (value.getFilterType() == GridFilterType.IsNotNull) {
            value.setFilterValue("");
            if (_targetType.equals(String.class))
                return _expression == null ? null : c -> {
                    String  str = _expression.apply(c).toString();
                    return str != null && ! str.trim().isEmpty();
                };
            else
                return _expression == null ? null : c -> _expression.apply(c) != null;
        }
        else
        {
            IFilterType<T, TData> filterType = _typeResolver.GetFilterType(_targetType);
            return filterType.GetFilterExpression(_expression, value.getFilterValue(), value.getFilterType(), source, removeDiacritics);
        }
    }
}