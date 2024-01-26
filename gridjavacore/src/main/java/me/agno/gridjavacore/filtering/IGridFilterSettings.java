package me.agno.gridjavacore.filtering;

import me.agno.gridjavacore.columns.IGridColumn;

import java.util.LinkedHashMap;
import java.util.List;

public interface IGridFilterSettings {

    LinkedHashMap<String, List<String>> getQuery();

    IFilterColumnCollection getFilteredColumns();

    boolean isInitState(IGridColumn column);
}
