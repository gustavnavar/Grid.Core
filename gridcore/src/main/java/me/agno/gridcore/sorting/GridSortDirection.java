package me.agno.gridcore.sorting;

public enum GridSortDirection {
    Ascending,
    Descending;

    @Override public String toString() {
        switch (this) {
            case Ascending:
                return "0";
            case Descending:
                return "1";
            default:
                return null;
        }
    }
}
