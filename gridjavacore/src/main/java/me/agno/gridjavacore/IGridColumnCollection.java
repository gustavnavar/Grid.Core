package me.agno.gridjavacore;

import me.agno.gridjavacore.columns.IGridColumn;

/**
 * Represents a collection of grid columns for a specific grid.
 *
 * @param <T> the type of the grid data
 */
public interface IGridColumnCollection<T>
{
    /**
     * Retrieves the grid associated with this IGridColumnCollection.
     *
     * @return the grid object
     */
    IGrid<T> getGrid();

    /**
     * Adds a column to the grid column collection.
     *
     * @param column the column to be added
     * @return the added grid column
     * @throws NullPointerException if column is null
     * @throws IllegalArgumentException if a column with the same name already exists in the grid
     */
    IGridColumn<T> add(IGridColumn<T> column);

    /**
     * Adds a new column to the grid column collection.
     *
     * @return the added grid column
     */
    IGridColumn<T> add();

    /**
     * Adds a column to the grid column collection with the specified hidden state.
     *
     * @param hidden true if the column should be hidden, false otherwise
     * @return the added grid column
     */
    IGridColumn<T> add(boolean hidden);

    /**
     * Adds a column to the grid column collection with the specified column name.
     *
     * @param columnName the name of the column to be added
     * @return the added grid column
     */
    IGridColumn<T> add(String columnName);

    /**
     * Adds a column to the grid column collection with the specified hidden state and column name.
     *
     * @param hidden true if the column should be hidden, false otherwise
     * @param columnName the name of the column to be added
     * @return the added grid column
     */
    IGridColumn<T> add(boolean hidden, String columnName);

    /**
     * Adds a column to the grid column collection with the specified expression and target type.
     *
     * @param expression the expression used to generate the column for the grid
     * @param targetType the class representing the target type of the column's data
     * @param <TData> the type of the column's data
     * @return the added grid column
     */
    <TData> IGridColumn<T> add(String expression, Class<TData> targetType);

    /**
     * Adds a column to the grid column collection with the specified expression, target type, and column name.
     *
     * @param expression the expression used to generate the column for the grid
     * @param targetType the class representing the target type of the column's data
     * @param columnName the name of the column to be added
     * @param <TData> the type of the column's data
     * @return the added grid column
     */
    <TData> IGridColumn<T> add(String expression, Class<TData> targetType, String columnName);

    /**
     * Adds a column to the grid column collection with the specified expression, target type, and hidden state.
     *
     * @param expression the expression used to generate the column for the grid
     * @param targetType the class representing the target type of the column's data
     * @param hidden true if the column should be hidden, false otherwise
     * @param <TData> the type of the column's data
     * @return the added grid column
     * @param <TData>
     */
    <TData> IGridColumn<T> add(String expression, Class<TData> targetType, boolean hidden);

    /**
     * Creates a column to the grid column collection with the specified expression, target type, and hidden state.
     *
     * @param expression the expression used to generate the column for the grid
     * @param targetType the class representing the target type of the column's data
     * @param hidden true if the column should be hidden, false otherwise
     * @param columnName the name of the column to be added
     * @param <TData> the type of the column's data
     * @return the added grid column
     * @param <TData>
     */
    <TData> IGridColumn<T> createColumn(String expression, Class<TData> targetType, boolean hidden, String columnName);

    /**
     * Retrieves a grid column with the specified name.
     *
     * @param name the name of the grid column to retrieve
     * @return the grid column with the specified name
     */
    IGridColumn<T> get(String name);
}
