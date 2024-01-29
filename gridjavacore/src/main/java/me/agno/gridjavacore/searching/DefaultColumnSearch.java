package me.agno.gridjavacore.searching;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * DefaultColumnSearch is a concrete implementation of the IColumnSearch interface. It is used to generate search expressions for a specific column in a grid.
 */
public class DefaultColumnSearch<T, TData> implements IColumnSearch<T> {

    private final String expression;

    private final Class<TData> targetType;

    /**
     * Represents a default column search object used for searching a specific column in a grid.
     *
     * @param expression the expression that represents the column
     * @param targetType the column target type
     */
    public DefaultColumnSearch(String expression, Class<TData> targetType)
    {
        this.expression = expression;
        this.targetType = targetType;
    }

    /**
     * Returns a Predicate object representing the search expression for a specific column in a grid.
     *
     * @param cb The CriteriaBuilder object to build the search expression.
     * @param root The Root object representing the root entity of the query.
     * @param value The search value to filter the column by.
     * @param onlyTextColumns Indicates whether the search should be applied only to text columns.
     * @return A Predicate object representing the search expression.
     */
    public Predicate getExpression(CriteriaBuilder cb, Root<T> root, String value, boolean onlyTextColumns) {
        return getExpression(cb, root, value, onlyTextColumns,null);
    }

    /**
     * Returns a Predicate object representing the search expression for a specific column in a grid.
     *
     * @param cb The CriteriaBuilder object to build the search expression.
     * @param root The Root object representing the root entity of the query.
     * @param value The search value to filter the column by.
     * @param onlyTextColumns Indicates whether the search should be applied only to text columns.
     * @param removeDiacritics The name of the function to remove diacritics from the search value.
     * @return A Predicate object representing the search expression.
     */
    public Predicate getExpression(CriteriaBuilder cb, Root<T> root, String value, boolean onlyTextColumns,
                                   String removeDiacritics)
    {
        if (value == null || value.trim().isEmpty())
            return null;

        var path = getPath(root, this.expression);

        if (onlyTextColumns && ! this.targetType.equals(String.class))
            return null;

        if(removeDiacritics == null) {
            return cb.like(cb.upper(path.as(String.class)),
                    '%' + value.toUpperCase() + '%');
        }
        else {
            return cb.like(cb.function(removeDiacritics, String.class,cb.upper(path.as(String.class))),
                    cb.function(removeDiacritics, String.class, cb.literal('%' + value.toUpperCase() + '%')));
        }
    }

    /**
     * Returns a Path object representing the path to a specific column in a grid.
     *
     * @param root The Root object representing the root entity of the query.
     * @param expression The expression that represents the column.
     * @return A Path object representing the path to the column.
     */
    public Path<TData> getPath(Root<T> root, String expression) {

        if(expression == null || expression.trim().isEmpty())
            return null;

        String[] names = expression.split("\\.");
        var path = root.get(names[0]);

        if(names.length > 1) {
            for(int i = 1; i < names.length; i ++) {
                path = path.get(names[i]);
            }
        }

        return (Path<TData>) path;
    }
}
