package me.agno.gridcore.totals;

import lombok.Getter;
import lombok.Setter;

import java.time.*;
import java.util.Optional;

@Getter
@Setter
public class Total {

    private boolean isNumber = false;

    private Optional<Double> number;

    private boolean isDateTime = false;

    private Optional<LocalDateTime> dateTime;

    private boolean isString = false;

    private String string;


    public Total()
    { }

    public Total(double number) {
        this.isNumber = true;
        this.number = Optional.of(number);
    }

    public Total(java.sql.Date dateTime) {
        this.isDateTime = true;
        this.dateTime = Optional.of(dateTime.toLocalDate().atStartOfDay());
    }

    public Total(java.sql.Time time) {
        this.isDateTime = true;
        this.dateTime = Optional.of(time.toLocalTime().atDate(LocalDate.now()));
    }

    public Total(java.sql.Timestamp timeStamp) {
        this.isDateTime = true;
        this.dateTime = Optional.ofNullable(timeStamp.toLocalDateTime());
    }

    public Total(java.util.Date date) {
        this.isDateTime = true;
        this.dateTime = Optional.of(LocalDateTime.from(date.toInstant()));
    }

    public Total(java.util.Calendar calendar) {
        this.isDateTime = true;
        this.dateTime = Optional.of(LocalDateTime.from(calendar.toInstant()));
    }

    public Total(java.time.Instant instant) {
        this.isDateTime = true;
        this.dateTime = Optional.of(LocalDateTime.from(instant));
    }

    public Total(LocalDate date) {
        this.isDateTime = true;
        this.dateTime = Optional.of(date.atStartOfDay());
    }

    public Total(LocalTime time) {
        this.isDateTime = true;
        this.dateTime = Optional.of(time.atDate(LocalDate.now()));
    }

    public Total(LocalDateTime dateTime) {
        this.isDateTime = true;
        this.dateTime = Optional.ofNullable(dateTime);
    }

    public Total(OffsetTime time) {
        this.isDateTime = true;
        this.dateTime = Optional.of(time.toLocalTime().atDate(LocalDate.now()));
    }

    public Total(OffsetDateTime dateTime) {
        this.isDateTime = true;
        this.dateTime = Optional.ofNullable(dateTime.toLocalDateTime());
    }

    public Total(ZonedDateTime dateTime) {
        this.isDateTime = true;
        this.dateTime = Optional.ofNullable(dateTime.toLocalDateTime());
    }

    public Total(String str) {
        this.isString = true;
        this.string = str;
    }

    public String GetString(String valuePattern) {
        Object value;
        if (this.isNumber)
            value = this.number;
        else if (this.isDateTime)
            value = this.dateTime;
        else if (this.isString)
            value = this.string;
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