package me.agno.gridjavacore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * This class represents the total maximum and minimum date values.
 * It provides getter and setter methods for accessing and modifying the date values.
 */
@Getter
@Setter
public class DateTimeTotals {

    /**
     * Represents the maximum date value.
     */
    private Date max;

    /**
     * This private instance variable represents the minimum date value.
     *
     * <p>
     * The minimum date value is used in the {@link DateTimeTotals} class to represent the
     * smallest date value in a collection of dates.
     * </p>
     *
     * <p>
     * This variable is private, meaning it can only be accessed and modified within the
     * {@link DateTimeTotals} class. It is recommended to use the getter and setter methods
     * provided in the {@link DateTimeTotals} class to access or modify this variable.
     * </p>
     *
     * @see DateTimeTotals
     */
    private Date min;

    /**
     * This method calculates the total duration between two given dates and returns the result as a {@code Duration} object.
     */
    public DateTimeTotals()
    {
    }
}
