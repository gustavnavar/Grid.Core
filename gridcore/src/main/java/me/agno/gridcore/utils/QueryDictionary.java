package me.agno.gridcore.utils;

import me.agno.gridcore.sorting.ColumnOrderValue;
import me.agno.gridcore.sorting.GridSortDirection;

import java.util.LinkedHashMap;

public class QueryDictionary<T> extends LinkedHashMap<String, T> implements IQueryDictionary<T> {

    public void AddParameter(String parameterName, T parameterValue) {
        if (parameterName == null || parameterName.trim() == "")
            throw new IllegalArgumentException("parameterName");

        this.AddOrSet(parameterName, parameterValue);
    }

    public void AddOrSet(String key, T value) {
        if (value == null)
            throw new IllegalArgumentException("key");

        if (this.containsKey(key))
            this.replace(key, value);
        else
            this.put(key, value);
    }

    public static ColumnOrderValue CreateColumnData(String queryParameterValue)
    {
        if (queryParameterValue == null ||queryParameterValue.trim() == "")
            return null;

        String[] data = queryParameterValue.split(ColumnOrderValue.SortingDataDelimeter);
        if (data.length != 3)
            return null;

        return new ColumnOrderValue(data[0], GridSortDirection.valueOf(data[1]), Integer.valueOf(data[2]));
    }
}
