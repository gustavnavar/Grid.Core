package me.agno.gridjavacore.totals;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Optional;

/**
 * Represents a total value in a grid.
 */
@Getter
@Setter
public class Total {

    /**
     * Private instance variable representing the type of grid total.
     */
    private GridTotalType type = GridTotalType.NONE;

    /**
     * Represents an optional number.
     */
    private Optional<BigDecimal> number;

    /**
     * Represents an optional instant of date and time.
     */
    private Optional<Instant> dateTime;

    /**
     * Represents a private string variable.
     */
    private String string;


    /**
     * Represents a method for calculating the total.
     */
    public Total()
    { }

    /**
     * Represents a method for initializing the Total object with a Number value.
     *
     * @param number the Number value to initialize the Total object with
     */
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

    /**
     * Initializes a Total object with the provided date and sets the type to GridTotalType.DATE_TIME.
     *
     * @param dateTime the date to be used for initialization
     */
    public Total(java.sql.Date dateTime) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(dateTime.toInstant());
    }

    /**
     * Initializes a Total object with the provided java.sql.Time value and sets the type to GridTotalType.DATE_TIME.
     *
     * @param time the java.sql.Time value to initialize the Total object with
     */
    public Total(java.sql.Time time) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(time.toInstant());
    }

    /**
     * Initializes a Total object with the provided java.sql.Timestamp value and sets the type to GridTotalType.DATE_TIME.
     *
     * @param timeStamp the java.sql.Timestamp value to initialize the Total object with
     */
    public Total(java.sql.Timestamp timeStamp) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(timeStamp.toInstant());
    }

    /**
     * Initializes a Total object with the provided java.util.Date value and sets the type to GridTotalType.DATE_TIME.
     *
     * @param date the java.util.Date value to initialize the Total object with
     */
    public Total(java.util.Date date) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(date.toInstant());
    }

    /**
     * Initializes a Total object with the provided java.util.Calendar value and sets the type to GridTotalType.DATE_TIME.
     *
     * @param calendar the java.util.Calendar value to initialize the Total object with
     */
    public Total(java.util.Calendar calendar) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(calendar.toInstant());
    }

    /**
     * Initializes a Total object with the provided java.time.Instant value
     * and sets the type to GridTotalType.DATE_TIME.
     *
     * @param instant the java.time.Instant value to initialize the Total object with
     */
    public Total(java.time.Instant instant) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(instant);
    }

    /**
     * Initializes a Total object with the provided LocalDate value and sets the type to GridTotalType.DATE_TIME.
     *
     * @param date the LocalDate value to initialize the Total object with
     */
    public Total(LocalDate date) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(date.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    /**
     * Initializes a Total object with the provided LocalTime value and sets the type to GridTotalType.DATE_TIME.
     *
     * @param time the LocalTime value to initialize the Total object with
     */
    public Total(LocalTime time) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(time.atDate(LocalDate.now()).toInstant(ZoneOffset.UTC));
    }

    /**
     * Initializes a Total object with the provided LocalDateTime value and sets the type to GridTotalType.DATE_TIME.
     *
     * @param dateTime the LocalDateTime value to initialize the Total object with
     */
    public Total(LocalDateTime dateTime) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(dateTime.toInstant(ZoneOffset.UTC));
    }

    /**
     * Initializes a Total object with the provided OffsetTime value
     * and sets the type to GridTotalType.DATE_TIME.
     *
     * @param time the OffsetTime value to initialize the Total object with
     */
    public Total(OffsetTime time) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(time.atDate(LocalDate.now()).toInstant());
    }

    /**
     * Initializes a Total object with the provided OffsetDateTime value and sets the type of total to GridTotalType.DATE_TIME.
     *
     * @param dateTime the OffsetDateTime value to initialize the Total object with
     */
    public Total(OffsetDateTime dateTime) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(dateTime.toInstant());
    }

    /**
     * Initializes a Total object with the provided ZonedDateTime value and sets the type to GridTotalType.DATE_TIME.
     *
     * @param dateTime the ZonedDateTime value to initialize the Total object with
     */
    public Total(ZonedDateTime dateTime) {
        this.type = GridTotalType.DATE_TIME;
        this.dateTime = Optional.of(dateTime.toInstant());
    }

    /**
     * Initializes a Total object with the provided string value and sets the type to GridTotalType.STRING.
     *
     * @param str the string value to initialize the Total object with
     */
    public Total(String str) {
        this.type = GridTotalType.STRING;
        this.string = str;
    }

    /**
     * Returns a formatted string representation of the value based on the provided pattern.
     *
     * @param valuePattern the pattern used to format the value
     * @return the formatted string representation of the value
     */
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