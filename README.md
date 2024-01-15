# Grid.Core

GridCore is a back-end library for Spring Boot projects that performs paging, sorting, filtering and others, to support client Blazor projects to show data in a grid. 

It is in development and it is not production ready.

The long-term objective is to develop a client Grid library for Vue.js projects.


## Folder description
* [gridcore](./gridcore): Library to build back-ends bases on Spring Boot
* [demo](./demo): Spring Boot demo project supporting the gridcore library. It can be testes with this [Blazor WASM project](https://github.com/gustavnavar/Grid.Blazor/tree/master/GridBlazorSpring)

The SQL Server database for all demos can be downloaded from [here](./sample-database)

Alternatively, if you prefer to install a fresh version of the database you can perform the following steps:
- run this script from Microsoft web to create a new database: https://github.com/microsoft/sql-server-samples/blob/master/samples/databases/northwind-pubs/instnwnd.sql
- add a column to the Customers table with the name IsVip of type bit (NOT NULL) and default value 0:
    ```sql
        USE Northwind;
        ALTER TABLE dbo.Customers ADD IsVip bit NOT NULL DEFAULT 0 WITH VALUES;
        GO
    ```
- change manually some values of the new IsVip column to True
- review the datetime columns. Any mismatch between EF Core model and database definitions will produce an exception and filtering will not work as expected. If database columns are defined as ```datetime``` you must modify the ```NorthwindDbContext``` class including:
    ```c#
        modelBuilder.Entity<Order>().Property(r => r.OrderDate).HasColumnType("datetime");
    ```
    for each datetime column. Or you can change all database columns' type to ```datetime2(7)```.


