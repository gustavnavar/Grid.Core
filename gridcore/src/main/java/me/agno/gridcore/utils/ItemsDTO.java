package me.agno.gridcore.utils;

import lombok.Getter;
import lombok.Setter;
import me.agno.gridcore.pagination.PagerDTO;
import me.agno.gridcore.totals.TotalsDTO;

import java.util.List;

@Getter
@Setter
public class ItemsDTO<T> {

    public List<T> Items;

    public TotalsDTO Totals;

    public PagerDTO Pager;

    public ItemsDTO()
    {
    }
}
