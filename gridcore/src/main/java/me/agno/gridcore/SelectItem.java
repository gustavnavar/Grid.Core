package me.agno.gridcore;

import lombok.Getter;
import lombok.Setter;

public class SelectItem {
    public final String ListFilter = "ListFilter";

    @Getter
    @Setter
    public String Value;

    @Getter
    @Setter
    public String Title;

    public SelectItem()
    { }

    public SelectItem(String value, String title)
    {
        Value = value;
        Title = title;
    }
}
