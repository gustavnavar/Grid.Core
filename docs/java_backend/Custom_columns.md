# Custom columns

[Index](Documentation.md)

All customization for columns must be done on the client project, preferably in the razor page.

But there are some customization that is also required on the black-end side:

* You can create a custom column by calling the **columns.add** method in the **IGridColumnCollection** interface. For example:

    ```java
        columns.add("customers.companyName", String.class);
    ```

* As a general rule you can concatenate method calls of the **Column** object to configure each column. For example:

    ```java
        columns.add("customers.companyName", String.class)
                .setPrimaryKey(true);
    ```

## Hidden columns

All columns added to the grid are visible by default. If you want that a column will not appear on the screen you have to set it up in the **add** method call:

```java
    columns.add("id", Long.class, true);
```
The third parameter of the **add** method defines if the column will be hidden or not. 
In this example you will not see the **id** column, but you can get values at the client side using javascript. 
Important: you can't sort hidden columns.

## Sorting

If you want to enable sorting just for one column, you just call the **sortable(true)** method of that column:

```java
    columns.add("customers.companyName", String.class)
                .sortable(true);
```

Sorting will be implemented for the field that you specify the **add** method, in this example for the **customers.companyName** field.

If you pass an ordered collection of items to the Grid constructor and you want to display this by default, you have to specify the initial sorting options:

```java
    columns.add("customers.companyName", String.class)
                .sortable(true)
                .sortInitialDirection(GridSortDirection.DESCENDING);
```

Remember that you can also enable [Sorting](Sorting.md) for all columns of a grid. Sorting at grid level has precedence over sorting defined at column level.

## Column settings

Method name | Description | Example
------------- | ----------- | -------
sortable | Enable or disable sorting for current column | columns.add("name", String.class).sortable(true);
sortInitialDirection | Setup the initial sort deirection of the column (need to enable sorting) | columns.add("name", String.class).sortable(true).sortInitialDirection(GridSortDirection.DESCENDING);
setInitialFilter | Setup the initial filter of the column | columns.add("name", String.class).filterable(true).setInitialFilter(GridFilterType.EQUALS, "some name");
thenSortBy | Setup ThenBy sorting of current column | columns.add("name", String.class).sortable(true).thenSortBy("date");
thenSortByDescending | Setup ThenByDescending sorting of current column | columns.add("name", String.class).sortable(true).thenSortBy("date").thenSortByDescending("description");
filterable | Enable or disable filtering feauture on the column | columns.add("name", String.class).filterable(true);

[<- Paging](Paging.md) | [Totals ->](Totals.md)