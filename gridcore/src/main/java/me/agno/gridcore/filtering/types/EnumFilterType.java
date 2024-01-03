package me.agno.gridcore.filtering.types;

import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.orm.stream.JinqStream;

import java.util.function.Function;
import java.util.function.Predicate;

public class EnumFilterType<T> implements IFilterType<T, Enum> {

    @Getter
    public Class TargetType;

    public EnumFilterType(Class type) {
        TargetType = type;
    }

    public GridFilterType GetValidType(GridFilterType type) {
        return GridFilterType.Equals;
    }

    public Enum GetTypedValue(String value) {
        return Enum.valueOf(TargetType, value);
    }

    public Predicate<T> GetFilterExpression(Function<T, Enum> leftExpr, String value, GridFilterType filterType,
                                            JinqStream<T> source) {

        Enum typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        return c -> typedValue == leftExpr.apply(c);

    }

    public Predicate<T> GetFilterExpression(Function<T, Enum> leftExpr, String value, GridFilterType filterType,
                                            JinqStream<T> source, String removeDiacritics) {
        return GetFilterExpression(leftExpr, value, filterType, source);
    }
}
