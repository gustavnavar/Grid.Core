package me.agno.gridjavacore.sorting;

import me.agno.gridjavacore.columns.IGridColumn;

import java.util.List;

public interface IOrderColumnCollection extends List<ColumnOrderValue> {

    List<ColumnOrderValue> getByColumn(IGridColumn column);
}
