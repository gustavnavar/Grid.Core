package me.agno.gridjavacore.sorting;

import jakarta.persistence.criteria.*;

/**
 * The OrderByGridOrderer class is an implementation of the IColumnOrderer interface
 * that applies an order to a criteria query based on the column, direction, and the root of the query.
 */
public class OrderByGridOrderer<T, TData> implements IColumnOrderer<T> {

    private final String expression;

    /**
     * The OrderByGridOrderer class is an implementation of the IColumnOrderer interface
     * that applies an order to a criteria query based on the column, direction, and the root of the query.
     */
    public OrderByGridOrderer(String expression) {
        this.expression = expression;
    }

    /**
     * Applies the specified order to a criteria query based on the column, direction,
     * and the root of the query.
     *
     * @param cb the CriteriaBuilder object
     * @param root the Root object representing the entity query root
     * @param direction the sorting direction (ASCENDING or DESCENDING)
     * @return the Order object representing the applied order
     */
    public Order applyOrder(CriteriaBuilder cb, Root<T> root, GridSortDirection direction)
    {
        var path = getPath(root, this.expression);

        return switch (direction) {
            case ASCENDING -> cb.asc(path);
            case DESCENDING -> cb.desc(path);
            default -> throw new IllegalArgumentException("direction");
        };
    }

    /**
     * Returns the path for a given expression in a criteria query.
     *
     * @param root the Root object representing the entity query root
     * @param expression the expression representing the path in dot notation
     * @return the Path object representing the path for the given expression, or null if the expression is empty or null
     */
    public Path<TData> getPath(Root<T> root, String expression) {

        if(expression  == null || expression.trim().isEmpty())
            return null;

        String[] names = expression .split("\\.");
        var path = root.get(names[0]);

        if(names.length > 1) {
            for(int i = 1; i < names.length; i ++) {
                path = path.get(names[i]);
            }
        }

        return (Path<TData>) path;
    }
}
