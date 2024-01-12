package me.agno.gridcore.filtering.types;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

@Getter
public class TextFilterType<T> extends FilterTypeBase<T, String> {

    private final Class<String> targetType = String.class;

    public GridFilterType getValidType(GridFilterType type) {
        return switch (type) {
            case EQUALS, NOT_EQUALS, CONTAINS, STARTS_WITH, ENDS_WIDTH, IS_NULL, IS_NOT_NULL -> type;
            default -> GridFilterType.EQUALS;
        };
    }

    public String getTypedValue(String value) { return value == null ? "" : value; }

    public Predicate getFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {
        
        //Custom implementation of string filter type. Case insensitive compartion.
        filterType = getValidType(filterType);
        
        String typedValue = this.getTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;
        
        var path = getPath(root, expression);

        if(removeDiacritics == null) {
            return switch (filterType) {
                case EQUALS -> cb.equal(cb.upper(path), typedValue.toUpperCase());
                case IS_NULL -> cb.or(cb.isNull(path), cb.equal(cb.trim(path), ""));
                case NOT_EQUALS -> cb.notEqual(cb.upper(path), typedValue.toUpperCase());
                case IS_NOT_NULL -> cb.and(cb.isNotNull(path), cb.notEqual(cb.trim(path), ""));
                case CONTAINS -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase() + '%');
                case STARTS_WITH -> cb.like(cb.upper(path),
                        typedValue.toUpperCase() + '%');
                case ENDS_WIDTH -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase());
                default -> throw new IllegalArgumentException();
            };
        }
        else {
            return switch (filterType) {
                case EQUALS -> cb.equal(cb.upper(path), typedValue.toUpperCase());
                case IS_NULL -> cb.or(cb.isNull(path), cb.equal(cb.trim(path), ""));
                case NOT_EQUALS -> cb.notEqual(cb.upper(path), typedValue.toUpperCase());
                case IS_NOT_NULL -> cb.and(cb.isNotNull(path), cb.notEqual(cb.trim(path), ""));
                case CONTAINS -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase() + '%');
                case STARTS_WITH -> cb.like(cb.upper(path),
                        typedValue.toUpperCase() + '%');
                case ENDS_WIDTH -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase());
                default -> throw new IllegalArgumentException();
            };
        }
    }
}