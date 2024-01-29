package me.agno.gridjavacore.pagination;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.function.Function;

/**
 * IPagerProcessor is an interface that defines the contract for a pager processor.
 * It allows the processing of CriteriaQuery with pagination and provides a method to
 * set the processing function.
 */
public interface IPagerProcessor<T> {

    /**
     * Processes a CriteriaQuery with pagination.
     *
     * @param items the CriteriaQuery to be processed
     * @return a TypedQuery representing the processed CriteriaQuery
     */
    TypedQuery<T> process(CriteriaQuery<T> items);

    /**
     * Sets the processing function for the pager processor.
     *
     * @param process the function that processes a CriteriaQuery and returns a TypedQuery
     *                representing the processed CriteriaQuery
     */
    void setProcess(Function<CriteriaQuery<T>, TypedQuery<T>> process);
}
