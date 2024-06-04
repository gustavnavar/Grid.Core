package me.agno.gridjavacore.sorting;

import jakarta.persistence.criteria.*;
import me.agno.gridjavacore.columns.GridCoreColumn;

import java.util.Collection;

/**
 * The ThenByColumnOrderer class represents an implementation of the IColumnOrderer interface
 * that applies an order to a criteria query based on a specific column and direction.
 */
public class ThenByColumnOrderer <T, TData> implements IColumnOrderer<T> {

    private final GridCoreColumn<T, TData> column;
    private final GridSortDirection initialDirection;

    /**
     * The ThenByColumnOrderer class represents an implementation of the IColumnOrderer interface
     * that applies an order to a criteria query based on a specific column and direction.
     *
     * @param column the column
     * @param initialDirection the initial direction of the column
     */
    public ThenByColumnOrderer(GridCoreColumn<T, TData> column, GridSortDirection initialDirection) {
        this.column = column;
        this.initialDirection = initialDirection;
    }

    /**
     * Applies the initial direction to the given criteria query based on the column expression and the root entity.
     *
     * @param cb the CriteriaBuilder object
     * @param root the Root object representing the entity query root
     * @return the Order object representing the applied order
     */
    private Order apply(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root) {

        if (this.column.getTargetType().equals(Collection.class)) {
            var subquery = getCollectionCount(cb, cq, root);

            return switch (this.initialDirection) {
                case ASCENDING -> cb.asc(subquery);
                case DESCENDING -> cb.desc(subquery);
                default -> throw new IllegalArgumentException();
            };
        }
        else {
            var path = getPath(root, this.column.getExpression());

            return switch (this.initialDirection) {
                case ASCENDING -> cb.asc(path);
                case DESCENDING -> cb.desc(path);
                default -> throw new IllegalArgumentException();
            };
        }
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

    /**
     * Applies the sorting order to the given CriteriaBuilder object and Root object based on the sorting direction.
     *
     * @param cb        the CriteriaBuilder object
     * @param root      the Root object representing the entity query root
     * @param direction the sorting direction (ASCENDING or DESCENDING)
     * @return the Order object representing the applied sorting order
     */
    public Order applyOrder(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                            GridSortDirection direction) {
        return apply(cb, cq, root);
    }
}