package me.agno.gridjavacore.sorting;

import jakarta.persistence.criteria.*;

/**
 * The ThenByColumnOrderer class represents an implementation of the IColumnOrderer interface
 * that applies an order to a criteria query based on a specific column and direction.
 */
public class ThenByColumnOrderer <T, TData> implements IColumnOrderer<T> {

    private final String expression;
    private final GridSortDirection initialDirection;

    /**
     * The ThenByColumnOrderer class represents an implementation of the IColumnOrderer interface
     * that applies an order to a criteria query based on a specific column and direction.
     */
    public ThenByColumnOrderer(String expression, GridSortDirection initialDirection) {
        this.expression = expression;
        this.initialDirection = initialDirection;
    }

    /**
     * Applies the initial direction to the given criteria query based on the column expression and the root entity.
     *
     * @param cb the CriteriaBuilder object
     * @param root the Root object representing the entity query root
     * @return the Order object representing the applied order
     */
    private Order apply(CriteriaBuilder cb, Root<T> root) {

        var path = getPath(root, this.expression);

        return switch (this.initialDirection) {
            case ASCENDING -> cb.asc(path);
            case DESCENDING -> cb.desc(path);
            default -> throw new IllegalArgumentException();
        };
    }

    /**
     * Returns a path object representing the specified expression starting from the given root.
     * The expression is a dot-separated chain of property names.
     *
     * @param root the root object from which to start the path
     * @param expression the dot-separated chain of property names representing the desired path
     * @return a path object representing the specified expression
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

    /**
     * Applies the sorting order to the given CriteriaBuilder object and Root object based on the sorting direction.
     *
     * @param cb        the CriteriaBuilder object
     * @param root      the Root object representing the entity query root
     * @param direction the sorting direction (ASCENDING or DESCENDING)
     * @return the Order object representing the applied sorting order
     */
    public Order applyOrder(CriteriaBuilder cb, Root<T> root, GridSortDirection direction) {
        return apply(cb, root);
    }
}