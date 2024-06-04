package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.*;
import me.agno.gridjavacore.columns.GridCoreColumn;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;
import org.hibernate.query.sqm.tree.select.SqmSubQuery;

/**
 * FilterTypeBase is an abstract class that serves as a base for implementing custom filter types.
 * It provides common functionality and methods for filter types.
 *
 * @param <T>     The type of the root entity.
 * @param <TData> The type of the data being filtered.
 */
public abstract class FilterTypeBase<T, TData> implements IFilterType<T, TData> {

    /**
     * Determines the valid GridFilterType based on the input GridFilterType.
     *
     * @param type The input GridFilterType.
     * @return The valid GridFilterType.
     */
    public abstract GridFilterType getValidType(GridFilterType type);

    /**
     * Retrieves a typed value based on the provided string value.
     *
     * @param value The string value to parse into a typed value.
     * @return The typed value created from the string value.
     */
    public abstract TData getTypedValue(String value);

    /**
     * Retrieves the filter expression predicate based on the provided parameters.
     *
     * @param cb           The CriteriaBuilder object.
     * @param cq           The CriteriaQuery object.
     * @param root         The Root object.
     * @param source       The SqmQuerySpec object.
     * @param column       The column.
     * @param value        The filter value.
     * @param filterType The GridFilterType.
     * @return The Predicate representing the filter expression.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, GridCoreColumn<T, TData> column, String value,
                                         GridFilterType filterType) {
        return getFilterExpression(cb, cq, root, source, column, value, filterType, null);
    }

    /**
     * Retrieves the path based on the given root and expression.
     *
     * @param root       The root object.
     * @param expression The expression representing the path.
     * @return The path object corresponding to the expression.
     */
    public Path<TData> getPath(Root<?> root, String expression) {

        if(expression  == null || expression.trim().isEmpty())
            return null;

        String[] names = expression .split("\\.");
        var path = root.get(names[0]);

        if(names.length > 1) {
            for(int i = 1; i < names.length; i ++) {
                path = path.get(names[i]);
            }
        }

        return (Path<TData>) path;
    }

    /**
     * Determines if the specified column value is duplicated in the query result.
     *
     * @param cb               The CriteriaBuilder object.
     * @param cq               The CriteriaQuery object.
     * @param root             The Root object.
     * @param source           The SqmQuerySpec object.
     * @param columnTargetType The target type of the column.
     * @param expression       The expression representing the column.
     * @return A Predicate representing the duplication condition.
     */
    public Predicate isDuplicated(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                  SqmQuerySpec source, Class<TData> columnTargetType, String expression) {
        
        var subQuery = (SqmSubQuery<TData>) cq.subquery(columnTargetType);
        subQuery.setQueryPart(source);
        Root<?> subQueryRoot = subQuery.getRootList().get(0);

        subQuery.select(getPath(subQueryRoot, expression));
        subQuery.groupBy(getPath(subQueryRoot, expression));
        subQuery.having(cb.gt(cb.count(subQueryRoot), 1));

        return cb.in(getPath(root, expression)).value(subQuery);
    }

    /**
     * Determines if the specified column value is not duplicated in the query result.
     *
     * @param cb               The CriteriaBuilder object.
     * @param cq               The CriteriaQuery object.
     * @param root             The Root object.
     * @param source           The SqmQuerySpec object.
     * @param columnTargetType The target type of the column.
     * @param expression       The expression representing the column.
     * @return A Predicate representing the condition for not having duplicated values in the specified column.
     */
    public Predicate isNotDuplicated(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                  SqmQuerySpec source, Class<TData> columnTargetType, String expression) {

        var subQuery = (SqmSubQuery<TData>) cq.subquery(columnTargetType);
        subQuery.setQueryPart(source);
        Root<?> subQueryRoot = subQuery.getRootList().get(0);

        subQuery.select(getPath(subQueryRoot, expression));
        subQuery.groupBy(getPath(subQueryRoot, expression));
        subQuery.having(cb.le(cb.count(subQueryRoot), 1));

        return cb.in(getPath(root, expression)).value(subQuery);
    }
}