package me.agno.gridcore.totals;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Total {

    private boolean IsNumber = false;

    private double Number;

    private boolean IsDateTime = false;

    private Date DateTime;

    private boolean IsString = false;

    private String String;


    public Total()
    { }

    public Total(double number)
    {
        IsNumber = true;
        Number = number;
    }

    public Total(Date dateTime)
    {
        IsDateTime = true;
        DateTime = dateTime;
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