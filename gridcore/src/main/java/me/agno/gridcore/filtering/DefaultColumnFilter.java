package me.agno.gridcore.filtering;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridcore.filtering.types.FilterTypeResolver;
import me.agno.gridcore.filtering.types.IFilterType;

import java.util.List;

public class DefaultColumnFilter<T, TData> implements IColumnFilter<T> {

    private final String _expression;
    private final Class<TData> _targetType;
    private final FilterTypeResolver _typeResolver = new FilterTypeResolver();

    public DefaultColumnFilter(String expression, Class<TData> targetType) {
        _expression = expression;
        _targetType = targetType;
    }

    public Predicate ApplyFilter(CriteriaBuilder cb, Root<T> root, List<ColumnFilterValue> values) {
        return ApplyFilter(cb, root, values, null);
    }

    public Predicate ApplyFilter(CriteriaBuilder cb, Root<T> root, List<ColumnFilterValue> values,
                                     String removeDiacritics) {
        if (values == null || values.stream().noneMatch(ColumnFilterValue::isNotNull))
            throw new IllegalArgumentException ("values");

        GridFilterCondition condition;
        var cond = values.stream().filter(r -> r.isNotNull() && r.getFilterType() == GridFilterType.Condition).findAny();
        if (cond.isPresent()) {
            condition = GridFilterCondition.fromString(cond.get().getFilterValue());
            if(condition == null || condition.equals(GridFilterCondition.None))
                condition = GridFilterCondition.And;
        }
        else {
            condition = GridFilterCondition.And;
        }

        var filterValues = values.stream().filter(r -> r.isNotNull() && r.getFilterType() != GridFilterType.Condition).toList();

        return GetFilterExpression(cb, root, filterValues, condition, removeDiacritics);
    }

    private Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, List<ColumnFilterValue> values,
                                             GridFilterCondition condition, String removeDiacritics) {

        Predicate mainPredicate = null;

        for (var value : values) {

            if (value.isNull())
                continue;

            Predicate predicate  = GetExpression(cb, root, value, removeDiacritics);
            if (predicate != null) {
                if (mainPredicate == null)
                    mainPredicate = predicate;
                else if (condition.equals(GridFilterCondition.Or)) {
                    mainPredicate = cb.or(mainPredicate, predicate);
                }
                else {
                    mainPredicate = cb.and(mainPredicate, predicate);
                }
            }
        }

        //return filter expression
        return mainPredicate;
    }

    private Predicate GetExpression(CriteriaBuilder cb, Root<T> root, ColumnFilterValue value, String removeDiacritics)
    {
        IFilterType<T, TData> filterType = _typeResolver.GetFilterType(_targetType);
        return filterType.GetFilterExpression(cb, root, _expression, value.getFilterValue(), value.getFilterType(), removeDiacritics);
    }
}