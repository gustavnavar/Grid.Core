package me.agno.gridjavacore.filtering;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridjavacore.filtering.types.FilterTypeResolver;
import me.agno.gridjavacore.filtering.types.IFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.util.List;

public class DefaultColumnFilter<T, TData> implements IColumnFilter<T> {

    private final String expression;
    private final Class<TData> targetType;
    private final FilterTypeResolver typeResolver = new FilterTypeResolver();

    public DefaultColumnFilter(String expression, Class<TData> targetType) {
        this.expression = expression;
        this.targetType = targetType;
    }

    public Predicate applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                 SqmQuerySpec source, List<ColumnFilterValue> values) {
        return applyFilter(cb, cq, root, source, values,null);
    }

    public Predicate applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                 SqmQuerySpec source, List<ColumnFilterValue> values, String removeDiacritics) {
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

        return GetFilterExpression(cb, cq, root, source, filterValues, condition, removeDiacritics);
    }

    private Predicate GetFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                          SqmQuerySpec source, List<ColumnFilterValue> values, GridFilterCondition condition,
                                          String removeDiacritics) {

        Predicate mainPredicate = null;

        for (var value : values) {

            if (value.isNull())
                continue;

            Predicate predicate  = GetExpression(cb, cq, root, source, value, removeDiacritics);
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

    private Predicate GetExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                    SqmQuerySpec source, ColumnFilterValue value, String removeDiacritics)
    {
        IFilterType<T, TData> filterType = this.typeResolver.GetFilterType(this.targetType);
        return filterType.getFilterExpression(cb, cq, root, source, this.expression, value.getFilterValue(),
                value.getFilterType(), removeDiacritics);
    }
}