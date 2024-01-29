package me.agno.gridjavacore.totals;

import lombok.Getter;

/**
 * Represents a default implementation of the IColumnTotals interface.
 * It holds the expression used for column totals calculations.
 */
@Getter
public class DefaultColumnTotals implements IColumnTotals {

    /**
     * Represents a string expression used for column totals calculations.
     * The expression is stored as a private member variable and is immutable.
     */
    private final String expression;

    /**
     * Represents a default implementation of the IColumnTotals interface.
     * It holds the expression used for column totals calculations.
     * @param expression the name expression that represents the column
     */
    public DefaultColumnTotals(String expression) {
        this.expression = expression;
    }
}
