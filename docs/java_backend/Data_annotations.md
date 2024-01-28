# Data annotations

[Index](Documentation.md)

You can customize grid and column settings using data annotations. In other words, you can mark properties of your model class as grid columns, specify column options, call **autoGenerateColumns** method, and **GridJavaCore** will automatically create columns as you describe in your annotations.

There are some annotations for this:

* **GridTable**: applies to model classes and specify options for the grid (paging options...)
* **GridColumn**: applies to model public properties and configure a property as a column with a set of properties
* **NotMappedColumn**: applies to model public properties and configures a property as NOT a column. If a property has this attribute, **GridJavaCore** will not add that column to the column collection

For example a model class with the following data annotations:

```java
@GridTable(pagingType = PagingType.PAGINATION, pageSize = 20)
public class Foo {
   @GridColumn(position = 0, type = String.class, sortEnabled = true, filterEnabled = true)
   private String name;

   @GridColumn(position = 2, type = Boolean.class)
   private Boolean enabled;

   @GridColumn(position = 1, type = Instant.class)
   private Instant fooDate;
        
   @NotMappedColumn
   @GridColumn(position = 3, type = String.class)
   private String data;
}
```
describes that the grid table must contain 3 columns (**name**, **enabled** and **fooDate**) with custom options. It also enables paging for this grid table and page size as 20 rows.

The steps to build a grid razor page using data annotations with **GridJavaCore** are:

1. Create a view on the client project to render the grid.

2. Create a controller action in the back-end project. An example of this type of controller action is: 

    ```java
    @GetMapping(value = {"getordersgridordersautogeneratecolumns", "GetOrdersGridordersAutoGenerateColumns"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemsDTO<Order>> getOrdersGridordersAutoGenerateColumns(HttpServletRequest request) {
    
        EntityManager em = entityManagerFactory.createEntityManager();
            
        IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), null)
                .autoGenerateColumns()
                .sortable()
                .filterable()
                .setRemoveDiacritics("dbo.RemoveDiacritics");
        
        var items = server.getItemsToDisplay();
        return ResponseEntity.ok(items);
    }
    ```

    **Notes**:
    * The **columns** parameter passed to the **GridServer** constructor must be **null**

    * You must use the **autoGenerateColumns** method of the **GridServer**

**GridJavaCore** will generate columns based on your data annotations when the **autoGenerateColumns** method is invoked. 

You can add custom columns after or before this method is called, for example:

```java
    var server = new GridServer<Foo>(...).autoGenerateColumns().setColumns(columns -> columns.add("child.Price", Double.class))
```

You can also overwrite grid options. For example using the **withPaging** method:

```java
    var server = new GridServer<Foo>(...).autoGenerateColumns().withPaging(10);
```

**Note:** If you use the ```position``` custom option to order the columns, you must use it on all the columns of the table including the ones using the ```NotMappedColumn``` annotation. If you don't do it the ```autoGenerateColumns``` will throw an exception.

[<- Setup initial column filtering](Setup_initial_column_filtering.md) | [Front-end back-end API ->](API.md)