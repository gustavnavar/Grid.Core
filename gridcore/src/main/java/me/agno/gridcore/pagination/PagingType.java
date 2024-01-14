package me.agno.gridcore.pagination;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
public enum PagingType {
    NONE,
    PAGINATION,
    VIRTUALIZATION
}
