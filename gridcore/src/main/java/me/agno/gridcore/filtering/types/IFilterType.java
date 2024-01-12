package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridcore.filtering.GridFilterType;

public interface IFilterType<T, TData> {

    Class getTargetType();

    GridFilterType GetValidType(GridFilterType type);

    TData GetTypedValue(String value);

    Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value, GridFilterType filterType);

    Predicate GetFilterExpression(CriteriaBuilder cb,Root<T> root, String expression, String value, GridFilterType filterType,
                                  String removeDiacritics);
}
