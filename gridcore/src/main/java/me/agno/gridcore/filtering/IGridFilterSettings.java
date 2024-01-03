package me.agno.gridcore.filtering;

import me.agno.gridcore.columns.IGridColumn;
import me.agno.gridcore.utils.IQueryDictionary;

public interface IGridFilterSettings {

    IQueryDictionary<String[]> getQuery();

    IFilterColumnCollection getFilteredColumns();

    boolean IsInitState(IGridColumn column);
}
