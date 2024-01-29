package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.time.Instant;

/**
 * Represents a custom filter type for Instant data type.
 */
@Getter
public class InstantFilterType<T> extends FilterTypeBase<T, Instant> {

    private final Class<Instant> targetType = Instant.class;

    /**
     * Returns the valid GridFilterType based on the input type.
     *
     * @param type The input GridFilterType.
     * @return The valid GridFilterType. If the input type is not valid, returns GridFilterType.EQUALS.
     */
    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS,
                    IS_DUPLICATED, IS_NOT_DUPLICATED -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    /**
     * Retrieves a typed value based on the provided string value.
     *
     * @param value The string value to parse into a typed value.
     * @return The typed value created from the string value. Returns null if the parsing fails.
     */
    public Instant getTypedValue(String value) {
        try {
            return Instant.parse(value);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression used in the criteria query.
     *
     * @param cb                The CriteriaBuilder object.
     * @param cq                The CriteriaQuery object.
     * @param root              The Root object.
     * @param source            The SqmQuerySpec object.
     * @param expression        The filter expression.
     * @param value             The filter value.
     * @param filterType        The GridFilterType representing the type of filter.
     * @param removeDiacritics  Whether to remove diacritics from the filter value.
     * @return The Predicate representing the filter expression.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value, 
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        Instant typedValue = this.getTypedValue(value);
        if (typedValue == null &&
                filterType != GridFilterType.IS_DUPLICATED && filterType != GridFilterType.IS_NOT_DUPLICATED)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return switch (filterType) {
            case EQUALS -> cb.equal(path, typedValue);
            case NOT_EQUALS -> cb.notEqual(path, typedValue);
            case LESS_THAN -> cb.lessThan(path, typedValue);
            case LESS_THAN_OR_EQUALS -> cb.lessThanOrEqualTo(path, typedValue);
            case GREATER_THAN -> cb.greaterThan(path, typedValue);
            case GREATER_THAN_OR_EQUALS -> cb.greaterThanOrEqualTo(path, typedValue);
            case IS_DUPLICATED -> isDuplicated(cb, cq, root, source, this.targetType, expression);
            case IS_NOT_DUPLICATED -> isNotDuplicated(cb, cq, root, source, this.targetType,
                    expression);
            default -> throw new IllegalArgumentException();
        };
    }
}