package me.agno.gridcore.sorting;

public enum GridSortDirection {
    ASCENDING,
    DESCENDING;

    @Override public String toString() {
        return switch (this) {
            case ASCENDING -> "0";
            case DESCENDING -> "1";
            default -> null;
        };
    }
}
