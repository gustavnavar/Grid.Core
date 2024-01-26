package me.agno.gridjavacore.filtering;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Data
public class ColumnFilterValue {

    @Getter
    @Setter
    private String columnName;

    @Getter
    @Setter
    private GridFilterType filterType;

    private String filterValue;

    public String getFilterValueEncoded() throws UnsupportedEncodingException {
        return URLEncoder.encode(this.filterValue, StandardCharsets.UTF_8);
    }

    public void setFilterValueEncoded(String value) {
        this.filterValue = value;
    }

    public ColumnFilterValue(String name, GridFilterType type, String value)
    {
        this.columnName = name;
        this.filterType = type;
        this.filterValue = value;
    }

    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ColumnFilterValue other)) return false;
        if (!other.canEqual((Object)this)) return false;
        return this.columnName.equals(other.getColumnName()) && this.filterType.equals(other.getFilterType())
                && this.filterValue.equals(other.getFilterValue());
    }

    @Override public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result*PRIME) + (this.columnName == null ? 43 : this.columnName.hashCode());
        result = (result*PRIME) + this.filterType.hashCode();
        result = (result*PRIME)  + (this.filterValue == null ? 43 : this.filterValue.hashCode());
        return result;
    }

    public boolean isNull() { return !isNotNull(); }

    public boolean isNotNull() {
        return this.getFilterType() != null && this.getFilterType() != GridFilterType.NONE;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ColumnFilterValue;
    }

    public static ColumnFilterValue Null() {
        return new ColumnFilterValue(null, GridFilterType.NONE, null);
    }
}
