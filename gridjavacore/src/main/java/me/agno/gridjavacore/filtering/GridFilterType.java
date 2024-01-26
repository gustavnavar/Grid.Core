package me.agno.gridjavacore.filtering;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridFilterType {
    NONE,
    EQUALS,
    CONTAINS,
    STARTS_WITH,
    ENDS_WIDTH,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_OR_EQUALS,
    LESS_THAN_OR_EQUALS,
    CONDITION,
    NOT_EQUALS,
    IS_NULL,
    IS_NOT_NULL,
    IS_DUPLICATED,
    IS_NOT_DUPLICATED;

    @Override public String toString() {
        return switch (this) {
            case NONE -> "0";
            case EQUALS -> "1";
            case CONTAINS -> "2";
            case STARTS_WITH -> "3";
            case ENDS_WIDTH -> "4";
            case GREATER_THAN -> "5";
            case LESS_THAN -> "6";
            case GREATER_THAN_OR_EQUALS -> "7";
            case LESS_THAN_OR_EQUALS -> "8";
            case CONDITION -> "9";
            case NOT_EQUALS -> "10";
            case IS_NULL -> "11";
            case IS_NOT_NULL -> "12";
            case IS_DUPLICATED -> "13";
            case IS_NOT_DUPLICATED -> "14";
            default -> null;
        };
    }

    public static GridFilterType fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return GridFilterType.NONE;
        }
    }

    public static GridFilterType fromInteger(int x) {
        return GridFilterType.values()[x];
    }
}
