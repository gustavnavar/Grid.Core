package me.agno.gridjavacore.sorting;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Represents the sorting mode for a grid column.
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridSortMode {
    THREE_STATE,
    TWO_STATE;

    /**
     * Returns a string representation of the GridSortMode value.
     *
     * @return a string representation of the GridSortMode value
     */
    @Override public String toString() {
        return switch (this) {
            case THREE_STATE -> "0";
            case TWO_STATE -> "1";
            default -> null;
        };
    }

    /**
     * Converts a string representation of GridSortMode to its corresponding enum value.
     *
     * @param x the string representation of GridSortMode
     * @return the enum value corresponding to the given string representation, or null if the string cannot be parsed
     */
    public static GridSortMode fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Converts an integer value to the corresponding GridSortMode enum value.
     *
     * @param x the integer value
     * @return the enum value corresponding to the given integer value
     */
    public static GridSortMode fromInteger(int x) {
        return GridSortMode.values()[x];
    }
}
