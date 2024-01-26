package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.*;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;
import org.hibernate.query.sqm.tree.select.SqmSubQuery;

public abstract class FilterTypeBase<T, TData> implements IFilterType<T, TData> {

    public abstract GridFilterType getValidType(GridFilterType type);

    public abstract TData getTypedValue(String value);

    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value, GridFilterType filterType) {
        return getFilterExpression(cb, cq, root, source, expression, value, filterType, null);
    }

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