package me.agno.gridjavacore.sorting;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

/**
 * The IColumnOrderer interface represents an orderer for a specific column in a grid. It defines a method
 * that applies an order to a criteria query based on the column, direction, and the root of the query.
 */
public interface IColumnOrderer<T> {

    /**
     * Applies the specified order to a criteria query based on the column, direction, and the root of the query.
     *
     * @param cb the CriteriaBuilder object
     * @param cq the criteria query
     * @param root the Root object representing the entity query root
     * @param direction the sorting direction (ASCENDING or DESCENDING)
     * @return the Order object representing the applied order
     */
    Order applyOrder(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, GridSortDirection direction);
}
