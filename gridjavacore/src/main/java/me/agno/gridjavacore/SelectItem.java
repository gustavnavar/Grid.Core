package me.agno.gridjavacore;

import lombok.Getter;
import lombok.Setter;

public class SelectItem {
    public final String listFilter = "ListFilter";

    @Getter
    @Setter
    public String value;

    @Getter
    @Setter
    public String title;

    public SelectItem()
    { }

    public SelectItem(String value, String title)
    {
        this.value = value;
        this.title = title;
    }
}
