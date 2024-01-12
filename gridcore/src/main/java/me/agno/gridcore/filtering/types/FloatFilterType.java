package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

@Getter
public final class FloatFilterType<T> extends FilterTypeBase<T, Float> {

    public Class<Float> TargetType = Float.class;

    public GridFilterType GetValidType(GridFilterType type) {
        return switch (type) {
            case Equals, NotEquals, GreaterThan, GreaterThanOrEquals, LessThan, LessThanOrEquals -> type;
            default -> GridFilterType.Equals;
        };
    }

    public Float GetTypedValue(String value) { return Float.valueOf(value); }

    public Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        Float typedValue = GetTypedValue(value);

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