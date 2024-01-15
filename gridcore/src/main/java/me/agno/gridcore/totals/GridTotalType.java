package me.agno.gridcore.totals;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum GridTotalType {
    NONE,
    NUMBER,
    DATE_TIME,
    STRING;

    @Override public String toString() {
        return switch (this) {
            case NONE -> "0";
            case NUMBER -> "1";
            case DATE_TIME -> "2";
            case STRING -> "3";
            default -> null;
        };
    }
}
