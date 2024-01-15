package me.agno.gridcore.sorting;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class ColumnOrderValue {

    public static final String DEFAULT_SORTING_QUERY_PARAMETER = "grid-sorting";
    public static final String SORTING_DATA_DELIMETER = "__";

    private String columnName;

    private GridSortDirection direction;

    private int id;

    public ColumnOrderValue(String name, GridSortDirection direction, int id)
    {
        this.columnName = name;
        this.direction = direction;
        this.id = id;
    }

    @Override
    public String toString()
    {
        return this.columnName + SORTING_DATA_DELIMETER + this.direction.toString() + SORTING_DATA_DELIMETER
                + Integer.valueOf(this.id).toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof ColumnOrderValue other)) return false;
        if (!other.canEqual((Object)this)) return false;
        return Objects.equals(this.columnName, other.getColumnName()) && this.direction == other.getDirection()
                && this.id == other.getId();
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result*PRIME) + (this.columnName == null ? 43 : this.columnName.hashCode());
        result = (result*PRIME) + this.direction.hashCode();
        result = (result*PRIME) + Integer.valueOf(this.id).hashCode();
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

        String[] data = queryParameterValue.split(ColumnOrderValue.SORTING_DATA_DELIMETER);
        if (data.length != 3)
            return ColumnOrderValue.Null();

        return new ColumnOrderValue(data[0], GridSortDirection.fromString(data[1]), Integer.parseInt(data[2]));
    }
}
