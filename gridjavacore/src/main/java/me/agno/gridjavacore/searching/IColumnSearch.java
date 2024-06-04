package me.agno.gridjavacore.searching;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * The IColumnSearch interface represents a column search functionality for a specific type of data.
 */
public interface IColumnSearch<T> {

    /**
     * Returns a Predicate expression for column search functionality.
     *
     * @param cb               the CriteriaBuilder used to construct the expression
     * @param cq               the criteria query
     * @param root             the Root object representing the entity being queried
     * @param value            the value to be matched in the columns
     * @param onlyTextColumns  a flag indicating whether only text columns should be searched
     * @return a Predicate expression representing the column search functionality
     */
    Predicate getExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, String value, boolean onlyTextColumns);

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
    Predicate getExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, String value, boolean onlyTextColumns, String removeDiacritics);
}
