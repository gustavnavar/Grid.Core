package me.agno.gridcore.filtering;

import lombok.Getter;
import me.agno.gridcore.columns.IGridColumn;

import java.util.LinkedHashMap;
import java.util.List;

public class QueryStringFilterSettings implements IGridFilterSettings {

    public static final String DEFAULT_TYPE_QUERY_PARAMETER = "grid-filter";
    private static final String FILTER_DATA_DELIMETER = "__";
    public static final String DEFAULT_CLEAR_INIT_FILTER_QUERY_PARAMETER = "grid-clearinitfilter";

    @Getter
    private final LinkedHashMap<String, List<String>> query;

    private final DefaultFilterColumnCollection filterValues = new DefaultFilterColumnCollection();


    public QueryStringFilterSettings(LinkedHashMap<String, List<String>> query) {
        
        if (query == null)
            throw new IllegalArgumentException("No http context here!");
        this.query = query;

        var filters = this.query.get(DEFAULT_TYPE_QUERY_PARAMETER);
        if (filters.size() > 0)
        {
            for (String filter : filters)
            {
                ColumnFilterValue column = CreateColumnData(filter);
                if (column.isNotNull())
                    this.filterValues.add(column);
            }
        }
    }

    private ColumnFilterValue CreateColumnData(String queryParameterValue)
    {
        if (queryParameterValue == null || queryParameterValue.trim().isEmpty())
            return ColumnFilterValue.Null();

        String[] data = queryParameterValue.split(FILTER_DATA_DELIMETER);
        if (data.length != 2 && data.length != 3)
            return ColumnFilterValue.Null();

        GridFilterType type = Enum.valueOf(GridFilterType.class, data[1]);

        if (data.length == 2)
            return new ColumnFilterValue(data[0], type, "" );
        else
            return new ColumnFilterValue(data[0], type, data[2]);
    }

    public IFilterColumnCollection getFilteredColumns() {
        return this.filterValues;
    }

    public boolean isInitState(IGridColumn column)
    {
        if(column.getInitialFilterSettings() == null || column.getInitialFilterSettings().isNull()) {
            return false;
        }
        else {
            return this.query.get(DEFAULT_CLEAR_INIT_FILTER_QUERY_PARAMETER).stream()
                    .noneMatch(r -> r.equals(column.getName()));
        }
    }
}