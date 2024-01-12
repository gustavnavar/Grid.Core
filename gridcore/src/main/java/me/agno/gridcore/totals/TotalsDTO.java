package me.agno.gridcore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class TotalsDTO {

    private Map<String, Total> Sum;
    private Map<String, Total> Average;
    private Map<String, Total> Max;
    private Map<String, Total> Min;
    private Map<String, LinkedHashMap<String, Total>> Calculations;

    public TotalsDTO()
    {
        Sum = new HashMap<String, Total>();
        Average = new HashMap<String, Total>();
        Max = new HashMap<String, Total>();
        Min = new HashMap<String, Total>();
        Calculations = new HashMap<String, LinkedHashMap<String, Total>>();
    }

    public TotalsDTO(Map<String, Total> sum, Map<String, Total> average, Map<String, Total> max,
                     Map<String, Total> min, Map<String, LinkedHashMap<String, Total>> calculations)
    {
        Sum = sum;
        Average = average;
        Max = max;
        Min = min;
        Calculations = calculations;
    }
}
