package me.agno.gridjavacore.filtering;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridjavacore.columns.GridCoreColumn;
import me.agno.gridjavacore.filtering.types.FilterTypeResolver;
import me.agno.gridjavacore.filtering.types.IFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.util.List;

/**
 * DefaultColumnFilter is a class that implements the IColumnFilter interface. It provides methods to apply filters on column values based on the given filter conditions.
 */
public class DefaultColumnFilter<T, TData> implements IColumnFilter<T> {

    private final GridCoreColumn<T, TData> column;
    private final FilterTypeResolver typeResolver = new FilterTypeResolver();

    /**
     * Creates a DefaultColumnFilter object with the given expression and targetType.
     *
     * @param column the column
     */
    public DefaultColumnFilter(GridCoreColumn<T, TData> column) {
        this.column = column;
    }

    /**
     * Applies a filter to the given criteria query based on the provided criteria builder,
     * criteria query, root, query specification and column filter values.
     *
     * @param cb             the criteria builder
     * @param cq             the criteria query
     * @param root           the root entity
     * @param source         the query specification
     * @param values         the column filter values
     * @return the predicate representing the applied filter
     * @throws IllegalArgumentException if the column filter values are null or all values are null
     */
    public Predicate applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                 SqmQuerySpec source, List<ColumnFilterValue> values) {
        return applyFilter(cb, cq, root, source, values,null);
    }

    /**
     * Applies a filter to the given criteria query based on the provided criteria builder, criteria query,
     * root, query specification, column filter values, and optional diacritics removal.
     *
     * @param cb                  the criteria builder
     * @param cq                  the criteria query
     * @param root                the root entity
     * @param source              the query specification
     * @param values              the column filter values
     * @param removeDiacritics    the optional diacritics removal parameter
     * @return the predicate representing the applied filter
     * @throws IllegalArgumentException if the column filter values are null or all values are null
     */
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

        return getFilterExpression(cb, cq, root, source, filterValues, condition, removeDiacritics);
    }

    private Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                          SqmQuerySpec source, List<ColumnFilterValue> values, GridFilterCondition condition,
                                          String removeDiacritics) {

        Predicate mainPredicate = null;

        for (var value : values) {

            if (value.isNull())
                continue;

            Predicate predicate  = getExpression(cb, cq, root, source, value, removeDiacritics);
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

    private Predicate getExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                    SqmQuerySpec source, ColumnFilterValue value, String removeDiacritics)
    {
        IFilterType<T, TData> filterType = this.typeResolver.getFilterType(this.column.getTargetType());
        return filterType.getFilterExpression(cb, cq, root, source, this.column, value.getFilterValue(),
                value.getFilterType(), removeDiacritics);
    }
}