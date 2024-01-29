package me.agno.gridjavacore.pagination;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * PagingType is an enum class representing different types of paging options.
 *
 * - NONE: No paging is applied.
 * - PAGINATION: Paging is done by specifying the current page number and page size.
 * - VIRTUALIZATION: Paging is done by specifying the start index and virtualized count.
 */
@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum PagingType {
    NONE,
    PAGINATION,
    VIRTUALIZATION;

    /**
     * Returns a string representation of the enum constant.
     *
     * @return a string representation of the enum constant
     */
    @Override public String toString() {
        return switch (this) {
            case NONE -> "0";
            case PAGINATION -> "1";
            case VIRTUALIZATION -> "2";
            default -> null;
        };
    }

    /**
     * Converts a string representation to a corresponding {@link PagingType} enum constant.
     *
     * @param x the string representation of the enum constant
     * @return the {@link PagingType} enum constant corresponding to the string representation, or null if the string cannot be parsed
     */
    public static PagingType fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Converts an integer value to the corresponding {@link PagingType} enum constant.
     *
     * @param x the integer value representing the enum constant
     * @return the {@link PagingType} enum constant corresponding to the integer value
     */
    public static PagingType fromInteger(int x) {
        return PagingType.values()[x];
    }
}
