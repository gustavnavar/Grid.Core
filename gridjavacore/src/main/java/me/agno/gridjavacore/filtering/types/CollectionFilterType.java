package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.*;
import lombok.Getter;
import me.agno.gridjavacore.columns.GridCoreColumn;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SetFilterType is a class that extends FilterTypeBase and represents a filter type for filtering Set values (one-to-many table).
 */
@Getter
public final class CollectionFilterType<T> extends FilterTypeBase<T, Collection> {

    private final Class<Collection> targetType = Collection.class;

    /**
     * Retrieves the valid GridFilterType based on the given input GridFilterType.
     *
     * @param type The input GridFilterType.
     * @return The valid GridFilterType.
     */
    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS,
                    IS_DUPLICATED, IS_NOT_DUPLICATED -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    /**
     * Retrieves a typed value based on the provided string value.
     *
     * @param value The string value to parse into a typed value.
     * @return The typed value created from the string value.
     */
    public Collection getTypedValue(String value) {
        return null;
    }

    /**
     * Retrieves an integer value based on the provided string value.
     *
     * @param value The string value to parse into a typed value.
     * @return The integer value created from the string value.
     */
    public Integer getIntValue(String value) {
        try {
            return Integer.valueOf(value);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression to be used in a query.
     *
     * @param cb               The CriteriaBuilder object.
     * @param cq               The CriteriaQuery object.
     * @param root             The Root object.
     * @param source           The SqmQuerySpec object.
     * @param column           The column.
     * @param value            The filter value.
     * @param filterType The GridFilterType.
     * @param removeDiacritics Whether to remove diacritics from the filter value.
     * @return The Predicate representing the filter expression.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, GridCoreColumn<T, Collection> column, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        Integer typedValue = getIntValue(value);
        if (typedValue == null &&
                filterType != GridFilterType.IS_DUPLICATED && filterType != GridFilterType.IS_NOT_DUPLICATED)
            return null; //incorrent filter value;

        String[] names = column.getExpression().split("\\.");
        if(names.length >= 2 && names[names.length -1].equalsIgnoreCase("count")) {

            Subquery<Long> subquery = cq.subquery(Long.class);
            Root<?> subRoot = subquery.from(column.getSubgridTargetType());

            Predicate[] predicates = new Predicate[column.getSubgridKeys().length];
            for (int i = 0; i < column.getSubgridKeys().length; i++) {
                predicates[i] = cb.equal(root.get(column.getSubgridKeys()[i].getKey()),
                        subRoot.get(column.getSubgridKeys()[i].getValue()));
            }

            subquery.select(cb.count(cb.literal(1)))
                    .where(predicates);

            return switch (filterType) {
                case EQUALS -> cb.equal(subquery, typedValue);
                case NOT_EQUALS -> cb.notEqual(subquery, typedValue);
                case LESS_THAN -> cb.lt(subquery, typedValue);
                case LESS_THAN_OR_EQUALS -> cb.le(subquery, typedValue);
                case GREATER_THAN -> cb.gt(subquery, typedValue);
                case GREATER_THAN_OR_EQUALS -> cb.ge(subquery, typedValue);
                case IS_DUPLICATED -> isDuplicated(cb, cq, root, source, this.targetType, column.getExpression());
                case IS_NOT_DUPLICATED -> isNotDuplicated(cb, cq, root, source, this.targetType,
                        column.getExpression());
                default -> throw new IllegalArgumentException();
            };
        }
        else {
            return null; //incorrent column name;
        }
    }
}