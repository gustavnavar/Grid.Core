package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

import java.util.UUID;

@Getter
public class UuidFilterType<T> extends FilterTypeBase<T, UUID>{

    public Class<UUID> TargetType = UUID.class;

    public GridFilterType GetValidType(GridFilterType type) {
        return switch (type) {
            case Equals, NotEquals, Contains, StartsWith, EndsWidth -> type;
            default -> GridFilterType.Equals;
        };
    }

    public UUID GetTypedValue(String value) {
        return UUID.fromString(value);
    }

    public Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = GetValidType(filterType);

        UUID typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return switch (filterType) {
            case Equals -> cb.equal(path, typedValue);
            case NotEquals -> cb.notEqual(path, typedValue);
            case Contains -> cb.like(cb.upper(path.as(String.class)),
                    '%' + typedValue.toString().toUpperCase() + '%');
            case StartsWith -> cb.like(cb.upper(path.as(String.class)),
                    typedValue.toString().toUpperCase() + '%');
            case EndsWidth -> cb.like(cb.upper(path.as(String.class)),
                    '%' + typedValue.toString().toUpperCase());
            default -> throw new IllegalArgumentException();
        };
    }
}
