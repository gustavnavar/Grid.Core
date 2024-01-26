package me.agno.gridjavacore.filtering;

import me.agno.gridjavacore.columns.IGridColumn;

import java.util.List;

public interface IFilterColumnCollection extends List<ColumnFilterValue> {

    List<ColumnFilterValue> getByColumn(IGridColumn column);
}
