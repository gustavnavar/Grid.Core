package me.agno.gridjavacore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The SearchOptions class represents the search options for a grid. It provides properties and methods to enable or disable various search functionalities.
 */
@Getter
@Setter
@NoArgsConstructor
public class SearchOptions {

    /**
     * The enabled variable represents the flag indicating whether the search functionality is enabled or disabled.
     *
     * <p>
     * A value of {@code true} indicates that the search functionality is enabled, while a value of {@code false} indicates that it is disabled.
     * </p>
     *
     * <p>
     * The variable is a boolean type, which means it can only take the values {@code true} or {@code false}.
     * By default, it is set to {@code true} when no initial value is provided.
     * </p>
     *
     * @see SearchOptions
     */
    private boolean enabled = true;

    /**
     * The `onlyTextColumns` variable represents the flag indicating whether only text columns should be considered during a search operation.
     *
     * <p>
     * A value of `true` indicates that only text columns should be considered, while a value of `false` indicates that all columns should be considered.
     * </p>
     *
     * <p>
     * The variable is a boolean type, which means it can only take the values `true` or `false`.
     * By default, it is set to `true` when no initial value is provided.
     * </p>
     *
     * @see SearchOptions
     */
    private boolean onlyTextColumns = true;

    /**
     * The hiddenColumns variable represents the flag indicating whether hidden columns should be included during a search operation.
     *
     * <p>
     * A value of true indicates that hidden columns should be included, while a value of false indicates that hidden columns should be excluded.
     * </p>
     *
     * <p>
     * The variable is a boolean type, which means it can only take the values true or false.
     * By default, it is set to false when no initial value is provided.
     * </p>
     *
     * @see SearchOptions
     */
    private boolean hiddenColumns = false;

    /**
     * The variable "splittedWords" represents the flag indicating whether a search operation should consider splitted words.
     *
     * <p>
     * A value of {@code true} indicates that the search operation should consider splitted words, while a value of {@code false} indicates that it should not.
     * </p>
     *
     * <p>
     * The variable is a boolean type, which means it can only take the values {@code true} or {@code false}.
     * By default, it is set to {@code false} when no initial value is provided.
     * </p>
     *
     * @see SearchOptions
     */
    private boolean splittedWords = false;

    /**
     * Constructs a new SearchOptions instance with the specified enabled flag.
     *
     * @param enabled a boolean value indicating whether the search functionality is enabled or disabled
     *
     * @see SearchOptions
     */
    public SearchOptions(boolean enabled) {
        this.enabled = enabled;
    }
}
