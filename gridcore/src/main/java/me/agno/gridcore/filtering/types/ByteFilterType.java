package me.agno.gridcore.filtering.types;

import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.jpa.JPQL;
import org.jinq.orm.stream.JinqStream;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ByteFilterType<T> extends FilterTypeBase<T, Byte> {

    @Getter
    public Class TargetType = Byte.class;

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

    public Byte GetTypedValue(String value) {
        return value.getBytes(StandardCharsets.UTF_8)[0];
    }

    public Predicate<T> GetFilterExpression(Function<T, Byte> leftExpr, String value, GridFilterType filterType, JinqStream<T> source,
                                            String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        Byte typedValue = GetTypedValue(value);
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