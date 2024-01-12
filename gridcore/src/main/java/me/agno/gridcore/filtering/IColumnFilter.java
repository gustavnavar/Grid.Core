package me.agno.gridcore.filtering;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public interface IColumnFilter<T> {

    Predicate ApplyFilter(CriteriaBuilder cb, Root<T> root, List<ColumnFilterValue> values);

    Predicate ApplyFilter(CriteriaBuilder cb, Root<T> root, List<ColumnFilterValue> values, String removeDiacritics);
}
