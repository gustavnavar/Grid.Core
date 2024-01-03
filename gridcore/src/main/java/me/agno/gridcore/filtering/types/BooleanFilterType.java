package me.agno.gridcore.filtering.types;

import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.jinq.orm.stream.JinqStream;

import java.util.function.Function;
import java.util.function.Predicate;

public final class BooleanFilterType<T> extends FilterTypeBase<T, Boolean> {

        @Getter
        public Class TargetType = Boolean.class;


        public GridFilterType GetValidType(GridFilterType type) {
                //in any case Boolean types must compare by Equals filter type
                //We can't compare: contains(true) and etc.
                return GridFilterType.Equals;
        }

        public Boolean GetTypedValue(String value) {
                return "true".equalsIgnoreCase(value) ? true : false;
        }

        public Predicate<T> GetFilterExpression(Function<T, Boolean>leftExpr, String value, GridFilterType filterType, JinqStream<T> source,
                                                String removeDiacritics) {

                //base implementation of building filter expressions
                filterType = GetValidType(filterType);

                Boolean typedValue = GetTypedValue(value);
                if (typedValue == null)
                        return null; //incorrent filter value;

                switch (filterType) {
                        case Equals:
                                return c -> typedValue == leftExpr.apply(c);
                        default:
                                throw new IllegalArgumentException();
                }
        }
}