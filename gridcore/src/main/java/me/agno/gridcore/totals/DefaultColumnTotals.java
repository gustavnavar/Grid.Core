package me.agno.gridcore.totals;

import java.util.function.Function;

public class DefaultColumnTotals <T, TData> implements IColumnTotals<T, TData> {

    private final Function<T, TData> _expression;

    public DefaultColumnTotals(Function<T, TData> expression) {
        _expression = expression;
    }

    @Override
    public Function<T, TData> GetExpression() {
        return _expression;
    }
}
