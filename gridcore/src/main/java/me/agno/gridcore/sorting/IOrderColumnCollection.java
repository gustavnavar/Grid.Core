package me.agno.gridcore.sorting;

import me.agno.gridcore.columns.IGridColumn;

import java.util.Collection;

public interface IOrderColumnCollection extends Collection<ColumnOrderValue> {

    Collection<ColumnOrderValue> GetByColumn(IGridColumn column);
}
