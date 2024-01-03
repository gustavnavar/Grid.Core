package me.agno.gridcore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DateTimeTotals {
    private Date Max;
    private Date Min;

    public DateTimeTotals()
    {
    }
}
