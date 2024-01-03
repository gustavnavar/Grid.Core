package me.agno.gridcore.sorting;

import lombok.Getter;
import lombok.Setter;

public class ColumnOrderValue {
    public static final String DefaultSortingQueryParameter = "grid-sorting";
    public static final String SortingDataDelimeter = "__";

    @Getter
    @Setter
    public String ColumnName;

    @Getter
    @Setter
    public GridSortDirection Direction;

    @Getter
    @Setter
    public int Id;

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
        if (!(o instanceof ColumnOrderValue)) return false;
        ColumnOrderValue other = (ColumnOrderValue) o;
        if (!other.canEqual((Object)this)) return false;
        return  ColumnName == other.ColumnName && Direction == other.Direction && Id == other.Id;
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
}
