package me.agno.gridjavacore.sorting;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * The `GridSortDirection` enum represents the direction of sorting in a grid.
 * It has two possible values: ASCENDING and DESCENDING.
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridSortDirection {
    ASCENDING,
    DESCENDING;

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object. Returns "0" if the value of this object is ASCENDING, "1" if the value is DESCENDING, and null if the value is neither ASCENDING
     * nor DESCENDING.
     */
    @Override public String toString() {
        return switch (this) {
            case ASCENDING -> "0";
            case DESCENDING -> "1";
            default -> null;
        };
    }

    /**
     * Parses the given string and returns the corresponding GridSortDirection value.
     * If the string cannot be parsed into an integer, the method returns GridSortDirection.ASCENDING.
     *
     * @param x the string representation to parse
     * @return the corresponding GridSortDirection value
     */
    public static GridSortDirection fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return GridSortDirection.ASCENDING;
        }
    }

    /**
     * Converts an integer value to a GridSortDirection enumeration value.
     *
     * @param x the integer value to convert
     * @return the GridSortDirection enumeration value corresponding to the given integer value
     */
    public static GridSortDirection fromInteger(int x) {
        return GridSortDirection.values()[x];
    }
}
