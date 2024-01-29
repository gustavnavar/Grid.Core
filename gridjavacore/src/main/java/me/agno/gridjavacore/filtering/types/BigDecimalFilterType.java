package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.math.BigDecimal;

/**
 * A filter type specifically designed for filtering BigDecimal values.
 */
@Getter
public class BigDecimalFilterType<T> extends FilterTypeBase<T, BigDecimal> {

    private final Class<BigDecimal> targetType = BigDecimal.class;

    /**
     * Retrieves the valid filter type based on the specified type.
     *
     * @param type The filter type.
     * @return The valid filter type.
     */
    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS,
                    IS_DUPLICATED, IS_NOT_DUPLICATED -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    /**
     * Returns a {@code BigDecimal} value representing the given string value.
     * If the string value cannot be parsed as a {@code BigDecimal}, {@code null} is returned.
     *
     * @param value The string value to be parsed.
     * @return The parsed {@code BigDecimal} value, or {@code null} if the string value cannot be parsed.
     */
    public BigDecimal getTypedValue(String value) {
        try {
            return new BigDecimal(value);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression based on the provided criteria and filter parameters.
     *
     * @param cb              The CriteriaBuilder used to construct the predicate.
     * @param cq              The CriteriaQuery used to construct the predicate.
     * @param root            The root entity from which the filter expression is constructed.
     * @param source          The SqmQuerySpec representing the query specification.
     * @param expression      The expression used to identify the path in the root entity.
     * @param value           The value used in the filter expression.
     * @param filterType      The type of filter to apply.
     * @param removeDiacritics The flag indicating whether to remove diacritics.
     * @return The Predicate representing the filter expression.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        BigDecimal typedValue = getTypedValue(value);
        if (typedValue == null &&
                filterType != GridFilterType.IS_DUPLICATED && filterType != GridFilterType.IS_NOT_DUPLICATED)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return switch (filterType) {
            case EQUALS -> cb.equal(path, typedValue);
            case NOT_EQUALS -> cb.notEqual(path, typedValue);
            case LESS_THAN -> cb.lt(path, typedValue);
            case LESS_THAN_OR_EQUALS -> cb.le(path, typedValue);
            case GREATER_THAN -> cb.gt(path, typedValue);
            case GREATER_THAN_OR_EQUALS -> cb.ge(path, typedValue);
            case IS_DUPLICATED -> isDuplicated(cb, cq, root, source, this.targetType, expression);
            case IS_NOT_DUPLICATED -> isNotDuplicated(cb, cq, root, source,this.targetType,
                    expression);
            default -> throw new IllegalArgumentException();
        };
    }
}