package me.agno.gridcore.sorting;

import jakarta.persistence.criteria.*;

public class ThenByColumnOrderer <T, TData> implements IColumnOrderer<T> {
    private final String _expression;
    private final GridSortDirection _initialDirection;

    public ThenByColumnOrderer(String expression, GridSortDirection initialDirection) {
        _expression = expression;
        _initialDirection = initialDirection;
    }

    private Order Apply(CriteriaBuilder cb, Root<T> root) {

        var path = getPath(root, _expression);

        return switch (_initialDirection) {
            case Ascending -> cb.asc(path);
            case Descending -> cb.desc(path);
            default -> throw new IllegalArgumentException();
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

    public Order ApplyOrder(CriteriaBuilder cb, Root<T> root, GridSortDirection direction) {
        return Apply(cb, root);
    }
}