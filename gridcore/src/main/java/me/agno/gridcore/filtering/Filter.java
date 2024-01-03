package me.agno.gridcore.filtering;

import lombok.Data;

@Data
public class Filter {

    private String Type;

    private String Value;

    public Filter(String type, String value) {
        Type = type;
        Value = value;
    }
}
