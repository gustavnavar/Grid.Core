package me.agno.gridcore.filtering.types;

import java.util.ArrayList;
import java.util.List;

public class FilterTypeResolver {

    private final List<IFilterType> _filterCollection = new ArrayList();

    public FilterTypeResolver()
    {
        //add default filter types to collection:
        _filterCollection.add(new TextFilterType());
        _filterCollection.add(new BooleanFilterType());
        _filterCollection.add(new SqlDateFilterType());
        _filterCollection.add(new SqlTimeFilterType());
        _filterCollection.add(new SqlTimestampFilterType());
        _filterCollection.add(new DateFilterType());
        _filterCollection.add(new CalendarFilterType());
        _filterCollection.add(new InstantFilterType());
        _filterCollection.add(new LocalDateFilterType());
        _filterCollection.add(new LocalTimeFilterType());
        _filterCollection.add(new LocalDateTimeFilterType());
        _filterCollection.add(new OffsetTimeFilterType());
        _filterCollection.add(new OffsetDateTimeFilterType());
        _filterCollection.add(new ZonedDateTimeFilterType());
        _filterCollection.add(new ByteFilterType());
        _filterCollection.add(new BigDecimalFilterType());
        _filterCollection.add(new BigIntegerFilterType());
        _filterCollection.add(new IntegerFilterType());
        _filterCollection.add(new DoubleFilterType());
        _filterCollection.add(new LongFilterType());
        _filterCollection.add(new FloatFilterType());
        _filterCollection.add(new UuidFilterType());
    }

    public IFilterType GetFilterType(Class type)
    {
        if (type.isEnum())
            return new EnumFilterType(type);

        for (IFilterType filterType : _filterCollection)
        {
            if (filterType.getTargetType().equals(type))
                return filterType;
        }
        return new TextFilterType(); //try to process column type as text (not safe)
    }
}
