package me.agno.gridcore.filtering.types;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import me.agno.gridcore.filtering.GridFilterType;

@Getter
public class TextFilterType<T> extends FilterTypeBase<T, String> {

    public Class<String> TargetType = String.class;

    public GridFilterType GetValidType(GridFilterType type) {
        return switch (type) {
            case Equals, NotEquals, Contains, StartsWith, EndsWidth, IsNull, IsNotNull -> type;
            default -> GridFilterType.Equals;
        };
    }

    public String GetTypedValue(String value) { return value == null ? "" : value; }

    public Predicate GetFilterExpression(CriteriaBuilder cb, Root<T> root, String expression, String value,
                                         GridFilterType filterType, String removeDiacritics) {
        
        //Custom implementation of string filter type. Case insensitive compartion.
        filterType = GetValidType(filterType);
        
        String typedValue = GetTypedValue(value);
        if (typedValue == null)
            return null; //incorrent filter value;
        
        var path = getPath(root, expression);

        if(removeDiacritics == null) {
            return switch (filterType) {
                case Equals -> cb.equal(cb.upper(path), typedValue.toUpperCase());
                case IsNull -> cb.or(cb.isNull(path), cb.equal(cb.trim(path), ""));
                case NotEquals -> cb.notEqual(cb.upper(path), typedValue.toUpperCase());
                case IsNotNull -> cb.and(cb.isNotNull(path), cb.notEqual(cb.trim(path), ""));
                case Contains -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase() + '%');
                case StartsWith -> cb.like(cb.upper(path),
                        typedValue.toUpperCase() + '%');
                case EndsWidth -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase());
                default -> throw new IllegalArgumentException();
            };
        }
        else {
            return switch (filterType) {
                case Equals -> cb.equal(cb.upper(path), typedValue.toUpperCase());
                case IsNull -> cb.or(cb.isNull(path), cb.equal(cb.trim(path), ""));
                case NotEquals -> cb.notEqual(cb.upper(path), typedValue.toUpperCase());
                case IsNotNull -> cb.and(cb.isNotNull(path), cb.notEqual(cb.trim(path), ""));
                case Contains -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase() + '%');
                case StartsWith -> cb.like(cb.upper(path),
                        typedValue.toUpperCase() + '%');
                case EndsWidth -> cb.like(cb.upper(path),
                        '%' + typedValue.toUpperCase());
                default -> throw new IllegalArgumentException();
            };
        }
    }
}