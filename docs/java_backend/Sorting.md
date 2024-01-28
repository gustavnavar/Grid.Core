# Sorting

[Index](Documentation.md)

## Regular Sorting
You can enable sorting for all columns of a grid using the **sortable** method for the **GridServer** object:
```java
    IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns, 10)
        .sortable();
```

In this case you can select sorting pressing the column name on just one column at a time

Sorting at grid level has precedence over sorting defined at column level.

[<- Totals](Totals.md) | [Searching ->](Searching.md)