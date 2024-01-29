package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.IGridColumnCollection;
import me.agno.gridjavacore.totals.IColumnTotals;
import me.agno.gridjavacore.totals.Total;

import java.util.LinkedHashMap;
import java.util.function.Function;

/**
 * Represents a column that supports calculations and totals in a grid.
 */
public interface ITotalsColumn<T> {

    /**
     * Retrieves the column totals for the grid.
     *
     * @return The column totals.
     */
    IColumnTotals getTotals();

    /**
     * Retrieves the calculations for the grid columns.
     *
     * @return A LinkedHashMap containing the calculations for the grid columns. The key is the name of the calculation and the value is a Function that calculates the result.
     */
    LinkedHashMap<String, Function<IGridColumnCollection<T>, Object>> getCalculations();

    /**
     * Determines whether the sum calculation is enabled for this column in the grid.
     *
     * @return true if the sum calculation is enabled for this column, false otherwise
     */
    boolean isSumEnabled();

    /**
     * Sets the sum calculation enabled or disabled for this column in the grid.
     *
     * @param sumEnabled true to enable the sum calculation, false to disable it
     */
    void setSumEnabled(boolean sumEnabled);

    /**
     * Retrieves the sum value for the column in a grid.
     *
     * @return The sum value of the column in a grid.
     */
    Total getSumValue();

    /**
     * Sets the sum value for the column in a grid.
     *
     * @param sumValue The sum value to be set for the column.
     */
    void setSumValue(Total sumValue);

    /**
     * Determines whether the average calculation is enabled for this column in the grid.
     *
     * @return true if the average calculation is enabled for this column, false otherwise
     */
    boolean isAverageEnabled();

    /**
     * Sets whether the average calculation is enabled for this column in the grid.
     *
     * @param averageEnabled true to enable the average calculation, false to disable it
     */
    void setAverageEnabled(boolean averageEnabled);

    /**
     * Retrieves the average value for the column in a grid.
     *
     * @return The average value of the column in a grid.
     */
    Total getAverageValue();

    /**
     * Sets the average value for the column in a grid.
     *
     * @param averageValue The average value to be set for the column.
     */
    void setAverageValue(Total averageValue);

    /**
     * Determines whether the maximum calculation is enabled for this column in the grid.
     *
     * @return True if the maximum calculation is enabled for this column, false otherwise.
     */
    boolean isMaxEnabled();

    /**
     * Sets whether the maximum calculation is enabled for this column in the grid.
     *
     * @param maxEnabled true to enable the maximum calculation, false to disable it
     */
    void setMaxEnabled(boolean maxEnabled);

    /**
     * Retrieves the maximum value for the column in the grid.
     *
     * @return The maximum value of the column in the grid.
     */
    Total getMaxValue();

    /**
     * Sets the maximum value for the column in the grid.
     *
     * @param maxValue The maximum value to be set for the column.
     */
    void setMaxValue(Total maxValue);

    /**
     * Determines whether the minimum calculation is enabled for this column in the grid.
     *
     * @return true if the minimum calculation is enabled for this column, false otherwise
     */
    boolean isMinEnabled();

    /**
     * Sets whether the minimum calculation is enabled for this column in the grid.
     *
     * @param minEnabled true to enable the minimum calculation, false to disable it
     */
    void setMinEnabled(boolean minEnabled);

    /**
     * Retrieves the minimum value for the column in the grid.
     *
     * @return The minimum value of the column in the grid.
     */
    Total getMinValue();

    /**
     * Sets the minimum value for the column in the grid.
     *
     * @param minValue The minimum value to be set for the column.
     */
    void setMinValue(Total minValue);

    /**
     * Determines whether the calculations are enabled for this column in the grid.
     *
     * @return true if the calculations are enabled for this column, false otherwise
     */
    boolean isCalculationEnabled();

    /**
     * Sets whether the calculations are enabled for this column in the grid.
     *
     * @param calculationEnabled true to enable the calculations, false to disable them
     */
    void setCalculationEnabled(boolean calculationEnabled);

    /**
     * Retrieves the calculation values for the grid columns.
     *
     * @return A LinkedHashMap containing the calculation values for the grid columns. The key is the name of the calculation and the value is the Total object representing the calculated
     * result.
     */
    LinkedHashMap<String, Total> getCalculationValues();

    /**
     * Sets the calculation values for the grid columns.
     *
     * @param calculationValues A LinkedHashMap containing the calculation values for the grid columns.
     *                          The key is the name of the calculation and the value is the Total object
     *                          representing the calculated result.
     */
    void setCalculationValues(LinkedHashMap<String, Total> calculationValues);
}
