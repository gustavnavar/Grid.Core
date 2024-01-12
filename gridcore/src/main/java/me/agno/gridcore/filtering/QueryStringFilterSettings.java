package me.agno.gridcore.filtering;

import lombok.Getter;
import me.agno.gridcore.columns.IGridColumn;

import java.util.LinkedHashMap;
import java.util.List;

public class QueryStringFilterSettings implements IGridFilterSettings {

    public static final String DefaultTypeQueryParameter = "grid-filter";
    private static final String FilterDataDelimeter = "__";
    public static final String DefaultClearInitFilterQueryParameter = "grid-clearinitfilter";

    @Getter
    private final LinkedHashMap<String, List<String>> Query;

    private final DefaultFilterColumnCollection _filterValues = new DefaultFilterColumnCollection();


    public QueryStringFilterSettings(LinkedHashMap<String, List<String>> query) {
        
        if (query == null)
            throw new IllegalArgumentException("No http context here!");
        Query = query;

        var filters = Query.get(DefaultTypeQueryParameter);
        if (filters.size() > 0)
        {
            for (String filter : filters)
            {
                ColumnFilterValue column = CreateColumnData(filter);
                if (column.isNotNull())
                    _filterValues.add(column);
            }
        }
    }

    private ColumnFilterValue CreateColumnData(String queryParameterValue)
    {
        if (queryParameterValue == null || queryParameterValue.trim().isEmpty())
            return ColumnFilterValue.Null();

        String[] data = queryParameterValue.split(FilterDataDelimeter);
        if (data.length != 2 && data.length != 3)
            return ColumnFilterValue.Null();

        GridFilterType type = Enum.valueOf(GridFilterType.class, data[1]);

        if (data.length == 2)
            return new ColumnFilterValue(data[0], type, "" );
        else
            return new ColumnFilterValue(data[0], type, data[2]);
    }

    public IFilterColumnCollection getFilteredColumns() {
        return _filterValues;
    }

    public boolean IsInitState(IGridColumn column)
    {
        if(column.getInitialFilterSettings() == null || column.getInitialFilterSettings().isNull()) {
            return false;
        }
        else {
            return Query.get(DefaultClearInitFilterQueryParameter).stream().noneMatch(r -> r.equals(column.getName()));
        }
    }
}