package me.agno.gridcore.filtering;

import me.agno.gridcore.columns.IGridColumn;

import java.util.LinkedHashMap;
import java.util.List;

public interface IGridFilterSettings {

    LinkedHashMap<String, List<String>> getQuery();

    IFilterColumnCollection getFilteredColumns();

    boolean IsInitState(IGridColumn column);
}
