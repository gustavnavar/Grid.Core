package me.agno.gridcore.pagination;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.function.Function;

public interface IPagerProcessor<T> {

    TypedQuery<T> process(CriteriaQuery<T> items);

    void setProcess(Function<CriteriaQuery<T>, TypedQuery<T>> process);
}
