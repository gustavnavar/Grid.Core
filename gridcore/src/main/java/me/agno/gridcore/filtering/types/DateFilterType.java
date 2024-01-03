package me.agno.gridcore.filtering.types;

import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.jpa.JPQL;
import org.jinq.orm.stream.JinqStream;

import java.time.Instant;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;

public class DateFilterType<T> extends FilterTypeBase<T, Date> {

    @Getter
    public Class TargetType = Date.class;

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

    public Date GetTypedValue(String value) {
        var instant = Instant.parse(value);
        return Date.from(instant);
    }

    public Predicate<T> GetFilterExpression(Function<T, Date> leftExpr, String value, GridFilterType filterType, JinqStream<T> source,
                                            String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        Date typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        switch (filterType) {
            case Equals:
                return c -> typedValue.equals(leftExpr.apply(c));
            case NotEquals:
                return c -> !typedValue.equals(leftExpr.apply(c));
            case LessThan:
                return c -> typedValue.after(leftExpr.apply(c));
            case LessThanOrEquals:
                return c -> {
                    var expr = leftExpr.apply(c);
                    return typedValue.after(leftExpr.apply(c)) || typedValue.equals(expr);
                };
            case GreaterThan:
                return c -> typedValue.before(leftExpr.apply(c));
            case GreaterThanOrEquals:
                return c -> {
                    var expr = leftExpr.apply(c);
                    return typedValue.before(leftExpr.apply(c)) || typedValue.equals(expr);
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