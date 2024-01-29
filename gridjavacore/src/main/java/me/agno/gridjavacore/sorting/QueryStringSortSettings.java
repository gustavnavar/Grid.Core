package me.agno.gridjavacore.sorting;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * The QueryStringSortSettings class represents the settings for sorting a grid using query string parameters.
 * It implements the IGridSortSettings interface.
 */
@Getter
public class QueryStringSortSettings implements IGridSortSettings {

    /**
     * Represents the default direction query parameter for sorting a grid.
     * This variable is used in the QueryStringSortSettings class.
     */
    public static final String DEFAULT_DIRECTION_QUERY_PARAMETER = "grid-dir";

    /**
     * The {@code DEFAULT_COLUMN_QUERY_PARAMETER} constant represents the default query parameter for sorting a grid by column.
     * The value of this constant is "grid-column".
     */
    public static final String DEFAULT_COLUMN_QUERY_PARAMETER = "grid-column";

    /**
     * The column query parameter name for sorting a grid.
     */
    private String columnQueryParameterName;

    /**
     * The directionQueryParameterName represents the name of the query parameter used to specify the sorting direction in a grid.
     * It is a private instance variable in the QueryStringSortSettings class.
     *
     * Example usage:
     * QueryStringSortSettings sortSettings = new QueryStringSortSettings(query);
     * sortSettings.setDirectionQueryParameterName("direction");
     * String directionQueryParameterName = sortSettings.getDirectionQueryParameterName();
     *
     * Note: This variable is part of the IGridSortSettings interface, and is used to retrieve and set the direction of sorting in a grid.
     */
    private String directionQueryParameterName;

    /**
     * Represents the sort values for grid sorting.
     */
    private final DefaultOrderColumnCollection sortValues = new DefaultOrderColumnCollection();

    /**
     * Represents the query parameters for sorting a grid.
     *
     * The keys of the LinkedHashMap represent the column names,
     * and the values are Lists of query parameter values.
     */
    private LinkedHashMap<String, List<String>> query;

    /**
     * Represents the name of the column associated with an IGridSortSettings object.
     */
    @Setter
    private String columnName;

    /**
     * The `direction` variable represents the sorting direction in a grid.
     * It is of type `GridSortDirection` enum which has two possible values: ASCENDING and DESCENDING.
     */
    @Setter
    private GridSortDirection direction;

    /**
     * Represents the sorting settings for a query string.
     * The sorting settings include the column and direction query parameter names, as well as the sort values.
     */
    public QueryStringSortSettings(LinkedHashMap<String, List<String>> query) {

        if (query == null)
            throw new IllegalArgumentException("No http context here!");

        this.query = query;
        setColumnQueryParameterName(DEFAULT_COLUMN_QUERY_PARAMETER);
        setDirectionQueryParameterName(DEFAULT_DIRECTION_QUERY_PARAMETER);

        var sortings = query.get(ColumnOrderValue.DEFAULT_SORTING_QUERY_PARAMETER);
        if (sortings != null && !sortings.isEmpty()) {
            for (String sorting : sortings) {
                ColumnOrderValue column = ColumnOrderValue.CreateColumnData(sorting);
                if (!column.equals(ColumnOrderValue.Null()))
                    this.sortValues.add(column);
            }
        }
    }

    /**
     * Sets the name of the column query parameter.
     *
     * @param value the value to set the column query parameter name to
     */
    public void setColumnQueryParameterName(String value) {
        this.columnQueryParameterName = value;
        refreshColumn();
    }

    /**
     * Sets the name of the direction query parameter.
     *
     * @param value the value to set the direction query parameter name (must not be null)
     */
    public void setDirectionQueryParameterName(String value) {
        this.directionQueryParameterName = value;
        refreshDirection();
    }

    private void refreshColumn() {

        String currentSortColumn;
        var currentSortColumns = this.query.get(this.columnQueryParameterName);
        if(currentSortColumns == null ||currentSortColumns.isEmpty())
            currentSortColumn = "";
        else {
            currentSortColumn = currentSortColumns.get(0).toString();
            if (currentSortColumn == null || currentSortColumn.trim().isEmpty())
                currentSortColumn = "";
        }

        this.columnName = currentSortColumn;
        if (currentSortColumn.trim().isEmpty()) {
            this.direction = GridSortDirection.ASCENDING;
        }
    }

    private void refreshDirection()
    {
        String currentDirection;
        var currentDirections= this.query.get(this.directionQueryParameterName);
        if(currentDirections == null || currentDirections.isEmpty())
            currentDirection = "";
        else {
            currentDirection = currentDirections.get(0).toString();
            if (currentDirection == null || currentDirection.trim().isEmpty())
                currentDirection = "";
        }

        if (currentDirection.trim().isEmpty()) {
            this.direction = GridSortDirection.ASCENDING;
            return;
        }

        this.direction = GridSortDirection.fromString(currentDirection);
    }
}
