package me.agno.gridjavacore.filtering;

import lombok.Getter;
import me.agno.gridjavacore.columns.IGridColumn;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * The QueryStringFilterSettings class represents the settings for filtering data in a grid using query string parameters.
 * It implements the IGridFilterSettings interface.
 */
public class QueryStringFilterSettings implements IGridFilterSettings {

    /**
     * The DEFAULT_TYPE_QUERY_PARAMETER variable represents the default query parameter name for filtering data in a grid.
     * It is used to specify the type of filter to apply on the column.
     * The value is set to "grid-filter".
     *
     * This variable is used in the QueryStringFilterSettings class to retrieve the filter query and filtered columns.
     * It is also used in the DefaultFilterColumnCollection class to add and retrieve filter values by column.
     *
     * @see QueryStringFilterSettings#getQuery()
     * @see QueryStringFilterSettings#getFilteredColumns()
     * @see DefaultFilterColumnCollection#add(String, GridFilterType, String)
     * @see DefaultFilterColumnCollection#getByColumn(IGridColumn)
     */
    public static final String DEFAULT_TYPE_QUERY_PARAMETER = "grid-filter";

    /**
     * The FILTER_DATA_DELIMETER variable represents the delimiter used for filtering data in a grid.
     * It is a constant string with the value "__".
     *
     * Example usage:
     * String filterData = "column1__value1__value2";
     * String[] filterValues = filterData.split(FILTER_DATA_DELIMETER);
     */
    private static final String FILTER_DATA_DELIMETER = "__";

    /**
     * The default value for the query parameter used to clear the initial filter in a grid.
     * The value is set to "grid-clearinitfilter".
     */
    public static final String DEFAULT_CLEAR_INIT_FILTER_QUERY_PARAMETER = "grid-clearinitfilter";

    /**
     * The query variable represents a filter query used for filtering data in a grid.
     *
     * It is a LinkedHashMap, where the key is the name of the column and the value is a list of filter values applied to that column.
     */
    @Getter
    private final LinkedHashMap<String, List<String>> query;

    private final DefaultFilterColumnCollection filterValues = new DefaultFilterColumnCollection();

    /**
     * The QueryStringFilterSettings class represents the filter settings for a grid based on query string parameters.
     *
     * It takes a LinkedHashMap containing the query string parameters and initializes the filter settings accordingly.
     *
     * The filter settings are stored in the query field, which is a LinkedHashMap of string keys and list of string values.
     * The filters are extracted from the query parameter with the key DEFAULT_TYPE_QUERY_PARAMETER.
     * Each filter is parsed and converted into a ColumnFilterValue object, which is added to the filterValues list.
     */
    public QueryStringFilterSettings(LinkedHashMap<String, List<String>> query) {
        
        if (query == null)
            throw new IllegalArgumentException("No http context here!");
        this.query = query;

        var filters = this.query.get(DEFAULT_TYPE_QUERY_PARAMETER);
        if (filters != null && ! filters.isEmpty())
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

        GridFilterType type = GridFilterType.fromString(data[1]);
        if (data.length == 2)
            return new ColumnFilterValue(data[0], type, "" );
        else
            return new ColumnFilterValue(data[0], type, data[2]);
    }

    /**
     * Retrieves the filtered columns from the QueryStringFilterSettings.
     *
     * @return the filtered columns as an IFilterColumnCollection
     */
    public IFilterColumnCollection getFilteredColumns() {
        return this.filterValues;
    }

    /**
     * Checks if a column is in its initial state.
     *
     * @param column the IGridColumn to check
     * @return true if the column is in its initial state, false otherwise
     */
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