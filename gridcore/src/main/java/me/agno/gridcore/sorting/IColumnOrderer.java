package me.agno.gridcore.sorting;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;

public interface IColumnOrderer<T> {
    Order applyOrder(CriteriaBuilder cb, Root<T> root, GridSortDirection direction);
}
