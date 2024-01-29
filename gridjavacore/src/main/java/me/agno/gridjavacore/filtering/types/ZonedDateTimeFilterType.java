package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.time.ZonedDateTime;

/**
 * ZonedDateTimeFilterType is a class that extends the FilterTypeBase class to provide filtering functionality
 * for the ZonedDateTime data type.
 */
@Getter
public class ZonedDateTimeFilterType<T> extends FilterTypeBase<T, ZonedDateTime> {

    private final Class<ZonedDateTime> targetType = ZonedDateTime.class;

    /**
     * Retrieves the valid GridFilterType from the input GridFilterType.
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
     * Parses a string value into a typed value of ZonedDateTime.
     *
     * @param value The string value to parse into a typed value of ZonedDateTime.
     * @return The parsed value as ZonedDateTime, or null if parsing fails.
     */
    public ZonedDateTime getTypedValue(String value) {
        try {
            return ZonedDateTime.parse(value);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression for a given criteria.
     *
     * @param cb The CriteriaBuilder object.
     * @param cq The CriteriaQuery object.
     * @param root The Root object.
     * @param source The SqmQuerySpec object.
     * @param expression The filter expression.
     * @param value The filter value.
     * @param filterType The GridFilterType.
     * @param removeDiacritics Whether to remove diacritics from the filter value.
     * @return The filter expression as a Predicate.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value, 
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        ZonedDateTime typedValue = getTypedValue(value);
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