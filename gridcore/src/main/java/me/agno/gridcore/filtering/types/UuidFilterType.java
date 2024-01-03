package me.agno.gridcore.filtering.types;

import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.jpa.JPQL;
import org.jinq.orm.stream.JinqStream;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

public class UuidFilterType<T> extends FilterTypeBase<T, UUID>{

    @Getter
    public Class TargetType = UUID.class;

    public GridFilterType GetValidType(GridFilterType type) {
        switch (type) {
            case Equals:
            case NotEquals:
            case Contains:
            case StartsWith:
            case EndsWidth:
            case IsDuplicated:
            case IsNotDuplicated:
                return type;
            default:
                return GridFilterType.Equals;
        }
    }

    public UUID GetTypedValue(String value) {
        return UUID.fromString(value);
    }

    public Predicate<T> GetFilterExpression(Function<T, UUID> leftExpr, String value, GridFilterType filterType, JinqStream<T> source,
                                            String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        UUID typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        switch (filterType) {
            case Equals:
                return c -> typedValue.compareTo(leftExpr.apply(c)) == 0;
            case NotEquals:
                return c -> typedValue.compareTo(leftExpr.apply(c)) != 0;
            case Contains:
                return c -> leftExpr.apply(c).toString().toUpperCase().contains(typedValue.toString().toUpperCase());
            case StartsWith:
                return c -> leftExpr.apply(c).toString().toUpperCase().startsWith(typedValue.toString().toUpperCase());
            case EndsWidth:
                return c -> leftExpr.apply(c).toString().toUpperCase().endsWith(typedValue.toString().toUpperCase());
            case IsDuplicated:
                return c -> JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
            case IsNotDuplicated:
                return c -> !JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
            default:
                throw new IllegalArgumentException();
        }
    }
}
