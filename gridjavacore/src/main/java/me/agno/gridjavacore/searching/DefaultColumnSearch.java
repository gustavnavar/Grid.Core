package me.agno.gridjavacore.searching;

import jakarta.persistence.criteria.*;
import me.agno.gridjavacore.columns.GridCoreColumn;

import java.util.Collection;

/**
 * DefaultColumnSearch is a concrete implementation of the IColumnSearch interface. It is used to generate search expressions for a specific column in a grid.
 */
public class DefaultColumnSearch<T, TData> implements IColumnSearch<T> {

    private final GridCoreColumn<T, TData> column;

    /**
     * Represents a default column search object used for searching a specific column in a grid.
     *
     * @param column the column
     */
    public DefaultColumnSearch(GridCoreColumn<T, TData> column)
    {
        this.column = column;
    }

    /**
     * Returns a Predicate object representing the search expression for a specific column in a grid.
     *
     * @param cb The CriteriaBuilder object to build the search expression.
     * @param cq The criteria query
     * @param root The Root object representing the root entity of the query.
     * @param value The search value to filter the column by.
     * @param onlyTextColumns Indicates whether the search should be applied only to text columns.
     * @return A Predicate object representing the search expression.
     */
    public Predicate getExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                   String value, boolean onlyTextColumns) {
        return getExpression(cb, cq, root, value, onlyTextColumns,null);
    }

    /**
     * Returns a Predicate object representing the search expression for a specific column in a grid.
     *
     * @param cb The CriteriaBuilder object to build the search expression.
     * @param cq The criteria query
     * @param root The Root object representing the root entity of the query.
     * @param value The search value to filter the column by.
     * @param onlyTextColumns Indicates whether the search should be applied only to text columns.
     * @param removeDiacritics The name of the function to remove diacritics from the search value.
     * @return A Predicate object representing the search expression.
     */
    public Predicate getExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, String value,
                                   boolean onlyTextColumns, String removeDiacritics)
    {
        if (value == null || value.trim().isEmpty())
            return null;

        if (onlyTextColumns && !this.column.getTargetType().equals(String.class))
            return null;

        if (this.column.getTargetType().equals(Collection.class)) {

            var subquery = getCollectionCount(cb, cq, root);
            return cb.like(cb.upper(subquery.as(String.class)),
                    '%' + value.toUpperCase() + '%');
        }
        else {
            var path = getPath(root, this.column.getExpression());

            if (removeDiacritics == null) {
                return cb.like(cb.upper(path.as(String.class)),
                        '%' + value.toUpperCase() + '%');
            } else {
                return cb.like(cb.function(removeDiacritics, String.class, cb.upper(path.as(String.class))),
                        cb.function(removeDiacritics, String.class, cb.literal('%' + value.toUpperCase() + '%')));
            }
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

    /**
     * Returns a Path object representing the path to a specific column in a grid.
     *
     * @param cb The CriteriaBuilder object to build the search expression.
     * @param cq The criteria query
     * @param root The Root object representing the root entity of the query.
     * @return A Subquery object representing the path to the Collection column.
     */
    public Subquery<Long> getCollectionCount(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root) {

        String[] names = column.getExpression().split("\\.");
        if(names.length >= 2 && names[names.length -1].equalsIgnoreCase("count")) {

            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<?> subRoot = subquery.from(column.getSubgridTargetType());

            Predicate[] predicates = new Predicate[column.getSubgridKeys().length];
            for (int i = 0; i < column.getSubgridKeys().length; i++) {
                predicates[i] = cb.equal(root.get(column.getSubgridKeys()[i].getKey()),
                        subRoot.get(column.getSubgridKeys()[i].getValue()));
            }

            return subquery.select(cb.count(cb.literal(1)))
                    .where(predicates);
        }
        else {
            return null; //incorrent column name;
        }
    }
}
