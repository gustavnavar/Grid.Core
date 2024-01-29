package me.agno.gridjavacore.filtering;

import lombok.Data;

/**
 * Represents a filter with a type and value.
 */
@Data
public class Filter {

    /**
     * A private string variable that represents the type of a filter.
     */
    private String type;

    /**
     * Represents the value of a filter.
     */
    private String value;

    /**
     * Represents a filter with a type and value.
     * Used to filter items based on specific criteria.
     */
    public Filter(String type, String value) {
        this.type = type;
        this.value = value;
    }
}
