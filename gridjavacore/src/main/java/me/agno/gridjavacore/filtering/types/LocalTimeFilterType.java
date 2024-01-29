package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.time.LocalTime;

/**
 * LocalTimeFilterType is a class that represents a custom filter type for filtering LocalTime values.
 * It extends the FilterTypeBase class and provides the implementation of the abstract methods defined in the base class.
 */
@Getter
public class LocalTimeFilterType<T> extends FilterTypeBase<T, LocalTime> {

    private final Class<LocalTime> targetType = LocalTime.class;

    /**
     * Returns the valid GridFilterType based on the input type.
     *
     * @param type The input GridFilterType.
     * @return The valid GridFilterType.
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
     * @return The parsed LocalTime value, or null if the value cannot be parsed.
     */
    public LocalTime getTypedValue(String value) {
        try {
            return LocalTime.parse(value);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression based on the provided parameters.
     *
     * @param cb                The CriteriaBuilder object.
     * @param cq                The CriteriaQuery object.
     * @param root              The Root object.
     * @param source            The SqmQuerySpec object.
     * @param expression        The filter expression.
     * @param value             The filter value.
     * @param filterType        The GridFilterType.
     * @param removeDiacritics  Whether to remove diacritics from the filter value.
     * @return A Predicate representing the filter expression.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value, 
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        LocalTime typedValue = getTypedValue(value);
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