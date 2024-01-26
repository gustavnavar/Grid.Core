package me.agno.gridjavacore.filtering;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridFilterCondition {
    NONE,
    AND,
    OR;

    @Override public String toString() {
        return switch (this) {
            case NONE -> "0";
            case AND -> "1";
            case OR -> "2";
            default -> null;
        };
    }

    public static GridFilterCondition fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return GridFilterCondition.NONE;
        }
    }

    public static GridFilterCondition fromInteger(int x) {
        return GridFilterCondition.values()[x];
    }
}
