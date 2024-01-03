package me.agno.gridcore.searching;

import java.util.function.Function;
import java.util.function.Predicate;

public class DefaultColumnSearch<T, TData> implements IColumnSearch<T> {

    private final Function<T, TData> _expression;

    private final Class _targetType;

    public DefaultColumnSearch(Function<T, TData> expression, Class targetType)
    {
        _expression = expression;
        _targetType = targetType;
    }

    public Predicate<T> GetExpression(String value) {
        return GetExpression(value, null);
    }

    public Predicate<T> GetExpression(String value, Class removeDiacritics)
    {
        if (value == null ||value.trim() == "")
            return null;

        if (_targetType.equals(String.class))
        {
            if(removeDiacritics == null) {
                return c -> _expression.apply(c).toString().toUpperCase().contains(value.toUpperCase());
            }
            else {
                return c -> _expression.apply(c).toString().toUpperCase().contains(value.toUpperCase());
            }
        }

        return null;
    }
}
