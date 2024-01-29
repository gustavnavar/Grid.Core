package me.agno.gridjavacore;

import lombok.Getter;
import lombok.Setter;

/**
 * The SelectItem class represents an item in a select list. It contains a value and a title.
 */
public class SelectItem {
    public final String listFilter = "ListFilter";

    /**
     * The value property represents the value of an item in a select list.
     * It is a mutable property that can be accessed using the getter and setter methods.
     *
     * Example usage:
     *
     * SelectItem selectItem = new SelectItem("1", "Option 1");
     * selectItem.setValue("2");
     *
     * String value = selectItem.getValue(); // value = "2"
     *
     * Note: The value property is part of the SelectItem class, which represents an item in a select list.
     * The SelectItem class also contains a title property to represent the title of the item.
     *
     * @see SelectItem
     */
    @Getter
    @Setter
    private String value;

    /**
     * The Title property represents the title of an item in a select list. It is a mutable property that can be accessed using the getter and setter methods.
     *
     * Example usage:
     *
     * SelectItem selectItem = new SelectItem("1", "Option 1");
     * selectItem.setTitle("Option 2");
     *
     * String value = selectItem.getTitle(); // value = "Option 2"
     *
     * Note: The Title string is a standalone property that represents the title of an item in a select list.
     * The SelectItem class represents an item in a select list and contains both a value and a title property.
     *
     * @see SelectItem
     */
    @Getter
    @Setter
    private String title;

    public SelectItem()
    { }

    /**
     * Creates a new SelectItem with the given value and title.
     *
     * @param value The value of the item in the select list.
     * @param title The title of the item in the select list.
     */
    public SelectItem(String value, String title)
    {
        this.value = value;
        this.title = title;
    }
}
