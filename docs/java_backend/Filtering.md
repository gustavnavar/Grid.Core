# Filtering

[Index](Documentation.md)

You can enable the filtering option for your columns. To enable this functionality use the **filterable** method of the **Column** object:

```java
    columns.add("customers.CompanyName", String.class)
        .filterable(true);
```
After that you can filter this column. 

You can enable filtering for all columns of a grid using the **filterable** method for **GridServer** object:

```java
    IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
        .filterable();
```

**GridJavaCore** supports several types of columns (specified in the **add** method):

* String
* UUID
* Boolean
* SqlDate
* SqlTime
* SqlTimestamp
* Date
* Calendar
* Instant
* LocalDate
* LocalTime
* LocalDateTime
* OffsetTime
* OffsetDateTime
* ZonedDateTime
* Byte
* BigDecimal
* BigInteger
* Integer
* Short
* Double
* Long
* Float
* Enum

# Disable diacritics distinction

GridJavaCore distinguishes among letters with diacritics by default. If you filter by the term "bru", it will return all records that contains "bru", but it won't return any record containing "brú", "brû" or "brü". 

Anyway, it is possible to override the default behavior, so GridJavaCore will return any record containing "brú", "brû" or "brü". 

The solution to be implemented will depend on the back-end used to return the grid data. I will describe how to implement it for grids using JPA. It will be necessary to create a stored function on the database that will call it and configure the ```GridServer``` object:
1. For SQL Server you should open the ```SQL Server Studio Management``` tool and execute the following SQL query on your database to create the ```RemoveDiacritics``` function: 
        
    ```sql
    CREATE FUNCTION [dbo].[RemoveDiacritics] (
        @input varchar(max)
    ) RETURNS varchar(max)

    AS BEGIN
        DECLARE @result VARCHAR(max);

        select @result = @input collate SQL_Latin1_General_CP1253_CI_AI

        return @result
    END
    ```

2. and finally you must call the ```setRemoveDiacritics``` method of the ```GridServer``` class:
    ```java
    IGridServer<Order> server = new GridServer<>(em, Order.class, request.getParameterMap(), columns)
        .withPaging(10)
        .sortable()
        .filterable()
        .setRemoveDiacritics("dbo.RemoveDiacritics");
    ```

[<- Searching](Searching.md) | [Setup initial column filtering ->](Setup_initial_column_filtering.md)