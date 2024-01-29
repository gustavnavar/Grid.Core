package me.agno.gridjavacore.filtering;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.util.List;

/**
 * The IColumnFilter interface represents a filter applied to a column in a criteria query.
 */
public interface IColumnFilter<T> {

    /**
     * Applies a filter to a criteria query.
     *
     * @param cb      the CriteriaBuilder object
     * @param cq      the CriteriaQuery object
     * @param root    the Root object
     * @param source  the SqmQuerySpec object
     * @param values  the list of ColumnFilterValue objects representing the filter values
     * @return the Predicate object representing the filter
     */
    Predicate applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, SqmQuerySpec source,
                          List<ColumnFilterValue> values);

    /**
     * Applies a filter to a criteria query based on the provided criteria builder, criteria query, root,
     * filter values, and diacritic removal flag.
     *
     * @param cb                the CriteriaBuilder object used to build the filter predicate
     * @param cq                the CriteriaQuery object representing the criteria query
     * @param root              the Root object representing the root entity of the query
     * @param source            the SqmQuerySpec object representing the query specification
     * @param values            the list of ColumnFilterValue objects representing the filter values
     * @param removeDiacritics  a flag indicating whether to remove diacritics in the filter comparison
     * @return the Predicate object representing the filter to apply to the criteria query
     */
    Predicate applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, SqmQuerySpec source,
                          List<ColumnFilterValue> values, String removeDiacritics);
}
