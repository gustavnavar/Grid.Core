package me.agno.gridcore.filtering.types;


import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.jpa.JPQL;
import org.jinq.orm.stream.JinqStream;

import java.util.function.Function;
import java.util.function.Predicate;

public class TextFilterType<T> extends FilterTypeBase<T, String> {

    @Getter
    public Class TargetType = String.class;

    public GridFilterType GetValidType(GridFilterType type) {
        switch (type) {
            case Equals:
            case NotEquals:
            case Contains:
            case StartsWith:
            case EndsWidth:
            case IsNull:
            case IsNotNull:
            case IsDuplicated:
            case IsNotDuplicated:
                return type;
            default:
            return GridFilterType.Equals;
        }
    }

    public String GetTypedValue(String value) { return value == null ? "" : value; }

    public Predicate<T> GetFilterExpression(Function<T, String> leftExpr, String value, GridFilterType filterType,
                                            JinqStream<T> source, String removeDiacritics) {
        
        //Custom implementation of string filter type. Case insensitive compartion.
        filterType = GetValidType(filterType);
        
        String typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        if(removeDiacritics == null) {
            switch (filterType) {
                case Equals:
                    return c -> value.toUpperCase().equals(leftExpr.apply(c).toUpperCase());
                case IsNull:
                    return c -> {
                        var exp = leftExpr.apply(c);
                        return exp == null || exp.trim() == "";
                    };
                case NotEquals:
                    return c -> !value.toUpperCase().equals(leftExpr.apply(c).toUpperCase());
                case IsNotNull:
                    return c -> {
                        var exp = leftExpr.apply(c);
                        return exp != null && exp.trim() != "";
                    };
                case Contains:
                    return c -> leftExpr.apply(c).toUpperCase().contains(value.toUpperCase());
                case StartsWith:
                    return c -> leftExpr.apply(c).toUpperCase().contains(value.toUpperCase());
                case EndsWidth:
                    return c -> leftExpr.apply(c).toUpperCase().contains(value.toUpperCase());
                case IsDuplicated:
                    return c -> JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
                case IsNotDuplicated:
                    return c -> !JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
                default:
                    throw new IllegalArgumentException();
            }
        }
        else {
            switch (filterType) {
                case Equals:
                    return c -> value.toUpperCase().equals(leftExpr.apply(c).toUpperCase());
                case IsNull:
                    return c -> {
                        var exp = leftExpr.apply(c);
                        return exp == null || exp.trim() == "";
                    };
                case NotEquals:
                    return c -> !value.toUpperCase().equals(leftExpr.apply(c).toUpperCase());
                case IsNotNull:
                    return c -> {
                        var exp = leftExpr.apply(c);
                        return exp != null && exp.trim() != "";
                    };
                case Contains:
                    return c -> leftExpr.apply(c).toUpperCase().contains(value.toUpperCase());
                case StartsWith:
                    return c -> leftExpr.apply(c).toUpperCase().contains(value.toUpperCase());
                case EndsWidth:
                    return c -> leftExpr.apply(c).toUpperCase().contains(value.toUpperCase());
                case IsDuplicated:
                    return c -> JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
                case IsNotDuplicated:
                    return c -> !JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}