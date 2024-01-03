package me.agno.gridcore.utils;

import java.util.Map;

public interface IQueryDictionary<T> extends Map<String, T> {
    void AddParameter(String parameterName, T parameterValue);
}
