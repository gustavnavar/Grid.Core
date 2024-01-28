# Setup initial column filtering

[Index](Documentation.md)

Sometimes you need to setup initial column filtering, just after the grid is first time loaded. After that a user can use this pre-filtered grid or clear/change filter settings.

You can do this, using the **setInitialFilter** method:

```c#
    columns.add("customer.companyName", String.class)
        .thenSortByDescending("orderID")
        .setInitialFilter(GridFilterType.STARTS_WITH, "a")
```

[<- Filtering](Filtering.md) | [Data annotations ->](Data_annotations.md)