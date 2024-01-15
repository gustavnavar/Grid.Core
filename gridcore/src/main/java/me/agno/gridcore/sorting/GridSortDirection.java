package me.agno.gridcore.sorting;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridSortDirection {
    ASCENDING,
    DESCENDING;

    @Override public String toString() {
        return switch (this) {
            case ASCENDING -> "0";
            case DESCENDING -> "1";
            default -> null;
        };
    }

    public static GridSortDirection fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return GridSortDirection.ASCENDING;
        }
    }

    public static GridSortDirection fromInteger(int x) {
        return GridSortDirection.values()[x];
    }
}
