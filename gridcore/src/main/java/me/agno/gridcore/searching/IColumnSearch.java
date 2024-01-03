package me.agno.gridcore.searching;

import java.util.function.Predicate;

public interface IColumnSearch<T> {
    Predicate<T> GetExpression(String value);
    Predicate<T> GetExpression(String value, Class removeDiacritics);
}
