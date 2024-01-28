# Paging

[Index](Documentation.md)

You must configure the page size in the contructor of the **GridServer** object in the server project to enable paging:

```java
    IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns, 10);
```

**pageSize** is an optional parameter of the **GridServer** constructor. If you don't want to enable paging you must call the contructor with no parameter:

```java
    IGridServer<Order> server = new GridServer<Order>(em, Order.class, request.getParameterMap(), columns);
```

No configuration for paging is required on the client project.

[<- GridJavaCore configuration](GridJavaCore_configuration.md) | [Custom columns ->](Custom_columns.md)