package me.agno.gridcore.sorting;

import me.agno.gridcore.columns.IGridColumn;

import java.util.List;

public interface IOrderColumnCollection extends List<ColumnOrderValue> {

    List<ColumnOrderValue> GetByColumn(IGridColumn column);
}
