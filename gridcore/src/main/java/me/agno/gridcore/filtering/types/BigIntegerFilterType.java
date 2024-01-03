package me.agno.gridcore.filtering.types;

import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.jpa.JPQL;
import org.jinq.orm.stream.JinqStream;

import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.Predicate;

public class BigIntegerFilterType<T> extends FilterTypeBase<T, BigInteger> {

    @Getter
    public Class TargetType = BigInteger.class;

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

    public BigInteger GetTypedValue(String value) {
        return new BigInteger(value);
    }

    public Predicate<T> GetFilterExpression(Function<T, BigInteger> leftExpr, String value, GridFilterType filterType, JinqStream<T> source,
                                            String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        BigInteger typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        switch (filterType) {
            case Equals:
                return c -> typedValue.compareTo(leftExpr.apply(c)) == 0;
            case NotEquals:
                return c -> typedValue.compareTo(leftExpr.apply(c)) != 0;
            case LessThan:
                return c -> typedValue.compareTo(leftExpr.apply(c)) > 0;
            case LessThanOrEquals:
                return c -> typedValue.compareTo(leftExpr.apply(c)) >= 0;
            case GreaterThan:
                return c -> typedValue.compareTo(leftExpr.apply(c)) < 0;
            case GreaterThanOrEquals:
                return c -> typedValue.compareTo(leftExpr.apply(c)) <= 0;
            case IsDuplicated:
                return c -> JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
            case IsNotDuplicated:
                return c -> !JPQL.isIn(leftExpr.apply(c), GetGroupBy(source, leftExpr));
            default:
                throw new IllegalArgumentException();
        }
    }
}