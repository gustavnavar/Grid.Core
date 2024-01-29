package me.agno.gridjavacore.sorting;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Represents a value used for sorting a column in a grid.
 */
@Setter
@Getter
public class ColumnOrderValue {

    /**
     * Represents the default sorting query parameter for a grid column.
     */
    public static final String DEFAULT_SORTING_QUERY_PARAMETER = "grid-sorting";

    /**
     * Represents a value used for sorting a column in a grid.
     */
    public static final String SORTING_DATA_DELIMETER = "__";

    /**
     * Represents the name of a column used for sorting in a grid.
     */
    private String columnName;

    /**
     * Represents the sorting direction for a grid column.
     *
     * The GridSortDirection enumeration is used in conjunction with the ColumnOrderValue class to represent
     * the direction in which a grid column should be sorted.
     */
    private GridSortDirection direction;

    /**
     * Represents the identifier of a ColumnOrderValue object.
     *
     * The id is used to uniquely identify a ColumnOrderValue instance within a grid.
     * It is an integer value and it is private, meaning it can only be accessed within the class it belongs to.
     */
    private int id;

    /**
     * Creates a new instance of ColumnOrderValue with the given parameters.
     *
     * @param name      the name of the column
     * @param direction the sorting direction for the column
     * @param id        the ID of the column
     */
    public ColumnOrderValue(String name, GridSortDirection direction, int id)
    {
        this.columnName = name;
        this.direction = direction;
        this.id = id;
    }

    /**
     * Returns a string representation of the object. The string representation consists of the column name, sorting direction, and ID, separated by the sorting data delimiter.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString()
    {
        return this.columnName + SORTING_DATA_DELIMETER + this.direction.toString() + SORTING_DATA_DELIMETER
                + Integer.valueOf(this.id).toString();
    }

    /**
     * Compares the given object to this ColumnOrderValue for equality.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof ColumnOrderValue other)) return false;
        if (!other.canEqual((Object)this)) return false;
        return Objects.equals(this.columnName, other.getColumnName()) && this.direction == other.getDirection()
                && this.id == other.getId();
    }

    /**
     * Calculates the hash code value for this ColumnOrderValue object.
     * The hash code is calculated based on the column name, sorting direction, and ID.
     *
     * @return the hash code value for this ColumnOrderValue object
     */
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

    /**
     * Returns a new instance of the {@link ColumnOrderValue} class with null values.
     *
     * @return a new instance of the {@link ColumnOrderValue} class with null values
     */
    public static ColumnOrderValue Null() {
        return new ColumnOrderValue(null, null, 0);
    }

    /**
     * Creates a new instance of ColumnOrderValue with the given query parameter value.
     *
     * @param queryParameterValue the query parameter value to create the ColumnOrderValue from
     * @return a new instance of ColumnOrderValue
     */
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
