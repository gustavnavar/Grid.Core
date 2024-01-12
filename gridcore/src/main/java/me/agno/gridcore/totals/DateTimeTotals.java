package me.agno.gridcore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DateTimeTotals {
    private Date max;
    private Date min;

    public DateTimeTotals()
    {
    }
}
