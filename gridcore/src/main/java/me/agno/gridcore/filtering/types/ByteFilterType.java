package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

import java.nio.charset.StandardCharsets;

@Getter
public final class ByteFilterType<T> extends FilterTypeBase<T, Byte> {

    private final Class<Byte> targetType = Byte.class;

    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    public Byte getTypedValue(String value) {
        return value.getBytes(StandardCharsets.UTF_8)[0];
    }

    public Predicate getFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value, GridFilterType filterType,
                                         String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = this.getValidType(filterType);

        Byte typedValue = this.getTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return switch (filterType) {
            case EQUALS -> cb.equal(path, typedValue);
            case NOT_EQUALS -> cb.notEqual(path, typedValue);
            case LESS_THAN -> cb.lt(path, typedValue);
            case LESS_THAN_OR_EQUALS -> cb.le(path, typedValue);
            case GREATER_THAN -> cb.gt(path, typedValue);
            case GREATER_THAN_OR_EQUALS -> cb.ge(path, typedValue);
            default -> throw new IllegalArgumentException();
        };
    }
}