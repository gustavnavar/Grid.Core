package me.agno.gridjavacore.searching;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class DefaultColumnSearch<T, TData> implements IColumnSearch<T> {

    private final String expression;

    private final Class<TData> targetType;

    public DefaultColumnSearch(String expression, Class<TData> targetType)
    {
        this.expression = expression;
        this.targetType = targetType;
    }

    public Predicate getExpression(CriteriaBuilder cb, Root<T> root, String value, boolean onlyTextColumns) {
        return getExpression(cb, root, value, onlyTextColumns,null);
    }

    public Predicate getExpression(CriteriaBuilder cb, Root<T> root, String value, boolean onlyTextColumns,
                                   String removeDiacritics)
    {
        if (value == null || value.trim().isEmpty())
            return null;

        var path = getPath(root, this.expression);

        if (onlyTextColumns && ! this.targetType.equals(String.class))
            return null;

        if(removeDiacritics == null) {
            return cb.like(cb.upper(path.as(String.class)),
                    '%' + value.toUpperCase() + '%');
        }
        else {
            return cb.like(cb.function(removeDiacritics, String.class,cb.upper(path.as(String.class))),
                    cb.function(removeDiacritics, String.class, cb.literal('%' + value.toUpperCase() + '%')));
        }
    }

    public Path<TData> getPath(Root<T> root, String expession) {

        if(expession == null || expession.trim().isEmpty())
            return null;

        String[] names = expession.split("\\.");
        var path = root.get(names[0]);

        if(names.length > 1) {
            for(int i = 1; i < names.length; i ++) {
                path = path.get(names[i]);
            }
        }

        return (Path<TData>) path;
    }
}
