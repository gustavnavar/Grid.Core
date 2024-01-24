package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.util.Objects;

@Getter
public final class BooleanFilterType<T> extends FilterTypeBase<T, Boolean> {

    private final Class<Boolean> targetType = Boolean.class;

    public GridFilterType getValidType(GridFilterType type) {
        //in any case Boolean types must compare by Equals filter type
        //We can't compare: contains(true) and etc.
        return GridFilterType.EQUALS;
    }

    public Boolean getTypedValue(String value) {
        return "true".equalsIgnoreCase(value);
    }

    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        Boolean typedValue = this.getTypedValue(value);

        var path = getPath(root, expression);

        if (Objects.requireNonNull(filterType) == GridFilterType.EQUALS) {
            return cb.equal(path, typedValue);
        }
        throw new IllegalArgumentException();
    }
}