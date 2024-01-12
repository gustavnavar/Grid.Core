package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

import java.nio.charset.StandardCharsets;

@Getter
public final class ByteFilterType<T> extends FilterTypeBase<T, Byte> {

    public Class<Byte> TargetType = Byte.class;

    public GridFilterType GetValidType(GridFilterType type) {
        return switch (type) {
            case Equals, NotEquals, GreaterThan, GreaterThanOrEquals, LessThan, LessThanOrEquals -> type;
            default -> GridFilterType.Equals;
        };
    }

    public Byte GetTypedValue(String value) {
        return value.getBytes(StandardCharsets.UTF_8)[0];
    }

    public Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value, GridFilterType filterType,
                                         String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        Byte typedValue = GetTypedValue(value);
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