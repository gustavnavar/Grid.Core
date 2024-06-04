package me.agno.gridjavacore.filtering.types;

import java.util.ArrayList;
import java.util.List;

/**
 * The FilterTypeResolver class is responsible for resolving the appropriate filter type
 * based on the provided class type. It contains a collection of filter types and provides
 * a method to retrieve the appropriate filter type for a given class.
 */
public class FilterTypeResolver {

    private final List<IFilterType> filterCollection = new ArrayList<>();

    /**
     * The FilterTypeResolver class is responsible for resolving the appropriate filter type
     * based on the provided class type. It contains a collection of filter types and provides
     * a method to retrieve the appropriate filter type for a given class.
     */
    public FilterTypeResolver()
    {
        //add default filter types to collection:
        filterCollection.add(new TextFilterType());
        filterCollection.add(new BooleanFilterType());
        filterCollection.add(new SqlDateFilterType());
        filterCollection.add(new SqlTimeFilterType());
        filterCollection.add(new SqlTimestampFilterType());
        filterCollection.add(new DateFilterType());
        filterCollection.add(new CalendarFilterType());
        filterCollection.add(new InstantFilterType());
        filterCollection.add(new LocalDateFilterType());
        filterCollection.add(new LocalTimeFilterType());
        filterCollection.add(new LocalDateTimeFilterType());
        filterCollection.add(new OffsetTimeFilterType());
        filterCollection.add(new OffsetDateTimeFilterType());
        filterCollection.add(new ZonedDateTimeFilterType());
        filterCollection.add(new ByteFilterType());
        filterCollection.add(new BigDecimalFilterType());
        filterCollection.add(new BigIntegerFilterType());
        filterCollection.add(new IntegerFilterType());
        filterCollection.add(new ShortFilterType());
        filterCollection.add(new DoubleFilterType());
        filterCollection.add(new LongFilterType());
        filterCollection.add(new FloatFilterType());
        filterCollection.add(new UuidFilterType());
        filterCollection.add(new CollectionFilterType());
    }

    /**
     * Retrieves the appropriate filter type for the given class type.
     *
     * @param type The class type for which to retrieve the filter type.
     * @return The filter type for the given class type.
     */
    public IFilterType getFilterType(Class type)
    {
        if (type.isEnum())
            return new EnumFilterType(type);

        for (IFilterType filterType : filterCollection)
        {
            if (filterType.getTargetType().equals(type))
                return filterType;
        }
        return new TextFilterType(); //try to process column type as text (not safe)
    }
}
