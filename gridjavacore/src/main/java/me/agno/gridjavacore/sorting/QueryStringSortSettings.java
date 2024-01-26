package me.agno.gridjavacore.sorting;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class QueryStringSortSettings implements IGridSortSettings {
    
    public static final String DEFAULT_DIRECTION_QUERY_PARAMETER = "grid-dir";
    public static final String DEFAULT_COLUMN_QUERY_PARAMETER = "grid-column";

    private String columnQueryParameterName;

    private String directionQueryParameterName;

    private final DefaultOrderColumnCollection sortValues = new DefaultOrderColumnCollection();

    private LinkedHashMap<String, List<String>> query;

    @Setter
    private String columnName;

    @Setter
    private GridSortDirection direction;

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

    public void setColumnQueryParameterName(String value) {
        this.columnQueryParameterName = value;
        refreshColumn();
    }

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
