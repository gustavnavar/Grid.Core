package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

@Getter
public class EnumFilterType<T> extends FilterTypeBase<T, Enum> implements IFilterType<T, Enum> {

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

    public Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                            GridFilterType filterType, String removeDiacritics) {
        Enum typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return cb.equal(path, typedValue);
    }
}
