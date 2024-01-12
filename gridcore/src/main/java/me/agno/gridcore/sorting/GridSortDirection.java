package me.agno.gridcore.sorting;

public enum GridSortDirection {
    Ascending,
    Descending;

    @Override public String toString() {
        return switch (this) {
            case Ascending -> "0";
            case Descending -> "1";
            default -> null;
        };
    }
}
