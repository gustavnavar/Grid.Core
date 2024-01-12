package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

@Getter
public class EnumFilterType<T> extends FilterTypeBase<T, Enum> implements IFilterType<T, Enum> {

    private final Class targetType;

    public EnumFilterType(Class type) {
        targetType = type;
    }

    public GridFilterType getValidType(GridFilterType type) {
        return GridFilterType.EQUALS;
    }

    public Enum getTypedValue(String value) {
        return Enum.valueOf(targetType, value);
    }

    public Predicate getFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {
        Enum typedValue = this.getTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return cb.equal(path, typedValue);
    }
}
