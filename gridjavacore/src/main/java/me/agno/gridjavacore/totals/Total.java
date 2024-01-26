package me.agno.gridjavacore.totals;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Optional;

@Getter
@Setter
public class Total {

    private GridTotalType type = GridTotalType.NONE;

    private Optional<BigDecimal> number;

    private Optional<Instant> dateTime;

    private String string;


    public Total()
    { }

    public Total(Number number) {
        this.type = GridTotalType.NUMBER;
        if(number instanceof Byte)
            this.number = Optional.of(BigDecimal.valueOf(number.longValue()));
        if(number instanceof Integer)
            this.number = Optional.of(BigDecimal.valueOf(number.longValue()));
        if(number instanceof Long)
            this.number = Optional.of(BigDecimal.valueOf(number.longValue()));
        if(number instanceof Float)
            this.number = Optional.of(BigDecimal.valueOf(number.doubleValue()));
        if(number instanceof Double)
            this.number = Optional.of(BigDecimal.valueOf((double)number));
        if(number instanceof BigInteger)
            this.number = Optional.of(new BigDecimal((BigInteger) number));
        if(number instanceof BigDecimal)
            this.number = Optional.of((BigDecimal)number);
    }

    public Total(java.sql.Date dateTime) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(dateTime.toInstant());
    }

    public Total(java.sql.Time time) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(time.toInstant());
    }

    public Total(java.sql.Timestamp timeStamp) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(timeStamp.toInstant());
    }

    public Total(java.util.Date date) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(date.toInstant());
    }

    public Total(java.util.Calendar calendar) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(calendar.toInstant());
    }

    public Total(java.time.Instant instant) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(instant);
    }

    public Total(LocalDate date) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(date.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    public Total(LocalTime time) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(time.atDate(LocalDate.now()).toInstant(ZoneOffset.UTC));
    }

    public Total(LocalDateTime dateTime) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(dateTime.toInstant(ZoneOffset.UTC));
    }

    public Total(OffsetTime time) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(time.atDate(LocalDate.now()).toInstant());
    }

    public Total(OffsetDateTime dateTime) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(dateTime.toInstant());
    }

    public Total(ZonedDateTime dateTime) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(dateTime.toInstant());
    }

    public Total(String str) {
        this.type = GridTotalType.STRING;
        this.string = str;
    }

    public String GetString(String valuePattern) {
        Object value;
        if (this.type == GridTotalType.NUMBER)
            value = this.number;
        else if (this.type == GridTotalType.DATE_TIME)
            value = this.dateTime;
        else if (this.type == GridTotalType.STRING)
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