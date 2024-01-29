package me.agno.gridjavacore.totals;

import lombok.Getter;
import lombok.Setter;

/**
 * StringTotals is a class that represents the totals of strings.
 * It stores the maximum and minimum strings.
 */
@Getter
@Setter
public class StringTotals {

    /**
     * The maximum string value.
     */
    private String max;

    /**
     * Represents the minimum string value.
     *
     * This variable is used to store the minimum string value in the StringTotals class.
     *
     * The minimum string value can be accessed using the getter and modified using the setter methods.
     *
     * Example usage:
     *
     *     StringTotals stringTotals = new StringTotals();
     *     stringTotals.setMin("hello");
     *     String min = stringTotals.getMin();
     *
     * Note:
     * This variable is private and can only be accessed within the StringTotals class.
     *
     * @see StringTotals
     */
    private String min;

    /**
     * The StringTotals class represents the totals of strings.
     * It stores the maximum and minimum strings.
     */
    public StringTotals()
    {
    }
}
