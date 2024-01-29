package me.agno.gridjavacore.filtering.types;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridjavacore.filtering.GridFilterType;
import org.hibernate.query.sqm.tree.select.SqmQuerySpec;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * Represents a type of filter specific to the Calendar data type.
 */
@Getter
public class CalendarFilterType<T> extends FilterTypeBase<T, Calendar> {

    private final Class<Calendar> targetType = Calendar.class;

    /**
     * Retrieves a valid GridFilterType based on the given type.
     *
     * @param type The GridFilterType to validate.
     * @return The validated GridFilterType.
     */
    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, GREATER_THAN, GREATER_THAN_OR_EQUALS, LESS_THAN, LESS_THAN_OR_EQUALS,
                    IS_DUPLICATED, IS_NOT_DUPLICATED -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    /**
     * Retrieves a Calendar object based on the provided value.
     *
     * @param value The value to parse into a Calendar object.
     * @return The Calendar object created from the value, or null if the parsing fails.
     */
    public Calendar getTypedValue(String value) {
        try {
            var date = LocalDateTime.parse(value);
            Calendar cal = Calendar.getInstance();
            cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
            return cal;
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Retrieves the filter expression based on the given criteria builder, criteria query,
     * root, source, expression, value, filter type, and remove diacritics flag.
     *
     * @param cb                The criteria builder.
     * @param cq                The criteria query.
     * @param root              The root entity.
     * @param source            The query source.
     * @param expression        The expression to filter on.
     * @param value             The value to filter against.
     * @param filterType        The type of filter to apply.
     * @param removeDiacritics  The flag indicating whether to remove diacritics or not.
     * @return The filter expression predicate.
     */
    public Predicate getFilterExpression(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root,
                                         SqmQuerySpec source, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {

        //base implementation of building filter expressions
        filterType = getValidType(filterType);

        Calendar typedValue = getTypedValue(value);
        if (typedValue == null &&
                filterType != GridFilterType.IS_DUPLICATED && filterType != GridFilterType.IS_NOT_DUPLICATED)
            return null; //incorrent filter value;

        var path = getPath(root, expression);

        return switch (filterType) {
            case EQUALS -> cb.equal(path, typedValue);
            case NOT_EQUALS -> cb.notEqual(path, typedValue);
            case LESS_THAN -> cb.lessThan(path, typedValue);
            case LESS_THAN_OR_EQUALS -> cb.lessThanOrEqualTo(path, typedValue);
            case GREATER_THAN -> cb.greaterThan(path, typedValue);
            case GREATER_THAN_OR_EQUALS -> cb.greaterThanOrEqualTo(path, typedValue);
            case IS_DUPLICATED -> isDuplicated(cb, cq, root, source, this.targetType, expression);
            case IS_NOT_DUPLICATED -> isNotDuplicated(cb, cq, root, source,  this.targetType,
                    expression);
            default -> throw new IllegalArgumentException();
        };
    }
}