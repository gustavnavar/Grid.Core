package me.agno.gridjavacore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a data transfer object (DTO) that holds the sum, average, max, min, and calculation totals for a grid.
 */
@Getter
@Setter
public class TotalsDTO {

    /**
     * Represents a map that holds the sum values for a grid.
     * The keys of the map are strings that represent the columns of the grid,
     * and the values are Total objects that represent the sum of the values in each column.
     */
    private Map<String, Total> sum;

    /**
     * Represents a map that holds the average values for a grid.
     * The keys of the map are strings that represent the columns of the grid,
     * and the values are Total objects that represent the average of the values in each column.
     */
    private Map<String, Total> average;

    /**
     * Represents a map that holds the maximum values for a grid.
     * The keys of the map are strings that represent the columns of the grid,
     * and the values are Total objects that represent the maximum of the values in each column.
     */
    private Map<String, Total> max;

    /**
     * Represents a map that holds the minimum values for a grid.
     * The keys of the map are strings that represent the columns of the grid,
     * and the values are Total objects that represent the minimum of the values in each column.
     */
    private Map<String, Total> min;

    /**
     * Represents a map that holds the calculation details for each column in a grid.
     * The keys of the outer map are strings that represent the columns of the grid.
     * The values of the outer map are LinkedHashMap objects that hold the calculation details for each row in the column.
     * The keys of the inner map are strings that represent the rows of the grid.
     * The values of the inner map are Total objects that represent the calculation details.
     */
    private Map<String, LinkedHashMap<String, Total>> calculations;

    /**
     * Represents a data transfer object (DTO) that holds the sum, average, max, min, and calculation totals for a grid.
     */
    public TotalsDTO()
    {
        this.sum = new HashMap<String, Total>();
        this.average = new HashMap<String, Total>();
        this.max = new HashMap<String, Total>();
        this.min = new HashMap<String, Total>();
        this.calculations = new HashMap<String, LinkedHashMap<String, Total>>();
    }

    /**
     * Represents a data transfer object (DTO) that holds the sum, average, max, min, and calculation totals for a grid.
     *
     * @param sum          a map that holds the sum values for a grid.
     *                     The keys of the map are strings that represent the columns of the grid,
     *                     and the values are Total objects that represent the sum of the values in each column.
     * @param average      a map that holds the average values for a grid.
     *                     The keys of the map are strings that represent the columns of the grid,
     *                     and the values are Total objects that represent the average of the values in each column.
     * @param max          a map that holds the maximum values for a grid.
     *                     The keys of the map are strings that represent the columns of the grid,
     *                     and the values are Total objects that represent the maximum of the values in each column.
     * @param min          a map that holds the minimum values for a grid.
     *                     The keys of the map are strings that represent the columns of the grid,
     *                     and the values are Total objects that represent the minimum of the values in each column.
     * @param calculations a map that holds the calculation details for each column in a grid.
     *                     The keys of the outer map are strings that represent the columns of the grid.
     *                     The values of the outer map are LinkedHashMap objects that hold the calculation details for each row in the column.
     *                     The keys of the inner map are strings that represent the rows of the grid.
     *                     The values of the inner map are Total objects that represent the calculation details.
     */
    public TotalsDTO(Map<String, Total> sum, Map<String, Total> average, Map<String, Total> max,
                     Map<String, Total> min, Map<String, LinkedHashMap<String, Total>> calculations)
    {
        this.sum = sum;
        this.average = average;
        this.max = max;
        this.min = min;
        this.calculations = calculations;
    }
}
