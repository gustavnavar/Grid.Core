package me.agno.gridjavacore.sorting;

import jakarta.persistence.criteria.*;
import me.agno.gridjavacore.columns.GridCoreColumn;

import java.util.Collection;

/**
 * The OrderByGridOrderer class is an implementation of the IColumnOrderer interface
 * that applies an order to a criteria query based on the column, direction, and the root of the query.
 */
public class OrderByGridOrderer<T, TData> implements IColumnOrderer<T> {

    private final GridCoreColumn<T, TData> column;

    /**
     * The OrderByGridOrderer class is an implementation of the IColumnOrderer interface
     * that applies an order to a criteria query based on the column, direction, and the root of the query.
     *
     * @param column the column
     */
    public OrderByGridOrderer(GridCoreColumn<T, TData> column) {
        this.column = column;
    }

    /**
     * Applies the specified order to a criteria query based on the column, direction,
     * and the root of the query.
     *
     * @param cb the CriteriaBuilder object
     * @param cq the criteria query
     * @param root the Root object representing the entity query root
     * @param direction the sorting direction (ASCENDING or DESCENDING)
     * @return the Order object representing the applied order
     */
    public Order applyOrder(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                            GridSortDirection direction)
    {
        if (this.column.getTargetType().equals(Collection.class)) {
            var subquery = getCollectionCount(cb, cq, root);

            return switch (direction) {
                case ASCENDING -> cb.asc(subquery);
                case DESCENDING -> cb.desc(subquery);
                default -> throw new IllegalArgumentException("direction");
            };
        }
        else {
            var path = getPath(root, this.column.getExpression());

            return switch (direction) {
                case ASCENDING -> cb.asc(path);
                case DESCENDING -> cb.desc(path);
                default -> throw new IllegalArgumentException("direction");
            };
        }
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
