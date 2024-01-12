package me.agno.gridcore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

import java.util.Objects;

@Getter
public final class BooleanFilterType<T> extends FilterTypeBase<T, Boolean> {

        public Class<Boolean> TargetType = Boolean.class;

        public GridFilterType GetValidType(GridFilterType type) {
                //in any case Boolean types must compare by Equals filter type
                //We can't compare: contains(true) and etc.
                return GridFilterType.Equals;
        }

        public Boolean GetTypedValue(String value) {
                return "true".equalsIgnoreCase(value);
        }

        public Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                             GridFilterType filterType, String removeDiacritics) {

                //base implementation of building filter expressions
                filterType = GetValidType(filterType);

                Boolean typedValue = GetTypedValue(value);

            var path = getPath(root, expression);

            if (Objects.requireNonNull(filterType) == GridFilterType.Equals) {
                return cb.equal(path, typedValue);
            }
            throw new IllegalArgumentException();
        }
}