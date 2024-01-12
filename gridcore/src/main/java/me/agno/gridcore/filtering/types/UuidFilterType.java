package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

import java.util.UUID;

@Getter
public class UuidFilterType<T> extends FilterTypeBase<T, UUID>{

    private final Class<UUID> targetType = UUID.class;

    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, CONTAINS, STARTS_WITH, ENDS_WIDTH -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    public UUID getTypedValue(String value) {
        return UUID.fromString(value);
    }

    public Predicate getFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        UUID typedValue = this.getTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return switch (filterType) {
            case EQUALS -> cb.equal(path, typedValue);
            case NOT_EQUALS -> cb.notEqual(path, typedValue);
            case CONTAINS -> cb.like(cb.upper(path.as(String.class)),
                    '%' + typedValue.toString().toUpperCase() + '%');
            case STARTS_WITH -> cb.like(cb.upper(path.as(String.class)),
                    typedValue.toString().toUpperCase() + '%');
            case ENDS_WIDTH -> cb.like(cb.upper(path.as(String.class)),
                    '%' + typedValue.toString().toUpperCase());
            default -> throw new IllegalArgumentException();
        };
    }
}
