package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.columns.GridCoreColumn;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.util.Objects;

/**
 * A class representing a boolean filter type for grid filtering.
 */
@Getter
public final class BooleanFilterType<T> extends FilterTypeBase<T, Boolean> {

    private final Class<Boolean> targetType = Boolean.class;

    /**
     * Returns a valid filter type for the given type.
     *
     * @param type The filter type to validate.
     * @return The valid filter type.
     */
    public GridFilterType getValidType(GridFilterType type) {
        //in any case Boolean types must compare by Equals filter type
        //We can't compare: contains(true) and etc.
        return GridFilterType.EQUALS;
    }

    /**
     * Retrieves the typed value of a given string.
     *
     * @param value The string value to be converted.
     * @return The typed value of the given string. If the string value is "true" (case-insensitive), returns true. Otherwise, returns false.
     */
    public Boolean getTypedValue(String value) {
        return "true".equalsIgnoreCase(value);
    }

    /**
     * Retrieves the filter expression based on the provided parameters.
     *
     * @param cb The CriteriaBuilder object.
     * @param cq The CriteriaQuery object.
     * @param root The root object.
     * @param source The SqmQuerySpec object.
     * @param column The column.
     * @param value The value to filter with.
     * @param filterType The GridFilterType.
     * @param removeDiacritics The flag indicating whether to remove diacritics from the value.
     * @return The Predicate representing the filter expression.
     * @throws IllegalArgumentException if the filterType is not supported.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, GridCoreColumn<T, Boolean> column,
                                         String value, GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        Boolean typedValue = this.getTypedValue(value);

        var path = getPath(root, column.getExpression());

        if (Objects.requireNonNull(filterType) == GridFilterType.EQUALS) {
            return cb.equal(path, typedValue);
        }
        throw new IllegalArgumentException();
    }
}