package me.agno.gridjavacore.sorting;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridSortMode {
    THREE_STATE,
    TWO_STATE;

    @Override public String toString() {
        return switch (this) {
            case THREE_STATE -> "0";
            case TWO_STATE -> "1";
            default -> null;
        };
    }

    public static GridSortMode fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static GridSortMode fromInteger(int x) {
        return GridSortMode.values()[x];
    }
}
