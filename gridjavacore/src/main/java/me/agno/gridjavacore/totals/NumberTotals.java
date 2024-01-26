package me.agno.gridjavacore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class NumberTotals {

    private Optional<Double> sum;
    private Optional<Double> average;
    private Optional<Double> max;
    private Optional<Double> min;

    public NumberTotals()
    {
    }
}
