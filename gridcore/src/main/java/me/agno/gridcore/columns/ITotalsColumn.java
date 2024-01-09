package me.agno.gridcore.columns;

import me.agno.gridcore.IGridColumnCollection;
import me.agno.gridcore.totals.IColumnTotals;
import me.agno.gridcore.totals.Total;
import me.agno.gridcore.utils.QueryDictionary;

import java.util.function.Function;

public interface ITotalsColumn<T> {

    IColumnTotals getTotals();

    QueryDictionary<Function<IGridColumnCollection<T>, Object>> getCalculations();

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

    QueryDictionary<Total> getCalculationValues();

    void setCalculationValues(QueryDictionary<Total> calculationValues);
}
