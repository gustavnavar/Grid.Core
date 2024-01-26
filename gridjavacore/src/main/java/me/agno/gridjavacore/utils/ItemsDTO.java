package me.agno.gridjavacore.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.agno.gridjavacore.pagination.PagerDTO;
import me.agno.gridjavacore.totals.TotalsDTO;

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
