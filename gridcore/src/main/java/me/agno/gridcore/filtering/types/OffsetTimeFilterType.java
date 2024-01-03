package me.agno.gridcore.filtering.types;

import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.jpa.JPQL;
import org.jinq.orm.stream.JinqStream;

import java.time.OffsetTime;
import java.util.function.Function;
import java.util.function.Predicate;

public class OffsetTimeFilterType<T> extends FilterTypeBase<T, OffsetTime> {

    @Getter
    public Class TargetType = OffsetTime.class;

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

    public OffsetTime GetTypedValue(String value) {
        return OffsetTime.parse(value);
    }

    public Predicate<T> GetFilterExpression(Function<T, OffsetTime> leftExpr, String value, GridFilterType filterType, JinqStream<T> source,
                                            String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        OffsetTime typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        switch (filterType) {
            case Equals:
                return c -> typedValue.isEqual(leftExpr.apply(c));
            case NotEquals:
                return c -> !typedValue.isEqual(leftExpr.apply(c));
            case LessThan:
                return c -> typedValue.isAfter(leftExpr.apply(c));
            case LessThanOrEquals:
                return c -> {
                    var expr = leftExpr.apply(c);
                    return typedValue.isAfter(leftExpr.apply(c)) || typedValue.equals(expr);
                };
            case GreaterThan:
                return c -> typedValue.isBefore(leftExpr.apply(c));
            case GreaterThanOrEquals:
                return c -> {
                    var expr = leftExpr.apply(c);
                    return typedValue.isBefore(leftExpr.apply(c)) || typedValue.equals(expr);
                };
            case IsDuplicated:
                return c -> JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
            case IsNotDuplicated:
                return c -> !JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
            default:
                throw new IllegalArgumentException();
        }
    }
}