package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

import java.time.LocalTime;

@Getter
public class LocalTimeFilterType<T> extends FilterTypeBase<T, LocalTime> {

    private final Class<LocalTime> targetType = LocalTime.class;

    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    public LocalTime getTypedValue(String value) {
        return LocalTime.parse(value);
    }

    public Predicate getFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        LocalTime typedValue = getTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return switch (filterType) {
            case EQUALS -> cb.equal(path, typedValue);
            case NOT_EQUALS -> cb.notEqual(path, typedValue);
            case LESS_THAN -> cb.lessThan(path, typedValue);
            case LESS_THAN_OR_EQUALS -> cb.lessThanOrEqualTo(path, typedValue);
            case GREATER_THAN -> cb.greaterThan(path, typedValue);
            case GREATER_THAN_OR_EQUALS -> cb.greaterThanOrEqualTo(path, typedValue);
            default -> throw new IllegalArgumentException();
        };
    }
}