package me.agno.gridcore.filtering;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.util.List;

public interface IColumnFilter<T> {

    Predicate applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, SqmQuerySpec source,
                          List<ColumnFilterValue> values);

    Predicate applyFilter(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, SqmQuerySpec source,
                          List<ColumnFilterValue> values, String removeDiacritics);
}
