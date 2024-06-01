package me.agno.gridjavacore.utils;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public static LocalDateTime getLocalDateTime(String value) {
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
        catch (Exception e) {
            try {
                return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
            }
            catch (Exception e1) {
                return null;
            }
        }
    }

    public static LocalDate getLocalDate(String value) {
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate();
        }
        catch (Exception e) {
            try {
                return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
            }
            catch (Exception e1) {
                return null;
            }
        }
    }

    public static LocalTime getLocalTime(String value) {
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalTime();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Calendar getCalendar(String value) {
        try {
            var date = getLocalDateTime(value);
            Calendar cal = Calendar.getInstance();
            cal.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth(), date.getHour(), date.getMinute(), date.getSecond());
            return cal;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Instant getInstant(String value) {
        try {
            var localDateTime = getLocalDateTime(value);
            return localDateTime.toInstant(ZoneOffset.UTC);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Date getDate(String value) {
        try {
            var instant = getInstant(value);
            return Date.from(instant);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static OffsetDateTime getOffsetDateTime(String value) {
        try {
            var localDateTime = getLocalDateTime(value);
            return localDateTime.atOffset(ZoneOffset.UTC);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static OffsetTime getOffsetTime(String value) {
        try {
            var offsetDateTime = getOffsetDateTime(value);
            return offsetDateTime.toOffsetTime();
        }
        catch (Exception e) {
            return null;
        }
    }

    public static java.sql.Date getSqlDate(String value) {
        try {
            var date = getLocalDate(value);
            return java.sql.Date.valueOf(date);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Time getSqlTime(String value) {
        try {
            var time = getLocalTime(value);
            return Time.valueOf(time);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Timestamp getSqlTimestamp(String value) {
        try {
            var localDateTime = getLocalDateTime(value);
            return Timestamp.valueOf(localDateTime);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static ZonedDateTime getZonedDateTime(String value) {
        try {
            var localDateTime = getOffsetDateTime(value);
            return localDateTime.toZonedDateTime();
        }
        catch (Exception e) {
            return null;
        }
    }
}
