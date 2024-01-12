package me.agno.gridcore.sorting;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class QueryStringSortSettings implements IGridSortSettings {
    
    public static final String DefaultDirectionQueryParameter = "grid-dir";
    public static final String DefaultColumnQueryParameter = "grid-column";

    private String ColumnQueryParameterName;

    private String DirectionQueryParameterName;

    private final DefaultOrderColumnCollection SortValues = new DefaultOrderColumnCollection();

    private LinkedHashMap<String, List<String>> Query;

    @Setter
    private String ColumnName;

    @Setter
    private GridSortDirection Direction;

    public QueryStringSortSettings(LinkedHashMap<String, List<String>> query) {

        if (query == null)
            throw new IllegalArgumentException("No http context here!");

        Query = query;
        ColumnQueryParameterName = DefaultColumnQueryParameter;
        DirectionQueryParameterName = DefaultDirectionQueryParameter;

        var sortings = query.get(ColumnOrderValue.DefaultSortingQueryParameter);
        if (!sortings.isEmpty()) {
            for (String sorting : sortings) {
                ColumnOrderValue column = ColumnOrderValue.CreateColumnData(sorting);
                if (!column.equals(ColumnOrderValue.Null()))
                    SortValues.add(column);
            }
        }
    }

    public void setColumnQueryParameterName(String value) {
        ColumnQueryParameterName = value;
        RefreshColumn();
    }

    public void setDirectionQueryParameterName(String value) {
        DirectionQueryParameterName = value;
        RefreshDirection();
    }


    private void RefreshColumn()
    {
        //Columns
        String currentSortColumn = Query.get(ColumnQueryParameterName).toString();
        if(currentSortColumn == null  || currentSortColumn.trim().isEmpty())
            currentSortColumn = "";

        ColumnName = currentSortColumn;
        if (currentSortColumn.trim().isEmpty()) {
            Direction = GridSortDirection.Ascending;
        }
    }

    private void RefreshDirection()
    {
        //Direction
        String currentDirection = Query.get(DirectionQueryParameterName).toString();
        if(currentDirection == null  || currentDirection.trim().isEmpty())
            currentDirection = "";

        if (currentDirection.trim().isEmpty()) {
            Direction = GridSortDirection.Ascending;
            return;
        }

        Direction = Enum.valueOf(GridSortDirection.class, currentDirection);
    }
}
