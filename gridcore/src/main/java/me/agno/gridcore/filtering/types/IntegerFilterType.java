package me.agno.gridcore.filtering.types;

import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.jpa.JPQL;
import org.jinq.orm.stream.JinqStream;

import java.util.function.Function;
import java.util.function.Predicate;

public final class IntegerFilterType<T> extends FilterTypeBase<T, Integer> {

    @Getter
    public Class TargetType = Integer.class;

    public GridFilterType GetValidType(GridFilterType type) {
        switch (type) {
            case Equals:
            case NotEquals:
            case GreaterThan:
            case GreaterThanOrEquals:
            case LessThan:
            case LessThanOrEquals:
            case IsDuplicated:
            case IsNotDuplicated:
                return type;
            default:
                return GridFilterType.Equals;
        }
    }

    public Integer GetTypedValue(String value) { return Integer.valueOf(value); }

    public Predicate<T> GetFilterExpression(Function<T, Integer> leftExpr, String value, GridFilterType filterType, JinqStream<T> source,
                                            String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        Integer typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        switch (filterType) {
            case Equals:
                return c -> typedValue == leftExpr.apply(c);
            case NotEquals:
                return c -> typedValue != leftExpr.apply(c);
            case LessThan:
                return c -> leftExpr.apply(c) < typedValue;
            case LessThanOrEquals:
                return c -> leftExpr.apply(c) <= typedValue;
            case GreaterThan:
                return c -> leftExpr.apply(c) > typedValue;
            case GreaterThanOrEquals:
                return c -> leftExpr.apply(c) >= typedValue;
            case IsDuplicated:
                return c -> JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
            case IsNotDuplicated:
                return c -> !JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
            default:
                throw new IllegalArgumentException();
        }
    }
}