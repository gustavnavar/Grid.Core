package me.agno.gridcore.sorting;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class ColumnOrderValue {
    public static final String DefaultSortingQueryParameter = "grid-sorting";
    public static final String SortingDataDelimeter = "__";

    private String ColumnName;

    private GridSortDirection Direction;

    private int Id;

    public ColumnOrderValue(String name, GridSortDirection direction, int id)
    {
        ColumnName = name;
        Direction = direction;
        Id = id;
    }

    @Override
    public String toString()
    {
        return ColumnName + SortingDataDelimeter + Direction.toString() + SortingDataDelimeter + Integer.valueOf(Id).toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof ColumnOrderValue other)) return false;
        if (!other.canEqual((Object)this)) return false;
        return Objects.equals(ColumnName, other.ColumnName) && Direction == other.Direction && Id == other.Id;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result*PRIME) + (ColumnName == null ? 43 : ColumnName.hashCode());
        result = (result*PRIME) + Direction.hashCode();
        result = (result*PRIME) + Integer.valueOf(Id).hashCode();
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ColumnOrderValue;
    }

    public static ColumnOrderValue Null() {
        return new ColumnOrderValue(null, null, 0);
    }

    public static ColumnOrderValue CreateColumnData(String queryParameterValue)
    {
        if (queryParameterValue == null || queryParameterValue.trim().isEmpty())
            return ColumnOrderValue.Null();

        String[] data = queryParameterValue.split(ColumnOrderValue.SortingDataDelimeter);
        if (data.length != 3)
            return ColumnOrderValue.Null();

        return new ColumnOrderValue(data[0], GridSortDirection.valueOf(data[1]), Integer.parseInt(data[2]));
    }
}
