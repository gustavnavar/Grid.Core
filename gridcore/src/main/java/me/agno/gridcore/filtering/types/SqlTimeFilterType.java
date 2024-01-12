package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

import java.sql.Time;
import java.time.LocalTime;

@Getter
public class SqlTimeFilterType<T> extends FilterTypeBase<T, Time> {

    public Class<Time> TargetType = Time.class;

    public GridFilterType GetValidType(GridFilterType type) {
        return switch (type) {
            case Equals, NotEquals, GreaterThan, GreaterThanOrEquals, LessThan, LessThanOrEquals -> type;
            default -> GridFilterType.Equals;
        };
    }

    public Time GetTypedValue(String value) {
        var time = LocalTime.parse(value);
        return Time.valueOf(time);
    }

    public Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        Time typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return switch (filterType) {
            case Equals -> cb.equal(path, typedValue);
            case NotEquals -> cb.notEqual(path, typedValue);
            case LessThan -> cb.lessThan(path, typedValue);
            case LessThanOrEquals -> cb.lessThanOrEqualTo(path, typedValue);
            case GreaterThan -> cb.greaterThan(path, typedValue);
            case GreaterThanOrEquals -> cb.greaterThanOrEqualTo(path, typedValue);
            default -> throw new IllegalArgumentException();
        };
    }
}