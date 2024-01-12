package me.agno.gridcore.pagination;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.function.Function;

public interface IPagerProcessor<T> {
    TypedQuery<T> Process(CriteriaQuery<T> items);

    void SetProcess(Function<CriteriaQuery<T>, TypedQuery<T>> process);
}
