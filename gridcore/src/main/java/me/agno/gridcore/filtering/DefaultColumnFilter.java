package me.agno.gridcore.filtering;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridcore.filtering.types.FilterTypeResolver;
import me.agno.gridcore.filtering.types.IFilterType;

import java.util.List;

public class DefaultColumnFilter<T, TData> implements IColumnFilter<T> {

    private final String expression;
    private final Class<TData> targetType;
    private final FilterTypeResolver typeResolver = new FilterTypeResolver();

    public DefaultColumnFilter(String expression, Class<TData> targetType) {
        this.expression = expression;
        this.targetType = targetType;
    }

    public Predicate applyFilter(CriteriaBuilder cb, Root<T> root, List<ColumnFilterValue> values) {
        return applyFilter(cb, root, values, null);
    }

    public Predicate applyFilter(CriteriaBuilder cb, Root<T> root, List<ColumnFilterValue> values,
                                 String removeDiacritics) {
        if (values == null && values.stream().noneMatch(ColumnFilterValue::isNotNull))
            throw new IllegalArgumentException ("values");

        GridFilterCondition condition;
        var cond = values.stream().filter(r -> r.isNotNull() && r.getFilterType() == GridFilterType.CONDITION).findAny();
        if (cond.isPresent()) {
            condition = GridFilterCondition.fromString(cond.get().getFilterValue());
            if(condition == null || condition.equals(GridFilterCondition.NONE))
                condition = GridFilterCondition.AND;
        }
        else {
            condition = GridFilterCondition.AND;
        }

        var filterValues = values.stream().filter(r -> r.isNotNull() && r.getFilterType() != GridFilterType.CONDITION).toList();

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
                else if (condition.equals(GridFilterCondition.OR)) {
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
        IFilterType<T, TData> filterType = this.typeResolver.GetFilterType(this.targetType);
        return filterType.getFilterExpression(cb, root, this.expression, value.getFilterValue(),
                value.getFilterType(), removeDiacritics);
    }
}