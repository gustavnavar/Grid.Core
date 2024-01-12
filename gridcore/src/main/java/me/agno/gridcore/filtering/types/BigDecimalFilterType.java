package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

import java.math.BigDecimal;

@Getter
public class BigDecimalFilterType<T> extends FilterTypeBase<T, BigDecimal> {

    public Class<BigDecimal> TargetType = BigDecimal.class;

    public GridFilterType GetValidType(GridFilterType type) {
        return switch (type) {
            case Equals, NotEquals, GreaterThan, GreaterThanOrEquals, LessThan, LessThanOrEquals -> type;
            default -> GridFilterType.Equals;
        };
    }

    public BigDecimal GetTypedValue(String value) {
        return new BigDecimal(value);
    }

    public Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value, GridFilterType filterType,
                                         String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        BigDecimal typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return switch (filterType) {
            case Equals -> cb.equal(path, typedValue);
            case NotEquals -> cb.notEqual(path, typedValue);
            case LessThan -> cb.lt(path, typedValue);
            case LessThanOrEquals -> cb.le(path, typedValue);
            case GreaterThan -> cb.gt(path, typedValue);
            case GreaterThanOrEquals -> cb.ge(path, typedValue);
            default -> throw new IllegalArgumentException();
        };
    }
}