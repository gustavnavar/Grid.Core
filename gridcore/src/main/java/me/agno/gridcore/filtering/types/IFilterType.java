package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridcore.filtering.GridFilterType;

public interface IFilterType<T, TData> {

    Class getTargetType();

    GridFilterType getValidType(GridFilterType type);

    TData getTypedValue(String value);

    Predicate getFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value, GridFilterType filterType);

    Predicate getFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value, GridFilterType filterType,
                                  String removeDiacritics);
}
