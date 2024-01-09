package me.agno.gridcore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class NumberTotals {

    private Optional<Double> Sum;
    private Optional<Double> Average;
    private Optional<Double> Max;
    private Optional<Double> Min;

    public NumberTotals()
    {
    }
}
