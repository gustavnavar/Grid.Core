package me.agno.gridjavacore.pagination;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum PagingType {
    NONE,
    PAGINATION,
    VIRTUALIZATION;

    @Override public String toString() {
        return switch (this) {
            case NONE -> "0";
            case PAGINATION -> "1";
            case VIRTUALIZATION -> "2";
            default -> null;
        };
    }

    public static PagingType fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static PagingType fromInteger(int x) {
        return PagingType.values()[x];
    }
}
