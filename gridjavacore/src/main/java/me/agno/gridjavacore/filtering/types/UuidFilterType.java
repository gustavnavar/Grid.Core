package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.util.UUID;

/**
 * UuidFilterType is a concrete class that represents a filter type for UUID values.
 */
@Getter
public class UuidFilterType<T> extends FilterTypeBase<T, UUID>{

    private final Class<UUID> targetType = UUID.class;

    /**
     * Retrieves a valid GridFilterType based on the input type.
     *
     * @param type The input GridFilterType.
     * @return The valid GridFilterType.
     */
    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, CONTAINS, STARTS_WITH, ENDS_WIDTH, IS_DUPLICATED, IS_NOT_DUPLICATED -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    /**
     * Retrieves a UUID typed value from a string.
     *
     * @param value The string value to parse into a UUID.
     * @return The UUID parsed from the string value, or null if the parsing fails.
     */
    public UUID getTypedValue(String value) {
        try {
            return UUID.fromString(value);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression predicate based on the specified criteria and filter type.
     *
     * @param cb                The CriteriaBuilder object.
     * @param cq                The CriteriaQuery object.
     * @param root              The Root object.
     * @param source            The SqmQuerySpec object.
     * @param expression        The filter expression.
     * @param value             The filter value.
     * @param filterType        The GridFilterType.
     * @param removeDiacritics  Whether to remove diacritics from the filter value.
     * @return The filter expression predicate.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value, 
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        UUID typedValue = this.getTypedValue(value);
        if (typedValue == null &&
                filterType != GridFilterType.IS_DUPLICATED && filterType != GridFilterType.IS_NOT_DUPLICATED)
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
            case IS_DUPLICATED -> isDuplicated(cb, cq, root, source, this.targetType, expression);
            case IS_NOT_DUPLICATED -> isNotDuplicated(cb, cq, root, source, this.targetType,
                    expression);
            default -> throw new IllegalArgumentException();
        };
    }
}
