package me.agno.gridcore.totals;

import java.util.function.Function;

public interface IColumnTotals<T, TData> {
    Function<T, TData> GetExpression();
}
