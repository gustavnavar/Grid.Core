package me.agno.gridcore.sorting;

import jakarta.persistence.criteria.*;

public class OrderByGridOrderer<T, TData> implements IColumnOrderer<T> {
    private final String expression;

    public OrderByGridOrderer(String expression) {
        this.expression = expression;
    }

    public Order applyOrder(CriteriaBuilder cb, Root<T> root, GridSortDirection direction)
    {
        var path = getPath(root, this.expression);

        return switch (direction) {
            case ASCENDING -> cb.asc(path);
            case DESCENDING -> cb.desc(path);
            default -> throw new IllegalArgumentException("direction");
        };
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
