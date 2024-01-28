# GridJavaCore configuration

[Index](Documentation.md) 

You can configure the settings of the grid with the parameters and methods of the **GridServer** objects. Remember that the **GridClient** object in the front-end client project and the **GridServer** object on the server project must have compatible settings.

## GridServer parameters

Parameter | Description                                                                                | Example
--------- |--------------------------------------------------------------------------------------------| -------
entityManager | **EntityManager** object to create queries to the database                                 | em
targetType | class of the object to be listed on the grid                                               | Order.class
query | map containing all grid parameters                                                         | Must be the **HttpServletRequest.getParameterMap()** of the action controller
columns | lambda expression to define the columns included in the grid (**Optional**)                | **Columns** lambda expression defined in the controller of the example
pageSize | integer to define the number of rows returned by the web service (**Optional**)            | 10

## GridServer methods

Method name | Description                                                                             | Example
----------- |-----------------------------------------------------------------------------------------| -------
autoGenerateColumns | Generates columns for all properties of the model using data annotations                | GridServer<Order>(...).autoGenerateColumns();
sortable | Enable or disable sorting for all columns of the grid                                   | GridServer<Order>(...).sortable(true);
searchable | Enable or disable searching on the grid                                                 | GridServer<Order>(...).searchable(true, true);
filterable | Enable or disable filtering for all columns of the grid                                 | GridServer<Order>(...).filterable(true);
withPaging | Enable paging and setting the number of items to be shown                               | GridServer<Order>(...).withPaging(10);
setColumns | lambda expression to define the columns included in the grid                            | GridServer<Order>(...).setColumns(columns);
setRemoveDiacritics | disable diacritics distinction on the database                                          | GridServer<Order>(...).setRemoveDiacritics("dbo.RemoveDiacritics");
setPredicate | Query to get the initial elements of the grid                                           | GridServer<Order>(...).setPredicate(cb.equal(root.get("orderID"), orderId));
setOrder | List of jakarta.persistence.criteria.Order for sorting the initial elements of the grid | GridServer<Order>(...).setOrder(sortingList)

[<- Quick start](Quick_start.md) | [Paging ->](Paging.md)