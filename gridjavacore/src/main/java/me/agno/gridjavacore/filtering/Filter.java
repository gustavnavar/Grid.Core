package me.agno.gridjavacore.filtering;

import lombok.Data;

@Data
public class Filter {

    private String type;

    private String value;

    public Filter(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
