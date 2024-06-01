package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import me.agno.gridjavacore.utils.DateTimeUtils;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.util.Date;

/**
 * DateFilterType is a class that represents a filter type for filtering Date values in a grid.
 */
@Getter
public class DateFilterType<T> extends FilterTypeBase<T, Date> {

    private final Class<Date> targetType = Date.class;

    /**
     * Retrieves the valid GridFilterType based on the input GridFilterType.
     *
     * @param type The GridFilterType to be validated.
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
     * Retrieves the typed value of a String representation of a date.
     *
     * @param value The String representation of the date value.
     * @return The typed value of the date, or null if the value is not a valid date.
     */
    public Date getTypedValue(String value) {
        return DateTimeUtils.getDate(value);
    }


    /**
     * Retrieves the filter expression based on the provided parameters.
     *
     * @param cb              The CriteriaBuilder object.
     * @param cq              The CriteriaQuery object.
     * @param root            The Root object.
     * @param source          The SqmQuerySpec object.
     * @param expression      The expression to be filtered.
     * @param value           The value to be filtered.
     * @param filterType      The type of filter.
     * @param removeDiacritics Indicates whether to remove diacritics from the filter value.
     * @return The filter expression as a Predicate object, or null if the filter value is incorrect.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value, 
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        Date typedValue = this.getTypedValue(value);
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