package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.columns.GridCoreColumn;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

/**
 * FloatFilterType is a class that represents a filter type for floating-point values.
 * It extends the FilterTypeBase class and provides specific implementation for the Float data type.
 */
@Getter
public final class FloatFilterType<T> extends FilterTypeBase<T, Float> {

    private final Class<Float> targetType = Float.class;

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
     * Parses a string value into a typed value of type Float.
     *
     * @param value The string value to parse into a typed value.
     * @return The parsed Float value, or null if parsing fails.
     */
    public Float getTypedValue(String value) {
        try {
            return Float.valueOf(value);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression based on the given criteria.
     *
     * @param cb             The CriteriaBuilder instance.
     * @param cq             The CriteriaQuery instance.
     * @param root           The Root instance.
     * @param source         The source SqmQuerySpec instance.
     * @param column         The column.
     * @param value          The value to be filtered.
     * @param filterType The GridFilterType.
     * @param removeDiacritics The flag indicating whether diacritics should be removed.
     * @return The Predicate representing the filter expression, or null if the filter value is incorrect.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, GridCoreColumn<T, Float> column, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        Float typedValue = getTypedValue(value);
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
            case IS_DUPLICATED -> isDuplicated(cb, cq, root, source, this.targetType, column.getExpression());
            case IS_NOT_DUPLICATED -> isNotDuplicated(cb, cq, root, source, this.targetType,
                    column.getExpression());
            default -> throw new IllegalArgumentException();
        };
    }
}