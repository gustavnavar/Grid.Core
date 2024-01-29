package me.agno.gridjavacore.filtering;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * The ColumnFilterValue class represents a filter applied to a column in a grid.
 *
 * It contains information about the column name, filter type, and filter value.
 */
@Data
public class ColumnFilterValue {

    /**
     * The columnName variable represents the name of a column in the grid.
     * It is used in the ColumnFilterValue class to specify the column on which the filter is applied.
     */
    @Getter
    @Setter
    private String columnName;

    /**
     * The filterType variable represents the type of filter applied to a column in a grid.
     * It is a property of the ColumnFilterValue class.
     *
     * The filterType variable is of type GridFilterType, which is an enumeration representing different types of filters.
     * The available filter types are:
     * - NONE: No filter applied
     * - EQUALS: Filter for equality
     * - CONTAINS: Filter for containing a value
     * - STARTS_WITH: Filter for starting with a value
     * - ENDS_WIDTH: Filter for ending with a value
     * - GREATER_THAN: Filter for values greater than a specified value
     * - LESS_THAN: Filter for values less than a specified value
     * - GREATER_THAN_OR_EQUALS: Filter for values greater than or equal to a specified value
     * - LESS_THAN_OR_EQUALS: Filter for values less than or equal to a specified value
     * - CONDITION: Filter based on a custom condition
     * - NOT_EQUALS: Filter for inequality
     * - IS_NULL: Filter for null values
     * - IS_NOT_NULL: Filter for non-null values
     * - IS_DUPLICATED: Filter for duplicated values
     * - IS_NOT_DUPLICATED: Filter for non-duplicated values
     *
     * The filterType variable is annotated with @Getter and @Setter annotations to provide getter and setter methods.
     *
     * The filterType variable is used in the ColumnFilterValue class to specify the type of filter applied to a column.
     */
    @Getter
    @Setter
    private GridFilterType filterType;

    /**
     * A private variable that represents the filter value used for column filtering.
     */
    private String filterValue;

    /**
     * Encodes the filter value using URL encoding.
     *
     * @return the URL-encoded filter value
     * @throws UnsupportedEncodingException if the encoding is not supported
     */
    public String getFilterValueEncoded() throws UnsupportedEncodingException {
        return URLEncoder.encode(this.filterValue, StandardCharsets.UTF_8);
    }

    /**
     * Sets the encoded filter value for the column filter.
     *
     * @param value the encoded filter value to set
     */
    public void setFilterValueEncoded(String value) {
        this.filterValue = value;
    }

    /**
     * Represents a column filter value for a grid.
     *
     * @param name   the name of the column
     * @param type   the type of filter to apply on the column
     * @param value  the filter value for the column
     */
    public ColumnFilterValue(String name, GridFilterType type, String value)
    {
        this.columnName = name;
        this.filterType = type;
        this.filterValue = value;
    }

    /**
     * Checks if the current ColumnFilterValue object is equal to the given object.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ColumnFilterValue other)) return false;
        if (!other.canEqual((Object)this)) return false;
        return this.columnName.equals(other.getColumnName()) && this.filterType.equals(other.getFilterType())
                && this.filterValue.equals(other.getFilterValue());
    }

    /**
     * Generates a hash code value for the ColumnFilterValue object.
     *
     * @return the hash code value
     */
    @Override public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result*PRIME) + (this.columnName == null ? 43 : this.columnName.hashCode());
        result = (result*PRIME) + this.filterType.hashCode();
        result = (result*PRIME)  + (this.filterValue == null ? 43 : this.filterValue.hashCode());
        return result;
    }

    /**
     * Checks if the ColumnFilterValue object is null.
     *
     * @return true if the object is null, false if the object is not null
     */
    public boolean isNull() { return !isNotNull(); }

    /**
     * Checks if the ColumnFilterValue object is not null.
     *
     * @return true if the object is not null, false if the object is null
     */
    public boolean isNotNull() {
        return this.getFilterType() != null && this.getFilterType() != GridFilterType.NONE;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ColumnFilterValue;
    }

    /**
     * Creates a ColumnFilterValue object with null values.
     *
     * @return the ColumnFilterValue object with null values
     */
    public static ColumnFilterValue Null() {
        return new ColumnFilterValue(null, GridFilterType.NONE, null);
    }
}
