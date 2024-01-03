package me.agno.gridcore.annotations;

import me.agno.gridcore.sorting.GridSortDirection;

public interface GridColumn {

    GridSortDirection getInitialSortDirection();

    void setInitialSortDirection(GridSortDirection gridSortDirection);

    boolean isSortEnabled();

    void setSortEnabled(boolean sortEnabled);

    boolean isFilterEnabled();

    void setFilterEnabled(boolean filterEnabled);
}
