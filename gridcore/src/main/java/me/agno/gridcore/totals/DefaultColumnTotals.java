package me.agno.gridcore.totals;

public class DefaultColumnTotals implements IColumnTotals {

    private final String _expression;

    public DefaultColumnTotals(String expression) {
        _expression = expression;
    }

    @Override
    public String GetExpression() {
        return _expression;
    }
}
