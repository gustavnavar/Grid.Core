package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

/**
 * Represents a filter type for working with enum values.
 */
@Getter
public class EnumFilterType<T> extends FilterTypeBase<T, Enum> implements IFilterType<T, Enum> {

    private final Class targetType;

    /**
     * Constructs an instance of EnumFilterType with the specified type.
     *
     * @param type the target type for the filter
     */
    public EnumFilterType(Class type) {
        targetType = type;
    }

    /**
     * Returns the valid type for the grid filter based on the given type.
     *
     * @param type The grid filter type.
     * @return The valid grid filter type.
     */
    public GridFilterType getValidType(GridFilterType type) {
        return GridFilterType.EQUALS;
    }

    /**
     * Retrieves the typed value of a given string value based on the targetType of the EnumFilterType instance.
     *
     * @param value The string value to convert to a typed Enum value.
     * @return The Enum value corresponding to the given string value. Returns null if the string value cannot be converted to the targetType.
     */
    public Enum getTypedValue(String value) {
        try {
            return Enum.valueOf(targetType, value);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression predicate based on the criteria builder, criteria query, root, source, expression, value, filter type,
     * and remove diacritics flag.
     *
     * @param cb The criteria builder.
     * @param cq The criteria query.
     * @param root The criteria root.
     * @param source The SqmQuerySpec source.
     * @param expression The filter expression.
     * @param value The filter value.
     * @param filterType The filter type.
     * @param removeDiacritics The remove diacritics flag.
     * @return The filter expression predicate.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value, 
                                         GridFilterType filterType, String removeDiacritics) {
        Enum typedValue = this.getTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return cb.equal(path, typedValue);
    }
}
