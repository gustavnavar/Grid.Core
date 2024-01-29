package me.agno.gridjavacore.filtering;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Represents the condition for filtering in a grid.
 *
 * This enumeration includes the following conditions:
 * - NONE: No condition applied.
 * - AND: Apply conditions using logical AND.
 * - OR: Apply conditions using logical OR.
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridFilterCondition {
    NONE,
    AND,
    OR;

    /**
     * Converts the GridFilterCondition enum value to a string representation.
     *
     * @return The string representation of the GridFilterCondition enum value.
     *         Returns "0" for NONE, "1" for AND, "2" for OR, and null for any other value.
     */
    @Override public String toString() {
        return switch (this) {
            case NONE -> "0";
            case AND -> "1";
            case OR -> "2";
            default -> null;
        };
    }

    /**
     * Converts a string representation of GridFilterCondition to GridFilterCondition enum.
     *
     * @param x The string representation of GridFilterCondition.
     * @return The corresponding GridFilterCondition enum value. If the string cannot be parsed, returns GridFilterCondition.NONE.
     */
    public static GridFilterCondition fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return GridFilterCondition.NONE;
        }
    }

    /**
     * Converts an integer value to a GridFilterCondition enum value.
     *
     * This method takes an integer value and returns the corresponding GridFilterCondition enum value.
     * If the integer value is out of range, an IndexOutOfBoundsException will be thrown.
     *
     * @param x The integer value to be converted to a GridFilterCondition enum value.
     * @return The GridFilterCondition enum value corresponding to the given integer value.
     * @throws IndexOutOfBoundsException if the integer value is out of range.
     */
    public static GridFilterCondition fromInteger(int x) {
        return GridFilterCondition.values()[x];
    }
}
