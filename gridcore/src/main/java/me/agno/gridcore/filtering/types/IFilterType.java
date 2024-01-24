package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridcore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

public interface IFilterType<T, TData> {

    Class<TData> getTargetType();

    GridFilterType getValidType(GridFilterType type);

    TData getTypedValue(String value);

    Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, SqmQuerySpec source,
                                  String expression, String value, GridFilterType filterType);

    Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, SqmQuerySpec source,
                                  String expression, String value, GridFilterType filterType, String removeDiacritics);
}
