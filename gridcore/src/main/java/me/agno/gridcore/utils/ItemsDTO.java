package me.agno.gridcore.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.agno.gridcore.pagination.PagerDTO;
import me.agno.gridcore.totals.TotalsDTO;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemsDTO<T> {

    public List<T> items;

    public TotalsDTO totals;

    public PagerDTO pager;
}
