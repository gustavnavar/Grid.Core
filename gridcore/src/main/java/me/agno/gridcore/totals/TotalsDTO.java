package me.agno.gridcore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class TotalsDTO {

    private Map<String, Total> sum;
    private Map<String, Total> average;
    private Map<String, Total> max;
    private Map<String, Total> min;
    private Map<String, LinkedHashMap<String, Total>> calculations;

    public TotalsDTO()
    {
        this.sum = new HashMap<String, Total>();
        this.average = new HashMap<String, Total>();
        this.max = new HashMap<String, Total>();
        this.min = new HashMap<String, Total>();
        this.calculations = new HashMap<String, LinkedHashMap<String, Total>>();
    }

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
