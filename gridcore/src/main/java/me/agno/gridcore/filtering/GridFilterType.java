package me.agno.gridcore.filtering;

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
    IS_NOT_NULL
}
