package me.agno.gridcore.filtering;

import me.agno.gridcore.columns.IGridColumn;

import java.util.List;

public interface IFilterColumnCollection extends List<ColumnFilterValue> {

    List<ColumnFilterValue> GetByColumn(IGridColumn column);
}
