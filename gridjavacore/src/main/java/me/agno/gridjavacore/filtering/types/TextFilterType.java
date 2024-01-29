package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

/**
 * The TextFilterType class is a generic class that represents a custom filter type for filtering text-based data.
 * It extends the FilterTypeBase class and provides implementation for common filter type functionality.
 */
@Getter
public class TextFilterType<T> extends FilterTypeBase<T, String> {

    private final Class<String> targetType = String.class;

    /**
     * Returns a valid GridFilterType based on the input type.
     *
     * @param type The input GridFilterType.
     * @return The valid GridFilterType. If the input type is valid, returns the input type. Otherwise, returns GridFilterType.EQUALS.
     */
    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, CONTAINS, STARTS_WITH, ENDS_WIDTH, IS_NULL, IS_NOT_NULL,
                    IS_DUPLICATED, IS_NOT_DUPLICATED -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    /**
     * Retrieves the typed value of a given string value.
     *
     * @param value The string value to parse into a typed value.
     * @return The typed value. Returns an empty string if the input value is null.
     */
    public String getTypedValue(String value) { return value == null ? "" : value; }

    /**
     * Retrieves the filter expression based on the given criteria builder, criteria query, root, and other parameters.
     *
     * @param cb              The CriteriaBuilder object.
     * @param cq              The CriteriaQuery object.
     * @param root            The Root object.
     * @param source          The SqmQuerySpec object.
     * @param expression      The filter expression.
     * @param value           The filter value.
     * @param filterType      The GridFilterType.
     * @param removeDiacritics Whether to remove diacritics from the filter value.
     * @return The Predicate object representing the filter expression.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value, 
                                         GridFilterType filterType, String removeDiacritics) {
        
        //Custom implementation of string filter type. Case insensitive compartion.
        filterType = getValidType(filterType);
        
        String typedValue = this.getTypedValue(value);
        if (typedValue == null &&
                filterType != GridFilterType.IS_DUPLICATED && filterType != GridFilterType.IS_NOT_DUPLICATED &&
                filterType != GridFilterType.IS_NULL && filterType!= GridFilterType.IS_NOT_NULL)
            return null; //incorrent filter value;
        
        var path = getPath(root, expression);

        if(removeDiacritics == null || removeDiacritics.isBlank()) {
            return switch (filterType) {
                case EQUALS -> cb.equal(cb.upper(path), typedValue.toUpperCase());
                case IS_NULL -> cb.or(cb.isNull(path), cb.equal(cb.trim(path), ""));
                case NOT_EQUALS -> cb.notEqual(cb.upper(path), typedValue.toUpperCase());
                case IS_NOT_NULL -> cb.and(cb.isNotNull(path), cb.notEqual(cb.trim(path), ""));
                case CONTAINS -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase() + '%');
                case STARTS_WITH -> cb.like(cb.upper(path),
                        typedValue.toUpperCase() + '%');
                case ENDS_WIDTH -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase());
                case IS_DUPLICATED -> isDuplicated(cb, cq, root, source, this.targetType,
                        expression);
                case IS_NOT_DUPLICATED -> isNotDuplicated(cb, cq, root, source, this.targetType,
                        expression);
                default -> throw new IllegalArgumentException();
            };
        }
        else {
            return switch (filterType) {
                case EQUALS -> cb.equal(cb.function(removeDiacritics, String.class, cb.upper(path)),
                        cb.function(removeDiacritics, String.class, cb.literal(typedValue.toUpperCase())));
                case IS_NULL -> cb.or(cb.isNull(path), cb.equal(cb.trim(path), ""));
                case NOT_EQUALS -> cb.notEqual(cb.function(removeDiacritics, String.class, cb.upper(path)),
                        cb.function(removeDiacritics, String.class, cb.literal(typedValue.toUpperCase())));
                case IS_NOT_NULL -> cb.and(cb.isNotNull(path), cb.notEqual(cb.trim(path), ""));
                case CONTAINS -> cb.like(cb.function(removeDiacritics, String.class, cb.upper(path)),
                        cb.function(removeDiacritics, String.class, cb.literal('%' + typedValue.toUpperCase() + '%')));
                case STARTS_WITH -> cb.like(cb.function(removeDiacritics, String.class, cb.upper(path)),
                        cb.function(removeDiacritics, String.class, cb.literal(typedValue.toUpperCase() + '%')));
                case ENDS_WIDTH -> cb.like(cb.function(removeDiacritics, String.class, cb.upper(path)),
                        cb.function(removeDiacritics, String.class, cb.literal('%' + typedValue.toUpperCase())));
                case IS_DUPLICATED -> isDuplicated(cb, cq, root, source, this.targetType,
                        expression);
                case IS_NOT_DUPLICATED -> isNotDuplicated(cb, cq, root, source, this.targetType,
                        expression);
                default -> throw new IllegalArgumentException();
            };
        }
    }
}