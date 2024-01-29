package me.agno.gridjavacore.totals;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Represents the types of grid total.
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridTotalType {
    NONE,
    NUMBER,
    DATE_TIME,
    STRING;

    /**
     * Returns a string representation of the GridTotalType.
     *
     * @return the string representation of the GridTotalType
     */
    @Override public String toString() {
        return switch (this) {
            case NONE -> "0";
            case NUMBER -> "1";
            case DATE_TIME -> "2";
            case STRING -> "3";
            default -> null;
        };
    }

    /**
     * Converts a string representation to the corresponding GridTotalType enum value.
     *
     * @param x the string representation of the GridTotalType
     * @return the GridTotalType enum value
     */
    public static GridTotalType fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Converts an integer value to the corresponding GridTotalType enum value.
     *
     * @param x the integer value representing the GridTotalType
     * @return the GridTotalType enum value
     */
    public static GridTotalType fromInteger(int x) {
        return GridTotalType.values()[x];
    }
}
