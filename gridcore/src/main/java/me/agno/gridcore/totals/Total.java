package me.agno.gridcore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Optional;

@Getter
@Setter
public class Total {

    private boolean IsNumber = false;

    private Optional<Double> Number;

    private boolean IsDateTime = false;

    private Optional<Date> DateTime;

    private boolean IsString = false;

    private String String;


    public Total()
    { }

    public Total(double number)
    {
        IsNumber = true;
        Number = Optional.of(number);
    }

    public Total(Date dateTime)
    {
        IsDateTime = true;
        DateTime = Optional.ofNullable(dateTime);
    }

    public Total(String str)
    {
        IsString = true;
        String = str;
    }

    public String GetString(String valuePattern)
    {
        Object value;
        if (IsNumber)
            value = Number;
        else if (IsDateTime)
            value = DateTime;
        else if (IsString)
            value = String;
        else
            return null;

        try
        {
            if (valuePattern != null && valuePattern.trim() != "")
                return String.format(valuePattern, value);
            else
                return value.toString();
        }
        catch (Exception e)
        {
            return value.toString();
        }
    }
}