package me.agno.gridcore.filtering;

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
    private String ColumnName;

    @Getter
    @Setter
    private GridFilterType FilterType;

    private String FilterValue;

    public String getFilterValueEncoded() throws UnsupportedEncodingException {
        return URLEncoder.encode(FilterValue, StandardCharsets.UTF_8);
    }

    public void setFilterValueEncoded(String value) {
        FilterValue = value;
    }

    public ColumnFilterValue(String name, GridFilterType type, String value)
    {
        ColumnName = name;
        FilterType = type;
        FilterValue = value;
    }

    @Override public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ColumnFilterValue other)) return false;
        if (!other.canEqual((Object)this)) return false;
        return ColumnName.equals(other.ColumnName) && FilterType.equals(other.FilterType) && FilterValue.equals(other.FilterValue);
    }

    @Override public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = (result*PRIME) + (ColumnName == null ? 43 : ColumnName.hashCode());
        result = (result*PRIME) + FilterType.hashCode();
        result = (result*PRIME)  + (FilterValue == null ? 43 : FilterValue.hashCode());
        return result;
    }

    public boolean isNull() { return !isNotNull(); }

    public boolean isNotNull() {
        return this.getFilterType() != null && this.getFilterType() != GridFilterType.None;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ColumnFilterValue;
    }

    public static ColumnFilterValue Null() {
        return new ColumnFilterValue(null, GridFilterType.None, null);
    }
}
