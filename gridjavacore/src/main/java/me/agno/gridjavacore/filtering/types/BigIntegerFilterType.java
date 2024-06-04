package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.columns.GridCoreColumn;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.math.BigInteger;

/**
 * A class representing a filter type for BigInteger values.
 */
@Getter
public class BigIntegerFilterType<T> extends FilterTypeBase<T, BigInteger> {

    private final Class<BigInteger> targetType = BigInteger.class;

    /**
     * Retrieves a valid GridFilterType based on the given type.
     *
     * @param type The GridFilterType to validate.
     * @return The validated GridFilterType. If the given type is a valid GridFilterType, it is returned as is.
     *         Otherwise, GridFilterType.EQUALS is returned.
     */
    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS,
                    IS_DUPLICATED, IS_NOT_DUPLICATED -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    /**
     * Retrieves a BigInteger value from a string representation.
     *
     * @param value The string value to convert to a BigInteger.
     * @return The BigInteger value parsed from the input string. If the parsing fails, returns null.
     */
    public BigInteger getTypedValue(String value) {
        try {
            return new BigInteger(value);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression based on the given parameters.
     *
     * @param cb The CriteriaBuilder object used for building filter expressions.
     * @param cq The CriteriaQuery object used for building filter expressions.
     * @param root The root entity in the CriteriaQuery.
     * @param source The SqmQuerySpec object representing the query specification.
     * @param column The column.
     * @param value The value to be filtered.
     * @param filterType The GridFilterType.
     * @param removeDiacritics The flag indicating whether diacritics should be removed.
     * @return The Predicate representing the filter expression. Returns null if the filter value is incorrect.
     * @throws IllegalArgumentException if the filter type is not recognized.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, GridCoreColumn<T, BigInteger> column,
                                         String value, GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        BigInteger typedValue = this.getTypedValue(value);
        if (typedValue == null &&
                filterType != GridFilterType.IS_DUPLICATED && filterType != GridFilterType.IS_NOT_DUPLICATED)
            return null; //incorrent filter value;

        var path = getPath(root, column.getExpression());

        return switch (filterType) {
            case EQUALS -> cb.equal(path, typedValue);
            case NOT_EQUALS -> cb.notEqual(path, typedValue);
            case LESS_THAN -> cb.lt(path, typedValue);
            case LESS_THAN_OR_EQUALS -> cb.le(path, typedValue);
            case GREATER_THAN -> cb.gt(path, typedValue);
            case GREATER_THAN_OR_EQUALS -> cb.ge(path, typedValue);
            case IS_DUPLICATED -> isDuplicated(cb, cq, root, source,this.targetType, column.getExpression());
            case IS_NOT_DUPLICATED -> isNotDuplicated(cb, cq, root, source,this.targetType,
                    column.getExpression());
            default -> throw new IllegalArgumentException();
        };
    }
}