package me.agno.gridjavacore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/**
 * The NumberTotals class represents the summary statistics of a collection of numbers. It provides the total sum, average,
 * maximum value, and minimum value.
 */
@Getter
@Setter
public class NumberTotals {

    /**
     * Represents the sum of a collection of numbers.
     * The sum can be accessed using the optional type, which allows for the possibility of the sum being null.
     */
    private Optional<Double> sum;

    /**
     * Represents the average value of a collection of numbers.
     * The average can be accessed using the optional type, which allows for the possibility of the average being null.
     *
     * @see NumberTotals
     */
    private Optional<Double> average;

    /**
     * Represents the maximum value of a collection of numbers.
     * The maximum value can be accessed using the optional type, which allows for the possibility of the maximum value being null.
     *
     * @see NumberTotals
     */
    private Optional<Double> max;

    /**
     * Represents the minimum value of a collection of numbers.
     * The minimum value can be accessed using the optional type, which allows for the possibility of the minimum value being null.
     *
     * @see NumberTotals
     */
    private Optional<Double> min;

    /**
     * Initializes a new instance of the {@code NumberTotals} class.
     */
    public NumberTotals()
    {
    }
}
