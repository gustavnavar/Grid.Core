package me.agno.gridcore.filtering;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridFilterCondition {
    NONE,
    AND,
    OR;

    public static GridFilterCondition fromString(String x) {
        try {
            return fromInteger(Integer.parseInt(x));
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static GridFilterCondition fromInteger(int x) {
        return GridFilterCondition.values()[x];
    }
}
