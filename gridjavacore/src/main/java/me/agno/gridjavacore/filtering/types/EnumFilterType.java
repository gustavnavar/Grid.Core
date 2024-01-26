package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

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
        try {
            return Enum.valueOf(targetType, value);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value, 
                                         GridFilterType filterType, String removeDiacritics) {
        Enum typedValue = this.getTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return cb.equal(path, typedValue);
    }
}
