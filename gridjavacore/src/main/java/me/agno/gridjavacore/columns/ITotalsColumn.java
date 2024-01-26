package me.agno.gridjavacore.columns;

import me.agno.gridjavacore.IGridColumnCollection;
import me.agno.gridjavacore.totals.IColumnTotals;
import me.agno.gridjavacore.totals.Total;

import java.util.LinkedHashMap;
import java.util.function.Function;

public interface ITotalsColumn<T> {

    IColumnTotals getTotals();

    LinkedHashMap<String, Function<IGridColumnCollection<T>, Object>> getCalculations();

    boolean isSumEnabled();

    void setSumEnabled(boolean sumEnabled);

    Total getSumValue();

    void setSumValue(Total sumValue);

    boolean isAverageEnabled();

    void setAverageEnabled(boolean averageEnabled);

    Total getAverageValue();

    void setAverageValue(Total averageValue);

    boolean isMaxEnabled();

    void setMaxEnabled(boolean maxEnabled);

    Total getMaxValue();

    void setMaxValue(Total maxValue);

    boolean isMinEnabled();

    void setMinEnabled(boolean minEnabled);

    Total getMinValue();

    void setMinValue(Total minValue);

    boolean isCalculationEnabled();

    void setCalculationEnabled(boolean calculationEnabled);

    LinkedHashMap<String, Total> getCalculationValues();

    void setCalculationValues(LinkedHashMap<String, Total> calculationValues);
}
