package me.agno.gridcore.filtering;

import me.agno.gridcore.columns.IGridColumn;

import java.util.Collection;

public interface IFilterColumnCollection extends Collection<ColumnFilterValue> {

    Collection<ColumnFilterValue> GetByColumn(IGridColumn column);
}
