package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.*;
import me.agno.gridcore.filtering.GridFilterType;

public abstract class FilterTypeBase<T, TData> implements IFilterType<T, TData> {

    public abstract GridFilterType getValidType(GridFilterType type);

    public abstract TData getTypedValue(String value);

    public Predicate getFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value, GridFilterType filterType) {
        return getFilterExpression(cb, root, expression, value, filterType, null);
    }

    public Path<TData> getPath(Root<T> root, String expression) {

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
}