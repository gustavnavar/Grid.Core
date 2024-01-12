package me.agno.gridcore.totals;

import lombok.Getter;
import lombok.Setter;

import java.time.*;
import java.util.Optional;

@Getter
@Setter
public class Total {

    private boolean IsNumber = false;

    private Optional<Double> Number;

    private boolean IsDateTime = false;

    private Optional<LocalDateTime> DateTime;

    private boolean IsString = false;

    private String String;


    public Total()
    { }

    public Total(double number) {
        IsNumber = true;
        Number = Optional.of(number);
    }

    public Total(java.sql.Date dateTime) {
        IsDateTime = true;
        DateTime = Optional.of(dateTime.toLocalDate().atStartOfDay());
    }

    public Total(java.sql.Time time) {
        IsDateTime = true;
        DateTime = Optional.of(time.toLocalTime().atDate(LocalDate.now()));
    }

    public Total(java.sql.Timestamp timeStamp) {
        IsDateTime = true;
        DateTime = Optional.ofNullable(timeStamp.toLocalDateTime());
    }

    public Total(java.util.Date date) {
        IsDateTime = true;
        DateTime = Optional.of(LocalDateTime.from(date.toInstant()));
    }

    public Total(java.util.Calendar calendar) {
        IsDateTime = true;
        DateTime = Optional.of(LocalDateTime.from(calendar.toInstant()));
    }

    public Total(java.time.Instant instant) {
        IsDateTime = true;
        DateTime = Optional.of(LocalDateTime.from(instant));
    }

    public Total(LocalDate date) {
        IsDateTime = true;
        DateTime = Optional.of(date.atStartOfDay());
    }

    public Total(LocalTime time) {
        IsDateTime = true;
        DateTime = Optional.of(time.atDate(LocalDate.now()));
    }

    public Total(LocalDateTime dateTime) {
        IsDateTime = true;
        DateTime = Optional.ofNullable(dateTime);
    }

    public Total(OffsetTime time) {
        IsDateTime = true;
        DateTime = Optional.of(time.toLocalTime().atDate(LocalDate.now()));
    }

    public Total(OffsetDateTime dateTime) {
        IsDateTime = true;
        DateTime = Optional.ofNullable(dateTime.toLocalDateTime());
    }

    public Total(ZonedDateTime dateTime) {
        IsDateTime = true;
        DateTime = Optional.ofNullable(dateTime.toLocalDateTime());
    }

    public Total(String str) {
        IsString = true;
        String = str;
    }

    public String GetString(String valuePattern) {
        Object value;
        if (IsNumber)
            value = Number;
        else if (IsDateTime)
            value = DateTime;
        else if (IsString)
            value = String;
        else
            return null;

        try {
            if (valuePattern != null && !valuePattern.trim().isEmpty())
                return java.lang.String.format(valuePattern, value);
            else
                return value.toString();
        }
        catch (Exception e) {
            return value.toString();
        }
    }
}