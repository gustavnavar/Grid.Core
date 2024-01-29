package me.agno.gridjavacore;

/**
 * The IGridOptions interface represents the options available for a grid.
 * It provides methods to check if different calculations are enabled for the grid.
 * The calculations include sum, average, maximum, minimum, and custom calculations.
 */
public interface IGridOptions {

    /**
     * Checks if the sum is enabled for any column in the grid.
     *
     * @return true if the sum is enabled for any column, false otherwise
     */
    boolean isSumEnabled();

    /**
     * Determines whether the average is enabled for any column in the grid.
     *
     * @return true if the average is enabled for any column, false otherwise
     */
    boolean isAverageEnabled();

    /**
     * Checks if the maximum value calculation is enabled for any column in the grid.
     *
     * @return true if the maximum value calculation is enabled for any column, false otherwise
     */
    boolean isMaxEnabled();

    /**
     * Checks if the minimum value calculation is enabled for any column in the grid.
     *
     * @return true if the minimum value calculation is enabled for any column, false otherwise
     */
    boolean isMinEnabled();

    /**
     * Checks if the calculation is enabled for any column in the grid.
     *
     * @return true if the calculation is enabled for any column, false otherwise
     */
    boolean isCalculationEnabled();
}
