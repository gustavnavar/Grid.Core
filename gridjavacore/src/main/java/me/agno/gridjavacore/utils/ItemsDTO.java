package me.agno.gridjavacore.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.agno.gridjavacore.pagination.PagerDTO;
import me.agno.gridjavacore.totals.TotalsDTO;

import java.util.List;

/**
 * Represents a data transfer object (DTO) that holds a list of items, totals, and pager information for a grid.
 *
 * @param <T> the type of items in the list
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemsDTO<T> {

    /**
     * A list of items used as a data transfer object (DTO) for a grid.
     */
    public List<T> items;

    /**
     * Represents a data transfer object (DTO) that holds the sum, average, max, min, and calculation totals for a grid.
     */
    public TotalsDTO totals;

    /**
     * Represents a Data Transfer Object for a pager used in pagination or virtualization.
     */
    public PagerDTO pager;
}
