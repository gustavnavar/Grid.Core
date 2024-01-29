package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

/**
 * Represents an interface for filter types.
 * @param <T> The type of data being filtered.
 * @param <TData> The typed value of the filter.
 */
public interface IFilterType<T, TData> {

    /**
     * Returns the target type of the filter.
     *
     * @return The target type of the filter.
     */
    Class<TData> getTargetType();

    /**
     * Returns a valid GridFilterType based on the input type.
     *
     * @param type The GridFilterType to check.
     * @return The valid GridFilterType. If the input type is not a valid type, returns GridFilterType.EQUALS.
     */
    GridFilterType getValidType(GridFilterType type);

    /**
     * Retrieves the typed value of a given string value based on the targetType. The targetType should be set in the constructor of the implementing class.
     *
     * @param value The string value to convert to a typed value.
     * @return The typed value corresponding to the given string value. Returns null if the string value cannot be converted to the targetType.
     */
    TData getTypedValue(String value);

    /**
     * Filters the data based on a given expression and value using the specified filter type.
     *
     * @param cb The criteria builder used for constructing queries.
     * @param cq The criteria query being constructed.
     * @param root The root entity being queried.
     * @param source The query specification for the current query.
     * @param expression The expression to filter on.
     * @param value The value to filter against.
     * @param filterType The filter type to apply.
     * @return A predicate representing the filter expression.
     */
    Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, SqmQuerySpec source,
                                  String expression, String value, GridFilterType filterType);

    /**
     * Retrieves the filter expression predicate based on the provided parameters.
     *
     * @param cb The CriteriaBuilder object.
     * @param cq The CriteriaQuery object.
     * @param root The Root object.
     * @param source The SqmQuerySpec object.
     * @param expression The filter expression.
     * @param value The filter value.
     * @param filterType The GridFilterType.
     * @param removeDiacritics Whether to remove diacritics from the filter value.
     * @return The Predicate representing the filter expression.
     */
    Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, SqmQuerySpec source,
                                  String expression, String value, GridFilterType filterType, String removeDiacritics);
}
