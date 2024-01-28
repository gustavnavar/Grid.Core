# Quick start with GridJavaCore and GridBlazor

[Index](Documentation.md)

Imagine that you have to retrieve a collection of model items in your project. For example if your model class is:
    
```java
    public class Order
    {
        private Integer orderID;
        private String customerID;
        private LocalDateTime orderDate;
        private Customer customer;
        ...
    }
```

The steps to build a grid are:

1. Add a reference to **GridBlazor**, **GridBlazor.Pages**, **GridShared** and **GridShared.Utility** in the **_Imports.razor** file of the client project's root folder

    ```razor
        ...
        @using GridBlazor
        @using GridBlazor.Pages
        @using GridShared
        @using GridShared.Utility
        ...
    ```

2. Create a razor page on the client project to render the grid. The page file must have a .razor extension. An example of razor page is:

    ```razor
        @page "/gridsample"
        @using GridShared
        @using GridShared.Utility
        @using Microsoft.Extensions.Primitives
        @inject NavigationManager NavigationManager
        @inject HttpClient HttpClient

        @if (_task.IsCompleted)
        {
            <GridComponent T="Order" Grid="@_grid"></GridComponent>
        }
        else
        {
            <p><em>Loading...</em></p>
        }

        @code
        {
            private CGrid<Order> _grid;
            private Task _task;

            public static Action<IGridColumnCollection<Order>> Columns = c =>
            {
                c.Add(o => o.OrderID);
                c.Add(o => o.OrderDate, "OrderCustomDate").Format("{0:yyyy-MM-dd}");
                c.Add(o => o.Customer.CompanyName);
                c.Add(o => o.Customer.IsVip);
            };

            protected override async Task OnParametersSetAsync()
            {
                string url = NavigationManager.GetBaseUri() + "api/SampleData/GetOrdersGridForSample";
                var query = new QueryDictionary<StringValues>();

                var client = new GridClient<Order>(HttpClient, url, query, false, "ordersGrid", Columns);
                _grid = client.Grid;

                _task = client.UpdateGrid();
                await _task;
            }
        }
    ```

3. Create a controller action in the server project. An example of this type of controller action is: 

    ```java
        @RestController
        @RequiredArgsConstructor
        @RequestMapping(value = {"/api/sampledata", "/api/SampleData"})
        public class SampleDataController
        {
            @Autowired
            EntityManagerFactory entityManagerFactory;

            @GetMapping(value = {"getordersgrid", "GetOrdersGrid"}, produces = MediaType.APPLICATION_JSON_VALUE)
            public ResponseEntity<ItemsDTO<Order>> getOrdersGrid(HttpServletRequest request) {
                
                EntityManager em = entityManagerFactory.createEntityManager();
                
                Consumer<IGridColumnCollection<Order>> columns = c -> {
                    c.add("orderID", Integer.class);
                    c.add("orderDate", LocalDateTime.class, "orderCustomDate");
                    c.add("customer.companyName", String.class);
                    c.add("customer.contactName", String.class);
                    c.add("customer.country", String.class, true);
                    c.add("freight", BigDecimal.class);
                    c.add("customer.isVip",Boolean.class);
                };

                IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
                    .withPaging(10)
                    .sortable()
                    .filterable()
                    .setRemoveDiacritics("dbo.RemoveDiacritics");

                var items = server.getItemsToDisplay();
                return ResponseEntity.ok(items);
            }
        }
    ```

**Notes**:

* You must create a **GridClient** object in the **OnParametersSetAsync** of the Blazor page. This object contains a parameter of **CGrid** type called **Grid**. 

* You can use multiple methods of the **GridClient** object to configure a grid. For example:
    ```c#
        var client = new GridClient<Order>(HttpClient, url, query, false, "ordersGrid", Columns, locale)
            .SetRowCssClasses(item => item.Customer.IsVip ? "success" : string.Empty)
            .Sortable()
            .Filterable()
            .WithMultipleFilters();
    ```

* You must call the **UpdateGrid** method of the **GridClient** object at the end of the **OnParametersSetAsync** of the razor page because it will request for the required rows to the server

* If you need to update the component out of ```OnParametersSetAsync``` method you must use a reference to the component:
    ```c#
        <GridComponent @ref="Component" T="Order" Grid="@_grid"></GridComponent>
    ```

    and then call the ```UpdateGrid``` method:
    ```c#
        await Component.UpdateGrid();
    ```

* The **GridComponent** tag must contain at least these 2 attributes:
    * **T**: type of the model items
    * **Grid**: grid object that has to be created in the **OnParametersSetAsync** method of the razor page

* You should use a **GridServer** object in the server controller action.

* You can use multiple methods of the **GridServer** object to configure a grid on the server. For example:
    ```java
        IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
                    .withPaging(10)
                    .sortable()
                    .filterable()
                    .setRemoveDiacritics("dbo.RemoveDiacritics");
    ```

* The **GridClient** object on the client project and the **GridServer** object on the server project must have compatible settings.

* The server action returns a json including the model rows to be shown on the grid and other information requirired for paging, etc. The object type returned by the action must be **ItemsDTO<T>**.

For more documentation about column options, please see: [Custom columns](Custom_columns.md).

[<- Installation](Installation.md) | [GridJavaCore configuration ->](GridJavaCore_configuration.md)