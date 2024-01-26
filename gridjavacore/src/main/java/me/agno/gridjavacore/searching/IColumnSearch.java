package me.agno.gridjavacore.searching;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface IColumnSearch<T> {
    Predicate getExpression(CriteriaBuilder cb, Root<T> root, String value, boolean onlyTextColumns);
    Predicate getExpression(CriteriaBuilder cb, Root<T> root, String value, boolean onlyTextColumns, String removeDiacritics);
}